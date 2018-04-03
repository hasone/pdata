package com.cmcc.vrp.boss.hainan.model;

/**
 * @ClassName: HNBOSSUserInfo
 * @Description: 海南BOSS接口用户校验接口封装类
 * @author: Rowe
 * @date: 2016年4月29日 下午3:06:35
 */
public class HNBOSSUserInfo {

    private String mobile;//手机号码

    private String status;//状态：0：正常，1：非海南移动号码，2：该号码用户不存在，3：停机

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
