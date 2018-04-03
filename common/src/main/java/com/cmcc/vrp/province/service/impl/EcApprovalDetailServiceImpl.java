
package com.cmcc.vrp.province.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.EcApprovalDetailMapper;
import com.cmcc.vrp.province.model.EcApprovalDetail;
import com.cmcc.vrp.province.service.EcApprovalDetailService;

/**
 * @author wujiamin
 * @date 2016年10月21日
 */
@Service("ecApprovalDetail")
public class EcApprovalDetailServiceImpl implements EcApprovalDetailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EcApprovalDetailServiceImpl.class);
    
    @Autowired
    EcApprovalDetailMapper mapper;
    
    /**
     * 插入
     * @Title: insert 
     * @param ecApprovalDetail
     * @return
     * @Author: wujiamin
     * @date 2016年10月21日
     */
    @Override
    public boolean insert(EcApprovalDetail ecApprovalDetail) {
        return mapper.insert(ecApprovalDetail) == 1;
    }

    /**
     * selectByRequestId
     * */
    @Override
    public EcApprovalDetail selectByRequestId(Long requestId) {
        return requestId == null? null: mapper.selectByRequestId(requestId);
    }
}
