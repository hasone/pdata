package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.cache.AbstractCacheSupport;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.RateLimitService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 登录频率限制
 * <p>
 * Created by sunyiwei on 2016/11/17.
 */
public abstract class AbstractRateLimitServiceImpl extends AbstractCacheSupport implements RateLimitService {
    @Autowired
    protected GlobalConfigService globalConfigService;

    @Override
    final public boolean allowToContinue(String key) {
        RateLimitInfo rli = getRateLimitInfo();

        String cacheValue = null;
        if (rli != null  //表示开启这种状态的限制
            && StringUtils.isNotBlank(cacheValue = cacheService.get(key))  //且设置了相应的值
            && NumberUtils.toInt(cacheValue, Integer.MAX_VALUE) >= rli.getCount()) {  //且缓存中的值已经超过了上限值
            throw new RuntimeException("对不起，您的操作过于频繁，请稍候再试！");
        }

        return true;
    }

    @Override
    final public void add(String key) {
        RateLimitInfo rli = getRateLimitInfo();
        if (rli != null) { //只有在开启限制的时候才需要记录
            cacheService.incrOrUpdate(key, rli.getTimeRange());
        }
    }

    //是否开启了相应的限制开关
    private AbstractRateLimitServiceImpl.RateLimitInfo getRateLimitInfo() {
        if ("ON".equalsIgnoreCase(globalConfigService.get(getLimitSwitchKey().getKey()))) {
            int range = NumberUtils.toInt((globalConfigService.get(getLimitTimeRangeKey().getKey())), 60);
            int count = NumberUtils.toInt((globalConfigService.get(getLimitCountKey().getKey())), 5);

            return new RateLimitInfo(range, count);
        }

        return null;
    }

    //限制开关的key
    protected abstract GlobalConfigKeyEnum getLimitSwitchKey();

    //限制时间间隔的key
    protected abstract GlobalConfigKeyEnum getLimitTimeRangeKey();

    //限制频率的key
    protected abstract GlobalConfigKeyEnum getLimitCountKey();

    protected class RateLimitInfo {
        private int timeRange;
        private int count;

        public RateLimitInfo(int timeRange, int count) {
            this.timeRange = timeRange;
            this.count = count;
        }

        public int getTimeRange() {
            return timeRange;
        }

        public int getCount() {
            return count;
        }
    }
}
