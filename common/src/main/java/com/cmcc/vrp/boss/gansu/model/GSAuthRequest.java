/**
 *
 */
package com.cmcc.vrp.boss.gansu.model;

/**
 * <p>Description: </p>
 *
 * @author xj
 * @date 2016年6月2日
 */
public class GSAuthRequest {

    private String version;

    private int testFlag;

    private String appId;

    private String dynamicToken;

    private String userPhoneNumber;

    private GSChargeRequestTransInfo transInfo;

    private GSChargeRequestAuthorization authorization;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getTestFlag() {
        return testFlag;
    }

    public void setTestFlag(int testFlag) {
        this.testFlag = testFlag;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDynamicToken() {
        return dynamicToken;
    }

    public void setDynamicToken(String dynamicToken) {
        this.dynamicToken = dynamicToken;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public GSChargeRequestTransInfo getTransInfo() {
        return transInfo;
    }

    public void setTransInfo(GSChargeRequestTransInfo transInfo) {
        this.transInfo = transInfo;
    }

    public GSChargeRequestAuthorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(GSChargeRequestAuthorization authorization) {
        this.authorization = authorization;
    }


}
