package com.cmcc.vrp.boss.henan.model;

/**
 * 河南省渠道充值请求包体
 * Created by leelyn on 2016/6/24.
 */
public class HaChargeReq {
    String GBILL_ID;
    String FLAG;
    String BILL_ID;
    String VALID_MONTH;
    String VALID_TYPE;
    String MEM_SRVPKG;
    String CUST_ORDER_ID;
    String SMS_TEMPLATE;

    public String getGBILL_ID() {
        return GBILL_ID;
    }

    public void setGBILL_ID(String GBILL_ID) {
        this.GBILL_ID = GBILL_ID;
    }

    public String getFLAG() {
        return FLAG;
    }

    public void setFLAG(String FLAG) {
        this.FLAG = FLAG;
    }

    public String getBILL_ID() {
        return BILL_ID;
    }

    public void setBILL_ID(String BILL_ID) {
        this.BILL_ID = BILL_ID;
    }

    public String getVALID_MONTH() {
        return VALID_MONTH;
    }

    public void setVALID_MONTH(String VALID_MONTH) {
        this.VALID_MONTH = VALID_MONTH;
    }

    public String getVALID_TYPE() {
        return VALID_TYPE;
    }

    public void setVALID_TYPE(String VALID_TYPE) {
        this.VALID_TYPE = VALID_TYPE;
    }

    public String getMEM_SRVPKG() {
        return MEM_SRVPKG;
    }

    public void setMEM_SRVPKG(String MEM_SRVPKG) {
        this.MEM_SRVPKG = MEM_SRVPKG;
    }

    public String getCUST_ORDER_ID() {
        return CUST_ORDER_ID;
    }

    public void setCUST_ORDER_ID(String CUST_ORDER_ID) {
        this.CUST_ORDER_ID = CUST_ORDER_ID;
    }

    public String getSMS_TEMPLATE() {
        return SMS_TEMPLATE;
    }

    public void setSMS_TEMPLATE(String SMS_TEMPLATE) {
        this.SMS_TEMPLATE = SMS_TEMPLATE;
    }
}
