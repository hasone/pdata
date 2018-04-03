package com.cmcc.vrp.boss.shyc.pojos;

import com.cmcc.vrp.boss.shyc.annotations.NeedSign;
import com.cmcc.vrp.boss.shyc.annotations.SignAlias;
import com.cmcc.vrp.boss.shyc.annotations.SignTarget;

/**
 * 上海月呈的单号码充值
 *
 * Created by sunyiwei on 2017/3/14.
 */
public class SingleChargeRequest {
    private final String action = "charge";
    private final String v = "1.2";
    private int range;

    @NeedSign
    private String account;

    @NeedSign
    private String mobile;

    @NeedSign
    @SignAlias(value = "package")
    private String packageName;

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

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
