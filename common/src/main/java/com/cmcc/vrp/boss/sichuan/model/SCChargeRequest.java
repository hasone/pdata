/**
 * 1.1.1.3集团产品成员新增请求输入参数类
 */
package com.cmcc.vrp.boss.sichuan.model;

/**
 * 集团产品成员新增接口
 *
 * @author JamieWu
 */
public class SCChargeRequest {
    private String appKey;

    private String timeStamp;

    private String userName;

    private String loginNo;

    private String prcId;

    private String exp_rule;

    private String phone_no;

    private String userId;

    private String sign;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginNo() {
        return loginNo;
    }

    public void setLoginNo(String loginNo) {
        this.loginNo = loginNo;
    }

    public String getPrcId() {
        return prcId;
    }

    public void setPrcId(String prcId) {
        this.prcId = prcId;
    }

    public String getExp_rule() {
        return exp_rule;
    }

    public void setExp_rule(String exp_rule) {
        this.exp_rule = exp_rule;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
