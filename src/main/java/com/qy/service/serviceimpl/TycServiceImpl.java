package com.qy.service.serviceimpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qy.dao.TycDao;
import com.qy.entity.ParkConfigEntity;
import com.qy.entity.ParkEntity;
import com.qy.service.ParkConfigService;
import com.qy.service.TycService;
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
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Because of you on 2020/1/6.
 */
@Slf4j
@Service
@DS("tyc")
public class TycServiceImpl implements TycService {
    @Autowired
    private ParkConfigService parkConfigService;
    @Autowired
    private TycDao tycDao;
    @Override
    public List<ParkEntity> queryParks(Map<String, String> map, String imgneturl) {
            String[] dates = DateUtils.createDateStrs();
            List<ParkEntity> list = tycDao.queryParks(dates);
            if(list!=null&&list.size()>0) {
                String imgshareurl = map.get("imgshareurl");
                String tccbh = map.get("tccbh");
                Map parkConfigMap = parkConfigService.getMap(new QueryWrapper<ParkConfigEntity>().select("tcwz", "imgbasepath").eq("tccbh", tccbh));
                String imgbasepath = (String) parkConfigMap.get("IMGBASEPATH");
                String dir1 = DateUtils.dateToStr("yyyyMMdd", new Date());
                imgbasepath += File.separator + dir1;
                FileUtils.creatDir(imgbasepath);
                Iterator<ParkEntity> iterator = list.iterator();
                while (iterator.hasNext()) {
                    ParkEntity parkEntity = iterator.next();
                    String hphm = parkEntity.getHphm();
                    String tplj = parkEntity.getTplj();
                    if(!hphm.isEmpty()&&!tplj.isEmpty()) {
                        String tcwz = parkEntity.getTcwz();
                        if (tcwz.contains("天汇")) {
                            parkEntity.setTcwz("天汇停车场");
                            parkEntity.setTccbh("440203000000050008");
                        } else if (tcwz.contains("天悦")) {
                            parkEntity.setTcwz("天悦停车场");
                            parkEntity.setTccbh("440203000000050009");
                        } else if (tcwz.contains("中天")) {
                            parkEntity.setTcwz("中天停车场");
                            parkEntity.setTccbh("440203000000050011");
                        } else if (tcwz.contains("芙蓉湾")) {
                            parkEntity.setTcwz("芙蓉湾停车场");
                            parkEntity.setTccbh("440200000000050001");
                        } else if (tcwz.contains("花地") || tcwz.equals("云支付")) {
                            parkEntity.setTcwz("花地停车场");
                            parkEntity.setTccbh("440203000000050007");
                        } else if (tcwz.contains("芷兰湾") || tcwz.equals("A芙蓉9街湾摩托车入口")) {
                            parkEntity.setTcwz("芷兰湾停车场");
                            parkEntity.setTccbh("440203000000050010");
                        } else if (tcwz.equals("A4街8出口")) {
                            parkEntity.setTcwz("碧桂园太阳城A4街停车场");
                            parkEntity.setTccbh("440203000000050006");
                        } else if (tcwz.equals("4街1地库入口")) {
                            parkEntity.setTcwz("碧桂园太阳城4街停车场");
                            parkEntity.setTccbh("440203000000050004");
                        } else if (tcwz.equals("8街车库汽车出口")) {
                            parkEntity.setTcwz("碧桂园太阳城8街停车场");
                            parkEntity.setTccbh("440203000000050005");
                        }
                        hphm = hphm.replace("-", "");
                        parkEntity.setHphm(hphm);
                        String[] strs = tplj.split(";");
                        if (!strs[0].isEmpty()) {
                            tplj = strs[0];
                        } else {
                            tplj = strs[1];
                        }
                        String pictureName = tplj.substring(tplj.lastIndexOf("\\") + 1);
                        String d = tplj.substring(0, tplj.lastIndexOf("\\") + 1).replace("\\", "/");
                        String tmpUrl = imgshareurl + "/Images/" + d;
                        jcifs.Config.setProperty("jcifs.smb.client.disablePlainTextPasswords", "false");
                       /* SmbFile remoteFile = null;
                        try {
                            remoteFile = new SmbFile(imgshareurl);
                            remoteFile.connect();
                            ShareUtils.downloadFile(tmpUrl, imgbasepath ,pictureName);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        String pictureString = imgbasepath + File.separator + pictureName;
                        try {
                            pictureString = SysUtils.byte2hex(pictureString.getBytes("UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        String imgpath = imgneturl + pictureString;
                        parkEntity.setTplj(imgpath);
                    }else{
                        iterator.remove();
                    }
                }
            }
        return list;
    }
}
