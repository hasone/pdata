package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.ActivityConfig;

import java.util.List;


/**
 * 营销活动配置
 * create by qinqinyan on 2017/08/31
 * */
public interface ActivityConfigMapper {

    /**
     * @title:
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * @title:
     * */
    int insert(ActivityConfig record);

    /**
     * @title:
     * */
    int insertSelective(ActivityConfig record);

    /**
     * @title:
     * */
    ActivityConfig selectByPrimaryKey(Long id);

    /**
     * @title:
     * */
    int updateByPrimaryKeySelective(ActivityConfig record);

    /**
     * @title:
     * */
    int updateByPrimaryKey(ActivityConfig record);

    /**
     * @title:
     * */
    List<ActivityConfig> getActivityConfig();
}