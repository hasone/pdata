/**
 * @Title: RedpacketVolumeCalculator.java
 * @Package com.cmcc.vrp.util
 * @author: sunyiwei
 * @date: 2015年3月18日 上午10:10:40
 * @version V1.0
 */
package com.cmcc.vrp.util;

import java.util.Random;

/**
 * @ClassName: RedpacketVolumeCalculator
 * @Description: 红包流量大小计算器，支持平均算法和随机算法
 * @author: sunyiwei
 * @date: 2015年3月18日 上午10:10:40
 *
 */
public class RandomNumberCalculator {
    /**
     * 每个红包最小值，单位为M
     */
    private final static double minVolume = 1;

    /**
     *
     * @Title:getAverageRedpacket
     * @Description: 获取平均红包，最小为1M
     * @param count
     * @param totalVolume
     * @return
     * @throws
     * @author: sunyiwei
     */
    public static long getAverageRedpacket(long count, long totalVolume)
        throws IllegalArgumentException {
        return getRedpacketVolume(count, totalVolume,
            Constants.ENTREDPACKET_MODE.AVERAGE);
    }

    /**
     *
     * @Title:getRandomRedpacket
     * @Description: 获取随机红包, 最小为1M
     * @param count
     * @param totalVolume
     * @return
     * @throws IllegalArgumentException
     * @throws
     * @author: sunyiwei
     */
    public static long getRandomRedpacket(long count, long totalVolume)
        throws IllegalArgumentException {
        return getRedpacketVolume(count, totalVolume,
            Constants.ENTREDPACKET_MODE.RANDOM);
    }

    private static long getRedpacketVolume(long count, long totalVolume,
                                           Constants.ENTREDPACKET_MODE mode) throws IllegalArgumentException {
        int nMaxCount = (int) Math.ceil(totalVolume / minVolume);
        if (count <= 0 || count > nMaxCount) {
            throw new IllegalArgumentException("红包个数不能少于红包总量，每个红包最小为"
                + minVolume + "M!");
        }

        if (mode == Constants.ENTREDPACKET_MODE.AVERAGE) {
            return getAverage(count, totalVolume);
        } else if (mode == Constants.ENTREDPACKET_MODE.RANDOM) {
            return getRandom(count, totalVolume);
        } else {
            return 0;
        }
    }

    /**
     *
     * @Title:getRandom
     * @Description: 随机红包分配算法， 目标
     * 1、每个人都要能够领取到红包; 　
     * 2、每个人领取到的红包金额总和=总金额;
     * 3、每个人领取到的红包金额不等，但也不能差的太离谱;
     *  实现算法：
     *  1. 每个人最少领到minVolume的红包
     *  2. 随机模型采用N(avg, sigma)模型， 其中avg为红包的期望，sigma为方差，待定
     *  3. 根据正态分布理论，按照2中规定的随机模型产生的数值：
     *  	位于1倍方差的范围的概率为： 68.26% ==> [avg-sigma, avg+sigma]
     *  	位于2倍方差的范围的概率为： 95.44% ==> [avg-2*sigma, avg+2*sigma]
     *  	位于3倍方差的范围的概率为： 99.74% ==> [avg-3*sigma, avg+3*sigma]
     *
     *  结合这些分析及红包的特点，这里将sigma的值固定为avg
     *
     * @param count
     * @param totalVolume
     * @return
     * @throws
     * @author: sunyiwei
     */
    private static long getRandom(long count, long totalVolume) {
        if (count == 1) {
            return totalVolume;
        }

        Random random = new Random();
        double dbAverage = totalVolume / (1.0 * count);
        double dbSigma = dbAverage;

        double dbRestValue = minVolume * (count - 1);
        double dbMax = totalVolume - dbRestValue;

        double dbRndNum;
        do {
            dbRndNum = dbSigma * random.nextGaussian() + dbAverage;
        } while (dbRndNum < minVolume || dbRndNum > dbMax);

        return (long) Math.floor(dbRndNum);
    }

    /**
     *
     * @Title:getAverage
     * @Description: 平均红包分配算法
     * @param count
     * @param totalVolume
     * @return
     * @throws
     * @author: sunyiwei
     */
    private static long getAverage(long count, long totalVolume) {
        return (long) Math.floor(totalVolume / (1.0 * count));
    }

}
