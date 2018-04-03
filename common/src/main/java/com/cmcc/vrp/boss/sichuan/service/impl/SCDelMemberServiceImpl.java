package com.cmcc.vrp.boss.sichuan.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.sichuan.model.SCDelMemberRequest;
import com.cmcc.vrp.boss.sichuan.model.Sign;
import com.cmcc.vrp.boss.sichuan.service.SCDelMemberService;
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
@Service("SCDelService")
public class SCDelMemberServiceImpl implements SCDelMemberService {
    private static Logger logger = Logger.getLogger(SCDelMemberService.class);

    @Autowired
    GlobalConfigService globalConfigService;


    @Override
    public String generateRequestString(SCDelMemberRequest request) {
        request.setAppKey(getAppKey());
        request.setLoginNo(getLoginNo());
        request.setUserName(getUserName());

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
        request.setTimeStamp(dateformat.format(new Date()));

        String params = "appKey=" + request.getAppKey() + "&login_no=" + request.getLoginNo()
            + "&phone_no=" + request.getPhone_no() + "&prc_id=" + request.getPrc_id()
            + "&timeStamp=" + request.getTimeStamp() + "&userName=" + request.getUserName()
            + "&user_id=" + request.getUserId();

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
    public Boolean sendDelMemberRequest(SCDelMemberRequest request) {
        String requestStr = generateRequestString(request);
        logger.info("集团产品成员删除接口请求参数：" + requestStr);
        HttpConnection httpConnection = new HttpConnection();
        try {
            String returnStr = httpConnection.sendGetRequest(getDelMemberUrl(), requestStr);
            logger.info("集团产品成员删除接口返回结果" + returnStr);
            if (!StringUtils.isEmpty(returnStr)) {
                JSONObject json = (JSONObject) JSONObject.parse(returnStr);
                String resCode = json.getString("resCode");
//                String resMsg = json.getString("resMsg");
                if ("0000000".equals(resCode)) {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.info("发送http请求错误:" + e.toString());
            e.printStackTrace();
        }
        return false;
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

    public String getDelMemberUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_DEL_MEMBER_URL.getKey());
    }
}
