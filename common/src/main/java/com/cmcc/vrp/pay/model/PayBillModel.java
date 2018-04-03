package com.cmcc.vrp.pay.model;

import java.util.Date;

/**
 * 支付账单的模型
 * 
 */
public class PayBillModel {
    private String originId;//接入平台标识
    
    private String merchantId;//商户ID
    
    private String productId;//产品ID
    
    private String orderid;//业务平台订单号                   现在看是pay_order_id
    
    private String payAmount;//支付总金额    0.01
    
    private String tradeNo;//支付平台订单号                     现在看是doneCode  新版已弃用
    
    private String thirdOrderid;  // 新版已弃用
    
    private String transactionId;//业务平台交易ID    现在看是transactionId
    
    private String transactionDate;
    
    private String payInfo;
    
    private String accountCode;
    
    private String accountType;
    
    private String payPeriod;
    
    private String payTime;
    
    private String proCount;
    
    private String productName;  // 产品名称 新版弃用
    
    private String orderType;
    
    private String type;//支付方式       1、和包；2、支付宝；4、微信；5、银联
    
    private String thirdAmount;
    
    private String integralNum;
    
    private String hemiNum;
    
    private String line;
    
    private Date createTime;
    
    private Long id;

    private String date;
    
    /**
     * 20170712更新
     */
    private String subOrderid;  //业务平台分订单号
    private String payOrderid;  //支付平台主订单号 原先的TradeNo
    private String subPayOrderid; //支付平台分订单号
    
    /**
     * 读行
     */
    public static PayBillModel readFromLine(String line){
        String[] params = line.split("\\|");
        if(params == null || params.length<19){
            return null;
        }
        PayBillModel model = new PayBillModel();
        model.setOriginId(params[0]);
        model.setMerchantId(params[1]);
        model.setProductId(params[2]);
        model.setOrderid(params[3]);
        model.setPayAmount(params[4]);
        model.setTradeNo(params[5]);
        model.setThirdOrderid(params[6]);
        model.setTransactionId(params[7]);
        model.setTransactionDate(params[8]);
        model.setPayInfo(params[9]);
        model.setAccountCode(params[10]);
        model.setAccountType(params[11]);
        model.setPayPeriod(params[12]);
        model.setPayTime(params[13]);
        model.setProCount(params[14]);
        model.setProductName(params[15]);
        model.setOrderType(params[16]);
        model.setType(params[17]);
        model.setThirdAmount(params[18]);
        model.setLine(line);
        return model;
    }
    
    /**
     * 读行新版,保留原版用于可兼容
     */
    public static PayBillModel readFromLineNew(String line){
        String[] params = line.split("\\|");
        if(params == null || params.length<19){
            return null;
        }
        PayBillModel model = new PayBillModel();
        model.setOriginId(params[0]);
        model.setMerchantId(params[1]);
        model.setProductId(params[2]);
        model.setOrderid(params[3]);
        model.setSubOrderid(params[4]);
        model.setPayOrderid(params[5]);
        model.setSubPayOrderid(params[6]);
        model.setPayAmount(params[7]);
        model.setTransactionId(params[8]);
        model.setTransactionDate(params[9]);
        model.setPayInfo(params[10]);
        model.setAccountCode(params[11]);
        model.setAccountType(params[12]);
        model.setPayPeriod(params[13]);
        model.setPayTime(params[14]);
        model.setProCount(params[15]);
        model.setOrderType(params[16]);
        model.setType(params[17]);
        model.setThirdAmount(params[18]);
        model.setLine(line);
        return model;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getThirdOrderid() {
        return thirdOrderid;
    }

    public void setThirdOrderid(String thirdOrderid) {
        this.thirdOrderid = thirdOrderid;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(String payInfo) {
        this.payInfo = payInfo;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getPayPeriod() {
        return payPeriod;
    }

    public void setPayPeriod(String payPeriod) {
        this.payPeriod = payPeriod;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getProCount() {
        return proCount;
    }

    public void setProCount(String proCount) {
        this.proCount = proCount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThirdAmount() {
        return thirdAmount;
    }

    public void setThirdAmount(String thirdAmount) {
        this.thirdAmount = thirdAmount;
    }

    public String getIntegralNum() {
        return integralNum;
    }

    public void setIntegralNum(String integralNum) {
        this.integralNum = integralNum;
    }

    public String getHemiNum() {
        return hemiNum;
    }

    public void setHemiNum(String hemiNum) {
        this.hemiNum = hemiNum;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubOrderid() {
        return subOrderid;
    }

    public void setSubOrderid(String subOrderid) {
        this.subOrderid = subOrderid;
    }

    public String getPayOrderid() {
        return payOrderid;
    }

    public void setPayOrderid(String payOrderid) {
        this.payOrderid = payOrderid;
    }

    public String getSubPayOrderid() {
        return subPayOrderid;
    }

    public void setSubPayOrderid(String subPayOrderid) {
        this.subPayOrderid = subPayOrderid;
    }
    
    
}
