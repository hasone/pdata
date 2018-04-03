/**
 *
 */
package com.cmcc.vrp.boss.gansu.model;

/**
 * <p>Description: </p>
 *
 * @author xj
 * @date 2016年5月24日
 */
public class GSDynamicTokenResponse {

    private String rspCode;

    private String rspDesc;

    private String dynamicToken;

    private String expiretime;

    public String getRspCode() {
        return rspCode;
    }

    public void setRspCode(String rspCode) {
        this.rspCode = rspCode;
    }

    public String getRspDesc() {
        return rspDesc;
    }

    public void setRspDesc(String rspDesc) {
        this.rspDesc = rspDesc;
    }

    public String getDynamicToken() {
        return dynamicToken;
    }

    public void setDynamicToken(String dynamicToken) {
        this.dynamicToken = dynamicToken;
    }

    public String getExpiretime() {
        return expiretime;
    }

    public void setExpiretime(String expiretime) {
        this.expiretime = expiretime;
    }


}
