package com.cmcc.vrp.enums;

/**
 * Created by leelyn on 2016/7/2.
 */
public enum PrizeType {

    FLOWPACKAGE(2, "流量包"),
    FLOWPOOL(1, "流量池");

    private int type;
    private String name;

    PrizeType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
