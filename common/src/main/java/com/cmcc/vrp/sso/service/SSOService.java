package com.cmcc.vrp.sso.service;

/**
 * 单点登录相关服务
 *
 */
public interface SSOService {
    
    /**
     * 向单点登录平台发送退出的请求
     */
    boolean gdSendLogoutReq(String mobilePhone);
}
