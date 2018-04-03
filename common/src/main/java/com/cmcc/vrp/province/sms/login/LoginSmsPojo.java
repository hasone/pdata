/**
 * @Title: LoginSmsPojo.java
 * @Package com.cmcc.vrp.province.sms.login
 * @author: qihang
 * @date: 2015年6月10日 下午2:35:07
 * @version V1.0
 */
package com.cmcc.vrp.province.sms.login;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: LoginSmsPojo
 * @Description: 储存在redis中的类，会转化为JSON格式
 * @author: qihang
 * @date: 2015年6月10日 下午2:35:07
 *
 */
public class LoginSmsPojo implements Serializable {


    private static final long serialVersionUID = 276261984834630857L;

    public String mobile; //手机号码

    public String password; //产生的密码6位长度

    private Date lastTime;//记录最后一次的登陆时间

    private int todayFrequency;//记录今天的登陆次数

    public LoginSmsPojo() {

    }


    //默认构造函数，设置时间为当前时间，今天登陆次数为1
    public LoginSmsPojo(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
        lastTime = new Date();
        todayFrequency = 1;
    }

    public LoginSmsPojo(String mobile, String password, int todayFrequency) {
        this.mobile = mobile;
        this.password = password;
        lastTime = new Date();
        this.todayFrequency = todayFrequency;
    }

    public LoginSmsPojo(String mobile, String password, Date lastTime, int todayFrequency) {
        this.lastTime = lastTime;
        this.todayFrequency = todayFrequency;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public int getTodayFrequency() {
        return todayFrequency;
    }

    public void setTodayFrequency(int todayFrequency) {
        this.todayFrequency = todayFrequency;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
