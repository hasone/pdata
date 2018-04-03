package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.CrowdfundingActivityDetailMapper;
import com.cmcc.vrp.province.model.CrowdfundingActivityDetail;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.CrowdfundingActivityDetailService;
import com.cmcc.vrp.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by qinqinyan on 2017/1/6.
 */
@Service("crowdfundingActivityDetailService")
public class CrowdfundingActivityDetailServiceImpl implements CrowdfundingActivityDetailService {

    @Autowired
    ActivitiesService activitiesService;
    @Autowired
    CrowdfundingActivityDetailMapper mapper;
    @Autowired
    ActivityPrizeService activityPrizeService;

    /**
     * @Description:deleteByPrimaryKey
     * @param id
     * */
    @Override
    public boolean deleteByPrimaryKey(Long id) {
        if(id==null){
            return false;
        }
        return mapper.deleteByPrimaryKey(id)==1;
    }

    /**
     * @Description:insert
     * @param record
     * */
    @Override
    public boolean insert(CrowdfundingActivityDetail record) {
        if(record==null){
            return false;
        }
        return mapper.insert(record)==1;
    }

    /**
     * @Description:insertSelective
     * @param record
     * */
    @Override
    public boolean insertSelective(CrowdfundingActivityDetail record) {
        if(record==null){
            return false;
        }
        return mapper.insertSelective(record)==1;
    }

    /**
     * @Description:selectByPrimaryKey
     * @param id
     * */
    @Override
    public CrowdfundingActivityDetail selectByPrimaryKey(Long id) {
        if(id==null){
            return null;
        }
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * @Description:selectByActivityId
     * @param activityId
     * */
    @Override
    public CrowdfundingActivityDetail selectByActivityId(String activityId) {
        if(StringUtils.isEmpty(activityId)){
            return null;
        }
        return mapper.selectByActivityId(activityId);
    }

    /**
     * @Description:updateByPrimaryKeySelective
     * @param record
     * */
    @Override
    public boolean updateByPrimaryKeySelective(CrowdfundingActivityDetail record) {
        if(record==null){
            return false;
        }
        return mapper.updateByPrimaryKeySelective(record)==1;
    }

    /**
     * @Description:updateByPrimaryKey
     * @param record
     * */
    @Override
    public boolean updateByPrimaryKey(CrowdfundingActivityDetail record) {
        if(record==null){
            return false;
        }
        return mapper.updateByPrimaryKey(record)==1;
    }


    @Override
    public boolean updateCurrentCount(CrowdfundingActivityDetail record) {        
        return mapper.updateCurrentCount(record) == 1;
    }
}
