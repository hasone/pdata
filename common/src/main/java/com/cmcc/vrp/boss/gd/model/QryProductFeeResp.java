package com.cmcc.vrp.boss.gd.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("QryProductFeeResp")
public class QryProductFeeResp {

    @XStreamAlias("Result")
    private String result;
    
    @XStreamAlias("ResultMsg")
    private String resultMsg;
    
    @XStreamAlias("ProductCode")
    private String productCode;
    
    @XStreamAlias("ProductName")
    private String productName;
    
    @XStreamAlias("OwingCharge")
    private String owingCharge;
    
    @XStreamAlias("ReCharge")
    private String reCharge;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOwingCharge() {
        return owingCharge;
    }

    public void setOwingCharge(String owingCharge) {
        this.owingCharge = owingCharge;
    }

    public String getReCharge() {
        return reCharge;
    }

    public void setReCharge(String reCharge) {
        this.reCharge = reCharge;
    }

}
