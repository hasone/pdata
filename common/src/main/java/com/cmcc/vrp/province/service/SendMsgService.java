/**
 * @Title: SendMsgService.java
 * @Package com.cmcc.vrp.province.service
 * @author: qihang
 * @date: 2015年6月9日 下午6:56:49
 * @version V1.0
 */
package com.cmcc.vrp.province.service;

import com.cmcc.vrp.enums.SmsType;

/**
 * @ClassName: SendMsgService
 * @Description: TODO
 * @author: qihang
 * @date: 2015年6月9日 下午6:56:49
 *
 */
public interface SendMsgService {

    /** 
     * @Title: sendRandomCode 
    */
    String sendRandomCode(String mobile, SmsType smstype);

    /** 
     * @Title: sendVerifyCode 
    */
    boolean sendVerifyCode(String mobile, String content);

    //boolean sendMessage(String mobile, String content, MessageType messageType);
    /** 
     * @Title: sendMessage 
    */
    boolean sendMessage(String mobile, String content, String messageType);

}

