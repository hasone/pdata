package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import org.springframework.stereotype.Service;


/**
 * 单个赠送频率限制
 * PresentSingleRateLimitServiceImpl.java
 * @author wujiamin
 * @date 2017年2月13日
 */
@Service
public class PresentSingleRateLimitServiceImpl extends AbstractRateLimitServiceImpl {
    @Override
    protected GlobalConfigKeyEnum getLimitSwitchKey() {
        return GlobalConfigKeyEnum.PRESENT_SINGLE_RATE_LIMIT;
    }

    @Override
    protected GlobalConfigKeyEnum getLimitTimeRangeKey() {
        return GlobalConfigKeyEnum.PRESENT_SINGLE_RATE_LIMIT_RANGE;
    }

    @Override
    protected GlobalConfigKeyEnum getLimitCountKey() {
        return GlobalConfigKeyEnum.PRESENT_SINGLE_RATE_LIMIT_COUNT;
    }

    @Override
    protected String getPrefix() {
        return "Present_Single_Limit_";
    }
}
