package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.Masking;

/**
 * @author lgk8023
 *
 */
public interface MaskingMapper {

    /**
     * @param masking
     * @return
     */
    int insert(Masking masking);
    
    Masking getByAdminId(Long adminId);

    /**
     * @param masking
     * @return
     */
    int updateByPrimaryKeySelective(Masking masking);
}
