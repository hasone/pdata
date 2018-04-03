package com.cmcc.vrp.boss.heilongjiang.fee;

import java.net.URLEncoder;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSign {

    private Logger LOG = LoggerFactory.getLogger(TestSign.class);

    @Test
    public void test() throws Exception {
        /*
         * String str = "appKey=10001007&phone_no=13112345667&timeStamp=20161019085208"; String keyfile = "D:\\keys\\test\\rsa_private_key_pkcs8.pem";
         * LOG.info("签名字符串{}", str);
         * 
         * // 1、字典排序 // 2、请求参数做urlEncode str = URLEncoder.encode(str, "UTF-8"); LOG.info("一次URL编码后{}", str);
         * 
         * // 3、对字符串做MD5加密 String md5Str = DigestUtils.md5DigestAsHex(str.getBytes()); LOG.info("M5得到{}", md5Str);
         * 
         * // 4、私钥加密 // String sign = MyRSA.encryptWithFile(md5Str, keyfile);
         * 
         * String sign = MyRSA.signWithFile(md5Str, keyfile); LOG.info("私钥签名后{}", sign);
         * 
         * String result = URLEncoder.encode(sign, "UTF-8"); LOG.info("二次URL编码后{}", result);
         */
        String result = "AkMkRPmBrH9CczT2CEmhFKlo9VFxwax62xowrKxAieKrLf62VN22IJqrmkrDdCRzOunHsOM0DsKgstleqkj7OvMTkv9Wr0tloUOjBxXeFW2iqvGwsy96FFhLwh7weD1amMrYwqG9tEOjn/0H8Ks7Cau8ClLHduB28LttMPyOguc=";
        String temp = "AkMkRPmBrH9CczT2CEmhFKlo9VFxwax62xowrKxAieKrLf62VN22IJqrmkrDdCRzOunHsOM0DsKgstleqkj7OvMTkv9Wr0tloUOjBxXeFW2iqvGwsy96FFhLwh7weD1amMrYwqG9tEOjn%2F0H8Ks7Cau8ClLHduB28LttMPyOguc%3D";
        result = URLEncoder.encode(result, "UTF-8");
        System.out.println(result.equals(temp));
        LOG.info("三次URL编码后{}", result);
    }
}
