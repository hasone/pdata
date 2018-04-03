package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.AccountChangeDetail;

/**
 * @author wujiamin
 * @date 2016年10月17日下午5:26:00
 */
public interface AccountChangeDetailService {

    /** 
     * @Title: insert 
     * @param record
     * @return
     * @Author: wujiamin
     * @date 2016年10月17日下午5:26:05
    */
    boolean insert(AccountChangeDetail record);

    /** 
     * @Title: selectByRequestId 
     * @param requestId
     * @return
     * @Author: wujiamin
     * @date 2016年10月17日下午5:26:21
    */
    AccountChangeDetail selectByRequestId(Long requestId);

}
