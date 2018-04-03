package com.cmcc.vrp.boss.guangxi.model;

/**
 * Created by leelyn on 2016/9/22.
 */
public class InitDataDTO {

    private String prdouctId;
    private String mobile;
    private String pCode;
    private String bipCode;
    private String activityCode;
    private String bossReqNum;
    private String flag;
    private String actionCode;

    public String getPrdouctId() {
        return prdouctId;
    }

    public void setPrdouctId(String prdouctId) {
        this.prdouctId = prdouctId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return
     */
    public String getpCode() {
        return pCode;
    }

    /**
     * @param pCode
     */
    public void setpCode(String pCode) {
        this.pCode = pCode;
    }

    public String getBipCode() {
        return bipCode;
    }

    public void setBipCode(String bipCode) {
        this.bipCode = bipCode;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getBossReqNum() {
        return bossReqNum;
    }

    public void setBossReqNum(String bossReqNum) {
        this.bossReqNum = bossReqNum;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }
}
