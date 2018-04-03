/**
 *
 */
package com.cmcc.vrp.boss.sichuan.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.sichuan.model.SCBalanceRequest;
import com.cmcc.vrp.boss.sichuan.model.SCBalanceResponse;
import com.cmcc.vrp.boss.sichuan.model.SCBalanceResponseOutData;
import com.cmcc.vrp.boss.sichuan.model.Sign;
import com.cmcc.vrp.boss.sichuan.service.SCBalanceService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpConnection;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>Description: </p>
 *
 * @author xj
 * @date 2016年4月25日
 */

@Service("SCBalanceService")
public class SCBalanceServiceImpl implements SCBalanceService {


    private static Logger logger = Logger.getLogger(SCBalanceServiceImpl.class);

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
    public String generateRequestString(SCBalanceRequest request) {

        request.setAppKey(getAppKey());
        request.setLoginNo(getLoginNo());
        request.setUserName(getUserName());
        request.setMsg_flag(getMsg_flag());

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
        request.setTimeStamp(dateformat.format(new Date()));

        String params = "appKey=" + request.getAppKey() + "&login_no=" + request.getLoginNo()
            + "&msg_flag=" + request.getMsg_flag() + "&phone_no=" + request.getPhoneNo()
            + "&timeStamp=" + request.getTimeStamp() + "&userName=" + request.getUserName();

        String sign = null;

        try {
            sign = Sign.sign(params, getPrivateKeyPath());
        } catch (Exception e) {
            // TODO Auto-generated catch block
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
    public SCBalanceResponse parseResponse(String responseStr) {

        JSONObject json = (JSONObject) JSONObject.parse(responseStr);
        String resCode = json.getString("resCode");
        String resMsg = json.getString("resMsg");

        SCBalanceResponse response = new SCBalanceResponse();
        response.setResCode(resCode);
        response.setResMsg(resMsg);
        if (response.getResCode().equals("0000000")) {
            String outDataStr = json.getString("outData");
            SCBalanceResponseOutData outData = JSONObject.parseObject(outDataStr, SCBalanceResponseOutData.class);
            response.setOutData(outData);
        }

        logger.info("查询余额接口返回结果" + responseStr);

        return response;

    }

    /** 
     * @Title: sendBalanceRequest 
     * @param request
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
    */
    public SCBalanceResponse sendBalanceRequest(SCBalanceRequest request) {
        String requestStr = generateRequestString(request);
        logger.info("查询余额请求参数:" + requestStr);
        HttpConnection httpConnection = new HttpConnection();
        try {
            String returnStr = httpConnection.sendGetRequest(getBalanceUrl(), requestStr);
            logger.info("查询余额返回:" + returnStr + ", 请求参数:" + requestStr);
            return parseResponse(returnStr);

        } catch (Exception e) {
            logger.info("发送http请求错误:" + e.toString());
            e.printStackTrace();
            return new SCBalanceResponse();
        }
    }

    /** 
     * @Title: getBalanceUrl 
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
    */
    public String getBalanceUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_BALANCE_URL.getKey());
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
     * @Title: getMsg_flag 
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
    */
    public String getMsg_flag() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_BALANCE_MSG_FLAG.getKey());
    }
}
