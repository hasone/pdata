package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.Masking;

/**
 * @author lgk8023
 *
 */
public interface MaskingService {

    /**
     * @param masking
     * @return
     */
    boolean insert(Masking masking);
    
    Masking getByAdminId(Long adminId);
    
    /**
     * @param masking
     * @return
     */
    boolean updateByPrimaryKeySelective(Masking masking);

    /**
     * @param masking
     * @return
     */
    Boolean insertOrUpdate(Masking masking);
}
