package com.cmcc.vrp.boss.zhejiang.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * The class TimestampUtil for generate a local timestamp.
 */
public final class TimestampUtil {
    /**
     * The Constant UTC.
     */
    private static final String UTC = "UTC";

    /**
     * The Constant UTC_FORMAT.
     */
    private static final String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /**
     * Instantiates a new wsse util.
     */
    private TimestampUtil() {
    }

    /**
     * Gets the local utc timestamp. Get a local timestamp and the timezone is
     * UTC.
     *
     * @return the local utc timestamp
     */
    public static String getLocalUtcTimestamp() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(UTC_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone(UTC));
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取交易日期
     *
     * @return
     */
    public static String getTradeDate() {
        SimpleDateFormat haFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return haFormat.format(new Date());
    }

    /**
     * 获取清算日期
     *
     * @return
     */
    public static String getAccountDate() {
        Date date = getDateAfter(new Date(), 1);
        SimpleDateFormat haFormat = new SimpleDateFormat("yyyyMMdd");
        return haFormat.format(date);
    }

    /**
     * 获取下day天的日期
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    public static void main(String[] args) {
        String time = TimestampUtil.getTradeDate();
        System.out.println(time);
    }
}
