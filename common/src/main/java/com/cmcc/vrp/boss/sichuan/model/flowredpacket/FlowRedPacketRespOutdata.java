package com.cmcc.vrp.boss.sichuan.model.flowredpacket;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * <!--[if !supportLists]-->1.1.1.5. <!--[endif]-->响应参数
 * 流量红包返回参数
 * @author qihang
 *
 */
public class FlowRedPacketRespOutdata {
    @JSONField(name="LOGIN_ACCEPT")
    private String loginAccept;
    
    @JSONField(name="EFF_DATE")
    private String effDate;
    
    @JSONField(name="EXP_DATE")
    private String expDate;
    
    @JSONField(name="BRAND_ID")
    private String brandId;
    
    @JSONField(name="EFFEXP_MODE")
    private String effexpMode;
    
    @JSONField(name="ORDER_ID")
    private String orderId;
    

    public String getLoginAccept() {
        return loginAccept;
    }

    public void setLoginAccept(String loginAccept) {
        this.loginAccept = loginAccept;
    }

    public String getEffDate() {
        return effDate;
    }

    public void setEffDate(String effDate) {
        this.effDate = effDate;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getEffexpMode() {
        return effexpMode;
    }

    public void setEffexpMode(String effexpMode) {
        this.effexpMode = effexpMode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
   
}

