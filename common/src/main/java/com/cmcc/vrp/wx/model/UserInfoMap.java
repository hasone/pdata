package com.cmcc.vrp.wx.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月5日 上午8:58:02
*/
@XStreamAlias("UserInfoMap")
public class UserInfoMap {
    @XStreamAlias("OptType")
    private String optType;

    @XStreamAlias("ItemName")
    private String itemName;

    @XStreamAlias("ItemValue")
    private String itemValue;

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }
    
    
}
