package com.cmcc.vrp.province.service;

import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.model.HistoryEnterpriseFile;

/**
 * Created by qinqinyan on 2016/10/12.
 */
@Service("historyEnterpriseFileService")
public interface HistoryEnterpriseFileService {

    /**
    * @Title: insertSelective
    * @Description: TODO
    */ 
    boolean insertSelective(HistoryEnterpriseFile record);

    /**
     * 根据审核请求id查找
     * @param requestId
     * @return
     * @author qinqinyan
     * */
    HistoryEnterpriseFile selectByRequestId(Long requestId);
}
