package com.cmcc.vrp.boss.heilongjiang.fee;

import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午10:35:48
*/
public class GroupPersonRequest {
    private String appKey;
    private Date timestamp;
    private String orderNo;
    private String orderDate;
    private String groupAccNo;
    private String accNbr;
    private String totalFee;
    private String groupNo;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "GroupPersonRequest [appKey=" + appKey + ", timestamp=" + timestamp + ", orderNo=" + orderNo
                + ", orderDate=" + orderDate + ", groupAccNo=" + groupAccNo + ", accNbr=" + accNbr + ", totalFee="
                + totalFee + ", groupNo=" + groupNo + "]";
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

    public String getAccNbr() {
        return accNbr;
    }

    public void setAccNbr(String accNbr) {
        this.accNbr = accNbr;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }
}
