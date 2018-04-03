package com.cmcc.vrp.ec.model;

import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by leelyn on 2016/5/18.
 */
@Component
public class SignKey {
    @Autowired
    private GlobalConfigService globalConfigService;

    //prefixkeysuffix
    /**
     * @param key
     * @return
     */
    public byte[] buildKey(String key) {
        return (getPrefix() + key + getSuffix()).getBytes();
    }

    public String getPrefix() {
        return globalConfigService.get(GlobalConfigKeyEnum.JWT_SIGN_PREFIX.getKey());
    }

    public String getSuffix() {
        return globalConfigService.get(GlobalConfigKeyEnum.JWT_SIGN_SUFFIX.getKey());
    }
}

