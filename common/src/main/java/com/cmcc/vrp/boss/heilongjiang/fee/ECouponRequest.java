package com.cmcc.vrp.boss.heilongjiang.fee;

import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午10:34:15
*/
public class ECouponRequest {

    private String appKey;
    private Date timestamp;
    private String orderNo;
    private String orderTime;
    private String accNbr;
    private String bossOrderNo;
    private String orderDate;
    private String groupAccNo;
    private String groupNo;
    private String phoneNo;

    @Override
    public String toString() {
        return "ECouponRequest [appKey=" + appKey + ", timeStamp=" + timestamp + ", orderNo=" + orderNo + ", orderTime="
                + orderTime + ", accNbr=" + accNbr + ", bossOrderNo=" + bossOrderNo + ", orderDate=" + orderDate
                + ", groupAccNo=" + groupAccNo + ", groupNo=" + groupNo + ", phoneNo=" + phoneNo + "]";
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getAccNbr() {
        return accNbr;
    }

    public void setAccNbr(String accNbr) {
        this.accNbr = accNbr;
    }

    public String getBossOrderNo() {
        return bossOrderNo;
    }

    public void setBossOrderNo(String bossOrderNo) {
        this.bossOrderNo = bossOrderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getGroupAccNo() {
        return groupAccNo;
    }

    public void setGroupAccNo(String groupAccNo) {
        this.groupAccNo = groupAccNo;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

}
