package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.EnterpriseUserId;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:20:59
*/
public interface EnterpriseUserIdMapper {
    /**
     * @param enterpriseUserId
     * @return
     */
    int insert(EnterpriseUserId enterpriseUserId);

    String getUserIdByEnterpriseCode(String code);
}
