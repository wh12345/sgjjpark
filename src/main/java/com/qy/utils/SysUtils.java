package com.qy.utils;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Because of you on 2019/12/30.
 */
public class SysUtils {
    /**
     * 任务字符串参数转map
     * @param params
     * @return
     */
    public static Map<String,String> parseParams(String params) {
        Map<String,String> map = new HashMap<String,String>();
        String[] array = params.split(",");
        for (String value:array) {
            String[] tmpArray = value.split("=");
            map.put(tmpArray[0],tmpArray[1]);
        }
        return map;
    }

    /**
     * 获取uuid
     * @return
     */
    public static String getId() {
        return UUID.randomUUID().toString();
    }


    /**
     * 字符串转base64字符串
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        String hs="";
        String stmp="";
        for (int n=0;n<b.length;n++){
            stmp=(Integer.toHexString(b[n] & 0XFF));
            if (stmp.length()==1)
                hs=hs+"0"+stmp;
            else hs=hs+stmp;
            //if (n<b.length-1)  hs=hs+":";
        }
        return hs.toUpperCase();
    }

    /**
     * base64字符串转为数组
     * @param h
     * @return
     */
    public static byte[] hex2byte(String h) {
        byte[] ret = new byte[h.length() / 2];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = Integer.decode("#" + h.substring(2 * i, 2 * i + 2)).byteValue();
        }
        return ret;
    }

    public static void main(String[] args) throws ParseException {
    }
}
