package com.qy.service.serviceimpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qy.entity.ParkConfigEntity;
import com.qy.entity.ParkEntity;
import com.qy.pojo.BshcRequestParam;
import com.qy.service.BshcService;
import com.qy.service.ParkConfigService;
import com.qy.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Because of you on 2020/1/2.
 */
@Slf4j
@Service
public class BshcServiceImpl implements BshcService {
    @Autowired
    private ParkConfigService parkConfigService;
    public List<ParkEntity> getParks(Map<String,String> map,String imgneturl) {
        List<ParkEntity>  parks = null;
        String tccbh = map.get("tccbh");
        String appkey = map.get("appkey");
        String secret = map.get("secret");
        String url = map.get("url");
        //碧水花城获取uuid
        String u1 = map.get("u1");
        String u2 = map.get("u2");
        Long time = System.currentTimeMillis();
        String param1 = "{\"appkey\":\"" + appkey + "\",\"time\":\"" + time + "\"}";
        String token1 = Md5.md5(u1 + param1 + secret);
        String uuidurl = url+u1+"?token=" + token1;
        String retParam = HttpUtils.sendPost(uuidurl, param1);
        log.info("碧水花城获取uuid:" + retParam);
        JSONObject json1 = JSONObject.parseObject(retParam);
        String errorCode1 = json1.getString("errorCode");
        if ("0".equals(errorCode1)) {
            String uuid = json1.getString("data");
            //获取停车场数据,每隔五分钟取一次
            Long startTime = DateUtils.getDateByDiff(new Date(),-5).getTime();
            //当前时间,可为时间戳
            Long endTime = System.currentTimeMillis();
            //一页每次取50条记录
            BshcRequestParam brp = new BshcRequestParam(appkey, endTime, 1, 300, uuid, startTime, endTime);
            String param = JSONObject.toJSONString(brp);
            String token = Md5.md5(u2 + param + secret);
            String vehurl = url+u2 + "?token=" + token;
            log.info("请求碧水花城停车场接口vehurl" + vehurl + " :param" + param);
            String result = HttpUtils.sendPost(vehurl, param);
            JSONObject json = JSONObject.parseObject(result);
            String errorCode = json.getString("errorCode");
            String errorMessage = json.getString("errorMessage");
            if ("0".equals(errorCode)) {
                JSONObject data = json.getJSONObject("data");
                JSONArray list = data.getJSONArray("list");
                Map parkConfigMap = parkConfigService.getMap(new QueryWrapper<ParkConfigEntity>().select("tcwz","imgbasepath").eq("tccbh",tccbh));
                String imgbasepath = (String) parkConfigMap.get("IMGBASEPATH");
                String dir = new SimpleDateFormat("yyyyMMdd").format(new Date());
                imgbasepath += File.separator + dir;
                FileUtils.creatDir(imgbasepath);
                parks = new ArrayList<ParkEntity>();
                ParkEntity parkEntity = null;
                for (int i = 0; i < list.size(); i++) {
                    JSONObject parkinfo = list.getJSONObject(i);
                    parkEntity = new ParkEntity();
                    parkEntity.setId(SysUtils.getId());
                    parkEntity.setPkey("1");
                    parkEntity.setTccbh(tccbh);
                    parkEntity.setTcwz((String) parkConfigMap.get("TCWZ"));
                    parkEntity.setScbj("0");
                    //判断出入场
                    String carout = parkinfo.getString("carOut");
                    String fxlx = ("1".equals(carout)) ? "1" : "2";
                    parkEntity.setFxlx(fxlx);
                    parkEntity.setCrkbh(("1".equals(carout)) ? "1" : "2");
                    try {
                        parkEntity.setTxsj(DateUtils.strToDate("yyyy-MM-dd HH:mm:ss", parkinfo.getString("crossTime")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String plateNo = parkinfo.getString("plateNo");
                    if (!"无车牌".equals(plateNo.trim())&&plateNo.length()>=7) {
                        parkEntity.setHphm(plateNo);
                        String nStr = plateNo.substring(plateNo.length() - 1, plateNo.length());
                        parkEntity.setHpzl("学".equals(nStr) ? "016" : "02");
                        parkEntity.setHpys("学".equals(nStr) ? "1" : "2");
                        String vehiclePicUrl = parkinfo.getString("vehiclePicUrl");
                        String imgpath = imgbasepath+File.separator + parkinfo.getString("recordUuid") + ".jpg";
                        HttpUtils.downloadCarimg(vehiclePicUrl, imgpath);
                        String param2 = null;
                        try {
                            param2 = SysUtils.byte2hex(imgpath.getBytes("UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        //String imgurl = imgneturl + "/" + param2;
                        String imgurl = imgneturl + param2;
                        parkEntity.setTplj(imgurl);
                        parks.add(parkEntity);
                    }
                }
            }
        }
        return parks;
    }
}
