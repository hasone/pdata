package com.cmcc.vrp.boss.shangdong.boss.model;

/** 
 * @ClassName:  RecordDetail 
 * @Description:  话单的基本格式文件
 * @author: qihang
 * @date:   2016年3月24日 下午3:50:08 
 *  
 *  0828补充，该类是先进行对账，本次是用于数据库的model对象，添加了部分对象用于兼容2个功能
 */
public class RecordDetail {
    private Long id;
    
    private String serialNumber="";
    
    private Integer customerType=1;
    
    private String customerID="";//ecId
    
    private String userID="";//订购关系ID
    
    private String memberPhone="";//手机号
    
    private String siid="0";//企业ID(SI厂商)编码  ？？？需要确认
    
    private String serviceID="";//108706 product_code???需要确认
    
    private String measureMode="0102001";//固定
    
    private String chargingType="03"; //固定
    
    private String orderDate="";//需要填对账单里的信息
    
    private String expireDate="";//需要填对账单里的信息
    
    private String beginTime="";//空
    
    private String endTime="";//空
    
    private String duration="" ;//空
    
    private String usageAmount = "";//空
    
    private String orderQuantity = "";//空
    
    private String originalPrice = "";//初始价格
    
    private String preferentialPrice = "";//乘以折扣后的价格
    
    private Integer payType=0;//0
    
    //为了与数据库类型统一，方便mysql直接统计，添加了以下三个数据的转化类型
    private Integer orderQuantityInt;//空
    
    private Double originalPriceDouble;//初始价格
    
    private Double preferentialPriceDouble;//乘以折扣后的价格
    
    /**
     * 将对象转化成str输出
     * @return
     */
    public String toBillRecord(){
        StringBuffer buffer =new StringBuffer();
        buffer.append(serialNumber+ "|");
        buffer.append(customerType+"|");
        buffer.append(customerID+"|");
        buffer.append(userID+"|");
        buffer.append(memberPhone+"|");
        buffer.append(siid+"|");
        buffer.append(serviceID+"|");
        buffer.append(measureMode+"|");
        buffer.append(chargingType+"|");
        buffer.append(orderDate+"|");
        buffer.append(expireDate+"|");
        buffer.append(beginTime+"|");
        buffer.append(endTime+"|");
        buffer.append("|");
        buffer.append(usageAmount + "|");
        buffer.append(orderQuantity+"|");
        buffer.append(originalPrice+"|");
        buffer.append(preferentialPrice+"|");
        buffer.append(payType+"|");
        return buffer.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getSiid() {
        return siid;
    }

    public void setSiid(String siid) {
        this.siid = siid;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getMeasureMode() {
        return measureMode;
    }

    public void setMeasureMode(String measureMode) {
        this.measureMode = measureMode;
    }

    public String getChargingType() {
        return chargingType;
    }

    public void setChargingType(String chargingType) {
        this.chargingType = chargingType;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getUsageAmount() {
        return usageAmount;
    }

    public void setUsageAmount(String usageAmount) {
        this.usageAmount = usageAmount;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getPreferentialPrice() {
        return preferentialPrice;
    }

    public void setPreferentialPrice(String preferentialPrice) {
        this.preferentialPrice = preferentialPrice;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getOrderQuantityInt() {
        return orderQuantityInt;
    }

    public void setOrderQuantityInt(Integer orderQuantityInt) {
        this.orderQuantityInt = orderQuantityInt;
    }

    public Double getOriginalPriceDouble() {
        return originalPriceDouble;
    }

    public void setOriginalPriceDouble(Double originalPriceDouble) {
        this.originalPriceDouble = originalPriceDouble;
    }

    public Double getPreferentialPriceDouble() {
        return preferentialPriceDouble;
    }

    public void setPreferentialPriceDouble(Double preferentialPriceDouble) {
        this.preferentialPriceDouble = preferentialPriceDouble;
    }
    
    
}
