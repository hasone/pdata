package com.cmcc.vrp.province.dao;

import java.util.List;

import com.cmcc.vrp.province.model.YqxChargeInfo;

/**
 * YqxChargeInfoMapper.java
 * @author wujiamin
 * @date 2017年5月10日
 */
public interface YqxChargeInfoMapper {
    /** 
     * @Title: deleteByPrimaryKey 
     */
    int deleteByPrimaryKey(Long id);

    /** 
     * @Title: insert 
     */
    int insert(YqxChargeInfo record);

    /** 
     * @Title: insertSelective 
     */
    int insertSelective(YqxChargeInfo record);

    /** 
     * @Title: selectByPrimaryKey 
     */
    YqxChargeInfo selectByPrimaryKey(Long id);

    /** 
     * @Title: updateByPrimaryKeySelective 
     */
    int updateByPrimaryKeySelective(YqxChargeInfo record);

    /** 
     * @Title: updateByPrimaryKey 
     */
    int updateByPrimaryKey(YqxChargeInfo record);

    /** 
     * @Title: updateReturnSystemNum 
     */
    int updateReturnSystemNum(YqxChargeInfo record);
    
    /** 
     * @Title: selectByReturnSystemNum 
     */
    List<YqxChargeInfo> selectByReturnSystemNum(String returnSystemNum);

    /** 
     * @Title: selectBySerialNum 
     */
    YqxChargeInfo selectBySerialNum(String serialNum);
}