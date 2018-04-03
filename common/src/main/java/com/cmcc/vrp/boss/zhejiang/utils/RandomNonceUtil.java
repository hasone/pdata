package com.cmcc.vrp.boss.zhejiang.utils;

import java.util.Random;

/**
 * The class RandomNonceUtil for generate a random string, consist of letters and figures.
 */
public final class RandomNonceUtil {

    /**
     * The Constant CHAR_ARRAY.
     */
    private static final char[] CHAR_ARRAY = new char[]{'0', '1', '2', '3',
        '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
        'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
        'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
        'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
        'U', 'V', 'W', 'X', 'Y', 'Z'};

    /**
     * Instantiates a new random nonce util.
     */
    private RandomNonceUtil() {
    }

    /**
     * Gen string random nonce, the length is 25.
     * take a random string: the letters and numbers
     *
     * @return the string
     */
    public static String genRandomNonce() {
        return genRandomNonce(25);
    }

    /**
     * Gets the random string.
     * take a random string: the letters and numbers
     *
     * @param length the length
     * @return the random string
     */
    private static String genRandomNonce(int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        int nextPos = CHAR_ARRAY.length;
        int tmp = 0;
        for (int i = 0; i < length; i++) {
            tmp = random.nextInt(nextPos);
            sb.append(CHAR_ARRAY[tmp]);
        }

        return sb.toString();
    }
}
