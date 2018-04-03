package com.cmcc.vrp.ec.bean.individual;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("OrderRequest")
public class OrderRequest {
    @XStreamAlias("Mobile")
    String mobile;
    
    @XStreamAlias("ProductCode")
    String productCode;
    
    @XStreamAlias("SerialNum")
    String serialNum;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }
}
