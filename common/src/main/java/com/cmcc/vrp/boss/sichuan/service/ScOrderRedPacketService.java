package com.cmcc.vrp.boss.sichuan.service;

import com.cmcc.vrp.boss.sichuan.model.flowredpacket.OrderRedPacketReq;
import com.cmcc.vrp.boss.sichuan.model.flowredpacket.OrderRedPacketResp;

/**
 * 四川流量红包服务
 *
 */
public interface ScOrderRedPacketService {
    /**
     * 得到发送数据，包括sign
     */
    String generateRequestString(OrderRedPacketReq req);
    
    /**
     * 发送http请求，得到结果，失败为空
     */
    OrderRedPacketResp sendRequest(OrderRedPacketReq req);

    /**
     * 得到发送地址
     */
    String getReqUrl();
    
    /**
     * 得到私钥
     */
    String getPrivateKeyPath();
}
