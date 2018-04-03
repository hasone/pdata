package com.cmcc.vrp.province.reconcile.model;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;



/**
 * 江苏对账，给开发平台的数据
 *
 */
public class JSModel {
    private String phone;
    
    private String prdCode;
    
    private Date chargeTime;
    
    private Integer status;
    
    private String systemNum;

    private String serialNum;

    

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrdCode() {
        return prdCode;
    }

    public void setPrdCode(String prdCode) {
        this.prdCode = prdCode;
    }

    public Date getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(Date chargeTime) {
        this.chargeTime = chargeTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSystemNum() {
        return systemNum;
    }

    public void setSystemNum(String systemNum) {
        this.systemNum = systemNum;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }
    
    /**
     * 转换为需要的String
     */
    @Override
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append(StringUtils.trimToEmpty(phone) + "|");
        buffer.append(StringUtils.trimToEmpty(prdCode) + "|");
        if(chargeTime != null){
            buffer.append(new DateTime(chargeTime).toString("yyyyMMddHHmmss") + "|");
        }else{
            buffer.append("|");
        }
        if(status != null){
            buffer.append(status + "|");
        }else{
            buffer.append("|");
        }
        
        buffer.append(StringUtils.trimToEmpty(serialNum) + "|");
        buffer.append(StringUtils.trimToEmpty(systemNum));
        
        return buffer.toString();
        
    }
    
    
}
