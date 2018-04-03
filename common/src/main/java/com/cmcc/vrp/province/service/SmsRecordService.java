/**
 * @Title: SmsRecordService.java
 * @Package com.cmcc.vrp.province.service
 * @author: sunyiwei
 * @date: 2015年6月10日 下午6:32:16
 * @version V1.0
 */
package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.SmsRecord;

/**
 * @ClassName: SmsRecordService
 * @Description: 手机验证码记录
 * @author: sunyiwei
 * @date: 2015年6月10日 下午6:32:16
 *
 */
public interface SmsRecordService {
    /*
     * 获取短信记录
     */
    /** 
     * @Title: get 
    */
    SmsRecord get(String mobile);

    /*
     * 删除短信记录
     */
    /** 
     * @Title: delete 
    */
    boolean delete(String mobile);

    /*
     * 插入短信记录
     */
    /** 
     * @Title: insert 
    */
    boolean insert(SmsRecord record);
}
