package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.IndividualActivitySerialNum;

/**
 * IndividualActivitySerialNumService.java
 * @author wujiamin
 * @date 2017年1月12日
 */
public interface IndividualActivitySerialNumService {
    /** 
     * @Title: insert 
     */
    boolean insert(IndividualActivitySerialNum record);

    /** 
     * @Title: insertSelective 
     */
    boolean insertSelective(IndividualActivitySerialNum record);


    /** 
     * @Title: updateByPrimaryKeySelective 
     */
    boolean updateByPrimaryKeySelective(IndividualActivitySerialNum record);

    /** 
     * @Title: updateByPrimaryKey 
     */
    boolean updateByPrimaryKey(IndividualActivitySerialNum record);

    /** 
     * @Title: selectByPrimaryKey 
     */
    IndividualActivitySerialNum selectByPrimaryKey(Integer id);
}
