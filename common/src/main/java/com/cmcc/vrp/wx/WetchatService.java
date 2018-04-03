package com.cmcc.vrp.wx;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cmcc.vrp.wx.beans.InviteQrcodeResp;
import com.cmcc.vrp.wx.beans.WxUserInfo;

/**
 * Created by leelyn on 2017/1/3.
 */
public interface WetchatService {

    public String getPhoneByOpenId(String openId);

    /** 
     * 发送模板消息
     * @Title: sendWetchatMsg 
     */
    public boolean sendWetchatMsg(String mobile, Integer templateMsgType, Long activityId, Map<String, String> params);

    /** 
     * 向公众号获取code
     * @Title: getCode 
     */
    String getCode(String redirectURI);

    
    /** 
     * 用code向微信公众号获取用户类型,返回待跳转的链接
     * @Title: getUserType 
     */
    String getUserType(HttpServletRequest request);

    /** 
     * 向微信公众号获取邀请二维码（临时二维码）
     * @Title: getInviteQrcode 
     */
    //InviteQrcodeResp getInviteQrcode(Integer sceneId);
    
    /** 
     * 向微信公众号获取邀请二维码（临时二维码）
     * @Title: getInviteQrcode 
     * @author qinqinyan
     * @date 20170712
     */
    InviteQrcodeResp getInviteQrcode(String sceneStr);

    /** 
     * 向微信公众号获取用户信息
     * @Title: getWxUserInfo 
     */
    WxUserInfo getWxUserInfo(String mobile);
    
    /** 
     * 发送模板消息
     * @Title: sendTemplateMag 
     */
    String sendTemplateMag(String param);
}
