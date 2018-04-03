package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.IndividualActivitySerialNum;

/**
 * IndividualActivitySerialNumMapper
 *
 */
public interface IndividualActivitySerialNumMapper {
    /**
     * int deleteByPrimaryKey(Integer id);
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * int insert(IndividualActivitySerialNum record);
     */
    int insert(IndividualActivitySerialNum record);

    /**
     * int insertSelective(IndividualActivitySerialNum record);
     */
    int insertSelective(IndividualActivitySerialNum record);

    /**
     * IndividualActivitySerialNum selectByPrimaryKey(Integer id);
     */
    IndividualActivitySerialNum selectByPrimaryKey(Integer id);

    /**
     * int updateByPrimaryKeySelective(IndividualActivitySerialNum record);
     */
    int updateByPrimaryKeySelective(IndividualActivitySerialNum record);

    /**
     * int updateByPrimaryKey(IndividualActivitySerialNum record);
     */
    int updateByPrimaryKey(IndividualActivitySerialNum record);
}