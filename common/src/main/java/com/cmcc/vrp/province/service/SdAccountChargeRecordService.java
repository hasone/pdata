package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.SdAccountChargeRecord;

/**
 * 
 * @ClassName: SdAccountChargeRecordService 
 * @Description: 山东BOSS账户充值记录服务类
 * @author: Rowe
 * @date: 2017年9月1日 上午9:47:54
 */
public interface SdAccountChargeRecordService {
    
    /**
     * 
     * @Title: insertSelective 
     * @Description: TODO
     * @param record
     * @return
     * @return: boolean
     */
    boolean insertSelective(SdAccountChargeRecord record);
    
    
    /**
     * 
     * @Title: updateByPrimaryKeySelective 
     * @Description: TODO
     * @param record
     * @return
     * @return: boolean
     */
    boolean updateByPrimaryKeySelective(SdAccountChargeRecord record);
}
