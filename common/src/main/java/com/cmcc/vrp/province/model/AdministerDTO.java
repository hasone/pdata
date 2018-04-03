package com.cmcc.vrp.province.model;

import java.util.Date;
import java.util.Random;

/**
 * 修改用户密码用
 *
 * */
public class AdministerDTO {

    private String mobilePhone;

    private Long msgTime;

    private String message;

    private Administer administer;

    private String errorMsg;

    private String sjmm;

    private String passWord;

    private String passWord2;

    public static void main(String[] args) {
        System.out.println(new AdministerDTO().getRandomString(64));
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    //验证码有效期 10分钟
    public boolean getMsgTime() {
        if (!((new Date().getTime() - msgTime) > 600000)) {
            return true;
        }
        return false;
    }

    public void setMsgTime(Long msgTime) {
        this.msgTime = msgTime;
    }

    //发送间隔一分钟
    public boolean getCurrentTime() {
        if (!((new Date().getTime() - msgTime) > 60000)) {
            return true;
        }
        return false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessage() {
        this.message = getRandomString(6);
    }

    public Administer getAdminister() {
        return administer;
    }

    public void setAdminister(Administer administer) {
        this.administer = administer;
    }

    public String getRandomString(int length) { //length表示生成字符串的长度
        String base = "0123456789qweasdzxcrtyuioplkjhgfvbnm";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getSjmm() {
        return sjmm;
    }

    public void setSjmm(String sjmm) {
        this.sjmm = sjmm;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPassWord2() {
        return passWord2;
    }

    public void setPassWord2(String passWord2) {
        this.passWord2 = passWord2;
    }


}
