package com.cmcc.vrp.wx;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cmcc.vrp.wx.model.WxExchangeRecord;

/**
 * WxExchangeRecordService.java
 * @author wujiamin
 * @date 2017年3月17日
 */
public interface WxExchangeRecordService {
    
    /** 
     * @Title: insert 
     */
    public boolean insert(WxExchangeRecord record);


    /** 
     * @Title: selectBySystemNum 
     */
    WxExchangeRecord selectBySystemNum(String systemNum);


    /** 
     * @Title: updateByPrimaryKeySelective 
     */
    boolean updateByPrimaryKeySelective(WxExchangeRecord record);
    
    /** 
     * 根据月份，adminId计算某账户某月兑换流量的总量
     * @Title: sumProductSize 
     */
    int sumProductSize(int month, Long adminId);
    
    /**
     * 根据日期，adminId计算某账户某月兑换流量的总量
     * @param day
     * @param adminId
     * @return
     */
    int sumDayProductSize(Date begin, Date end, Long adminId);
    
    /**
     * 计算当月平台兑换总量
     * @param month
     * @return
     */
    int sumAllProductSize(int month);
    
    /** 
     * 根据月份，productId计算某产品某月兑换的总个数
     * @Title: sumProductNum 
     */
    int sumProductNum(int month, Long productId);
    
    /** 
     * @Title: selectByMap 
     */
    List<WxExchangeRecord> selectByMap(Map map);


    /** 
     * @Title: countByMap 
     */
    int countByMap(Map map);
}
