package com.cmcc.vrp.boss.jilin.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.PrivateKey;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年4月1日 上午8:54:47
*/
public class Sign {
    public static void main(String[] args) {
        //String string = toSign("123456");
        //System.out.println(string);
    }
    /**
     * @param orginStr
     * @return
     */
    public static String toSign(String orginStr, String skprivateKeyFile) {
        String content = orginStr;
        String signUseSitechKey = null;
        String enQryStr = null;
//        String skprivateKeyFile = "D:\\airreCharge_private_key.pem";// 私钥//wgyx_private_key.pem
        PrivateKey skPrivateKey = RSAUtils.fileToPrivateKey(skprivateKeyFile);
        try {
            content = StringUtil.sortOrginReqStr(content);
            System.out.println("转码前content：" + content);
            enQryStr = URLEncoder.encode(content, "UTF-8");
            System.out.println("转码后enQryStr：" + enQryStr);
            String md5Str = MD5.toMD5(enQryStr);
            System.out.println("md5Str::::" + md5Str);
            signUseSitechKey = RSAUtils.sign(md5Str.getBytes(), skPrivateKey);
            signUseSitechKey = URLEncoder.encode(signUseSitechKey, "UTF-8");// 本地测试可以不用encode
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("参数：" + content);
        System.out.println("sign：" + signUseSitechKey);
        return signUseSitechKey;
    }
    /**
     * @param url
     * @param param
     * @return
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"ISO8859-1"));//getBytes("ISO8859-1")
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
