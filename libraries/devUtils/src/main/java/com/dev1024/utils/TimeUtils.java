package com.dev1024.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类。
 *
 * @author Eric
 */
public class TimeUtils {

    /**
     * 获取当前时间的时间戳。
     *
     * @return
     */
    public static String getTimestamp() {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        return String.valueOf(ts.getTime());
    }

    public static long getTimestampSecond() {
        String timestamp = getTimestamp().substring(0, 10);
        return Long.parseLong(timestamp);
    }

    /**
     * 把时间戳转化为日期。
     *
     * @param timestampString
     * @return
     */
    public static String timeStamp2Date(String timestampString) {
        return timeStamp2Date(timestampString, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 把时间戳转化为日期。
     *
     * @param timestampString
     * @param formats
     * @return
     */
    public static String timeStamp2Date(String timestampString, String formats) {
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat(formats).format(new Date(timestamp));
        return date;
    }

    /**
     * 获取当前日前
     *
     * @param format 日期格式，如：yyyyMMdd
     * @return
     */
    public static String getDateTimeByFormat(String format) {
        return convertByFormatter(new Date(), format);
    }


    /**
     * @param date
     * @param format
     * @return
     */
    public static String convertByFormatter(final Date date, final String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
}
