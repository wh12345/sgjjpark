package com.qy.service.serviceimpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qy.dao.SgsgnjDao;
import com.qy.entity.ParkConfigEntity;
import com.qy.entity.ParkEntity;
import com.qy.service.ParkConfigService;
import com.qy.service.SgsgnjService;
import com.qy.utils.DateUtils;
import com.qy.utils.FileUtils;
import com.qy.utils.FtpUtils;
import com.qy.utils.SysUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Because of you on 2019/12/30.
 */
@Slf4j
@Service
@DS("sgsgnj")
public class SgsgnjServiceImpl implements SgsgnjService{
    @Autowired
    private ParkConfigService parkConfigService;
    @Autowired
    private SgsgnjDao sgsgnjDao;
    @Override
    public List<ParkEntity> queryParks(Map<String,String> map,String imgneturl) {
        String[] dates = DateUtils.createDateStrs();
        List<ParkEntity> list = sgsgnjDao.queryParks(dates);
        log.info("韶关市公安局停车场查询过车数据:"+list.toString());
        if(list!=null&&list.size()>0) {
            String tccbh = map.get("tccbh");
            String ftpip = map.get("ftpip");
            Integer ftpport =Integer.parseInt( map.get("ftpport"));
            String ftpusername = map.get("ftpusername");
            String ftppassword = map.get("ftppassword");
            Map parkConfigMap = parkConfigService.getMap(new QueryWrapper<ParkConfigEntity>().select("tcwz","imgbasepath").eq("tccbh",tccbh));
            String imgbasepath = (String) parkConfigMap.get("IMGBASEPATH");
            String dir = DateUtils.dateToStr("yyyyMMdd",new Date());
            imgbasepath += File.separator+dir;
            FileUtils.creatDir(imgbasepath);
            Iterator<ParkEntity> iterator = list.iterator();
            while(iterator.hasNext()) {
                ParkEntity park = iterator.next();
                String hphm = park.getHphm();
                if(!StringUtils.isBlank(hphm)) {
                    if(hphm.contains("学")) {
                        park.setHphm(hphm.substring(0,hphm.length()-1));
                        park.setHpzl("16");
                        park.setHpys("1");
                    }
                    park.setId(SysUtils.getId());
                    park.setTccbh(tccbh);
                    park.setTcwz((String) parkConfigMap.get("TCWZ"));
                    if("Out".equals(park.getFxlx())) {
                        park.setFxlx("2");
                    }else{
                        park.setFxlx("1");
                    }
                    Date txsj = park.getTxsj();
                    String txsjStr1 = DateUtils.dateToStr("yyyy-MM-dd",txsj);
                    String txsjStr2 = DateUtils.dateToStr("HH",txsj);
                    String dateStr = DateUtils.dateToStr("yyyyMMddHHmmss",txsj);
                    String ftppath = "/"+txsjStr1+"/"+txsjStr2;
                    String imgPath = imgbasepath+File.separator+dateStr+"_"+park.getHphm()+".jpg";
                    FTPClient ftpClient = FtpUtils.initFtp(ftpip,ftpport,ftpusername,ftppassword);
                    if(ftpClient!=null) {
                        FtpUtils.downloadImgByDate(ftpClient, ftppath, dateStr, imgPath);
                    }
                    try {
                        String imgPathStr =  SysUtils.byte2hex(imgPath.getBytes("UTF-8"));
                        String imgUrl = imgneturl+"/"+imgPathStr;
                        park.setTplj(imgUrl);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    };

                }else{
                    iterator.remove();
                }
            }
        }
        return list;
    }
}
