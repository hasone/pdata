package com.cmcc.vrp.queue.pojo;

/**
 * 卓望的充值对象
 * <p>
 * Created by sunyiwei on 2016/10/18.
 */
public class ZwChargePojo {
    private Long entId;
    private Long splPid;
    private String mobile;
    private String serialNum;

    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public Long getSplPid() {
        return splPid;
    }

    public void setSplPid(Long splPid) {
        this.splPid = splPid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }
}
