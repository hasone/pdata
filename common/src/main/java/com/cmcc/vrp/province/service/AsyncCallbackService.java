package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.AsyncCallbackReq;

/**
 * 处理异常回调的服务类
 * <p>
 * Created by sunyiwei on 2016/10/11.
 */
public interface AsyncCallbackService {
    /**
     * 处理BOSS侧的回调， 包括但不限于以下操作
     * <p>
     * 1. 更新充值记录
     * 2. 更新流水号表
     * 3. 更新活动记录或接口记录
     * 4. 如果失败了， 要进行退钱操作
     * 5. 回调EC
     *
     * @param acr
     * @return
     */
    boolean process(AsyncCallbackReq acr);
}
