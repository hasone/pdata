package com.cmcc.vrp.boss.sichuan.service;

import com.cmcc.vrp.boss.sichuan.model.SCChargeRequest;
import com.cmcc.vrp.boss.sichuan.model.SCChargeResponse;

/**
 * @author wujiamin
 * @date 2016年11月1日
 */
public interface SCAddMemberService {
    /**
     * 封装请求参数
     *
     * @param request
     * @return
     */
    String generateRequestString(SCChargeRequest request);

    /**
     * 解析输出参数
     *
     * @param responseStr
     * @return
     */
    SCChargeResponse parseResponse(String responseStr);

    /**
     * 发送请求
     *
     * @param requestStr
     * @return
     */
    SCChargeResponse sendChargeRequest(SCChargeRequest request);
}
