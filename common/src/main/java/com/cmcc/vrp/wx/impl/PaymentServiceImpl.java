package com.cmcc.vrp.wx.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.wx.PaymentService;
import com.cmcc.vrp.wx.beans.PayParameter;
import com.cmcc.vrp.wx.utils.DigestUtil;

/**
 * Created by leelyn on 2017/1/6.
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private static final String SYMBOL = "&";

    private static final String DIGESTSYMBOL = ",";

    @Autowired
    GlobalConfigService globalConfigService;

    @Override
    public boolean sendChargeRequest(String param) {
        String url = getUrl();
        HttpClient client = new HttpClient();
   
        //路径与参数组合
        String totalUrl = url + "?" + param;
       
        GetMethod method = new GetMethod(totalUrl);

        method.addRequestHeader("Connection", "close");

        //method.addRequestHeader("Content-Type", "text/html; charset=UTF-8");
        client.getParams().setContentCharset("UTF-8");
        // Provide custom retry handler is necessary

        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
            new DefaultHttpMethodRetryHandler(3, false));
        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode == HttpStatus.SC_OK) {
                LOGGER.info("Response message: " + StreamUtils.copyToString(method.getResponseBodyAsStream(), 
                        Charset.forName(method.getResponseCharSet())));
                return true;
            } else {
                LOGGER.info("Response Code: " + statusCode);
                return false;
            }
        } catch (HttpException e) {
            LOGGER.info("Fatal protocol violation: " + e.toString());
        } catch (IOException e) {
            LOGGER.info("Fatal transport error: " + e.toString());
        } finally {
            method.releaseConnection();
        }
        return false;
    }
    
    
    @Override
    public String combinePayPara(PayParameter parameter) {
        if (parameter == null) {
            LOGGER.error("参数为空");
            return null;
        }
        try {
            return getPayPara(parameter);
        } catch (Exception e) {
            LOGGER.error("组装URL参数抛出异常:{}", e);
        }
        return null;
    }

    private String getPayPara(PayParameter parameter) throws Exception {
        StringBuffer urlSb = new StringBuffer();
        if (StringUtils.isNotBlank(parameter.getcId())) {
            urlSb.append("c_id=" + parameter.getcId());
        } else {
            urlSb.append("c_id=");
        }
        urlSb.append(SYMBOL);

        if (StringUtils.isNotBlank(parameter.getCdType())) {
            urlSb.append("cd_type=" + parameter.getCdType());
        } else {
            urlSb.append("cd_type=");
        }
        urlSb.append(SYMBOL);

        if (StringUtils.isNotBlank(parameter.gethPixel())) {
            urlSb.append("h_pixel=" + parameter.gethPixel());
        } else {
            urlSb.append("h_pixel=");
        }
        urlSb.append(SYMBOL);

        if (StringUtils.isNotBlank(parameter.getwPixel())) {
            urlSb.append("w_pixel=" + parameter.getwPixel());
        } else {
            urlSb.append("w_pixel=");
        }
        urlSb.append(SYMBOL);

        if (StringUtils.isNotBlank(parameter.getMerchant())) {
            urlSb.append("merchant=" + parameter.getMerchant());
        } else {
            urlSb.append("merchant=");
        }
        urlSb.append(SYMBOL);

        if (StringUtils.isNotBlank(parameter.getSrvId())) {
            urlSb.append("srv_id=" + parameter.getSrvId());
        } else {
            urlSb.append("srv_id=");
        }
        urlSb.append(SYMBOL);

        if (StringUtils.isNotBlank(parameter.getpMethod())) {
            urlSb.append("p_method=" + parameter.getpMethod());
        } else {
            urlSb.append("p_method=");
        }
        urlSb.append(SYMBOL);

        if (StringUtils.isNotBlank(parameter.getpDirect())) {
            urlSb.append("p_direct=" + parameter.getpDirect());
        } else {
            urlSb.append("p_direct=");
        }
        urlSb.append(SYMBOL);

        if (StringUtils.isNotBlank(parameter.getoId())) {
            urlSb.append("o_id=" + parameter.getoId());
        } else {
            urlSb.append("o_id=");
        }
        urlSb.append(SYMBOL);

        if (StringUtils.isNotBlank(parameter.getoTime())) {
            urlSb.append("o_time=" + parameter.getoTime());
        } else {
            urlSb.append("o_time=");
        }
        urlSb.append(SYMBOL);

        if (StringUtils.isNotBlank(parameter.getUser())) {
            urlSb.append("user=" + parameter.getUser());
        } else {
            urlSb.append("user=");
        }
        urlSb.append(SYMBOL);

        if (StringUtils.isNotBlank(parameter.getpUser())) {
            urlSb.append("p_user=" + parameter.getpUser());
        } else {
            urlSb.append("p_user=");
        }
        urlSb.append(SYMBOL);

        if (StringUtils.isNotBlank(parameter.getPoint())) {
            urlSb.append("point=" + parameter.getPoint());
        } else {
            urlSb.append("point=");
        }
        urlSb.append(SYMBOL);

        if (StringUtils.isNotBlank(parameter.getAmount())) {
            urlSb.append("amount=" + parameter.getAmount());
        } else {
            urlSb.append("amount=");
        }
        urlSb.append(SYMBOL);

        if (StringUtils.isNotBlank(parameter.getTitle())) {
            String title = urlEncode(parameter.getTitle());
            urlSb.append("title=" + title);
        } else {
            urlSb.append("title=");
        }
        urlSb.append(SYMBOL);

        if (StringUtils.isNotBlank(parameter.getCity())) {
            urlSb.append("city=" + parameter.getCity());
        } else {
            urlSb.append("city=");
        }
        urlSb.append(SYMBOL);

        if (StringUtils.isNotBlank(parameter.getoUrl())) {
            String oUrl = urlEncode(parameter.getoUrl());
            urlSb.append("o_url=" + oUrl);
        } else {
            urlSb.append("o_url=");
        }
        urlSb.append(SYMBOL);

//        if (StringUtils.isNotBlank(parameter.getBackUrl())) {
//            String backUrl = urlEncode(getBackUrl());
//            urlSb.append("back_url=" + backUrl);
//        } else {
//            urlSb.append("back_url=");
//        }
        String backUrl = urlEncode(getBackUrl());
        urlSb.append("back_url=" + backUrl);
        urlSb.append(SYMBOL);

//        if (StringUtils.isNotBlank(parameter.getNotifyUrl())) {
//            String notifyUrl = urlEncode(getNotifyUrl());
//            urlSb.append("notify_url=" + notifyUrl);
//        } else {
//            urlSb.append("notify_url=");
//        }
        String notifyUrl = urlEncode(getNotifyUrl());
        urlSb.append("notify_url=" + notifyUrl);
        urlSb.append(SYMBOL);

        if (StringUtils.isNotBlank(parameter.getDesc())) {
            String desc = urlEncode(parameter.getDesc());
            urlSb.append("desc=" + desc);
        } else {
            urlSb.append("desc=");
        }
        urlSb.append(SYMBOL);

        String disest = urlEncode(getDigest(parameter));
        urlSb.append("digest=" + disest);
        urlSb.append(SYMBOL);

        if (StringUtils.isNotBlank(parameter.getRev1())) {
            String desc = urlEncode(parameter.getRev1());
            urlSb.append("rev1=" + desc);
        } else {
            urlSb.append("rev1=");
        }
        urlSb.append(SYMBOL);

        if (StringUtils.isNotBlank(parameter.getRev2())) {
            String desc = urlEncode(parameter.getRev2());
            urlSb.append("rev2=" + desc);
        } else {
            urlSb.append("rev2=");
        }

        return urlSb.toString();
    }

    private String urlEncode(String content) {
        try {
            return URLEncoder.encode(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("UrlEncode异常");
        }
        return null;
    }

    private String getDigest(PayParameter parameter) throws Exception {
        StringBuffer digSb = new StringBuffer();
        if (StringUtils.isNotBlank(parameter.getcId())) {
            digSb.append(parameter.getcId());
        } else {
            digSb.append("");
        }
        digSb.append(DIGESTSYMBOL);

        if (StringUtils.isNotBlank(parameter.getoId())) {
            digSb.append(parameter.getoId());
        } else {
            digSb.append("");
        }
        digSb.append(DIGESTSYMBOL);

        if (StringUtils.isNotBlank(parameter.getoTime())) {
            digSb.append(parameter.getoTime());
        } else {
            digSb.append("");
        }
        digSb.append(DIGESTSYMBOL);

        if (StringUtils.isNotBlank(parameter.getMerchant())) {
            digSb.append(parameter.getMerchant());
        } else {
            digSb.append("");
        }
        digSb.append(DIGESTSYMBOL);

        if (StringUtils.isNotBlank(parameter.getUser())) {
            digSb.append(parameter.getUser());
        } else {
            digSb.append("");
        }
        digSb.append(DIGESTSYMBOL);

        if (StringUtils.isNotBlank(parameter.getpUser())) {
            digSb.append(parameter.getpUser());
        } else {
            digSb.append("");
        }
        digSb.append(DIGESTSYMBOL);

        if (StringUtils.isNotBlank(parameter.getPoint())) {
            digSb.append(parameter.getPoint());
        } else {
            digSb.append("");
        }
        digSb.append(DIGESTSYMBOL);

        if (StringUtils.isNotBlank(parameter.getAmount())) {
            digSb.append(parameter.getAmount());
        } else {
            digSb.append("");
        }
        digSb.append(DIGESTSYMBOL);

        return DigestUtil.generateDigest(digSb.toString(), getPayDigestSecret());
    }

    private String getPayDigestSecret() {
        return globalConfigService.get(GlobalConfigKeyEnum.GUANGDONG_ZHONGCHOU_PAY_SECRET.getKey());
        //return "U2FsdGVkX18MBAY8UFe5IkRU";
    }
    
    private String getUrl() {
        //return "http://221.179.7.247/NGADCABInterface/TrafficZC/PmpOnePhaseSubmit.aspx";
        return globalConfigService.get(GlobalConfigKeyEnum.GUANGDONG_ZHONGCHOU_PAY_URL.getKey());
    }
    
    private String getBackUrl() {
      //return "https://gd10086.4ggogo.com/web-in/gdzc/payBack.html";
        return globalConfigService.get(GlobalConfigKeyEnum.GUANGDONG_ZHONGCHOU_PAY_BACK_URL.getKey());
    }
    private String getNotifyUrl() {
      //return "https://gd10086.4ggogo.com/web-in/gdzc/notify.html";
        return globalConfigService.get(GlobalConfigKeyEnum.GUANGDONG_ZHONGCHOU_PAY_NOTIFY_URL.getKey());
    }
}
