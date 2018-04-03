package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.EnterpriseChangeDetail;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:13:50
*/
public interface EnterpriseChangeDetailMapper {
    /**
     * @param record
     * @return
     */
    int insert(EnterpriseChangeDetail record);

    /**
     * @param record
     * @return
     */
    int insertSelective(EnterpriseChangeDetail record);
}