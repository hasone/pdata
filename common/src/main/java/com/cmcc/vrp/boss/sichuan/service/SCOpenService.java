package com.cmcc.vrp.boss.sichuan.service;

import com.cmcc.vrp.boss.sichuan.model.SCOpenRequest;
import com.cmcc.vrp.boss.sichuan.model.SCOpenResponse;

/**
 * @author wujiamin
 * @date 2016年11月1日
 */
public interface SCOpenService {
    /**
     * 封装请求参数
     *
     * @param request
     * @return
     */
    String generateRequestString(SCOpenRequest request);

    /**
     * 解析输出参数
     *
     * @param responseStr
     * @return
     */
    SCOpenResponse parseResponse(String responseStr);


    /**
     * 发送请求
     *
     * @param params
     * @return
     */
    SCOpenResponse sendOpenRequest(SCOpenRequest request);

}
