package com.cmcc.vrp.boss.sichuan.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.sichuan.model.SCShortAddModeRequest;
import com.cmcc.vrp.boss.sichuan.model.SCShortAddModeResponse;
import com.cmcc.vrp.boss.sichuan.model.SCShortAddModeResponseOutData;
import com.cmcc.vrp.boss.sichuan.model.Sign;
import com.cmcc.vrp.boss.sichuan.service.SCOpenService;
import com.cmcc.vrp.boss.sichuan.service.SCShortAddModeService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpConnection;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wujiamin
 * @date 2016年11月1日
 */
@Service("SCShortAddModeService")
public class SCShortAddModeServiceImpl implements SCShortAddModeService {
    private static Logger logger = Logger.getLogger(SCOpenService.class);

    @Autowired
    private GlobalConfigService globalConfigService;

    @Override
    public String generateRequestString(SCShortAddModeRequest request) {
        request.setAppKey(getAppKey());
        request.setLoginNo(getLoginNo());
        request.setUserName(getUserName());

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
        request.setTimeStamp(dateformat.format(new Date()));

        String params = "appKey=" + request.getAppKey() + "&login_no=" + request.getLoginNo()
            + "&operate_type=" + request.getOperateType() + "&phone_no=" + request.getPhoneNo()
            + "&prod_prcid=" + request.getProdPrcid() + "&smsPwd=" + request.getSmsPwd()
            + "&timeStamp=" + request.getTimeStamp() + "&userName=" + request.getUserName();

        String sign = null;
        try {
            sign = Sign.sign(params, getPrivateKeyPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        params = params + "&sign=" + sign;
        return params;
    }

    @Override
    public SCShortAddModeResponse parseResponse(String responseStr) {
        //SCOpenResponse response = (SCOpenResponse) JSON.parse(responseStr);
        JSONObject json = (JSONObject) JSONObject.parse(responseStr);
        String resCode = json.getString("resCode");
        String resMsg = json.getString("resMsg");
        SCShortAddModeResponse response = new SCShortAddModeResponse();
        response.setResMsg(resMsg);
        response.setResCode(resCode);
        if ("0000000".equals(resCode)) {
            String outDataStr = json.getString("outData");
            SCShortAddModeResponseOutData outData = JSONObject.parseObject(outDataStr, SCShortAddModeResponseOutData.class);
            response.setOutData(outData);
        }

        logger.info("ShortAddMode接口返回结果" + responseStr);

        return response;
    }

    @Override
    public SCShortAddModeResponse sendChargeRequest(
        SCShortAddModeRequest request) {
        String requestStr = generateRequestString(request);
        logger.info("开户参数" + requestStr);
        HttpConnection httpConnection = new HttpConnection();
        try {
            String returnStr = httpConnection.sendGetRequest(getOpenUrl(), requestStr);
            logger.info("返回参数" + returnStr);
            if (!StringUtils.isEmpty(returnStr)) {
                return parseResponse(returnStr);
            }
        } catch (Exception e) {
            logger.info("发送http请求错误:" + e.toString());
            e.printStackTrace();
        }
        return null;
    }

    public String getOpenUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_OPEN_URL.getKey());
    }

    public String getPrivateKeyPath() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_PRIVATE_KEY_PATH.getKey());
    }

    public String getAppKey() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_APP_KEY.getKey());
    }

    public String getUserName() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_USER_NAME.getKey());
    }

    public String getLoginNo() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_LOGIN_NO.getKey());
    }

}
