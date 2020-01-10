package com.qy.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;

/**
 * Created by Because of you on 2020/1/2.
 */
public class HttpUtils {
    public static HttpClient client = null;
    /**
     * 发送post请求
     */
    public static String sendPost(String _url,String param) {
        BufferedReader reader = null;
        URL url;
        try {
            url = new URL(_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.append(param);
            out.flush();
            out.close();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            String res = "";
            while((line = reader.readLine()) != null) {
                res += line;
            }
            reader.close();
            return res;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }

    /**
     * 下载图片
     * @param _url
     * @param path
     */
    public static void downloadCarimg(String _url,String path) {
        URL url = null;
        try {
            url = new URL(_url);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(5*1000);
            InputStream is = conn.getInputStream();
            byte[] bs = new byte[1024];
            int length;
            OutputStream os = new FileOutputStream(path);
            while((length=is.read(bs))!=-1) {
                os.write(bs,0,length);
            }
            os.close();
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 武江桥头(农行)停车场图片下载
     * @param loginUrl
     * @param data
     * @return
     */
    public static void downloadWjqtImg(String loginUrl, Map<String, String> data, String imgurl, String imgpath) {
        login(loginUrl, data);
        GetMethod get = new GetMethod(imgurl);
        FileOutputStream outputStream = null;
        try {
            int i = client.executeMethod(get);
            if(i==200) {
                outputStream = new FileOutputStream(imgpath);
                outputStream.write(get.getResponseBody());
            }else{
                client = null;
                login(loginUrl, data);
                client.executeMethod(get);
                outputStream = new FileOutputStream(imgpath);
                outputStream.write(get.getResponseBody());
            }
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            get.releaseConnection();
        }
    }
    /**
     * 武江桥头(农行)停车场登录
     * @param loginUrl
     * @param data
     */
    private static void login(String loginUrl,Map<String, String> data) {
        if(client==null) {
            client = new HttpClient();
            PostMethod post = new PostMethod(loginUrl);
            Set<Map.Entry<String, String>> set = data.entrySet();
            for (Map.Entry<String, String> entry : set) {
                post.addParameter(entry.getKey(), entry.getValue());
            }
            byte[] bytes;
            try {
                client.executeMethod(post);
                bytes = post.getResponseBody();
                String result = new String(bytes,"UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                post.releaseConnection();
            }
        }
    }
}
