package com.cmcc.vrp.boss.henan.model;

/**
 * Created by leelyn on 2016/8/17.
 */
public class HaQueryStatusReq {

    private String CUST_ORDER_ID;
    private String BILL_ID;

    public String getCUST_ORDER_ID() {
        return CUST_ORDER_ID;
    }

    public void setCUST_ORDER_ID(String CUST_ORDER_ID) {
        this.CUST_ORDER_ID = CUST_ORDER_ID;
    }

    public String getBILL_ID() {
        return BILL_ID;
    }

    public void setBILL_ID(String BILL_ID) {
        this.BILL_ID = BILL_ID;
    }
}
