package com.cmcc.vrp.province.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.wx.model.WxExchangeRecord;

/**
 * WxExchangeRecordMapper.java
 * @author wujiamin
 * @date 2017年3月17日
 */
public interface WxExchangeRecordMapper {
    /** 
     * @Title: deleteByPrimaryKey 
     */
    int deleteByPrimaryKey(Long id);

    /** 
     * @Title: insert 
     */
    int insert(WxExchangeRecord record);

    /** 
     * @Title: insertSelective 
     */
    int insertSelective(WxExchangeRecord record);

    /** 
     * @Title: selectByPrimaryKey 
     */
    WxExchangeRecord selectByPrimaryKey(Long id);

    /** 
     * @Title: updateByPrimaryKeySelective 
     */
    int updateByPrimaryKeySelective(WxExchangeRecord record);

    /** 
     * @Title: updateByPrimaryKey 
     */
    int updateByPrimaryKey(WxExchangeRecord record);

    /** 
     * @Title: selectBySystemNum 
     */
    WxExchangeRecord selectBySystemNum(String systemNum);

    /** 
     * @Title: sumProductSize 
     */
    int sumProductSize(@Param("month")int month, @Param("adminId")Long adminId);

    /** 
     * @Title: sumProductNum 
     */
    int sumProductNum(@Param("month")int month, @Param("productId")Long productId);

    /** 
     * @Title: selectByMap 
     */
    List<WxExchangeRecord> selectByMap(Map map);
    
    /** 
     * @Title: countByMap 
     */
    int countByMap(Map map);

    /**
     * @param month
     * @return
     */
    int sumAllProductSize(@Param("month")int month);

    /**
     * @param day
     * @param adminId
     * @return
     */
    int sumDayProductSize(@Param("begin")Date begin, @Param("end")Date end, @Param("adminId")Long adminId);
}