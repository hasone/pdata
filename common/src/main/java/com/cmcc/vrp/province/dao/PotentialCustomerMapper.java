package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.PotentialCustomer;

/**
 * @title:PotentialCustomerMapper
 * @description
 * */
public interface PotentialCustomerMapper {
    /**
     * @title:delete
     * @description
     * */
    int delete(Long id);

    /**
     * @title:insert
     * @description
     * */
    int insert(PotentialCustomer record);

    /**
     * @title:get
     * @description
     * */
    PotentialCustomer get(Long id);

    /**
     * @title:updateByPrimaryKey
     * @description
     * */
    int updateByPrimaryKey(PotentialCustomer record);

}