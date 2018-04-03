package com.cmcc.vrp.province.service.impl;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.RandomPointService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * 积分服务的简单实现
 *
 * Created by sunyiwei on 2017/2/23.
 */
@Service
public class RandomPointServiceImpl implements RandomPointService {
    @Autowired
    private GlobalConfigService globalConfigService;

    /**
     * 产生下一个随机积分
     *
     * @param mobile 积分将赋予的对象
     */
    @Override
    public int next(String mobile) {
    	ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextInt(1, maxValue());
    }

    private int maxValue() {
        return NumberUtils.toInt(globalConfigService.get(GlobalConfigKeyEnum.MAX_POINT_PER_DAY.getKey()), 1);
    }
}
