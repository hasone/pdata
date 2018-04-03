package com.cmcc.vrp.boss.shyc.pojos;

import com.cmcc.vrp.boss.shyc.annotations.NeedSign;
import com.cmcc.vrp.boss.shyc.annotations.SignTarget;

/**
 * 批量充值的请求体
 *
 * Created by sunyiwei on 2017/3/14.
 */
public class MultipleChargeRequest {
    private final String action = "chargeBat";
    private final String v = "1.2";
    private int range;

    @NeedSign
    private String account;

    @NeedSign
    private String cmPackage;

    @NeedSign
    private String cuPackage;

    @NeedSign
    private String ctPackage;

    @NeedSign
    private String mobile;

    @NeedSign
    private String orderNo;

    @NeedSign
    private String timeStamp;

    @SignTarget
    private String sign;

    public String getAction() {
        return action;
    }

    public String getV() {
        return v;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCtPackage() {
        return ctPackage;
    }

    public void setCtPackage(String ctPackage) {
        this.ctPackage = ctPackage;
    }

    public String getCuPackage() {
        return cuPackage;
    }

    public void setCuPackage(String cuPackage) {
        this.cuPackage = cuPackage;
    }

    public String getCmPackage() {
        return cmPackage;
    }

    public void setCmPackage(String cmPackage) {
        this.cmPackage = cmPackage;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }
}
