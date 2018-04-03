package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.EcApprovalDetail;

/**
 * Ec信息申请详情
 * @author wujiamin
 * @date 2016年10月21日
 */
public interface EcApprovalDetailService {
    /**
     * 插入记录
     * @Title: insert 
     * @param ecApprovalDetail
     * @return
     * @Author: wujiamin
     * @date 2016年10月21日
     */
    boolean insert(EcApprovalDetail ecApprovalDetail);
    
    /**
     * 根据requestId记录
     * @Title: selectByRequestId 
     * @param requestId
     * @return
     * @Author: xujue
     * @date 2016年10月21日
     */
    EcApprovalDetail selectByRequestId(Long requestId);
}
