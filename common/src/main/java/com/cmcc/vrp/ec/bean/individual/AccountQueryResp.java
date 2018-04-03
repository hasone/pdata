package com.cmcc.vrp.ec.bean.individual;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * AccountQueryResp.java
 * @author wujiamin
 * @date 2017年1月13日
 */
@XStreamAlias("Response")
public class AccountQueryResp {
    @XStreamAlias("User")
    private AccountQueryRespData user;

    public AccountQueryRespData getUser() {
        return user;
    }

    public void setUser(AccountQueryRespData user) {
        this.user = user;
    }
}
