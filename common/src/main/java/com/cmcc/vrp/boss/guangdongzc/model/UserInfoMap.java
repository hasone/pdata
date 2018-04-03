package com.cmcc.vrp.boss.guangdongzc.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/7/11.
 */
@XStreamAlias("USERINFOMAP")
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
