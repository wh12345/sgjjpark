package com.qy.controller;

import com.qy.utils.SysUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Because of you on 2019/12/31.
 */
@Slf4j
@Controller
@RequestMapping(value="/sgjjpark")
public class ResourceController {
    public static Map<String, String> showMaps = new HashMap<String, String>(){{
        put(".png", "image/png");
        put(".gif", "image/gif");
        put(".jpeg", "image/jpeg");
        put(".bmp", "image/bmp");
        put(".jpg", "image/jpeg");
        put(".mp4", "video/mp4");
        put(".flv", "video/flv");
        put(".avi", "video/avi");
    }};
    @RequestMapping(value = "/show/{para}",produces = MediaType.IMAGE_JPEG_VALUE,method = RequestMethod.GET)
    @ResponseBody
    public void show(@PathVariable String para, HttpServletResponse response){
        String parameter;
        try {
            parameter = new String(SysUtils.hex2byte(para),"UTF-8");
            log.info("resource-文件:"+parameter);
            if(new File(parameter).exists()) {
                String sb =parameter.substring(parameter.indexOf("."),parameter.length());
                if (showMaps.get(sb) != null) {
                    response.setContentType(showMaps.get(sb));
                    FileInputStream fis = new FileInputStream(parameter);
                    byte data[]=new byte[fis.available()];
                    fis.read(data);  //读数据
                    fis.close();
                    OutputStream os = response.getOutputStream();
                    os.write(data);
                    os.flush();
                    os.close();
                }else {
                    response.setContentType("text/html;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    writer.print("此类型不支持!");
                }
            }else{
                response.setContentType("text/html;charset=utf-8");
                PrintWriter writer = response.getWriter();
                writer.print("该文件不存在!");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
