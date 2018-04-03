package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.ActivityApprovalDetail;

import java.util.List;

/**
 * Created by qinqinyan on 2016/9/18.
 */
public interface ActivityApprovalDetailService {

    /**
     * @param record
     * @return
     */
    boolean insert(ActivityApprovalDetail record);

    /**
     * 根据申请请求id查询
     *
     * @author qinqinyan
     */
    ActivityApprovalDetail selectByRequestId(Long requestId);

    /**
     * 根据活动uuid查找活动审核记录
     * @author qinqinyan
     * */
    List<ActivityApprovalDetail> selectByActivityId(String activityId);
}
