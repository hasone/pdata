package com.cmcc.vrp.wx;

import com.cmcc.vrp.wx.model.GetPaymentOrderResp;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年5月8日 下午3:13:59
*/
public interface PayResultQueryService {
    
    /**
     * @param orderId
     * @return
     */
    public GetPaymentOrderResp payResultQuery(String orderId);

    /** 
     * 检查是否有支付中的支付记录
     * @Title: checkPayingStatus 
     */
    Boolean checkPayingStatus(String orderId);

}
