package com.cmcc.vrp.boss.shyc.pojos;

/**
 * 上海月呈定义的流量包
 *
 * Created by sunyiwei on 2017/3/14.
 */
public class FlowPacakge {
    private int packageSize;
    private String name;
    private double price;

    public int getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(int packageSize) {
        this.packageSize = packageSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
