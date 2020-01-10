package com.qy.processor;

import com.baomidou.dynamic.datasource.DynamicDataSourceCreator;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.provider.YmlDynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.druid.DruidConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Because of you on 2019/12/29.
 */
@Component
@PropertySource({"classpath:db.properties"})
public class DbProcessor implements BeanPostProcessor {
    @Value("${db.driver}")
    private String driver;
    @Value("${db.url}")
    private String url;
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;
    @Value("${db.sql}")
    private String sql;
    @Autowired
    private DefaultListableBeanFactory defaultListableBeanFactory;
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        //设置数据库配置
        if(StringUtils.endsWithIgnoreCase(beanName,"dynamicDataSourceProvider")) {
            boolean containsBean = defaultListableBeanFactory.containsBean("dynamicDataSourceProvider");
            if(containsBean) {
                defaultListableBeanFactory.removeBeanDefinition("dynamicDataSourceProvider");
            }
            defaultListableBeanFactory.registerBeanDefinition("dynamicDataSourceProvider", BeanDefinitionBuilder.genericBeanDefinition(DynamicDataSourceProperties.class).getBeanDefinition());
            bean = null;
            DynamicDataSourceCreator dynamicDataSourceCreator = (DynamicDataSourceCreator) defaultListableBeanFactory.getBean("dynamicDataSourceCreator");
            YmlDynamicDataSourceProvider ymlDynamicDataSourceProvider = new YmlDynamicDataSourceProvider(this.createDynamicDataSourceProperties(),dynamicDataSourceCreator);
            return ymlDynamicDataSourceProvider;
        }
        //设置主数据库
        if(StringUtils.endsWithIgnoreCase(beanName,"dataSource")) {
            DynamicRoutingDataSource dynamicRoutingDataSource = (DynamicRoutingDataSource)bean;
            dynamicRoutingDataSource.setPrimary("oracle");
            return  dynamicRoutingDataSource;
        }
        return bean;
    }
    public DynamicDataSourceProperties createDynamicDataSourceProperties() {
        DynamicDataSourceProperties dynamicDataSourceProperties = new DynamicDataSourceProperties();
        Map<String,DataSourceProperty> map = new HashMap<String,DataSourceProperty>();
        DataSourceProperty mainDbProperty = new DataSourceProperty();
        mainDbProperty.setDriverClassName(driver);
        mainDbProperty.setUrl(url);
        mainDbProperty.setUsername(username);
        mainDbProperty.setPassword(password);
        map.put("oracle",mainDbProperty);
        List<Map<String,String>> list = this.listDbs();
        if(list!=null&&list.size()>0) {
            DataSourceProperty otherDbProperty = null;
            for (Map<String, String> tmpMap : list) {
                otherDbProperty = new DataSourceProperty();
                String dbname = tmpMap.get("dbname");
                otherDbProperty.setDriverClassName(tmpMap.get("driver"));
                otherDbProperty.setUrl(tmpMap.get("url"));
                otherDbProperty.setUsername(tmpMap.get("username"));
                otherDbProperty.setPassword(tmpMap.get("password"));
                map.put(dbname, otherDbProperty);
            }
        }
        DruidConfig druidConfig = this.createDruidConfig();
        dynamicDataSourceProperties.setDruid(druidConfig);
        dynamicDataSourceProperties.setDatasource(map);
        return dynamicDataSourceProperties;
    }

    /**
     * 获取数据库连接
     * @return
     */
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        return DriverManager.getConnection(url,username,password);
    }

    /**
     * 查询所有数据库
     * @return
     */
    public List<Map<String,String>> listDbs() {
        List<Map<String,String>> resultList = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs =  null;
        try {
            connection =  this.getConnection();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            resultList = new ArrayList<Map<String,String>>();
            Map<String,String> resultMap = null;
            while (rs.next()) {
                String dbname = rs.getString("DBNAME");
                String dbdriver = rs.getString("DRIVER");
                String dburl = rs.getString("URL");
                String dbusername = rs.getString("USERNAME");
                String dbpassword = rs.getString("PASSWORD");
                resultMap = new HashMap<String,String>();
                resultMap.put("dbname",dbname);
                resultMap.put("driver",dbdriver);
                resultMap.put("url",dburl);
                resultMap.put("username",dbusername);
                resultMap.put("password",dbpassword);
                resultList.add(resultMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            if(rs!=null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(ps!=null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection!=null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return  resultList;
        }
    }

    /**
     * 配置druid连接池配置
     * @return
     */
    public DruidConfig createDruidConfig() {
        DruidConfig druidConfig = new DruidConfig();
        druidConfig.setInitialSize(10);
        druidConfig.setMaxActive(100);
        druidConfig.setMinIdle(10);
        druidConfig.setMaxWait(60000L);
        druidConfig.setPoolPreparedStatements(true);
        druidConfig.setMaxPoolPreparedStatementPerConnectionSize(20);
        druidConfig.setTimeBetweenEvictionRunsMillis(60000L);
        druidConfig.setMinEvictableIdleTimeMillis(300000L);
        //validation-query: SELECT 1 FROM DUAL
        druidConfig.setTestWhileIdle(true);
        druidConfig.setTestOnBorrow(false);
        druidConfig.setTestOnReturn(false);
        druidConfig.setFilters("stat,wall");
        return druidConfig;
    }
}
