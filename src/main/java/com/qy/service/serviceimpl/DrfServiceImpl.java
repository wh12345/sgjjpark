package com.qy.service.serviceimpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qy.dao.DrfDao;
import com.qy.entity.ParkConfigEntity;
import com.qy.entity.ParkEntity;
import com.qy.service.DrfService;
import com.qy.service.ParkConfigService;
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
 * Created by Because of you on 2020/1/6.
 */
@Slf4j
@Service
@DS("drf")
public class DrfServiceImpl implements DrfService {
    @Autowired
    private ParkConfigService parkConfigService;
    @Autowired
    private DrfDao drfDao;
    @Override
    public List<ParkEntity> queryParks(Map<String, String> map, String imgneturl) {
        String[] dates = DateUtils.createDateStrs();
        List<ParkEntity> list = drfDao.queryParks(dates);
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
                    String imgurl = url+tmp;
                    String imgpath = imgbasepath +File.separator+tmp.substring(tmp.lastIndexOf("/")+1);
                    log.info("浈江区大润发停车场imgurl:" + imgurl + " imgpath:" + imgpath);
                    HttpUtils.downloadCarimg(imgurl, imgpath);
                    String param = null;
                    try {
                        param = SysUtils.byte2hex(imgpath.getBytes("UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String imgurl1 =imgneturl + param;
                    parkEntity.setTplj(imgurl1);
                }else{
                    iterator.remove();
                }
            }
        }
        return list;
    }
}
