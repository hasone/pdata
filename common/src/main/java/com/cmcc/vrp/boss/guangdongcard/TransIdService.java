package com.cmcc.vrp.boss.guangdongcard;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 广东流量卡交易流水号服务
 * <p>
 * Created by sunyiwei on 2016/11/21.
 */
public final class TransIdService {
    private final static int MAX_SEQ = 10;
    private static long lastSec = 0l;
    private static int lastSeq = 0;

    /**
     * 流水号规则： ABS + 8位日期 + 6位唯一数
     *
     * @return
     */
    public static String buildTransId() {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());

        return "ABS" + date + buildSeq();
    }

    private static String buildSeq() {
        long currentSec = new DateTime().getSecondOfDay();
        if (currentSec == lastSec) {
            lastSeq++;
            if (lastSeq >= MAX_SEQ) {
                throw new RuntimeException("超过每秒的频率限制.");
            }
        } else {
            lastSeq = 0;
        }

        lastSec = currentSec;
        return String.format("%05d", lastSec) + String.valueOf(lastSeq);
    }
}
