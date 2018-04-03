/**
 * 集团产品成员新增输出参数类
 */
package com.cmcc.vrp.boss.sichuan.model;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午11:03:16
*/
public class SCDelMemberRequest {
    private String appKey;

    private String timeStamp;

    private String userName;

    private String loginNo;

    private String phone_no;

    private String userId;

    private String sign;

    private String prc_id;

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

    public String getPrc_id() {
        return prc_id;
    }

    public void setPrc_id(String prc_id) {
        this.prc_id = prc_id;
    }
}
