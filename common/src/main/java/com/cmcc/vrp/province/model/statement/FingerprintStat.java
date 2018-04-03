package com.cmcc.vrp.province.model.statement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by sunyiwei on 2016/9/19.
 */
@XStreamAlias("FingerPrint")
public class FingerprintStat {
    @XStreamAlias("Name")
    private String name;

    @XStreamAlias("Count")
    private int count;

    @XStreamAlias("FlowCount")
    private float flowCount;

    @XStreamAlias("Price")
    private float price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getFlowCount() {
        return flowCount;
    }

    public void setFlowCount(float flowCount) {
        this.flowCount = flowCount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
