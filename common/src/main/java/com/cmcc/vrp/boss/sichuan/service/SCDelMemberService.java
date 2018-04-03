package com.cmcc.vrp.boss.sichuan.service;

import com.cmcc.vrp.boss.sichuan.model.SCDelMemberRequest;

/**
 * @author wujiamin
 * @date 2016年11月1日
 */
public interface SCDelMemberService {
    /**
     * 封装请求参数
     *
     * @param request
     * @return
     */
    String generateRequestString(SCDelMemberRequest request);

    /**
     * 发送请求
     *
     * @param params
     * @return
     */
    Boolean sendDelMemberRequest(SCDelMemberRequest request);

}
