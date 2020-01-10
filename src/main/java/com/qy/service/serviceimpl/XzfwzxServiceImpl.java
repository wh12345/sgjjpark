package com.qy.service.serviceimpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qy.dao.XzfwzxDao;
import com.qy.entity.ParkConfigEntity;
import com.qy.entity.ParkEntity;
import com.qy.service.ParkConfigService;
import com.qy.service.XzfwzxService;
import com.qy.utils.DateUtils;
import com.qy.utils.FileUtils;
import com.qy.utils.HttpUtils;
import com.qy.utils.SysUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Because of you on 2020/1/3.
 */
@Slf4j
@Service
@DS("xzfwzx")
public class XzfwzxServiceImpl implements XzfwzxService{
    @Autowired
    private ParkConfigService parkConfigService;
    @Autowired
    private XzfwzxDao xzfwzxDao;
    public List<ParkEntity> queryParks(Map<String,String> map, String imgneturl){

        String[] dates = DateUtils.createDateStrs();
        List<ParkEntity> list = xzfwzxDao.queryParks(dates);
        if(list!=null&&list.size()>0) {
            String url = map.get("url");
            String tccbh = map.get("tccbh");
            Map parkConfigMap = parkConfigService.getMap(new QueryWrapper<ParkConfigEntity>().select("tcwz","imgbasepath").eq("tccbh",tccbh));
            String imgbasepath = (String) parkConfigMap.get("IMGBASEPATH");
            String dir1 = DateUtils.dateToStr("yyyyMMdd",new Date());
            imgbasepath += File.separator + dir1;
            FileUtils.creatDir(imgbasepath);
            Iterator<ParkEntity> iterator = list.iterator();
            while(iterator.hasNext()) {
                ParkEntity parkEntity = iterator.next();
                if(!parkEntity.getHphm().isEmpty()) {
                    parkEntity.setId(SysUtils.getId());
                    parkEntity.setTccbh(tccbh);
                    parkEntity.setTcwz((String)parkConfigMap.get("TCWZ"));
                    String tmp = parkEntity.getTplj();
                    String[] dirs = tmp.split("_");
                    String dir2 = dirs[0].substring(0, 8);
                    String dir3 = dirs[0].substring(8, 10);
                    String imgurl = url + "/" + dir2 + "/" + dirs[1] + "/" + dir3 + "/" + tmp;
                    String imgpath = imgbasepath + File.separator + tmp;
                    log.info("武江行政服务中心imgurl:" + imgurl + " imgpath:" + imgpath);
                    HttpUtils.downloadCarimg(imgurl, imgpath);
                    String pictureString = null;
                    try {
                        pictureString = SysUtils.byte2hex(imgpath.getBytes("UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String imgurl1 =imgneturl + pictureString;
                    parkEntity.setTplj(imgurl1);
                }else{
                    iterator.remove();
                }
            }
        }
        return list;
    }
}
