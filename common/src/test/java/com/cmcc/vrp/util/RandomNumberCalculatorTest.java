/**
 * @Title: RedpacketVolumeCalculatorTest.java
 * @Package com.cmcc.vrp.util
 * @author: sunyiwei
 * @date: 2015年3月18日 下午1:57:58
 * @version V1.0
 */
package com.cmcc.vrp.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @ClassName: RedpacketVolumeCalculatorTest
 * @Description: 随机数生成器的单元测试类
 * @author: sunyiwei
 * @date: 2015年3月18日 下午1:57:58
 *
 */
public class RandomNumberCalculatorTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     *
     * @Title:testGetRedpacket
     * @Description: 校验红包数大于红包总量的情况
     * @throws
     * @author: sunyiwei
     */
    @Test
    public void testGetRedpacket() {
        int count = 100;
        int volume = count - 1;

        thrown.expect(IllegalArgumentException.class);
        RandomNumberCalculator.getAverageRedpacket(count, volume);
    }

    /**
     *
     * @Title:testGetAverageRedpacketWithoutRemainder
     * @Description: 验证平均红包，整除的情况
     * @throws
     * @author: sunyiwei
     */
    @Test
    public void testGetAverageRedpacketWithoutRemainder() {
        int count = 17;
        int totalVolume = 34;

        // 产生数据
        List<Long> integers = new ArrayList<Long>();
        int nTmpTotalVolume = totalVolume;
        for (int i = 0; i < count; i++) {
            long nValue = RandomNumberCalculator.getAverageRedpacket(count - i,
                nTmpTotalVolume);
            integers.add(nValue);
            nTmpTotalVolume -= nValue;
        }

        // 校验
        int nAvg = (int) Math.floor(totalVolume / count);
        for (Long integer : integers) {
            assertEquals(integer.intValue(), nAvg);
        }
    }

    /**
     *
     * @Title:testGetAverageRedpacketWithRemainder
     * @Description: 验证平均红包，不整除的情况
     * @throws
     * @author: sunyiwei
     */
    @Test
    public void testGetAverageRedpacketWithRemainder() {
        int count = 17;
        int totalVolume = 33;

        // 产生数据
        List<Long> integers = new ArrayList<Long>();
        int nTmpTotalVolume = totalVolume;
        for (int i = 0; i < count; i++) {
            Long nValue = RandomNumberCalculator.getAverageRedpacket(count - i,
                nTmpTotalVolume);
            integers.add(nValue);
            nTmpTotalVolume -= nValue;
        }

        // 校验, 平均数，大于平均数的个数
        int nAvg = (int) Math.floor(totalVolume / count);
        long nMax = (long) (nAvg + 1);
        int nMaxCount = totalVolume % count;

        long max = Long.MIN_VALUE;
        int nActualMaxCount = 0;
        for (long integer : integers) {
            if (integer == nMax) {
                nActualMaxCount++;
            }

            if (integer > max) {
                max = integer;
            }
        }
        assertEquals(nMax, max);
        assertEquals(nActualMaxCount, nMaxCount);
    }

    /**
     * Test method for
     * {@link com.cmcc.vrp.util.RandomNumberCalculator#getRandomRedpacket(int, int)}
     * .
     */
    @Test
    public void testGetRandomRedpacket() {
        int nTotalCount = 17;
        int nTotalVolume = 200;
        double dbAvg = 1.0 * nTotalVolume / nTotalCount;

        int nTmpTotalVolume = nTotalVolume;
        List<Long> integers = new ArrayList<Long>();
        for (int i = 0; i < nTotalCount; i++) {
            Long nValue = RandomNumberCalculator.getRandomRedpacket(nTotalCount
                - i, nTmpTotalVolume);
            integers.add(nValue);
            nTmpTotalVolume -= nValue;
        }

        assertEquals(nTotalCount, integers.size());
        assertEquals(nTotalVolume, sum(integers), 0.1);
        assertEquals(dbAvg, avg(integers), 0.1);
    }

    /**
     *
     * @Title:avg
     * @Description: 平均数
     * @param array
     * @return
     * @throws
     * @author: sunyiwei
     */
    private double avg(List<Long> array) {
        int count = array.size();

        double dbAvg = 0;
        for (long integer : array) {
            dbAvg += integer;
        }
        dbAvg /= count;

        return dbAvg;
    }

    /**
     *
     * @Title:sum
     * @Description: 总量
     * @param array
     * @return
     * @throws
     * @author: sunyiwei
     */
    private double sum(List<Long> array) {
        double dbSum = 0;
        for (long integer : array) {
            dbSum += integer;
        }

        return dbSum;
    }
}
