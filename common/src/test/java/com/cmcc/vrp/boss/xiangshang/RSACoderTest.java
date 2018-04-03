package com.cmcc.vrp.boss.xiangshang;

import org.apache.commons.codec.binary.Base64;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * RSACoder的测试类
 * <p>
 * Created by sunyiwei on 2016/12/12.
 */
public class RSACoderTest {

    private String publicKey;
    private String privateKey;
    private Map<String, Object> keyMap = new HashMap<String, Object>();

    public static void main(String[] args) {
        RSACoderTest tt = new RSACoderTest();
        try {
            tt.setUp();//初始化公约私钥
            tt.test();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setUp() throws Exception {
        privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMvamDs9yZT5MpPgNMwlp/UqEituDR0GALH7HB32DFkyEBYvCgotmdh2hlCtdlVg6JPa+qB+77G90keME536eqFqA+qGrr1Cvnv6odqx1eUeJ4MhNGsi5rNj+nE/wwRLBFL3aglps0lyWaC8j4t6HVeLFhkyuDrsGzsCGKYpCC0HAgMBAAECgYEAoo45fmQS7vyYXFsZPwF4IYPe4Ursogw7WbHIBgxCZI4LTeVMuDkMyRQanxLznbmdPOoNmRYfxTh9ChilPACU793n9cb3aLLBddaRLKlcS96Hx23GiwAdR73U+aiYEIgCGZrTqxkYXB17M0f40/Uw1f/w3rD9gcXqoH4ffiLMbSECQQDlbUMcFkZ84eoYT9WAHzezJ0s8bFf2lvQpZYwgFFoa//s6ltU2m82QpdhFvPHNcED+b0Rd9kErpjNIpd3i/LU1AkEA43cRx5qj/GvXOnDimQeim46t3RJBWAtxHE4LXT0/TNPMthQNPgcs7VM367jQglk1bSFDc5unto9fbMufLE4MywJASk9QfEluvUZDF2rMQTpbRSjGAqUo/JK6NKpSb5WH9dTRn5F5L73ZIC/HVBc1zo+8TWzmnPSE+UlUtQQcUUf+vQJBAM6ttf4DyNTmt91mDL63bUyiy99/Ytg5LUFmuHSz3fxUxkD63z1pD61kW/9XIj4OCLlr6/nziOQcSbx1F+AN2xkCQQC7R3cbeKIIVaBozDa+JEq+FebQav2QeMydQIzfRBlNTtGAdOKokVtPGMMkseC/NyF53g9iVGpyqhPwVRg1Q4Mc";
        publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDG78YogpvISlW/mvP0cIVBbrVu1OuhRuyaMGgo00CZmn2556T0n0rmNMBFMdah//lfYvlRxZQk1x6luoP1w7p8P+V9aIvVJ6eaBflzRTVkODB+TI9nt4fL5WsHS6gaLc73lIpvbCywYNfyltKyTSOBHzT3WUoWPblFHTFciJE76wIDAQAB";
    }

    public void test() throws Exception {

        System.out.println("---------------------------私钥加密——公钥解密-----------------------------");
        String timestamp = getTimestampStr();//获取时间戳
        String waitSignStr = "deno10macidadminorderid20150630193234phone13588772413time" + timestamp;//待加密的字符串
        System.out.println("要加密的字符串:\r" + waitSignStr);
        byte[] data = waitSignStr.getBytes("UTF-8");

        byte[] encodedData = RSACoder.encryptByPrivateKey(data, privateKey);//私钥加密
        String outputEncodedDataStr = Base64.encodeBase64String(encodedData);
        System.out.println("私钥加密后base64的字符串:\r" + outputEncodedDataStr);

        byte[] decodedData = RSACoder.decryptByPublicKey(encodedData, publicKey);//公钥加密
        String outputStr = new String(decodedData);
        System.out.println("公钥解密后的字符串:\r" + outputStr);


        System.out.println("---------------------------私钥签名——公钥验证签名---------------------------");
        String sign = RSACoder.sign(data, privateKey); //签名
        System.out.println("私钥签名后的字符串:\r" + sign);

        boolean status = RSACoder.verify(data, publicKey, sign);   // 验证签名
        System.out.println("验证签名状态(true为验证签名通过, false为验证签名失败):\r" + status);

        //测试地址，直接复制到浏览器中运行
        String requestUrl = "http://42.120.5.186:82/shop/buyunit/orderpay.do?deno=10&macid=admin&orderid=20150630193234&phone=13588772413&sign=" + URLEncoder.encode(sign, "UTF-8") + "&time=" + timestamp;
        System.out.println("请求地址:\r" + requestUrl);
    }

    /**
     * 获取自1970年1月1号以来的毫秒数
     *
     * @return
     * @throws Exception
     */
    public String getTimestampStr() throws Exception {
        java.text.SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = formater.parse("1970-01-01 00:00:00");
        long time = System.currentTimeMillis() - date.getTime();
        String expected = String.valueOf(time).substring(0, 10);
        String actual = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() + 8 * 3600 * 1000));

        return expected;
    }
}