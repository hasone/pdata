package com.cmcc.vrp.province.common.util;

import com.cmcc.vrp.util.HttpConnection;
import com.cmcc.vrp.util.MD5;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WebserviceTest {
    private static String getCurDate() {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(new Date());
    }

    //@Test
    public void testBuild() throws Exception {
        String serialNumber = "W20160525849350004";//"";
        String account = "openec";
        String bisCode = "4b63840b275cd6feb0e50f7970e8a869";
        String timeStamp = getCurDate();
        String key = "dde1b779db45bcfb8abb3d69214344e2";

        String signMD5 = MD5.sign(serialNumber + account + bisCode + timeStamp, key, "UTF-8");

        System.out.format("serialNumber=%s&account=%s&bisCode=%s&timeStamp=%s&sign=%s%n", serialNumber, account, bisCode, timeStamp, signMD5);
    }

    //@Test
    public void post() {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost("http://localhost:8080/web-in/open/addMember");
        // HttpPost httppost = new HttpPost("http://localhost:8080/foss-ent/membership/add.ajax");
        // 创建参数队列
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        long time = System.currentTimeMillis();
        String serialNumber = "W20160525849350004";//"";
        String account = "openec";
        String bisCode = "0353e14998a147cf8958da9044878322";
        String timeStamp = getCurDate();
        String key = "048fc15ebead4919969887da303c1656";
        BasicNameValuePair serialNumberP = new BasicNameValuePair("serialNumber", serialNumber);
        BasicNameValuePair accountP = new BasicNameValuePair("account", account);
        BasicNameValuePair bisCodeP = new BasicNameValuePair("bisCode", bisCode);
        BasicNameValuePair timeStampP = new BasicNameValuePair("timeStamp", timeStamp);
        String signMD5 = MD5.sign(serialNumber + account + bisCode + timeStamp, key, "UTF-8");
        BasicNameValuePair signP = new BasicNameValuePair("sign", signMD5);
        BasicNameValuePair mobileP = new BasicNameValuePair("mobile", "18867103685");
        BasicNameValuePair serviceCodeP = new BasicNameValuePair("serviceCode", "771");
        BasicNameValuePair discntCodeP = new BasicNameValuePair("discntCode", "ACAZ25208");
        BasicNameValuePair monthP = new BasicNameValuePair("month", "6");
        BasicNameValuePair capFlowP = new BasicNameValuePair("capFlow", "");
        BasicNameValuePair effectiveWayP = new BasicNameValuePair("effectiveWay", "0");
        BasicNameValuePair effectiveCycleP = new BasicNameValuePair("effectiveCycle", "0");
        formParams.add(serialNumberP);
        formParams.add(accountP);
        formParams.add(bisCodeP);
        formParams.add(timeStampP);
        formParams.add(signP);
        formParams.add(mobileP);
        formParams.add(serviceCodeP);
        formParams.add(discntCodeP);
        formParams.add(monthP);
        formParams.add(capFlowP);
        formParams.add(effectiveWayP);
        formParams.add(effectiveCycleP);
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
            httppost.setEntity(uefEntity);
            System.out.println("请求参数  " + uefEntity.toString());
            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (null != entity) {
                    System.out.println("--请求结果：-------------------------");
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
                    System.out.println("------------------------------------");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    @Ignore
    public void sendActivityCallBackTest() {
        String url = "http://localhost:8080/web-in/api/charge.html";

        String requestStr = "{\"activeId\":\"1c76208e4bdb4fefaf2aad0db08262cb\",\"catName\":\"移动\","
            + "\"createTime\":1467701282712,\"deleteFlag\":0,\"description\":\"测试活动1\",\"enterId\":\"116\","
            + "\"gameType\":2,\"id\":1,\"mobile\":\"18867103685\",\"platName\":\"sgs_sctestdev\","
            + "\"prizeCount\":1,\"prizeId\":\"ACAZ25208\",\"prizeName\":\"10MB\","
            + "\"prizeResponse\":1,\"prizeType\":0,\"rankName\":\"一等奖\",\"validality\":1467701282712}";

        try {
            HttpConnection.doPost(url, requestStr, "text/plain", "utf-8", true);
            //HttpConnection.doPost(url, requestStr, "text/plain", "utf-8", true);

            //System.out.println("return:" + returnStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
