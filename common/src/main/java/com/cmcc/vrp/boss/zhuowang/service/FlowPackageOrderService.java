package com.cmcc.vrp.boss.zhuowang.service;

import com.cmcc.vrp.boss.zhuowang.bean.OrderRequestResult;

import java.util.List;
import java.util.Map;

/**
 * 流量包订购
 *
 * @author qinpo
 */
public interface FlowPackageOrderService<T> {

    /**
     * @return
     */
    OrderRequestResult sendRequest(List<T> list, String serialNum);

    Map<String, String> getAuthHead() throws Exception;

}
