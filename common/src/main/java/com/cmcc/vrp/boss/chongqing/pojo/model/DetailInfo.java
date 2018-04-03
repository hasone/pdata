package com.cmcc.vrp.boss.chongqing.pojo.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author lgk8023
 *
 */
public class DetailInfo {
//    1a  GSM语音（单位秒）
//    1b  WLAN语音（单位秒）
//    2  短信条数（单位条）
//    3  彩信条数（单位条）
//    4a  GPRS流量（单位KB）
//    4b  WLAN流量（单位KB）
//    5  费用（单位分）
//    6  彩铃条数（单位条）
    @SerializedName("TYPE")
    private String type;
    
//    业务对应大类分类，该项直接显示大类名称。
//    比如语音分为漫游、本地国内等
//    比如GPRS分为通用、本地、4G等
    @SerializedName("TYPENAME")
    private String typeName;
    
//    0代表已失效
//    1代表在用
//    2代表未生效
    @SerializedName("SERVICESTATUS")
    private String serviceStatus;
    
//    只有针对定向流量套餐才传送该信息；    其他非定向业务该项直接填空
    @SerializedName("SERVICENAME")
    private String serviceName;
    
    @SerializedName("SUMNUM")
    private String sumNum; //赠送量
    
//    针对不限流量套餐，此字段请展现为“不限”。
    @SerializedName("USEDNUM")
    private String userNum; //使用量
    
    @SerializedName("LEFTNUM")
    private String leftNum; //剩余量
    
//    1：定向不限流量
//    2：定向限量统付
//    3：通用不限流量
//    4：通用限量统付
//    其它传空
    @SerializedName("TFFLAG")
    private String tfFlag; //流量统付标记
    
    @SerializedName("CUSTPRODUCT")
    private String custProduct;//客户产品
//    套餐剩余量对应的优惠ID
    @SerializedName("PRIVID")
    private String privId; //转结标记
//    0：非转结量
//    1：历史转结量
    @SerializedName("HCOF")
    private String hcof; //是否流量不清零
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getServiceStatus() {
        return serviceStatus;
    }
    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }
    public String getServiceName() {
        return serviceName;
    }
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    public String getSumNum() {
        return sumNum;
    }
    public void setSumNum(String sumNum) {
        this.sumNum = sumNum;
    }
    public String getUserNum() {
        return userNum;
    }
    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }
    public String getLeftNum() {
        return leftNum;
    }
    public void setLeftNum(String leftNum) {
        this.leftNum = leftNum;
    }
    public String getTfFlag() {
        return tfFlag;
    }
    public void setTfFlag(String tfFlag) {
        this.tfFlag = tfFlag;
    }
    public String getCustProduct() {
        return custProduct;
    }
    public void setCustProduct(String custProduct) {
        this.custProduct = custProduct;
    }
    public String getPrivId() {
        return privId;
    }
    public void setPrivId(String privId) {
        this.privId = privId;
    }
    public String getHcof() {
        return hcof;
    }
    public void setHcof(String hcof) {
        this.hcof = hcof;
    }    
}
