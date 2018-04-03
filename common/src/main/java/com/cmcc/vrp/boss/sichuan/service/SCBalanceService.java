/**
 *
 */
package com.cmcc.vrp.boss.sichuan.service;

import com.cmcc.vrp.boss.sichuan.model.SCBalanceRequest;
import com.cmcc.vrp.boss.sichuan.model.SCBalanceResponse;


/**
 * <p>Description: </p>
 *
 * @author xj
 * @date 2016年4月25日
 */
public interface SCBalanceService {

    /**
     * 封装请求参数
     *
     * @param request
     * @return
     */
    String generateRequestString(SCBalanceRequest request);

    /**
     * 解析输出参数
     *
     * @param responseStr
     * @return
     */
    SCBalanceResponse parseResponse(String responseStr);

    /**
     * 发送请求
     *
     * @param requestStr
     * @return
     */
    SCBalanceResponse sendBalanceRequest(SCBalanceRequest request);


}
