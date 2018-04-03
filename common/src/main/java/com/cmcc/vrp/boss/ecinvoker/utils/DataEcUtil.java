package com.cmcc.vrp.boss.ecinvoker.utils;

import com.cmcc.vrp.ec.bean.ChargeReq;
import com.cmcc.vrp.ec.bean.ChargeReqData;

/**
 * Created by leelyn on 2016/7/15.
 */
public class DataEcUtil {

    /**
     * 构建充值请求实例
     *
     * @param requstTime
     * @param productCode
     * @param mobile
     * @param serialNum
     * @return
     */
    public static ChargeReq buildCR(String requstTime, String productCode, String mobile, String serialNum) {
        ChargeReq req = new ChargeReq();
        req.setRequestTime(requstTime);
        ChargeReqData reqData = new ChargeReqData();
        reqData.setMobile(mobile);
        reqData.setProductCode(productCode);
        reqData.setSerialNum(serialNum);
        req.setChargeReqData(reqData);
        return req;
    }
}
