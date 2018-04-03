package com.cmcc.vrp.boss.gd.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("USERINFOMAP")
public class USERINFOMAP {

    @XStreamAlias("ItemName")
    private String itemName; //业务配置参数名称
    
    @XStreamAlias("ItemValue")
    private String itemValue;//业务配置参数值

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
