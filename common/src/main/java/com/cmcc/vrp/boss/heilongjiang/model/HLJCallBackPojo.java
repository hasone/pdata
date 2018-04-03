package com.cmcc.vrp.boss.heilongjiang.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @ClassName: HLJCallBackPojo 
 * @Description: 黑龙江异步回调报文封装类
 * @author: Rowe
 * @date: 2017年9月18日 上午10:41:31
 */
@XStreamAlias("Request")
public class HLJCallBackPojo {
    
    @XStreamAlias("Datetime")
    private String datetime;
    
    @XStreamAlias("OrderId")
    private String orderId;
    
    @XStreamAlias("ExtOrderId")
    private String extOrderId;//平台发起的充值请求流水号
    
    @XStreamAlias("ReturnCode")
    private String returnCode;//充值结果状态码：0表成功，其他失败；
    
    @XStreamAlias("ReturnMsg")
    private String returnMsg;//充值结果描述信息

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getExtOrderId() {
        return extOrderId;
    }

    public void setExtOrderId(String extOrderId) {
        this.extOrderId = extOrderId;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

   
}
