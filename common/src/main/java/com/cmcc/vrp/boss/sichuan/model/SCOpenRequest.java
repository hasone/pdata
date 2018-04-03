/**
 * 本省流量统付开户接口（根据集团编码查询USERID）输入参数类
 */
package com.cmcc.vrp.boss.sichuan.model;

/**
 * @author JamieWu
 */
public class SCOpenRequest {

    private String appKey;

    private String timeStamp;

    private String userName;

    private String loginNo;

    private String regionId;

    private String opCode;

    private String unitId;

    private String prcId;

    private String discntCode;

    private String sign;


    public String getLoginNo() {
        return loginNo;
    }

    public void setLoginNo(String loginNo) {
        this.loginNo = loginNo;
    }

    public String getOpCode() {
        return opCode;
    }

    public void setOpCode(String opCode) {
        this.opCode = opCode;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getPrcId() {
        return prcId;
    }

    public void setPrcId(String prcId) {
        this.prcId = prcId;
    }


    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDiscntCode() {
        return discntCode;
    }

    public void setDiscntCode(String discntCode) {
        this.discntCode = discntCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
}
