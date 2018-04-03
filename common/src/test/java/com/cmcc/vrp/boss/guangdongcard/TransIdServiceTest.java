package com.cmcc.vrp.boss.guangdongcard;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

/**
 * 广东流量卡测试
 * <p>
 * Created by sunyiwei on 2016/11/21.
 */
public class TransIdServiceTest {

    /**
     * 测试生成唯一的序列号
     *
     * @throws Exception
     */
    @Test
    public void testBuildTransId() throws Exception {
        Set<String> strs = new TreeSet<String>();

        final int COUNT = 10;
        for (int i = 0; i < COUNT; i++) {
            Thread.sleep(1000);
            strs.add(TransIdService.buildTransId());
        }

        assertTrue(strs.size() == COUNT);

        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Pattern pattern = Pattern.compile("^ABS(\\d){14}$");
        for (String str : strs) {
            assertTrue(str.startsWith("ABS"));
            assertTrue(pattern.matcher(str).matches());
            assertTrue(df.format(new Date()).equals(str.substring(3, 3 + 8)));
        }
    }

    /**
     * 产生流水号的速率太快，导致RuntimeException
     *
     * @throws Exception
     */
    @Test(expected = RuntimeException.class)
    public void testTooFast() throws Exception {
        Set<String> strs = new TreeSet<String>();

        final int COUNT = 100;
        for (int i = 0; i < COUNT; i++) {
            strs.add(TransIdService.buildTransId());
        }
    }
}