package com.cmcc.vrp.boss.hunan.model;

/**
 * 湖南免费订购接口的请求对象
 * <p>
 * Created by sunyiwei on 2016/9/19.
 */
public class HNFreeOfChargeReq {
    private String mobileNum;  //手机号码
    private String packages;  //产品编码
    private String operation;  //操作， 0： 开通， 1： 取消， 2： 变更

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
