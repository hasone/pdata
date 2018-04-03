package com.cmcc.vrp.boss.sichuan.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.sichuan.model.SCOpenRequest;
import com.cmcc.vrp.boss.sichuan.model.SCOpenResponse;
import com.cmcc.vrp.boss.sichuan.model.SCOpenResponseOutData;
import com.cmcc.vrp.boss.sichuan.model.Sign;
import com.cmcc.vrp.boss.sichuan.service.SCOpenService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpConnection;

/**
 * @author wujiamin
 * @date 2016年11月1日
 */
@Service("SCOpenService")
public class SCOpenServiceImpl implements SCOpenService {
    private static Logger logger = Logger.getLogger(SCOpenService.class);

    @Autowired
    private GlobalConfigService globalConfigService;

    @Override
    public String generateRequestString(SCOpenRequest request) {
        request.setAppKey(getAppKey());
        request.setLoginNo(getLoginNo());
        request.setUserName(getUserName());
        request.setPrcId(getPrcId());
        request.setOpCode(getOpCode());

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
        request.setTimeStamp(dateformat.format(new Date()));

        String params = "appKey=" + request.getAppKey() + "&discnt_code=" + request.getDiscntCode()
            + "&login_no=" + request.getLoginNo() + "&op_code=" + request.getOpCode()
            + "&prc_id=" + request.getPrcId() + "&region_id=" + request.getRegionId()
            + "&timeStamp=" + request.getTimeStamp() + "&unit_id=" + request.getUnitId()
            + "&userName=" + request.getUserName();

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
    public SCOpenResponse parseResponse(String responseStr) {
        //SCOpenResponse response = (SCOpenResponse) JSON.parse(responseStr);
        JSONObject json = (JSONObject) JSONObject.parse(responseStr);
        String resCode = json.getString("resCode");
        String resMsg = json.getString("resMsg");
        SCOpenResponse response = new SCOpenResponse();
        response.setResMsg(resMsg);
        response.setResCode(resCode);
        if ("0000000".equals(resCode)) {
            String outDataStr = json.getString("outData");
            SCOpenResponseOutData outData = JSONObject.parseObject(outDataStr, SCOpenResponseOutData.class);
            response.setOutData(outData);
        }

        logger.info("开户接口返回结果" + responseStr);

        return response;
    }

    @Override
    public SCOpenResponse sendOpenRequest(SCOpenRequest request) {
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

    public String getOpCode() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_OPCODE.getKey());
    }

    public String getPrcId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_OPEN_PRCID.getKey());
    }
}
