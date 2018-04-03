package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.ActivityApprovalDetailMapper;
import com.cmcc.vrp.province.model.ActivityApprovalDetail;
import com.cmcc.vrp.province.service.ActivityApprovalDetailService;
import com.cmcc.vrp.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by qinqinyan on 2016/9/18.
 */
@Service("activityApprovalDetailService")
public class ActivityApprovalDetailServiceImpl implements ActivityApprovalDetailService {
    @Autowired
    ActivityApprovalDetailMapper mapper;

    @Override
    public boolean insert(ActivityApprovalDetail record) {
        if (record == null) {
            return false;
        }
        return mapper.insert(record) == 1;
    }

    @Override
    public ActivityApprovalDetail selectByRequestId(Long requestId) {
        if (requestId == null) {
            return null;
        }
        return mapper.selectByRequestId(requestId);
    }

    @Override
    public List<ActivityApprovalDetail> selectByActivityId(String activityId) {
        if(!StringUtils.isEmpty(activityId)){
            return mapper.selectByActivityId(activityId);
        }
        return null;
    }
}
