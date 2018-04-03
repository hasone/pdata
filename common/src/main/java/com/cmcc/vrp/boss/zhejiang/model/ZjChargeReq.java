package com.cmcc.vrp.boss.zhejiang.model;

/**
 * Created by leelyn on 2016/8/2.
 */
public class ZjChargeReq {

    private String optCode;
    private String tradeSerialNo;
    private String tradeDate;
    private String billId;
    private String accountDate;
    private String offerId;
    private String clientIp;
    private String macAddress;
    private String bizCode;

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getOptCode() {
        return optCode;
    }

    public void setOptCode(String optCode) {
        this.optCode = optCode;
    }

    public String getTradeSerialNo() {
        return tradeSerialNo;
    }

    public void setTradeSerialNo(String tradeSerialNo) {
        this.tradeSerialNo = tradeSerialNo;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getAccountDate() {
        return accountDate;
    }

    public void setAccountDate(String accountDate) {
        this.accountDate = accountDate;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}
