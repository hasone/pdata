package com.cmcc.vrp.ec.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by leelyn on 2016/5/20.
 */
public class SerialNumGenerator {
    private static Snowflake snowflake = new Snowflake();

    /**
     * @param length
     * @return
     */
    public static String buildBossReqSerialNum(int length) {
        return DigestUtils.sha1Hex(UUID.randomUUID().toString()).substring(0, length);
    }

    /**
     * 从流水号中解析日期信息
     *
     * @param sn 流水号
     */
    public static Date parseDateFromSerialNum(String sn) {
        long snl = NumberUtils.toLong(sn, -1);
        if (snl == -1) { //无效的日期
            return null;
        }

        return Snowflake.parse(snl);
    }

    /**
     * @return
     */
    public static String buildSerialNum() {
        return String.valueOf(snowflake.next());
    }

    /**
     * 使用场景 1、BOSS侧接受请求流水号 2、平台侧更新SerialNum表中bossReqSerialNum字段唯一标识
     */
    public static String buildNormalBossReqNum(String flag, int length) {
        return (flag + DigestUtils.sha1Hex(UUID.randomUUID().toString())).substring(0, length);
    }

    /**
     * 使用场景 1、BOSS侧不接受请求流水 2、平台侧更新SerialNum表中bossReqSerialNum字段的统一标识
     */
    public static String buildNullBossReqNum(String bossFlag) {
        return bossFlag + " is not accept request serailNum";
    }

    /**
     * 使用场景 1、BOSS侧不返回相应流水 2、平台侧更新SerialNum表中bossRespSerialNum字段的统一标识
     */
    public static String buildNullBossRespNum(String bossFlag) {
        return bossFlag + " is not return response serailNum";
    }

    /**
     * 生成指定位数的随机数
     */
    public static String genRandomNum(int length) {
        //35是因为数组是从0开始的，26个字母+10个数字
        final int maxNum = 36;
        int i; //生成的随机数
        int count = 0; //生成的密码的长度
        char[] str = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while (count < length) {
            //生成随机数，取绝对值，防止生成负数，
            i = Math.abs(r.nextInt(maxNum)); //生成的数最大为36-1
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count++;
            }
        }
        return pwd.toString();
    }


    public static void main(String[] args) throws InterruptedException {
        final int COUNT = 10;
        for (int i = 0; i < COUNT; i++) {
            Thread.sleep(new Random().nextInt(2000));
            String sn = SerialNumGenerator.buildSerialNum();
            Date snDate = SerialNumGenerator.parseDateFromSerialNum(sn);
            System.out.format("%s: %s%n", sn, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").format(snDate));
        }
    }
}
