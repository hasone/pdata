package com.cmcc.vrp.boss.sichuan.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.sichuan.model.SCCancelRequest;
import com.cmcc.vrp.boss.sichuan.model.Sign;
import com.cmcc.vrp.boss.sichuan.service.SCCancelService;
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
@Service("SCCancelService")
public class SCCancelServiceImpl implements SCCancelService {
    private static Logger logger = Logger.getLogger(SCCancelService.class);

    private final String configKey = "RANDOMPASS_CHECK";//数据库中globalconfig的随机验证码验证key值

    private final String checkPassKey = "OK";//globalconfig需要检验随机密码的value值

    @Autowired
    GlobalConfigService globalConfigService;

    @Override
    public String generateRequestString(SCCancelRequest request) {
        request.setAppKey(getAppKey());
        request.setLoginNo(getLoginNo());
        request.setUserName(getUserName());
        request.setOpCode(getOpCode());

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
        request.setTimeStamp(dateformat.format(new Date()));

        String params = "appKey=" + request.getAppKey()
            + "&login_no=" + request.getLoginNo() + "&op_code=" + request.getOpCode()
            + "&phone_no=" + request.getPhoneNo() + "&timeStamp=" + request.getTimeStamp()
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
    public Boolean sendCancelRequest(SCCancelRequest request) {
        String requestStr = generateRequestString(request);
        logger.info("集团账户注销接口参数：" + requestStr);
        HttpConnection httpConnection = new HttpConnection();
        try {
            String returnStr = httpConnection.sendGetRequest(getCancelUrl(), requestStr);
            logger.info("集团账户注销接口返回结果" + returnStr);

            //从数据库的global_config表中得到config_key对应的value值，如果不存在或者值不为"OK"则不进行检测验证码，返回
            String config_value = globalConfigService.get(configKey);
            if (config_value != null && !checkPassKey.equals(config_value)) {
                return true;
            }

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

    public String getCancelUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_CANCEL_URL.getKey());
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
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SC_CANCEL_OPCODE.getKey());
    }
}
