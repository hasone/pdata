package com.cmcc.vrp.boss.sichuan.service;

import com.cmcc.vrp.boss.sichuan.model.individual.ScFlowChgCfmRequest;
import com.cmcc.vrp.boss.sichuan.model.individual.ScFlowChgCfmResponse;

/**
 * ScFlowChgCfmService.java
 * @author wujiamin
 * @date 2016年11月10日
 */
public interface ScFlowChgCfmService {
    /**
     * 封装请求参数
     *
     * @param request
     * @return
     */
    String generateRequestString(ScFlowChgCfmRequest request);

    /**
     * 解析输出参数
     *
     * @param responseStr
     * @return
     */
    ScFlowChgCfmResponse parseResponse(String responseStr);

    /**
     * 发送请求
     *
     * @param requestStr
     * @return
     */
    ScFlowChgCfmResponse sendRequest(ScFlowChgCfmRequest request);
}
