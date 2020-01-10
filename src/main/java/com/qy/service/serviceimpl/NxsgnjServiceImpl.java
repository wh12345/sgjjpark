package com.qy.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qy.entity.ParkConfigEntity;
import com.qy.entity.ParkEntity;
import com.qy.service.NxsgnjService;
import com.qy.service.ParkConfigService;
import com.qy.utils.DateUtils;
import com.qy.utils.FileUtils;
import com.qy.utils.SysUtils;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Because of you on 2020/1/2.
 */
@Slf4j
@Service
public class NxsgnjServiceImpl implements NxsgnjService {
    public static Date cookieCreateDate = new Date();
    public static Map<String,String> cookieMap;
    @Autowired
    private ParkConfigService parkConfigService;
    @Override
    public List<ParkEntity> queryWebParks(Map<String, String> map,String imgneturl) {
        List<ParkEntity> list = null;
        String tccbh = map.get("tccbh");
        String url = map.get("url");
        String tess4jpath = map.get("tess4jpath");
        String user = map.get("user");
        String pwd = map.get("pwd");
        Map<String,String> data = null;
        try {
            Long diffValue = DateUtils.computeBetweenDateHour(cookieCreateDate,new Date());
            if(cookieMap==null||diffValue>12L) {
                cookieMap = this.login(url,user,pwd,tess4jpath);
            }
            data = new HashMap();
            data.put("dateTime1", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss",DateUtils.getDateByDiff(new Date(),-3)));
            data.put("dateTime2", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss",new Date()));
            data.put("op", "r203");
            data.put("search", "1");
            data.put("gotoPage", "1");
            Connection.Response response = Jsoup.connect(url+"/recordSearch").data(data).cookies(cookieMap).method(Connection.Method.POST).execute();
            Document document = response.parse();
            Elements tbodyElements = document.getElementById("carTypesTable").getElementsByTag("tbody");
            Elements trElements = tbodyElements.get(0).getElementsByTag("tr");
            if(trElements!=null&&trElements.size()>0) {
                list = new ArrayList<ParkEntity>();
                ParkEntity parkEntity = null;
                Map parkConfigMap = parkConfigService.getMap(new QueryWrapper<ParkConfigEntity>().select("tcwz","imgbasepath").eq("tccbh",tccbh));
                String imgbasepath = (String) parkConfigMap.get("IMGBASEPATH");
                String dateStr = DateUtils.dateToStr("yyyyMMdd",new Date());
                imgbasepath += File.separator+dateStr;
                FileUtils.creatDir(imgbasepath);
                String tcwz = (String)parkConfigMap.get("TCWZ");
                for(int i=0;i<trElements.size();i++) {
                    parkEntity = new ParkEntity();
                    Elements tdElements = trElements.get(i).getElementsByTag("td");
                    String hphm = tdElements.get(1).text();
                    String timeString = tdElements.get(2).text();
                    String flag = tdElements.get(4).text();
                    String href = tdElements.get(6).getElementsByTag("a").get(0).attr("href");
                    String id = href.substring(href.indexOf("('")+2, href.indexOf("')"));
                    id = id.split("',")[0];
                    log.info("南雄市公安局获取过车信息hphm:"+hphm+" timeString"+timeString+" flag"+flag+" id:"+id);
                    data = new HashMap();
                    data.put("op", "showRegPlateDetail");
                    data.put("id", id);
                    data.put("wpic", "1");
                    Connection.Response imgResponse =Jsoup.connect(url+"/recordSearch").data(data).cookies(cookieMap).method(Connection.Method.POST).execute();
                    Document document1 = imgResponse.parse();
                    Elements imgElements = document1.getElementsByTag("img");
                    String p = imgElements.get(1).attr("src");
                    String imgurl = url+p;
                    byte[] imgdata = Jsoup.connect(imgurl).ignoreContentType(true).cookies(cookieMap).execute().bodyAsBytes();
                    String imgname = DateUtils.dateToStr("yyyyMMddHHmmss",new Date())+"_"+hphm+".jpg";
                    FileUtils.saveImg(imgdata, imgbasepath,imgname);
                    String imgpath = imgbasepath + File.separator+imgname;
                    imgpath = SysUtils.byte2hex(imgpath.getBytes("UTF-8"));
                    //String param = imgneturl+"/"+imgpath;
                    String param = imgneturl+imgpath;
                    parkEntity.setTplj(param);
                    try {
                        Date date = new SimpleDateFormat("yy年MM月dd日 HH时mm分ss秒").parse(timeString);
                        if("入口".equals(flag)) {
                            parkEntity.setFxlx("1");
                            parkEntity.setTxsj(date);
                        }else{
                            parkEntity.setFxlx("2");
                            parkEntity.setTxsj(date);
                        }
                        parkEntity.setId(SysUtils.getId());
                        parkEntity.setPkey("1");
                        parkEntity.setTccbh(tccbh);
                        parkEntity.setHphm(hphm);
                        parkEntity.setTcwz(tcwz);
                        parkEntity.setCrkbh("1");
                        parkEntity.setHpys("2");
                        parkEntity.setHpzl("02");
                        parkEntity.setScbj("0");
                        list.add(parkEntity);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            cookieMap = null;
            e.printStackTrace();
        } finally {
            return list;
        }
    }

    /**
     *
     * @param url
     * @param user
     * @param pwd
     * @param tess4jpath
     * @return
     */
    @Override
    public Map login(String url, String user, String pwd,String tess4jpath) {
        Map<String,String> map = null;
        Connection.Response LoginResponse = null;
        try {
            LoginResponse = Jsoup.connect(url).method(Connection.Method.GET).execute();
            map = LoginResponse.cookies();
            Document document = LoginResponse.parse();
            Element element = document.getElementById("identityImg");
            String codeimgurl = url+element.attr("src");
            String codeimgpath = tess4jpath+"\\codeimg";
            //爬验证码图片
            byte[] codeimgdata = Jsoup.connect(codeimgurl).ignoreContentType(true).execute().bodyAsBytes();
            FileUtils.saveImg(codeimgdata, codeimgpath, "codeimg.jpg");
            //识别样本输出地址
            String ocrResult = codeimgpath+"\\codetmpimgtmp.jpg";
            String OriginalImg = codeimgpath+"\\codeimg.jpg";
            //去噪点
            FileUtils.removeBackground(OriginalImg, ocrResult);
            ITesseract instance =new Tesseract();
            instance.setDatapath(tess4jpath);
            File imgDir =new File(ocrResult);
            String code = instance.doOCR(imgDir);
            String vc = document.getElementById("vc").attr("value");
            String vl = document.getElementById("vl").attr("value");
            Map datas = new HashMap();
            datas.put("user", user);
            datas.put("pwd", pwd);
            datas.put("ident",code);
            //设置cookie有效期是12小时
            datas.put("cookietime", "12");
            datas.put("vc", vc);
            datas.put("vl", vl);
            log.info("南雄市公安局爬虫登录会话"+map);
            Connection connection = Jsoup.connect(url+"/login?op=userLogin");
            Connection.Response response1 = connection.data(datas).cookies(map).method(Connection.Method.POST).execute();
            map  = response1.cookies();
            cookieCreateDate = new Date();
        } catch (IOException e) {
            map = null;
            e.printStackTrace();
        } catch (TesseractException e) {
            map = null;
            e.printStackTrace();
        }finally {
            return map;
        }
    }
}
