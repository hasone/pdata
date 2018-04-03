package com.cmcc.vrp.boss.guangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/9/18.
 */
@XStreamAlias("AdditionInfo")
public class AdditionInfoReq {

    @XStreamAlias("ProductID")
    private String productID;

    @XStreamAlias("UserData")
    private UserDataReq userDataReq;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public UserDataReq getUserDataReq() {
        return userDataReq;
    }

    public void setUserDataReq(UserDataReq userDataReq) {
        this.userDataReq = userDataReq;
    }
}
