package com.qy.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * Created by Because of you on 2020/1/2.
 */
public class DateUtils {
    /**
     * 获取当前时间字符串、前一分钟时间字符串、前两分钟时间字符串
     * @return
     */
    public static String[] createDateStrs(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date1 = sdf.format(cal.getTime());
        cal.add(Calendar.MINUTE,-1);
        String date2 = sdf.format(cal.getTime());
        cal.add(Calendar.MINUTE,-1);
        String date3 = sdf.format(cal.getTime());
        return new String[]{date1,date2,date3};
    }

    /**
     * 计算两个时间相差多少个小时
     * @param date1
     * @param date2
     * @return
     */
    public static Long computeBetweenDateHour(Date date1, Date date2) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long diff = date2.getTime() - date1.getTime();
        Long  result = diff%nd/nh;
        return result;
    }

    /**
     * 获取相差多少分钟的时间
     * @param date
     * @param diff
     * @return
     */
    public static Date getDateByDiff(Date date,int diff) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE,diff);
        return calendar.getTime();
    }
    /**
     * 返回日期字符串
     * @param format
     * @param date
     * @return
     */
    public static String dateToStr(String format,Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static Date strToDate(String format,String str) throws ParseException {
        return new SimpleDateFormat(format).parse(str);
    }
}
