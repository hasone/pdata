package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.IndividualActivityOrder;

/**
 * IndividualActivityOrderMapper.java
 * @author wujiamin
 * @date 2017年3月22日
 */
public interface IndividualActivityOrderMapper {
    /** 
     * @Title: deleteByPrimaryKey 
     */
    int deleteByPrimaryKey(Long id);

    /** 
     * @Title: insert 
     */
    int insert(IndividualActivityOrder record);

    /** 
     * @Title: insertSelective 
     */
    int insertSelective(IndividualActivityOrder record);

    /** 
     * @Title: selectByPrimaryKey 
     */
    IndividualActivityOrder selectByPrimaryKey(Long id);

    /** 
     * @Title: updateByPrimaryKeySelective 
     */
    int updateByPrimaryKeySelective(IndividualActivityOrder record);

    /** 
     * @Title: updateByPrimaryKey 
     */
    int updateByPrimaryKey(IndividualActivityOrder record);
}