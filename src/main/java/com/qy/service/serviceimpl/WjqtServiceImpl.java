package com.qy.service.serviceimpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qy.dao.WjqtDao;
import com.qy.entity.ParkConfigEntity;
import com.qy.entity.ParkEntity;
import com.qy.service.ParkConfigService;
import com.qy.service.WjqtService;
import com.qy.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Because of you on 2020/1/3.
 */
@Slf4j
@Service
@DS("wjqt")
public class WjqtServiceImpl implements WjqtService {
    @Autowired
    private ParkConfigService parkConfigService;
    @Autowired
    private WjqtDao wjqtDao;
    public List<ParkEntity> queryParks(Map<String,String> map, String imgneturl){
        String[] dates = DateUtils.createDateStrs();
        List<ParkEntity> list = wjqtDao.queryParks(dates);
        if(list!=null&&list.size()>0) {
            String tccbh = map.get("tccbh");
            String url = map.get("url");
            String account = map.get("account");
            String password = Md5.md5(map.get("password"));
            String loginUrl = url+"/login/go";
            Map<String, String> data = new HashMap<String, String>();
            data.put("account", account);
            data.put("password",password);
            data.put("chooseBox", "-1@-1");
            Map parkConfigMap = parkConfigService.getMap(new QueryWrapper<ParkConfigEntity>().select("tcwz","imgbasepath").eq("tccbh",tccbh));
            String imgbasepath = (String) parkConfigMap.get("IMGBASEPATH");
            String dir = new SimpleDateFormat("yyyyMMdd").format(new Date());
            imgbasepath += File.separator + dir;
            FileUtils.creatDir(imgbasepath);
            for (ParkEntity parkEntity : list) {
                log.info("武江桥头西侧(农行)hphm:"+ parkEntity.getHphm());
                parkEntity.setId(SysUtils.getId());
                parkEntity.setTccbh(tccbh);
                parkEntity.setTcwz((String) parkConfigMap.get("TCWZ"));
                String tmp = parkEntity.getTplj();
                String tmp1 = tmp.substring(0,tmp.lastIndexOf("/")+1);
                String filename = tmp.substring(tmp.lastIndexOf("/")+1);
                String imgpath = imgbasepath+ File.separator+filename;
                String imgurl ="";
                try {
                    imgurl = url+tmp1+ URLEncoder.encode(filename, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                HttpUtils.downloadWjqtImg(loginUrl, data,imgurl, imgpath);
                String param = null;
                try {
                    param = SysUtils.byte2hex(imgpath.getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String imgurl1 = imgneturl+param;
                parkEntity.setTplj(imgurl1);
            }
        }
        return list;
    }
}
