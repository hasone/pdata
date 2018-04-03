package com.cmcc.vrp.boss.zhuowang.utils;

import java.util.Date;

/**
 * 生成唯一订单
 *
 * @author wangguangyao@chinamobile.com qihang@chinamobile.com
 * @date 2014年11月20日
 * @tag
 */
public class UIDUtil {
    private static final int ROTATION = 99999;
    private static Date date = new Date();
    private static StringBuilder buf = new StringBuilder();
    private static int seq = 0;

    /**
     * @return
     */
    public static synchronized String next() {
        if (seq > 99999) {
            seq = 0;
        }
        buf.delete(0, buf.length());
        date.setTime(System.currentTimeMillis());
        String str = String.format("%1$tY%1$tm%1$td%1$tk%1$tM%1$tS%2$05d", new Object[]{date, Integer.valueOf(seq++)});
        return str;
    }

    //生成唯一流水号，通过传过来的平台Id,platId长度固定为8
    /**
     * @param platId
     * @return
     */
    public static synchronized String next(long platId) {
        if (seq > 99999) {
            seq = 0;
        }
        buf.delete(0, buf.length());
        date.setTime(System.currentTimeMillis());
        String timestr = String.format("%1$tY%1$tm%1$td%1$tk%1$tM%1$tS%2$05d", new Object[]{date, Long.valueOf(seq++)});
        return timestr + Long.toString(platId);
    }

    //生成唯一流水号，通过传过来的平台Id和业务流水号,platId长度固定为8
    /**
     * @param platId
     * @param serialNo
     * @return
     */
    public static synchronized String next(long platId, String serialNo) {
        if (seq > 99999) {
            seq = 0;
        }
        buf.delete(0, buf.length());
        date.setTime(System.currentTimeMillis());
        String str = String.format("%1$tY%1$tm%1$td%1$tk%1$tM%1$tS%2$05d", new Object[]{date, Long.valueOf(seq++)});
        str = str + Long.toString(platId) + serialNo;
        return str;
    }
}