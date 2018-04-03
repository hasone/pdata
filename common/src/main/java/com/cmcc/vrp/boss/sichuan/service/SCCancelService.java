package com.cmcc.vrp.boss.sichuan.service;

import com.cmcc.vrp.boss.sichuan.model.SCCancelRequest;

/**
 * @author wujiamin
 * @date 2016年11月1日
 */
public interface SCCancelService {
    /**
     * 封装请求参数
     *
     * @param request
     * @return
     */
    String generateRequestString(SCCancelRequest request);

    /**
     * 发送请求
     *
     * @param params
     * @return
     */
    Boolean sendCancelRequest(SCCancelRequest request);

}
