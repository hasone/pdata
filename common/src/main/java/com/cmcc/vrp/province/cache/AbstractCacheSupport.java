package com.cmcc.vrp.province.cache;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;

/**
 * Created by sunyiwei on 2016/7/18.
 */
public abstract class AbstractCacheSupport implements ApplicationContextAware {
    protected ApplicationContext applicationContext;

    protected CacheService cacheService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    protected void postConstruct() {
        cacheService = applicationContext.getBean(CacheServiceImpl.class);

        CacheServiceImpl cacheServiceImpl = (CacheServiceImpl) cacheService;
        cacheServiceImpl.setPrefix(getPrefix());
    }

    protected abstract String getPrefix();

    protected String getKeySetKey() {
        return "key.set";
    }
}
