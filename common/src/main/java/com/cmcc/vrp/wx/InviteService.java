package com.cmcc.vrp.wx;

/**
 * 微信邀请服务
 * InviteService.java
 * @author wujiamin
 * @date 2017年2月23日
 */
public interface InviteService {
    
    /** 
     * 获取用户是否首次登录的标记
     * @Title: getFirstFlag 
     */
    public String getFirstFlag(Long adminId);
    
    /** 
     * 根据手机号码刷新二维码（二维码没过期则不做任何操作，二维码过期则更新）
     * @Title: refreshQrcode 
     */
    boolean refreshQrcode(String mobile, String openid);
    
    /** 
     * 处理微信发送过来的被邀请请求
     * @Title: processInvited 
     */
    boolean processInvited(String param);
    
}
