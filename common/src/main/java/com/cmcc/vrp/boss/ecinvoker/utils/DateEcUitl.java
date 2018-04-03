package com.cmcc.vrp.boss.ecinvoker.utils;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

/**
 * Created by leelyn on 2016/7/15.
 */
public class DateEcUitl {

    /**
     * 判断时间是否在两值之间
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @return            结果
     */
    public static String minutesBetween(DateTime startTime, DateTime endTime) {
        int minutes = (Minutes.minutesBetween(startTime, endTime).getMinutes());
        return String.valueOf(minutes);
    }
}
