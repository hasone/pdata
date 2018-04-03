package com.cmcc.vrp.sso.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.ContentType;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.sso.service.SSOService;
import com.cmcc.vrp.util.AESdecry;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;

/**
 * SSOServiceimpl
 *
 */
@Service
public class SSOServiceimpl implements SSOService {

    @Autowired
    GlobalConfigService globalConfigService;
    
    @Override
    public boolean gdSendLogoutReq(String mobilePhone) {
        String decyStr = AESdecry.encrypt(
                String.valueOf(new DateTime().getMillis()) + "&" + mobilePhone,getPublicKey());  
        
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("logoutToken", decyStr);
        
        HttpUtils.post(getLogoutUrl(), "", ContentType.APPLICATION_XML.getMimeType(), headers);
        return true;
    }
    
    /**
     * 获取加密公钥
     */
    private String getPublicKey(){
        return globalConfigService.get(GlobalConfigKeyEnum.GD_SSO_PUBLIC_KEY.getKey());
    }
    
    /**
     * 获取发送gdsso的url logout
     */
    private String getLogoutUrl(){
        return globalConfigService.get(GlobalConfigKeyEnum.GD_SSO_LOGOUT_URL.getKey());
    }
}
