package com.cmcc.vrp.boss.sichuan.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.sichuan.model.SCChargeRequest;
import com.cmcc.vrp.boss.sichuan.model.SCChargeResponse;
import com.cmcc.vrp.boss.sichuan.model.Sign;
import com.cmcc.vrp.boss.sichuan.service.SCAddMemberService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpConnection;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午11:04:52
*/
@Service("SCAddMemberService")
public class SCAddMemberServiceImpl implements SCAddMemberService {
    private static Logger logger = Logger.getLogger(SCAddMemberService.class);

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
    public String generateRequestString(SCChargeRequest request) {
        request.setAppKey(getAppKey());
        request.setLoginNo(getLoginNo());
        request.setUserName(getUserName());
        request.setExp_rule(getExpRule());

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
        request.setTimeStamp(dateformat.format(new Date()));

        String params = "appKey=" + request.getAppKey() + "&exp_rule=" + request.getExp_rule()
            + "&login_no=" + request.getLoginNo() + "&phone_no=" + request.getPhone_no()
            + "&prc_id=" + request.getPrcId() + "&timeStamp=" + request.getTimeStamp()
            + "&userName=" + request.getUserName() + "&user_id=" + request.getUserId();

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
    public SCChargeResponse parseResponse(String responseStr) {
        SCChargeResponse response = JSONObject.parseObject(responseStr, SCChargeResponse.class);
        return response;
    }

    /** 
     * @Title: sendChargeRequest 
     * @param request
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
    */
    @Override
    public SCChargeResponse sendChargeRequest(SCChargeRequest request) {
        String requestStr = generateRequestString(request);
        logger.info("集团成员增加请求参数:" + requestStr);
        HttpConnection httpConnection = new HttpConnection();
        try {
            String returnStr = httpConnection.sendGetRequest(getAddMemberUrl(), requestStr);
            logger.info("集团成员增加返回:" + returnStr + ", 请求参数:" + requestStr);
            return parseResponse(returnStr);

        } catch (Exception e) {
            logger.info("发送http请求错误:" + e.toString());
            e.printStackTrace();
        }
        return null;
    }


    /** 
     * @Title: getAddMemberUrl 
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
    */
    public String getAddMemberUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_ADD_MEMBER_URL.getKey());
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
     * @Title: getExpRule 
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
    */
    public String getExpRule() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_EXPIRE_RULE.getKey());
    }
}
