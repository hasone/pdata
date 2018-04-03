package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import org.springframework.stereotype.Service;

/**
 * 登录成功的频率限制服务
 * <p>
 * Created by sunyiwei on 2016/11/17.
 */
@Service
public class LoginSuccessRateLimitServiceImpl extends AbstractRateLimitServiceImpl {
    @Override
    protected GlobalConfigKeyEnum getLimitSwitchKey() {
        return GlobalConfigKeyEnum.LOGIN_SUCCESS_LIMIT;
    }

    @Override
    protected GlobalConfigKeyEnum getLimitTimeRangeKey() {
        return GlobalConfigKeyEnum.LOGIN_SUCCESS_LIMIT_RANGE;
    }

    @Override
    protected GlobalConfigKeyEnum getLimitCountKey() {
        return GlobalConfigKeyEnum.LOGIN_SUCCESS_LIMIT_COUNT;
    }

    @Override
    protected String getPrefix() {
        return "Login_Success_";
    }
}
