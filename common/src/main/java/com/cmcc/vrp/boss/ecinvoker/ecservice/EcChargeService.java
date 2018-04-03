package com.cmcc.vrp.boss.ecinvoker.ecservice;

import com.cmcc.vrp.ec.bean.ChargeReq;
import com.cmcc.vrp.ec.bean.ChargeResp;
import com.cmcc.vrp.ec.bean.QueryResp;

/**
 * Created by leelyn on 2016/7/14.
 */
public interface EcChargeService {

    /**
     * 调用EC接口充值
     *
     * @param chargeReq
     * @param token
     * @param signature
     * @return
     */
    ChargeResp charge(ChargeReq chargeReq, String token, String signature, String url);
    
    
    QueryResp qureyChargeResult(String token, String signature, String url);

}
