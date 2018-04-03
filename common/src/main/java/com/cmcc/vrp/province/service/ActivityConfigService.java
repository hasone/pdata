package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.ActivityConfig;

/**
 * Created by qinqinyan on 2017/8/31.
 */
public interface ActivityConfigService {

    /**
     * @title:
     * */
    boolean insert(ActivityConfig record);

    /**
     * @title:
     * */
    boolean updateByPrimaryKeySelective(ActivityConfig record);


    /**
     * @title:
     * */
    ActivityConfig getActivityConfig();

    /**
     * @title:
     * */
    String getProvince(String key);

    /**
     * @title:
     * */
    String getIsp(String isp);

    /**
     * @title:
     * */
    String getFromCache(String key);

}
