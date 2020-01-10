package com.qy.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qy.entity.ParkConfigEntity;
import com.qy.service.ParkConfigService;
import com.qy.service.WemService;
import com.qy.utils.DateUtils;
import com.qy.utils.FileUtils;
import com.qy.utils.ShareUtils;
import com.qy.utils.SysUtils;
import jcifs.smb.SmbFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Because of you on 2020/1/1.
 */
@Slf4j
@Service
public class WemServiceImpl implements WemService {
    @Autowired
    private ParkConfigService parkConfigService;
    public static String  imgDateFlag = "";
    public static Map<String, String>  imgMap = new HashMap<String, String>();

    @Override
    public Map<String, String> getShareImg(Map<String,String> map,String imgneturl) {
        Map<String,String> resultMap = null;
        String dateStr = DateUtils.dateToStr("yyyyMMdd",new Date());
        if(!dateStr.equals(imgDateFlag)) {
            imgMap.clear();
            imgDateFlag = dateStr;
        }
        String tccbh = map.get("tccbh");
        Map parkConfigMap = parkConfigService.getMap(new QueryWrapper<ParkConfigEntity>().select("imgbasepath").eq("tccbh",tccbh));
        String imgshareurl = map.get("imgshareurl");
        imgshareurl +="/"+dateStr+"/";
        String imgbasepath = (String) parkConfigMap.get("IMGBASEPATH");
        imgbasepath += File.separator+dateStr;
        FileUtils.creatDir(imgbasepath);
        SmbFile remoteFile =null;
        try {
            remoteFile = new SmbFile(imgshareurl);
            remoteFile.connect();
            String[] files = remoteFile.list();
            if(files!=null) {
                for (String fileStr:files) {
                    File file = new File(imgbasepath + File.separator + fileStr);
                    if(!imgMap.containsKey(fileStr)&&!file.exists()) {
                        ShareUtils.downloadFile(imgshareurl, imgbasepath, fileStr);
                        String imgname = fileStr.substring(0, fileStr.indexOf('.'));
                        resultMap = new HashMap<String,String>();
                        resultMap.put("imgname",imgname);
                        String param = imgbasepath+ "\\" +dateStr + "\\" + imgname+".jpg";
                        param = SysUtils.byte2hex(param.getBytes("UTF-8"));
                        //String imgurl = imgneturl+"/"+param;
                        String imgurl = imgneturl+param;
                        resultMap.put("imgurl",imgurl);
                        imgMap.put(fileStr,file.toString());
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return resultMap;
        }
    }
}
