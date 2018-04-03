package com.cmcc.vrp.boss.sichuan.service;

import com.cmcc.vrp.boss.sichuan.model.flowredpacket.FlowRedPacketReq;
import com.cmcc.vrp.boss.sichuan.model.flowredpacket.FlowRedPacketResp;

/**
 * 四川流量红包服务
 * @author qihang
 *
 */
public interface ScFlowRedPacketService {
    /**
     * 得到发送数据，包括sign
     */
    String generateRequestString(FlowRedPacketReq req);
    
    /**
     * 发送http请求，得到结果，失败为空
     */
    FlowRedPacketResp sendRequest(FlowRedPacketReq req);

    /**
     * 得到发送地址
     */
    String getReqUrl();
    
    /**
     * 得到私钥
     */
    String getPrivateKeyPath();
}
