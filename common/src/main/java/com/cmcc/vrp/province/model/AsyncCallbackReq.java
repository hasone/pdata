package com.cmcc.vrp.province.model;

/**
 * boss侧回调请求对象, 每个渠道可以有自己的回调定义，但必须封装成这个对象后由平台统一处理
 * <p>
 * Created by sunyiwei on 2016/10/11.
 */
public class AsyncCallbackReq {
    //平台流水号
    private String systemSerialNum;

    //充值手机号
    private String mobile;

    //充值结果
    private int chargeRecordStatus;

    //失败信息
    private String errorMsg;

    public String getSystemSerialNum() {
        return systemSerialNum;
    }

    public void setSystemSerialNum(String systemSerialNum) {
        this.systemSerialNum = systemSerialNum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getChargeRecordStatus() {
        return chargeRecordStatus;
    }

    public void setChargeRecordStatus(int chargeRecordStatus) {
        this.chargeRecordStatus = chargeRecordStatus;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
