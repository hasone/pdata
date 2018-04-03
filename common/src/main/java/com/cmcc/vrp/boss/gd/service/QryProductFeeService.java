package com.cmcc.vrp.boss.gd.service;

import com.cmcc.vrp.boss.gd.model.QryProductFeeResp;

/**
 * @author lgk8023
 *
 */
public interface QryProductFeeService {

    /**
     * @param ecPrdCode
     * @return
     */
    public QryProductFeeResp qryProductFeeResp(String ecPrdCode);
    
    public String getFee(String ecPrdCode);
    
}
