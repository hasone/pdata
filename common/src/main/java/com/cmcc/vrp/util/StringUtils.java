/**
 * @Title: StringUtils.java
 * @Package com.cmcc.vrp.util
 * @author: sunyiwei
 * @date: 2015年3月11日 下午4:25:25
 * @version V1.0
 */
package com.cmcc.vrp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: StringUtils
 * @Description: 工具类
 * @author: sunyiwei
 * @date: 2015年3月11日 下午4:25:25
 */
public class StringUtils {
    public static final String LOWER_UPPER_UNDERLINE_NUMBER_REGEX = "^(?![a-zA-Z0-9]+$)(?![^a-zA-Z/D]+$)(?![^0-9/D]+$).{10,20}$";
    public static final String MOBILE_REGEX = "^1[3-8][0-9]{9}$";
    public final static String EMAIL_REGEX = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)"
            + "|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    public static final int OPEN_ID_LENGTH = 28;

    public static boolean isValidMobile(String mobile) {
        if (org.apache.commons.lang.StringUtils.isBlank(mobile)) {
            return false;
        }

        return Pattern.compile(MOBILE_REGEX).matcher(mobile).matches();
    }

    /**
     * @param szPassword 密码字符串
     * @return
     * @throws
     * @Title:isValidPassword
     * @Description: 验证一个字符串是否为有效的密码 有效的密码为： 6~16位的 字母、数字及下划线组成的字符
     * @author: sunyiwei
     */
    public static boolean isValidPassword(String szPassword) {
        if (szPassword == null) {
            return false;
        }

        return szPassword.length() >= 10 && szPassword.length() <= 20
                && Pattern.compile(LOWER_UPPER_UNDERLINE_NUMBER_REGEX).matcher(szPassword).matches();
    }

    /**
     * @param str 字符串
     * @return
     * @throws
     * @Title:isNumeric
     * @Description: 判断String是否是数字，静态方法
     * @author:
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * @param str 字符串
     * @return
     * @throws
     * @Title:isEmpty
     * @Description: 判断String是否为空，静态方法
     * @author:
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * @param string
     * @return
     * @throws
     * @Title:trimString
     * @Description: 截取字符串前后的空格符，如果为null，直接返回
     * @author: sunyiwei
     */
    public static String trimString(String string) {
        if (string == null) {
            return null;
        }

        return string.trim();
    }

    /**
     * @param string
     * @return
     * @throws
     * @Title:trimAllString
     * @Description: 替换掉字符串里所有空格，如果为null，直接返回
     * @author:
     */
    public static String trimAllString(String string) {
        if (string == null) {
            return null;
        }
        return string.replaceAll(" ", "");
    }

    /**
     * 
     * @Title: randomString 
     * @Description: TODO
     * @param length
     * @return
     * @return: String
     */
    public static String randomString(long length) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            stringBuilder.append((char) ('a' + random.nextInt(26)));
        }

        return stringBuilder.toString();
    }

    /**
     * 根据指定分割符分割字符串,去除空字符
     *
     * @param str
     * @param regex
     * @return
     * @Title:split
     * @author: hexinxu
     */
    public static String[] split(String str, String regex) {

        String[] arry = null;
        if (org.apache.commons.lang.StringUtils.isNotBlank(str) && regex != null) {
            List<String> list = new ArrayList<String>();
            String[] strs = str.split(regex);
            for (String s : strs) {
                if (!isEmpty(s)) {
                    list.add(s);
                }
            }
            arry = new String[list.size()];
            list.toArray(arry);
        }
        return arry != null ? arry : (arry = new String[0]);
    }

    /**
     * 
     * @Title: isValidEmail 
     * @Description: 邮箱校验
     * @param email
     * @return
     * @return: boolean
     */
    public static boolean isValidEmail(String email) {
        if (org.apache.commons.lang.StringUtils.isBlank(email) || email.length() > 128) {
            return false;
        }
        Pattern p = Pattern.compile(EMAIL_REGEX);
        Matcher m = p.matcher(email);
        return m.matches();
    }
    
    /**
     * 判断字符串是否含有非数字或非字母字符
     * 
     * @param str
     * @return
     */
    public static boolean isHasSymble(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return !str.matches("^[\\da-zA-Z]*$");
    }
    
    /*
     * 判断是否为有效的微信openId, 判断规则为长度为28
     * @author qinqinyan
     */
    public static boolean isValidOpenId(String openId){
        if(org.apache.commons.lang.StringUtils.isBlank(openId) 
                || openId.length() != OPEN_ID_LENGTH){
            return false;
        }
        return true;
    }
    

    public static void main(String[] args) {
        //		String [] strs = split(" a ,a,a", ",");
        //		for(String s:strs){
        //			System.out.println(s);
        //		}
        String mobile = "18867101970";
        //System.out.println(isValidMobile(mobile));
        //System.out.println(isHasSymble("112356%abcdefg"));
        System.out.println(isValidPassword("1234fff$33"));
        //System.out.println(randomString(32));
    }
}
