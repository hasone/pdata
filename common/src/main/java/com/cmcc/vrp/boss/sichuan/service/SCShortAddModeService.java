package com.cmcc.vrp.boss.sichuan.service;

import com.cmcc.vrp.boss.sichuan.model.SCShortAddModeRequest;
import com.cmcc.vrp.boss.sichuan.model.SCShortAddModeResponse;

/**
 * @author wujiamin
 * @date 2016年11月1日
 */
public interface SCShortAddModeService {
    /**
     * 封装请求参数
     *
     * @param request
     * @return
     */
    String generateRequestString(SCShortAddModeRequest request);

    /**
     * 解析输出参数
     *
     * @param responseStr
     * @return
     */
    SCShortAddModeResponse parseResponse(String responseStr);

    /**
     * 发送请求
     *
     * @param requestStr
     * @return
     */
    SCShortAddModeResponse sendChargeRequest(SCShortAddModeRequest request);
}
