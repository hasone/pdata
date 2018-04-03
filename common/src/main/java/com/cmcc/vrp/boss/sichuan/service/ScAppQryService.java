package com.cmcc.vrp.boss.sichuan.service;

import com.cmcc.vrp.boss.sichuan.model.individual.ScAppQryRequest;
import com.cmcc.vrp.boss.sichuan.model.individual.ScAppQryResponse;

/**
 * @author wujiamin
 * @date 2016年11月1日
 */
public interface ScAppQryService {
    /**
     * 封装请求参数
     *
     * @param request
     * @return
     */
    String generateRequestString(ScAppQryRequest request);

    /**
     * 解析输出参数
     *
     * @param responseStr
     * @return
     */
    ScAppQryResponse parseResponse(String responseStr);

    /**
     * 发送请求
     *
     * @param requestStr
     * @return
     */
    ScAppQryResponse sendRequest(ScAppQryRequest request);
}
