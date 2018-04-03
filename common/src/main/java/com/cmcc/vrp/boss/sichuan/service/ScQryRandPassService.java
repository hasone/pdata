package com.cmcc.vrp.boss.sichuan.service;


/**
 * 四川短信验证码下发和校验接口
 * ScQryRandPassService.java
 * @author wujiamin
 * @date 2017年3月17日
 */
public interface ScQryRandPassService {
    
    /** 
     * @Title: sendQryRandPass 
     */
    boolean sendQryRandPass(String mobile, String type, String randPass, String msgType);
}
