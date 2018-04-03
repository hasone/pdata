package com.cmcc.vrp.util;

import java.util.Random;

public class PwdGenerator {

    /**
     * 生成随即密码，密码为纯数字
     *
     * @param pwd_len 生成的密码的总长度
     * @return 密码的字符串
     */
    public static String genRandomNum(int pwd_len) {
        //35是因为数组是从0开始的，10个数字
        final int maxNum = 10;
        int i;  //生成的随机数
        int count = 0; //生成的密码的长度
        char[] str = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while (count < pwd_len) {
            //生成随机数，取绝对值，防止生成负数，
            i = Math.abs(r.nextInt(maxNum));  //生成的数最大为10-1
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count++;
            }
        }

        return pwd.toString();
    }


    /**
     * 生成随即密码
     *
     * @param pwd_len 生成的密码的总长度
     * @return 密码的字符串
     */
    public static String genRandomNum2(int pwd_len) {
        //35是因为数组是从0开始的，26个字母+10个数字
        final int maxNum = 36;
        int i;  //生成的随机数
        int count = 0; //生成的密码的长度
        char[] str = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
            'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
            'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while (count < pwd_len) {
            //生成随机数，取绝对值，防止生成负数，
            i = Math.abs(r.nextInt(maxNum));  //生成的数最大为10-1

            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count++;
            }
        }
        return pwd.toString();
    }

    /**
     * 批量生成随即密码，密码为纯数字
     *
     * @param pwd_len   生成的密码的总长度
     * @param pwd_count 生成的密码的数量
     * @return 密码的字符串
     */
    public static String[] genBatchRandomNum(int pwd_len, int pwd_count) {
        //数组是从0开始的，10个数字
        final int maxNum = 10;
        char[] str = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        String[] passwords = new String[pwd_count];
        for (int j = 0; j < pwd_count; j++) {
            int i;  //生成的随机数
            int count = 0; //生成的密码的长度
            StringBuffer pwd = new StringBuffer("");
            Random r = new Random();
            while (count < pwd_len) {
                //生成随机数，取绝对值，防止生成负数，
                i = Math.abs(r.nextInt(maxNum));  //生成的数最大为10-1
                if (i >= 0 && i < str.length) {
                    pwd.append(str[i]);
                    count++;
                }
            }
            passwords[j] = pwd.toString();

        }
        return passwords;
    }

    public static void main(String[] args) {
        String[] passwords = genBatchRandomNum(8, 1000);
        for (String password : passwords) {
            System.out.println(password);
        }
    }
}

