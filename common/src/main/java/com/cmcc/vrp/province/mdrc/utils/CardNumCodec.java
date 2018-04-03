package com.cmcc.vrp.province.mdrc.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 将字符串形式的卡号与实际的卡号进行转换， 转换规则如下：
 * <p>
 * 1.单个卡号，如1000；
 * 2.卡号序列，以英文半角“-”号分隔，如“1000-1100”，该序列包含101张卡片；
 * 3.上述两种形式的任意组合，每2个单一表示之间用英文半角“,”分隔，如“1000,1100-1200,1300,1400,1500-1700,1800”，该序列包含206张卡片
 * <p>
 * Created by sunyiwei on 2016/5/31.
 */
public class CardNumCodec {
    /**
     * @param cardNumStr
     * @return
     */
    public static List<String> decode(String cardNumStr) {
        if (org.apache.commons.lang.StringUtils.isBlank(cardNumStr)) {
            return null;
        }

        List<String> cardNums = new LinkedList<String>();
        List<String> strList = Arrays.asList(cardNumStr.split(","));
        for (String str : strList) { //先按“,”分隔
            String[] nums = str.trim().split("-"); //再按-分隔

            final int COUNT = 18;
            if (nums.length == 2) { //加入连续的卡号
                final String PREFIX = nums[0].substring(0, COUNT);

                long min = NumberUtils.toLong(nums[0].substring(COUNT), Long.MIN_VALUE);
                long max = NumberUtils.toLong(nums[1].substring(COUNT), Long.MAX_VALUE);
                if (min == Long.MIN_VALUE || max == Long.MAX_VALUE) {
                    continue;
                }

                //[min, max]
                for (long i = min; i <= max; i++) {
                    cardNums.add(PREFIX + StringUtils.leftPad(String.valueOf(i), 9, '0'));
                }
            } else {
                cardNums.add(nums[0]);
            }
        }

        return cardNums;
    }

    /**
     * @param cardNums
     * @return
     */
    public static String encode(List<String> cardNums) {
        if (cardNums == null || cardNums.isEmpty()) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String cardNum : cardNums) {
            stringBuilder.append(cardNum).append(",");
        }

        //去掉最后一个逗号
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }
}
