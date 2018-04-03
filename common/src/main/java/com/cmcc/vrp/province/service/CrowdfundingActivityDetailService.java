package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.CrowdfundingActivityDetail;

/**
 * 广东流量众筹活动详情服务类
 * Created by qinqinyan on 2017/1/6.
 */
public interface CrowdfundingActivityDetailService {

    /**
     * @Description:根据id逻辑删除
     * @param id
     * @author qinqinyan
     * */
    boolean deleteByPrimaryKey(Long id);

    /**
     * @Description:insert
     * @param record
     * @author qinqinyan
     * */
    boolean insert(CrowdfundingActivityDetail record);

    /**
     * @Description:insertSelective
     * @param record
     * @author qinqinyan
     * */
    boolean insertSelective(CrowdfundingActivityDetail record);

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
    boolean updateByPrimaryKeySelective(CrowdfundingActivityDetail record);

    /**
     * @Description:updateByPrimaryKey
     * @param record
     * @author qinqinyan
     * */
    boolean updateByPrimaryKey(CrowdfundingActivityDetail record);

    /** 
     * @Title: updateCurrentCount 
     */
    boolean updateCurrentCount(CrowdfundingActivityDetail record);
}
