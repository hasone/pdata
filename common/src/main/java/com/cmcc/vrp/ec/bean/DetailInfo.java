package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("DETAILINFO")
public class DetailInfo {

    @XStreamAlias("TYPE")
    private String type;
    
    @XStreamAlias("TYPENAME")
    private String typeName;
    
    @XStreamAlias("SERVICESTATUS")
    private String serviceStatus;
    
    @XStreamAlias("SERVICENAME")
    private String serviceName;
    
    @XStreamAlias("SUMNUM")
    private String sumNum;
    
    @XStreamAlias("USEDNUM")
    private String usedNum;
    
    @XStreamAlias("LEFTNUM")
    private String leftNum;
    
    @XStreamAlias("TFFLAG")
    private String tfFlag;
    
    @XStreamAlias("CUSTPRODUCT")
    private String custProduct;
    
    @XStreamAlias("PRIVID")
    private String privid;
    
    @XStreamAlias("HCOF")
    private String hcof;

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

    public String getUsedNum() {
        return usedNum;
    }

    public void setUsedNum(String usedNum) {
        this.usedNum = usedNum;
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

    public String getPrivid() {
        return privid;
    }

    public void setPrivid(String privid) {
        this.privid = privid;
    }

    public String getHcof() {
        return hcof;
    }

    public void setHcof(String hcof) {
        this.hcof = hcof;
    }
}
