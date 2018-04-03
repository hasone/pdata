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
import org.springframework.util.StreamUtils;

import com.cmcc.vrp.ec.bean.AuthReq;
import com.cmcc.vrp.ec.bean.AuthReqData;
import com.cmcc.vrp.ec.bean.AuthResp;
import com.cmcc.vrp.ec.bean.individual.IndividualRedpacketActivityParam;
import com.cmcc.vrp.ec.bean.individual.IndividualRedpacketReq;
import com.cmcc.vrp.ec.bean.individual.OrderRequest;
import com.thoughtworks.xstream.XStream;

/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/conf/applicationContext.xml")*/
public class IndividualRedpacketControllerTest{

    protected final String appKey = "05bf57ad3f5a46f487851764955e296f";
    protected final String appSecret = "9e5d3d40315e43678ef66c3306986f2f";
    protected final String shuanchuan_appKey = "7ca971744c134520b38bc68202cc47c5";
    protected final String shuanchuan_appSecret = "901a5ef5661f4bd7918b1582189c85aa";

    //@Test
    public void testChargeController() throws Exception {
        //1. 获取token
        String token = getToken();
        System.out.println("Token = " + token);

        //2. 个人红包账户余额查询
        //query(token);
        
        //3、个人红包订购接口
        //order(token);
        
        //4、个人红包创建接口
        //create(token);
    }

    private void query(String token) {
        final String CHARGE_URL = "http://localhost:8080/web-in/redpacket/18867103685/query.html";

        doGet(CHARGE_URL, buildHeaders(token, ""));
        
    }
    
    private void order(String token) {
        final String url = "http://localhost:8080/web-in/redpacket/order.html";

        String requestStr = buildOrderReqStr();

        String response = doPost(url, requestStr, buildHeaders(token, requestStr));
        System.out.println("order接口返回：" + response);
    }
    
    private String buildOrderReqStr() {
        OrderRequest request = new OrderRequest();
        //request.setMobile("13981729724");
        //request.setMobile("13882069973");
        request.setMobile("18870000002");
        request.setProductCode("ACAZ27209");
        request.setSerialNum("ecSerialNumTest");
        XStream xStream = new XStream();
        xStream.alias("OrderRequest", OrderRequest.class);
        xStream.autodetectAnnotations(true);

        return xStream.toXML(request);
    }
    
    private void create(String token) {
        final String url = "http://localhost:8080/web-in/redpacket/create.html";

        String requestStr = buildCreateReqStr();

        String response = doPost(url, requestStr, buildHeaders(token, requestStr));
        System.out.println("create接口返回：" + response);
    }
    
    private String buildCreateReqStr() {
        IndividualRedpacketReq request = new IndividualRedpacketReq();
        request.setEcSerialNumber("ecSerialTest");
        IndividualRedpacketActivityParam param = new IndividualRedpacketActivityParam();
        param.setMobile("18867100000");
        param.setActivityName("wjm测试活动11");
        param.setStartTime("20170123080000");
        param.setType(8);
        param.setSize(4L);
        param.setCount(5L);
        param.setObject("对象对象对象对象测试");
        param.setRules("规则规则规则规则测试");
        request.setParam(param);
        
        XStream xStream = new XStream();
        xStream.alias("CreateRequest", IndividualRedpacketReq.class);
        xStream.autodetectAnnotations(true);

        return xStream.toXML(request);
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
