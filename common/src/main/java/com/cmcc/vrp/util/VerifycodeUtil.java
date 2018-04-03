package com.cmcc.vrp.util;

import java.util.Random;

public class VerifycodeUtil {
    public static final Random random = new Random();
    private static final char[] CHARS = {'2', '3', '4', '5', '6', '7', '8',
        '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M',
        'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static final char[] NUMS = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
        '9'};

    /**
     * @return String
     * @Title: getRandomString
     * @Description: 得到4位随机字母
     */
    public static String getRandomString() {
        return getRandomString(4);
    }

    /**
     * 根据指定长度生成随机字符串
     * <p>
     *
     * @param size
     * @return
     */
    public static String getRandomString(int size) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < size; i++) {
            buffer.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return buffer.toString();
    }

    public static String getRandomNum() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            buffer.append(NUMS[random.nextInt(NUMS.length)]);
        }
        return buffer.toString();
    }
}
