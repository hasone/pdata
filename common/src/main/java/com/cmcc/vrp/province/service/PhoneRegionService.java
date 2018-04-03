package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.PhoneRegion;

/**
 * 手机号段服务
 * PhoneRegionService.java
 */
public interface PhoneRegionService {
    /**
     * 查询手机号段信息
     *
     * @param mobile 手机号码
     * @return 手机号段信息
     */
    PhoneRegion query(String mobile);
    
    /**
     * 
     * @Title: checkPhoneRegionService 
     * @Description: TODO
     * @param mobile
     * @return
     * @return: String
     */
    public String checkPhoneRegionService(String mobile);
}
