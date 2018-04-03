package com.cmcc.vrp.province.service.impl;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * 测试工具方法类
 *
 * Created by sunyiwei on 2016/10/11.
 */
public class BaseTest {
    protected String randStr(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            stringBuilder.append((char) (random.nextInt(26) + 'a'));
        }

        return stringBuilder.toString();
    }

    protected File createTempFile() throws IOException {
        return File.createTempFile("prefix", "suffix");
    }

    protected String randStr() {
        return randStr(16);
    }

    protected Long randLong() {
        return new Random().nextLong();
    }

    protected int randInt() {
        return new Random().nextInt(100) + 10;
    }

    protected String randMobile() {
        int value = new Random().nextInt(10000);
        return "138" + StringUtils.rightPad(String.valueOf(value), 8, "0");
    }
}
