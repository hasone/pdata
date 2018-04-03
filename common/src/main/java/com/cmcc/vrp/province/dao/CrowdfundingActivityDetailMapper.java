package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.CrowdfundingActivityDetail;

/**
 * 广东众筹活动详情mapper
 * Created by qinqinyan on 2017/1/6.
 * */
public interface CrowdfundingActivityDetailMapper {

    /**
     * @Description:根据id逻辑删除
     * @param id
     * @author qinqinyan
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * @Description:insert
     * @param record
     * @author qinqinyan
     * */
    int insert(CrowdfundingActivityDetail record);

    /**
     * @Description:insertSelective
     * @param record
     * @author qinqinyan
     * */
    int insertSelective(CrowdfundingActivityDetail record);

    /**
     * @Description:根据id查询
     * @param id
     * @author qinqinyan
     * */
    CrowdfundingActivityDetail selectByPrimaryKey(Long id);

    /**
     * @Description:根据activityId查询
     * @param activityId
     * @author qinqinyan
     * */
    CrowdfundingActivityDetail selectByActivityId(String activityId);

    /**
     * @Description:updateByPrimaryKeySelective
     * @param record
     * @author qinqinyan
     * */
    int updateByPrimaryKeySelective(CrowdfundingActivityDetail record);

    /**
     * @Description:updateByPrimaryKey
     * @param record
     * @author qinqinyan
     * */
    int updateByPrimaryKey(CrowdfundingActivityDetail record);

    /** 
     * @Title: selectByActivityId 
     */
    //CrowdfundingActivityDetail selectByActivityId(String activityId);

    /** 
     * @Title: updateCurrentCount 
     */
    int updateCurrentCount(CrowdfundingActivityDetail record);
}