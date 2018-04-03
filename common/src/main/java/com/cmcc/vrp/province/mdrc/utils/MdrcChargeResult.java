package com.cmcc.vrp.province.mdrc.utils;

/**
 * 流量卡充值结果枚举类
 * <p>
 * Created by sunyiwei on 2016/6/21.
 */
public enum MdrcChargeResult {
    SUCCESS("充值成功", "10000"),
    FAIL("充值失败", "10001");

    private String retMsg;
    private String retCode;

    MdrcChargeResult(String retMsg, String retCode) {
        this.retMsg = retMsg;
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }
}
