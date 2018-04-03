package com.cmcc.vrp.boss.shangdong.boss.model;

import java.util.Date;

/** 
 * @ClassName:  ServiceModel 
 * @Description:  山东对账时从数据库中取数据的model
 * @author: qihang
 * @date:   2016年3月24日 下午8:40:47 
 *  
 */
public class ServiceModel {
    private String type; //类型
    
    private Long recordId; //记录Id
    
    private String mobile; //手机号
    
    private Date operateTime; //时间
    
    private String bossEnterpriseId; //custormerId
    
    private String userId; //userId
    
    private String productCode;  //productyCode 100864
    
    private Integer price;  //价格
    
    private Integer status; //状态
    
    private String errorMessage; //充值错误信息
    
    private Integer orderDiscount;  //订单里的折扣
    
    private String platformSerialNum; //平台流水号
    
    private String bossReqSerialNum; //发给boss的请求流水号 
    
    private Integer count;  //价格

    /**
     * 流量包大小
     */
    private String size;
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getOperateTime() {
        if(operateTime != null){
            return (Date) operateTime.clone();
        }else{
            return null;
        }
    }

    public void setOperateTime(Date operateTime) {
        if(operateTime != null){
            this.operateTime = (Date) operateTime.clone();
        }else{
            this.operateTime = null;
        }
    }

    public String getBossEnterpriseId() {
        return bossEnterpriseId;
    }

    public void setBossEnterpriseId(String bossEnterpriseId) {
        this.bossEnterpriseId = bossEnterpriseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getOrderDiscount() {
        return orderDiscount;
    }

    public void setOrderDiscount(Integer orderDiscount) {
        this.orderDiscount = orderDiscount;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPlatformSerialNum() {
        return platformSerialNum;
    }

    public void setPlatformSerialNum(String platformSerialNum) {
        this.platformSerialNum = platformSerialNum;
    }

    public String getBossReqSerialNum() {
        return bossReqSerialNum;
    }

    public void setBossReqSerialNum(String bossReqSerialNum) {
        this.bossReqSerialNum = bossReqSerialNum;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
    
    
}
