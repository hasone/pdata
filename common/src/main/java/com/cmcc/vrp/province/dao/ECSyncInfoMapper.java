package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.ECSyncInfo;

/**
 * @author lgk8023
 *
 */
public interface ECSyncInfoMapper {

    /**
     * @param ecSyncInfo
     * @return
     */
    int insert(ECSyncInfo ecSyncInfo);
    
    /**
     * @param ecSyncInfo
     * @return
     */
    int updateByPrimaryKeySelective(ECSyncInfo ecSyncInfo);

    /**
     * @param ecCode
     * @return
     */
    ECSyncInfo selectByECCode(String ecCode);
}
