package com.cmcc.vrp.boss.gd.service;

import com.cmcc.vrp.boss.gd.model.QryECSyncInfoResp;

/**
 * @author lgk8023
 *
 */
public interface QryECSyncInfoService {

    /**
     * @param ecCode
     * @return
     */
    public QryECSyncInfoResp qryECSyncInfo(String ecCode);
    
    /**
     * @param ecCode
     * @return
     */
    public String updateECInfo(String ecCode);
}
