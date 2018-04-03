package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import org.springframework.stereotype.Service;

/**
 * 普通赠送频率限制服务
 * <p>
 * Created by sunyiwei on 2016/11/17.
 */
@Service
public class PresentRateLimitServiceImpl extends AbstractRateLimitServiceImpl {
    @Override
    protected GlobalConfigKeyEnum getLimitSwitchKey() {
        return GlobalConfigKeyEnum.PRESENT_RATE_LIMIT;
    }

    @Override
    protected GlobalConfigKeyEnum getLimitTimeRangeKey() {
        return GlobalConfigKeyEnum.PRESENT_RATE_LIMIT_RANGE;
    }

    @Override
    protected GlobalConfigKeyEnum getLimitCountKey() {
        return GlobalConfigKeyEnum.PRESENT_RATE_LIMIT_COUNT;
    }

    @Override
    protected String getPrefix() {
        return "Present_Limit_";
    }
}
