package com.cmcc.vrp.province.service.impl;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.province.cache.AbstractCacheSupport;
import com.cmcc.vrp.province.dao.ActivityConfigMapper;
import com.cmcc.vrp.province.model.ActivityConfig;
import com.cmcc.vrp.province.service.ActivityConfigService;
import com.cmcc.vrp.util.Constants;
import com.cmcc.vrp.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by qinqinyan on 2017/8/31.
 */
@Service
public class ActivityConfigServiceImpl extends AbstractCacheSupport implements ActivityConfigService {

    private Logger logger = LoggerFactory.getLogger(ActivityConfigServiceImpl.class);

    @Autowired
    ActivityConfigMapper mapper;

    @Override
    public boolean insert(ActivityConfig record) {
        if(record != null){
            if(mapper.insert(record)!=1){
                logger.info("插入奖品配置失败。"+ JSON.toJSONString(record));
                return false;
            }
            //插入数据库后要更新到缓存
            if(!cacheService.add(Constants.ACTIVITY_CONFIG_KEY.PROVINCE.getResult(), record.getProvince())){
                logger.info("插入缓存失败。key = {}, value = {}",
                    getPrefix()+ Constants.ACTIVITY_CONFIG_KEY.PROVINCE.getResult(), record.getProvince());
            }

            if(!cacheService.add(Constants.ACTIVITY_CONFIG_KEY.ISP.getResult(), record.getIsp())){
                logger.info("插入缓存失败。key = {}, value = {}",
                    getPrefix()+ Constants.ACTIVITY_CONFIG_KEY.ISP.getResult(), record.getIsp());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean updateByPrimaryKeySelective(ActivityConfig record) {
        if(record != null){
            if(mapper.updateByPrimaryKeySelective(record)!=1){
                logger.info("更新奖品配置失败。"+ JSON.toJSONString(record));
                return false;
            }
            //更新缓存
            if(!cacheService.update(Constants.ACTIVITY_CONFIG_KEY.PROVINCE.getResult(), record.getProvince())){
                logger.info("更新缓存失败。key = {}, value = {}",
                    getPrefix()+ Constants.ACTIVITY_CONFIG_KEY.PROVINCE.getResult(), record.getProvince());
            }

            if(!cacheService.update(Constants.ACTIVITY_CONFIG_KEY.ISP.getResult(), record.getIsp())){
                logger.info("更新缓存失败。key = {}, value = {}",
                    getPrefix()+ Constants.ACTIVITY_CONFIG_KEY.ISP.getResult(), record.getIsp());
            }
            return true;
        }
        return false;
    }

    @Override
    protected String getPrefix() {
        return "activity.config.";
    }

    @Override
    public ActivityConfig getActivityConfig() {
        List<ActivityConfig> activityConfigs = mapper.getActivityConfig();
        if(activityConfigs!=null && activityConfigs.size()>0){
            return activityConfigs.get(0);
        }
        return null;
    }

    @Override
    public String getProvince(String key) {
        String province = getFromCache(key);
        if(StringUtils.isEmpty(province)){
            ActivityConfig activityConfig = getActivityConfig();
            if(activityConfig!=null){
                province = activityConfig.getProvince();
            }
        }
        return province;
    }

    @Override
    public String getIsp(String key) {
        String isp = getFromCache(key);
        if(StringUtils.isEmpty(isp)){
            ActivityConfig activityConfig = getActivityConfig();
            if(activityConfig!=null){
                isp = activityConfig.getIsp();
            }
        }
        return isp;
    }

    @Override
    public String getFromCache(String key) {
        if(!StringUtils.isEmpty(key)){
            return cacheService.get(key);
        }
        return null;
    }
}
