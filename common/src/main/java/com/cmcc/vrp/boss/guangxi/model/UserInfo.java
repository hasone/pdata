package com.cmcc.vrp.boss.guangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/9/13.
 */
@XStreamAlias("UserInfo")
public class UserInfo {
    @XStreamAlias("ProductID")
    private String productID;
    @XStreamAlias("UserData")
    private UserData userData;
    @XStreamAlias("EffRule")
    private Integer effRule;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public Integer getEffRule() {
        return effRule;
    }

    public void setEffRule(Integer effRule) {
        this.effRule = effRule;
    }
}
