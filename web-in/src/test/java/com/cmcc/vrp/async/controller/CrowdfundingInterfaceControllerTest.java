package com.cmcc.vrp.async.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.Charsets;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;

import com.cmcc.vrp.ec.bean.AuthReq;
import com.cmcc.vrp.ec.bean.AuthReqData;
import com.cmcc.vrp.ec.bean.AuthResp;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.webservice.crowdfunding.CrowdfundingCallbackService;
import com.cmcc.webservice.crowdfunding.pojo.CFChargePojo;
import com.cmcc.webservice.crowdfunding.pojo.CFChargeReq;
import com.cmcc.webservice.crowdfunding.pojo.JoinActivityReq;
import com.cmcc.webservice.crowdfunding.pojo.JoinActivityReqData;
import com.cmcc.webservice.crowdfunding.pojo.PaymentPojo;
import com.cmcc.webservice.crowdfunding.pojo.PaymentReq;
import com.cmcc.webservice.crowdfunding.pojo.QueryActResPojo;
import com.cmcc.webservice.crowdfunding.pojo.QueryActResReq;
import com.cmcc.webservice.crowdfunding.pojo.QueryActivityReq;
import com.cmcc.webservice.crowdfunding.pojo.QueryActivityReqData;
import com.cmcc.webservice.crowdfunding.pojo.QueryJoinResReq;
import com.cmcc.webservice.crowdfunding.pojo.QueryJoinResReqData;
import com.thoughtworks.xstream.XStream;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:/conf/applicationContext.xml")
public class CrowdfundingInterfaceControllerTest{

    protected final String appKey = "5d01c8d143144daf9cf9f4ba16568411";
    protected final String appSecret = "4869e5ab82f549ab91117cc1f7f8d00e";
    protected final String shuanchuan_appKey = "7ca971744c134520b38bc68202cc47c5";
    protected final String shuanchuan_appSecret = "901a5ef5661f4bd7918b1582189c85aa";

    @Autowired
    ActivitiesService activitiesService;
    
    @Autowired
    CrowdfundingCallbackService crowdfundingCallbackService;
    
    @Test
    @Ignore
    public void testChargeController() throws Exception {
        
        //1. 获取token
        String token = getToken();
        //System.out.println("Token = " + token);

        //众筹活动查询
        //queryActivity(token);

        //众筹活动报名
        //joinActivity(token);
        
        //众筹活动报名结果查询接口
        //queryJoinResult(token);
        
        //众筹成功通知
        //crowdfundingCallbackService.notifyCrowdFundingSucceed("wjm11");
        
        //众筹结果查询接口
        //queryActivityResult(token);
        
        //通知支付完成
        notifyPayResult(token);
        
        //充值
        //charge(token);
        
        //queryChargeResult(token);
    }

    
    private String buildQueryActivityReqStr() {
        String actId = activitiesService.encryptActivityId("wjm11");
        QueryActivityReq request = new QueryActivityReq();
        String requestTime = new DateTime().toString();
        request.setRequestTime(requestTime);
        QueryActivityReqData data = new QueryActivityReqData();
        data.setActivityId(actId);
        data.setEnterpriseCode("123456");
        request.setData(data);
        XStream xStream = new XStream();
        xStream.alias("Request", QueryActivityReq.class);
        xStream.autodetectAnnotations(true);

        return xStream.toXML(request);
    }
    
    private void queryActivity(String token) {
        final String url = "http://localhost:8080/web-in/crowdFundingInterface/queryActivity.html";

        String requestStr = buildQueryActivityReqStr();
        System.out.println("queryActivity, request = " + token);
        String response = doPost(url, requestStr, buildHeaders(token, requestStr));
        System.out.println("queryActivity接口返回：" + response);
    }
    
    private String buildJoinActivityStr() {
        String actId = activitiesService.encryptActivityId("62999b39ba40440886f7189b5272ba22");
        JoinActivityReq request = new JoinActivityReq();
        String requestTime = new DateTime().toString();
        request.setRequestTime(requestTime);
        
        JoinActivityReqData data = new JoinActivityReqData();
        data.setActivityId(actId);
        data.setEnterpriseCode("123456");
        data.setMobile("18867103717");
        data.setPrizeId(722L);
        request.setJoinData(data);
        
        XStream xStream = new XStream();
        xStream.alias("Request", JoinActivityReq.class);
        xStream.autodetectAnnotations(true);

        return xStream.toXML(request);
    }
    
    private void joinActivity(String token) {
        final String url = "http://localhost:8080/web-in/crowdFundingInterface/joinActivity.html";

        String requestStr = buildJoinActivityStr();
        System.out.println("joinActivity, request = " + token);
        String response = doPost(url, requestStr, buildHeaders(token, requestStr));
        System.out.println("joinActivity接口返回：" + response);
    }
    
    private String buildQueryJoinActivityStr() {
        String actId = activitiesService.encryptActivityId("62999b39ba40440886f7189b5272ba22");
        QueryJoinResReq request = new QueryJoinResReq();
        String requestTime = new DateTime().toString();
        request.setRequestTime(requestTime);
        
        QueryJoinResReqData data = new QueryJoinResReqData();
        data.setActivityId(actId);
        data.setEnterpriseCode("123456");
        data.setMobile("1867103717");
        request.setData(data);
        
        XStream xStream = new XStream();
        xStream.alias("Request", QueryJoinResReqData.class);
        xStream.autodetectAnnotations(true);

        return xStream.toXML(request);
    }
    
    private void queryJoinResult(String token) {
        final String url = "http://localhost:8080/web-in/crowdFundingInterface/queryJoinResult.html";

        String requestStr = buildQueryJoinActivityStr();
        System.out.println("joinActivity, request = " + token);
        String response = doPost(url, requestStr, buildHeaders(token, requestStr));
        System.out.println("joinActivity接口返回：" + response);
    }
    
   
    
    private void queryActivityResult(String token){
        final String url = "http://localhost:8080/web-in/crowdFundingInterface/queryActivityResult.html";
        
        //请求参数
        String reqStr = buildActivityResult();
        System.out.println("(1)请求参数-reqStr==");
        System.out.println(reqStr);
        String response = doPost(url, reqStr, buildHeaders(token, reqStr));
        System.out.println("(1)返回参数-response==");
        System.out.println(response);
    }
    
    /**
     * 构造查询众筹结构请求对象
     * @author qinqinyan
     * */
    private String buildActivityResult(){
        
        String actId = activitiesService.encryptActivityId("62999b39ba40440886f7189b5272ba22");
        QueryActResReq req = new QueryActResReq();
        req.setDateTime(new DateTime().toString());
        QueryActResPojo pojo = new QueryActResPojo();
        pojo.setActivityId(actId);
        pojo.setEnterpriseCode("123456");
        req.setQueryActResPojo(pojo);
        
        XStream xStream = new XStream();
        xStream.alias("Request", QueryActResReq.class);
        xStream.autodetectAnnotations(true);

        return xStream.toXML(req);
    }
    
    private void notifyPayResult(String token){
        final String url = "http://localhost:8080/web-in/crowdFundingInterface/notifyPayResult.html";

        String requestStr = buildPaymentReq();
        System.out.println("request = " + token);
        String response = doPost(url, requestStr, buildHeaders(token, requestStr));
        System.out.println("接口返回：" + response);
    }
    
    /**
     * 构造通知支付请求对象
     * @author qinqinyan
     * */
    private String buildPaymentReq(){
        PaymentReq req = new PaymentReq();
        req.setDateTime(new DateTime().toString());
        
        PaymentPojo reqPojo = new PaymentPojo();
        reqPojo.setMobile("18867103685");
        reqPojo.setSerialNum("third-test-serial");
        reqPojo.setPrizeId("977");
        reqPojo.setActivityId("277404A4289C103C3DF04FE41A10771BD8728D6FE92FB1FA89B9B24F3D092132ECC4127A8363DCB4A79B896A5FA538564C521203818BE65BAFF0C8D9372278E3");
        reqPojo.setEnterpriseCode("22222");
        
        req.setPaymentPojo(reqPojo);
        
        XStream xStream = new XStream();
        xStream.alias("Request", QueryActResReq.class);
        xStream.autodetectAnnotations(true);
        
        return xStream.toXML(req);
    }
    
    private void charge(String token){
        final String url = "http://localhost:8080/web-in/crowdFundingInterface/charge.html";

        String requestStr = buildCFChargeReq();
        System.out.println("request = " + token);
        String response = doPost(url, requestStr, buildHeaders(token, requestStr));
        System.out.println("接口返回：" + response);
    }
    
    /**
     * 构造充值请求对象
     * @author qinqinyan
     * */
    private String buildCFChargeReq(){
        CFChargeReq req = new CFChargeReq();
        req.setDateTime(new DateTime().toString());
        
        CFChargePojo reqPojo = new CFChargePojo();
        reqPojo.setRecordId("b2d55ab6713745cc93b7c3b653d94227");
        //reqPojo.setRecordId("b2d55ab6713745d94227");
        reqPojo.setMobile("18867103717");
        reqPojo.setSerialNum("test-charge-serial");
        
        req.setCfChargePojo(reqPojo);
        
        XStream xStream = new XStream();
        xStream.alias("Request", QueryActResReq.class);
        xStream.autodetectAnnotations(true);
        
        return xStream.toXML(req);
    }
    
    //查询充值结果
    private void queryChargeResult(String token){
        final String url = "http://localhost:8080/web-in/crowdFundingInterface/"
                + "b2d55ab6713745cc93b7c3b653d94227/queryChargeResult.html";
        
        String recordId = "b2d55ab6713745cc93b7c3b653d94227";
        String respStr = doGet(url, buildHeaders(token, recordId));
        System.out.println("respStr==="+respStr);
    }
    
    
    protected String getToken() {
        final String AUTH_URL = "http://localhost:8080/web-in/auth.html";

        String authStr = buildAuthStr(build());
        String tokenResult =doPost(AUTH_URL, authStr, null);

        AuthResp authResp = parse(tokenResult);
        return authResp.getAuthRespData().getToken();
    }

    
    
    protected AuthResp parse(String respStr) {
        XStream xStream = new XStream();
        xStream.alias("Response", AuthResp.class);
        xStream.autodetectAnnotations(true);

        return (AuthResp) xStream.fromXML(respStr);
    }

    private String buildAuthStr(AuthReq authReq) {
        XStream xStream = new XStream();
        xStream.alias("Request", AuthReq.class);
        xStream.autodetectAnnotations(true);

        return xStream.toXML(authReq);
    }

    private AuthReq build() {
        String requestTime = new DateTime().toString();
        String sign = DigestUtils.sha256Hex(appKey + requestTime + appSecret);

        AuthReq authReq = new AuthReq();

        AuthReqData ard = new AuthReqData();
        ard.setAppKey(appKey);
        ard.setSign(sign);

        authReq.setAuthorization(ard);
        authReq.setRequestTime(requestTime);

        return authReq;
    }

    protected Map<String, String> buildHeaders(String token, String reqStr) {
        final String TOKEN_HEADER = "4GGOGO-Auth-Token";
        final String SIGNATURE_HEADER = "HTTP-X-4GGOGO-Signature";

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(TOKEN_HEADER, token);
        headers.put(SIGNATURE_HEADER, DigestUtils.sha256Hex(reqStr + appSecret));

        return headers;
    }

    protected String doPost(String url, String requestStr, Map<String, String> headers) {
        HttpClient httpClient = new HttpClient();
        PostMethod httpMethod = new PostMethod(url);

        //add headers
        if(headers !=null ){
            for (String s : headers.keySet()) {
                httpMethod.addRequestHeader(s, headers.get(s));
            }
        }

        //add request entity
        try {
            httpMethod.setRequestEntity(new StringRequestEntity(requestStr, "application/xml", "utf-8"));

            //执行方法
            System.out.println("请求的内容为" + requestStr);
            httpClient.executeMethod(httpMethod);

            System.out.println("返回的状态码为" + httpMethod.getStatusCode());
            String content = StreamUtils.copyToString(httpMethod.getResponseBodyAsStream(), Charsets.UTF_8);
            System.out.println("返回的内容为" + content);
            
            if (httpMethod.getStatusCode() == HttpStatus.SC_OK) {               
                System.out.println("状态码为200时，返回的内容为" + content);

                return content;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected String doGet(String url, Map<String, String> headers) {
        HttpClient httpClient = new HttpClient();
        GetMethod httpMethod = new GetMethod(url);

        //add headers
        for (String s : headers.keySet()) {
            httpMethod.addRequestHeader(s, headers.get(s));
        }

        //add request entity
        try {
            //执行方法
            httpClient.executeMethod(httpMethod);

            System.out.println("返回的状态码为" + httpMethod.getStatusCode());
            if (httpMethod.getStatusCode() == HttpStatus.SC_OK) {
                String content = StreamUtils.copyToString(httpMethod.getResponseBodyAsStream(), Charsets.UTF_8);
                System.out.println("返回的内容为" + content);

                return content;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
