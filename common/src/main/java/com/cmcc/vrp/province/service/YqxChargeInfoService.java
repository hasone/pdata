package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.YqxChargeInfo;

/**
 * YqxChargeInfoService.java
 * @author wujiamin
 * @date 2017年5月10日
 */
public interface YqxChargeInfoService {
    /** 
     * @Title: insert 
     */
    boolean insert(YqxChargeInfo record);

    /** 
     * @Title: updateReturnSystemNum 
     */
    boolean updateReturnSystemNum(YqxChargeInfo record);

    /** 
     * @Title: selectByReturnSystemNum 
     */
    YqxChargeInfo selectByReturnSystemNum(String returnSystemNum);
    
    /** 
     * @Title: selectBySerialNum 
     */
    YqxChargeInfo selectBySerialNum(String serialNum);
}
