package com.cmcc.vrp.boss.sichuan.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.sichuan.model.Sign;
import com.cmcc.vrp.boss.sichuan.model.individual.ScAppQryRequest;
import com.cmcc.vrp.boss.sichuan.model.individual.ScAppQryResponse;
import com.cmcc.vrp.boss.sichuan.service.ScAppQryService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpConnection;

/**
 * 四川BOSS个人流量查询
 * @author wujiamin
 * @date 2016年11月1日
 */
@Service("scAppQryService")
public class ScAppQryServiceImpl implements ScAppQryService {
    private static Logger logger = Logger.getLogger(ScAppQryServiceImpl.class);

    @Autowired
    GlobalConfigService globalConfigService;

    /**
     * @Title: generateRequestString 
     * @param request
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
     */
    @Override
    public String generateRequestString(ScAppQryRequest request) {
        request.setAppKey(getAppKey());
        request.setLoginNo(getLoginNo());
        request.setUserName(getUserName());

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
        request.setTimeStamp(dateformat.format(new Date()));

        String params = "appKey=" + request.getAppKey() + "&login_no=" + request.getLoginNo()
            + "&phone_no=" + request.getPhoneNo() + "&qry_type=" + request.getQryType()
            + "&timeStamp=" + request.getTimeStamp() 
            + "&userName=" + request.getUserName() + "&year_month=" + request.getTimeStamp();

        String sign = null;
        try {
            sign = Sign.sign(params, getPrivateKeyPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        params = params + "&sign=" + sign;
        return params;
    }


    /**
     * @Title: parseResponse 
     * @param responseStr
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
     */
    @Override
    public ScAppQryResponse parseResponse(String responseStr) {
        ScAppQryResponse response = JSONObject.parseObject(responseStr, ScAppQryResponse.class);
        return response;
    }


    /**
     * @Title: sendRequest 
     * @param request
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
     */
    @Override
    public ScAppQryResponse sendRequest(ScAppQryRequest request) {
        String requestStr = generateRequestString(request);
        logger.info("个人流量查询请求参数:" + requestStr);
        try {
            String returnStr = HttpConnection.sendGetRequest(getAppQryUrl(), requestStr);
            logger.info("个人流量查询请求返回:" + returnStr + ", 请求参数:" + requestStr);
            return parseResponse(returnStr);

        } catch (Exception e) {
            logger.info("发送http请求错误:" + e.toString());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Title: getPrivateKeyPath 
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
     */
    public String getPrivateKeyPath() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_PRIVATE_KEY_PATH.getKey());
    }

    
    /** 
     * @Title: getAppKey 
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
    */
    public String getAppKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_APP_KEY.getKey());
    }

    /** 
     * @Title: getUserName 
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
    */
    public String getUserName() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_USER_NAME.getKey());
    }

    /** 
     * @Title: getLoginNo 
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
    */
    public String getLoginNo() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_LOGIN_NO.getKey());
    }

    /** 
     * @Title: getAppQryUrl 
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
    */
    public String getAppQryUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_APPQRY_URL.getKey());
    }
}
