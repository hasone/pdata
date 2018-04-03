/**
 * @Title: SmsType.java
 * @Package com.cmcc.vrp.enums
 * @author: qihang
 * @date: 2015年6月12日 上午9:20:49
 * @version V1.0
 */
package com.cmcc.vrp.enums;

/**
 * @ClassName: SmsType
 * @Description: TODO
 * @author: qihang
 * @date: 2015年6月12日 上午9:20:49
 *
 */
public enum SmsType {
    WEBINLOGIN_SMS(1, "登陆后台", "SmsLogin"),
    UPDATEPASS_SMS(2, "更改后台密码", "updateSmsLogin"),
    FLOWCARD_SMS(3, "登陆流量券", "flowcardLogin"),
    UPDATEPHONE_SMS(4, "更改手机号码", "updatePhone"),
    //这个是广东众筹的需求，更换手机号
    UPDATEPHON_FOR_WX_SMS(5, "更改手机号码", "updatePhoneForWx"),
    //登录页面的重置密码
    RESET_PASSWORD(6, "重置密码", "resetPassword");


    private int code;

    private String type;

    private String suffix;

    private SmsType(int code, String type, String suffix) {
        this.code = code;
        this.type = type;
        this.suffix = suffix;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }


}
