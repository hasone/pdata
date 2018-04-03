package com.cmcc.vrp.boss.heilongjiang.fee;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * { "resCode": "E130267", "resMsg": "取用户资料出错", "spec_prepay": " 0.00", "available_bill": " 0.00", "expire_time": "", "bank_cust": "", "pay_code": "",
 * "pay_name": "", "pre_bill": " 0.00", "unbill_owe": " 0.00", "show_prepay": " 0.00", "chSmCode": "(+P", "chSmName": " ", "limitowe": "0", "iLoginAccept": "",
 * "mowpay": " 0.00", "begin_flag": "", "lowest_flag": "0", "lowest_fee": " -0.00", "enct_name": "" }
 * 
 * @author Lenovo
 *
 */
public class BalanceQueryResponse {
    private String resCode;
    private String resMsg;
    private String specPrepay;
    private String availableBill;
    private String expireTime;
    private String bankCust;
    private String payCode;
    private String payName;
    private String preBill;
    private String unbillOwe;
    private String showPrepay;
    private String chSmCode;
    private String chSmName;
    private String limitowe;
    private String iLoginAccept;
    private String mowpay;
    private String beginFlag;
    private String lowestFlag;
    private String lowestFee;
    private String enctName;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public String getSpecPrepay() {
        return specPrepay;
    }

    @JsonProperty("spec_prepay")
    public void setSpecPrepay(String specPrepay) {
        this.specPrepay = specPrepay;
    }

    public String getAvailable_bill() {
        return availableBill;
    }

    @JsonProperty("available_bill")
    public void setAvailableBill(String availableBill) {
        this.availableBill = availableBill;
    }

    public String getExpireTime() {
        return expireTime;
    }

    @JsonProperty("expire_time")
    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getBankCust() {
        return bankCust;
    }

    @JsonProperty("bank_cust")
    public void setBankCust(String bankCust) {
        this.bankCust = bankCust;
    }

    public String getPay_code() {
        return payCode;
    }

    @JsonProperty("pay_code")
    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getPayName() {
        return payName;
    }

    @JsonProperty("pay_name")
    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getPreBill() {
        return preBill;
    }

    @JsonProperty("pre_bill")
    public void setPreBill(String preBill) {
        this.preBill = preBill;
    }

    public String getUnbillOwe() {
        return unbillOwe;
    }

    @JsonProperty("unbill_owe")
    public void setUnbillOwe(String unbillOwe) {
        this.unbillOwe = unbillOwe;
    }

    public String getShowPrepay() {
        return showPrepay;
    }

    @JsonProperty("show_prepay")
    public void setShowPrepay(String showPrepay) {
        this.showPrepay = showPrepay;
    }

    public String getChSmCode() {
        return chSmCode;
    }

    public void setChSmCode(String chSmCode) {
        this.chSmCode = chSmCode;
    }

    public String getChSmName() {
        return chSmName;
    }

    public void setChSmName(String chSmName) {
        this.chSmName = chSmName;
    }

    public String getLimitowe() {
        return limitowe;
    }

    public void setLimitowe(String limitowe) {
        this.limitowe = limitowe;
    }

    /**
     * @return
     */
    public String getiLoginAccept() {
        return iLoginAccept;
    }

    /**
     * @param iLoginAccept
     */
    public void setiLoginAccept(String iLoginAccept) {
        this.iLoginAccept = iLoginAccept;
    }

    public String getMowpay() {
        return mowpay;
    }

    public void setMowpay(String mowpay) {
        this.mowpay = mowpay;
    }

    public String getBeginFlag() {
        return beginFlag;
    }

    @JsonProperty("begin_flag")
    public void setBeginFlag(String beginFlag) {
        this.beginFlag = beginFlag;
    }

    public String getLowestFlag() {
        return lowestFlag;
    }

    @JsonProperty("lowest_flag")
    public void setLowestFlag(String lowestFlag) {
        this.lowestFlag = lowestFlag;
    }

    public String getLowestFee() {
        return lowestFee;
    }

    @JsonProperty("lowest_fee")
    public void setLowestFee(String lowestFee) {
        this.lowestFee = lowestFee;
    }

    public String getEnctName() {
        return enctName;
    }

    @JsonProperty("enct_name")
    public void setEnctName(String enctName) {
        this.enctName = enctName;
    }

    @Override
    public String toString() {
        return "BalanceQueryResponse [resCode=" + resCode + ", resMsg=" + resMsg + ", specPrepay=" + specPrepay
                + ", availableBill=" + availableBill + ", expireTime=" + expireTime + ", bankCust=" + bankCust
                + ", payCode=" + payCode + ", payName=" + payName + ", preBill=" + preBill + ", unbill_owe="
                + unbillOwe + ", showPrepay=" + showPrepay + ", chSmCode=" + chSmCode + ", chSmName=" + chSmName
                + ", limitowe=" + limitowe + ", iLoginAccept=" + iLoginAccept + ", mowpay=" + mowpay + ", beginFlag="
                + beginFlag + ", lowestFlag=" + lowestFlag + ", lowestFee=" + lowestFee + ", enct_name=" + enctName
                + "]";
    }

}
