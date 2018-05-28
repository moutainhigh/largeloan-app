package com.xianjinxia.cashman.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Created by MJH on 2017/9/11.
 */
public class DateUtil {


    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private static final DateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final DateFormat formatHaveZh = new SimpleDateFormat("yyyy年MM月dd日");

    public static Date addDay(Date sourceDate, int addDay) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(sourceDate);
        cl.add(Calendar.DAY_OF_MONTH, addDay);
        return cl.getTime();
    }

    public static Date addMonth(Date sourceDate, int month) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(sourceDate);
        cl.add(Calendar.MONTH, month);
        return cl.getTime();
    }

    public static void main(String[] args) {
//        System.out.println(yyyyMMdd(addMonth(new Date(), 2)));
        try {
            Date d1 = format.parse("2018-05-10");
            Date d2 = format.parse("2018-05-11");
            int i = daysBetween(d1, d2);
            System.out.println("相差天数：" + i);
            String d3 = "2018-05-21 19:51:11";
            Date date = yyyyMMddHHmmss.parse(d3);
            System.out.println("");

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static String yyyyMMdd() {
        return yyyyMMdd(new Date());
    }

    public static Date yyyyMMdd2Date(String yyyyMMdd) {
        try {
            return format.parse(yyyyMMdd);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Date yyyyMMddHHmmss2Date(String yyyyMMddHHmmss) {
        try {
            if (yyyyMMddHHmmss == null || "".equals(yyyyMMddHHmmss)) {
                return new Date();
            }
            return format.parse(yyyyMMddHHmmss);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Date yyyyMMddHHmmssToDate(String yyyyMMddHHmmss_) {
        try {
            return yyyyMMddHHmmss.parse(yyyyMMddHHmmss_);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String yyyyMMddHHmmss(Date date) {
        return yyyyMMddHHmmss.format(date);
    }

    public static String yyyyMMdd(Date date) {
        return format.format(date);
    }

    public static Date daysBefore(Date date, int before) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.set(Calendar.DATE, cl.get(Calendar.DATE) - before);
        return cl.getTime();
    }

    public static Date minutesAfter(Date date, int minutes) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(Calendar.MINUTE, minutes);
        return cl.getTime();
    }

    public static Date dateFilter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static int daysBetween(Date beginDate, Date endDate) {
        beginDate = dateFilter(beginDate);
        endDate = dateFilter(endDate);


        Calendar cal = Calendar.getInstance();
        cal.setTime(beginDate);
        long begin = cal.getTimeInMillis();
        cal.setTime(endDate);
        long end = cal.getTimeInMillis();
        long between_days = (end - begin) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 判断指定日期是不是今天
     * @param date 待判断的日期
     * @return boolean
     * */
    public static boolean isToday(Date date){
        return Objects.isNull(date) ? false : DateUtils.isSameDay(Calendar.getInstance().getTime(),date);
    }

    /**
     * 转换日期为为 ****年**月**日
     * @param date
     * @return
     */
    public static String yyyyMMddForZh(Date date) {
        return formatHaveZh.format(date);
    }
}
