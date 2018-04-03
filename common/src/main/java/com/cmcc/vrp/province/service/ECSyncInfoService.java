package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.ECSyncInfo;

/**
 * @author lgk8023
 *
 */
public interface ECSyncInfoService {

    /**
     * @param ecSyncInfo
     * @return
     */
    boolean insert(ECSyncInfo ecSyncInfo);
    
    /**
     * @param ecSyncInfo
     * @return
     */
    boolean updateByPrimaryKeySelective(ECSyncInfo ecSyncInfo);
    
    /**
     * @param ecCode
     * @return
     */
    ECSyncInfo selectByECCode(String ecCode);
    
    /**
     * @param ecSyncInfo
     * @return
     */
    boolean updateOrInsert(ECSyncInfo ecSyncInfo);
}
