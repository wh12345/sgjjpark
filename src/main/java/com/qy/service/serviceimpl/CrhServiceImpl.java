package com.qy.service.serviceimpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qy.dao.CrhDao;
import com.qy.entity.ParkConfigEntity;
import com.qy.entity.ParkEntity;
import com.qy.service.CrhService;
import com.qy.service.ParkConfigService;
import com.qy.utils.DateUtils;
import com.qy.utils.FileUtils;
import com.qy.utils.ShareUtils;
import com.qy.utils.SysUtils;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Because of you on 2020/1/3.
 */
@Slf4j
@Service
@DS("crh")
public class CrhServiceImpl implements CrhService {
    @Autowired
    private CrhDao crhDao;
    @Autowired
    private ParkConfigService parkConfigService;
    public List<ParkEntity> queryParks(Map<String,String> map, String imgneturl){
            String tccbh = map.get("tccbh");
            String imgshareurl = map.get("imgshareurl");
            String[] dates = DateUtils.createDateStrs();
            List<ParkEntity> list = crhDao.queryParks(dates);
            //下载图片，更改参数
            if(list!=null&&list.size()>0) {
                Map parkConfigMap = parkConfigService.getMap(new QueryWrapper<ParkConfigEntity>().select("tcwz","imgbasepath").eq("tccbh",tccbh));
                String imgbasepath = (String) parkConfigMap.get("IMGBASEPATH");
                String dir1 = DateUtils.dateToStr("yyyyMMdd",new Date());
                imgbasepath+=File.separator+dir1;
                FileUtils.creatDir(imgbasepath);
                for (ParkEntity park : list) {
                    park.setId(SysUtils.getId());
                    park.setTccbh(tccbh);
                    park.setTcwz((String) parkConfigMap.get("TCWZ"));
                    String pictureName = park.getTplj();
                    //可能放在这个文件夹中
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE,1);
                    String dir2 = new SimpleDateFormat( "yyyy-MM-dd").format(calendar.getTime());
                    SmbFile remoteFile =null;
                    try {
                        remoteFile = new SmbFile(imgshareurl);
                        log.info("高铁站共享url是否存在:"+new SmbFile(imgshareurl).exists());
                        remoteFile.connect();
                        ShareUtils.downloadFile(imgshareurl, imgbasepath ,pictureName);
                        String fx = "enter";
                        if("2".equals(park.getFxlx())) {
                            fx = "out";
                        }
                        ShareUtils.downloadFile(imgshareurl+"/"+fx+"/"+dir1+"/", imgbasepath ,pictureName);
                        ShareUtils.downloadFile(imgshareurl+"/"+fx+"/"+dir2+"/", imgbasepath ,pictureName);
                        String imgpath = imgbasepath+ File.separator+pictureName;
                        String param = SysUtils.byte2hex(imgpath.getBytes("UTF-8"));
                        String imgurl = imgneturl+param;
                        park.setTplj(imgurl);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (SmbException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        return list;
    }
}
