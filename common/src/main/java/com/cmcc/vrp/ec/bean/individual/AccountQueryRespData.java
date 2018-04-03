package com.cmcc.vrp.ec.bean.individual;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * AccountQueryRespData.java
 * @author wujiamin
 * @date 2017年1月13日
 */
@XStreamAlias("Response")
public class AccountQueryRespData {

    @XStreamAlias("Mobile")
    String mobile;
    @XStreamAlias("Account")
    Integer account;
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public Integer getAccount() {
        return account;
    }
    public void setAccount(Integer account) {
        this.account = account;
    }

}
