package com.cmcc.vrp.boss.jiangxi.util;

import com.huawei.eidc.slee.security.Base64;
import com.huawei.eidc.slee.security.DESTools;
import com.huawei.eidc.slee.security.MD5;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午10:58:48
*/
public class CommonMessageTool {
    /**
     * 对字符串进行加密
     * 1. 数字签名保证信息完整性
     * 2. 3DES加密保证不可阅读性
     * 3. BASE64编码
     * Base64( 3DES( MD5( 消息体 ) + 消息体)
     *
     * @param str String
     * @return String
     */
    public static String encrypt(String str, String desSecret) {
        String body = null;

        try {
            DESTools des = DESTools.getInstance(desSecret);
            System.out.println("加密前的消息体:");
            System.out.println(str);
            String md5str = MD5.md5(str);
            System.out.println("MD5后的签名:");
            System.out.println(md5str);
            String enStr = md5str + str;
            byte[] b = des.encrypt(enStr.getBytes("UTF8"));
            System.out.println("DES后的字节数组:");
            printBytes(b);
            body = Base64.encode(b);
            System.out.println("Base64后的字符串:");
            System.out.println(body);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return body;
    }

    /**
     * 对字符串进行解密
     * Base64( 3DES( MD5( 消息体 ) + 消息体) 逆操作
     *
     * @param str String
     * @return String
     * 解密时判断数据签名，如果不匹配则返回null
     */
    public static String decrypt(String str, String desSecret) {
        String body = null;
        try {
            DESTools des = DESTools.getInstance(desSecret);
            System.out.println("对方传来的消息源串：");
            System.out.println(str);
            byte[] b = Base64.decode(str);
            System.out.println("Base64解码之后的结果：");
            printBytes(b);
            String md5body = new String(des.decrypt(b), "UTF8");
            System.out.println("3DES解密后的结果：");
            System.out.println(md5body);
            if (md5body.length() < 32) {
                System.out.println("错误！消息体长度小于数字签名长度!");
                return null;
            }
            String md5Client = md5body.substring(0, 32);
            System.out.println("对方传来的数字签名：");
            System.out.println(md5Client);
            body = md5body.substring(32);
            System.out.println("对方传来的消息体：");
            System.out.println(body);
            String md5Server = MD5.md5(body);
            System.out.println("对消息体MD5后的数字签名：");
            System.out.println(md5Server);
            if (!md5Client.equals(md5Server)) {
                System.out.println("错误！数字签名不匹配:");
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return body;
    }

    /**
     * 打印字节数组
     *
     * @param b byte[]
     */
    private static void printBytes(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append("[" + b[i] + "],");
            if (i % 15 == 0) {
                sb.append("\n");
            }
        }
        System.out.println(sb);
    }
}
