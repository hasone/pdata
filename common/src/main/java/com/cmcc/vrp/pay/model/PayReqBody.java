package com.cmcc.vrp.pay.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 支付请求类
 *
 */
@XStreamAlias("BusiData")
public class PayReqBody implements PayReqBasicBody{
    /**
     * 客户业务平台帐户类型
     */
    @XStreamAlias("AccountType")
    private String accountType = "1";
    
    /**
     * 客户业务平台帐号
     */
    @XStreamAlias("AccountCode")
    private String accountCode = null;
    
    /**
     * 订单详情
     */
    @XStreamAlias("PayInfo")
    private String payInfo = null;

    /**
     * 订单编号
     */
    @XStreamAlias("OrderId")
    private String orderId = null;
    
    /**
     * 商户id
     */
    @XStreamAlias("MerchantId")
    private String merchantId = null;
    
    /**
     * 产品id
     */
    @XStreamAlias("ProductId")
    private String productId = null;
    
    /**
     * 产品个数
     */
    @XStreamAlias("ProCount")
    private String proCount = "1";
    
    /**
     * 支付金额
     */
    @XStreamAlias("PayAmount")
    private String payAmount = null;
    
    /**
     * 支付有效期 
     */
    @XStreamAlias("PayPeriod")
    private String payPeriod = null;
    
    /**
     * 支付页面通知URL
     */
    @XStreamAlias("PayNotifyIntURL")
    private String payNotifyIntURL= null;
    
    /**
     * 支付后台通知URL
     */
    @XStreamAlias("PayNotifyPageURL")
    private String payNotifyPageURL  = null;
    
    /**
     * 指定支付方式
     */
    @XStreamAlias("PayType")
    private String payType  = null;
    
    /**
     * 是否分润
     */
    @XStreamAlias("RoyaltyFlag")
    private String royaltyFlag = null;
    
    /**
     * 支付宝中断支付返回商家页面
     */
    @XStreamAlias("MerchantUrl")
    private String merchantUrl = null;
    
    /**
     * 扩展字段
     */
    @XStreamAlias("ExtraInfo")
    private String extraInfo = null;
    
    /**
     * 合并订单信息
     */
    @XStreamAlias("MergerInfo")
    private String mergerInfo = null;
    
    /**
     * 订单类型
     */
    @XStreamAlias("OrderType")
    private String orderType = "0";
    
    /**
     * 虚拟货币
     */
    @XStreamAlias("VirtualCurrency")
    private String virtualCurrency = null;
    
    /**
     * 优惠信息
     */
    @XStreamAlias("DiscontInfo")
    private String discontInfo = null;
    
    /**
     * 付款码
     */
    @XStreamAlias("AuthCode")
    private String authCode = null;
    
    /**
     * 支付方式
     */
    @XStreamAlias("PayTypes")
    private String payTypes = null;
      
   
    public PayReqBody(String accountCode, String merchantId, String productId,
            String payPeriod, String payNotifyIntURL, String payNotifyPageURL,
            String merchantUrl) {
        this.accountCode = accountCode;
        this.merchantId = merchantId;
        this.productId = productId;
        this.payPeriod = payPeriod;
        this.payNotifyIntURL = payNotifyIntURL;
        this.payNotifyPageURL = payNotifyPageURL;
        this.merchantUrl = merchantUrl;
    }
    
    
    



    public String getAccountType() {
        return accountType;
    }




    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }




    public String getAccountCode() {
        return accountCode;
    }




    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }




    public String getPayInfo() {
        return payInfo;
    }




    public void setPayInfo(String payInfo) {
        this.payInfo = payInfo;
    }




    public String getOrderId() {
        return orderId;
    }




    public void setOrderId(String orderId) {
        this.orderId = orderId;
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




    public String getProCount() {
        return proCount;
    }




    public void setProCount(String proCount) {
        this.proCount = proCount;
    }




    public String getPayAmount() {
        return payAmount;
    }




    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }




    public String getPayPeriod() {
        return payPeriod;
    }




    public void setPayPeriod(String payPeriod) {
        this.payPeriod = payPeriod;
    }




    public String getPayNotifyIntURL() {
        return payNotifyIntURL;
    }




    public void setPayNotifyIntURL(String payNotifyIntURL) {
        this.payNotifyIntURL = payNotifyIntURL;
    }




    public String getPayNotifyPageURL() {
        return payNotifyPageURL;
    }




    public void setPayNotifyPageURL(String payNotifyPageURL) {
        this.payNotifyPageURL = payNotifyPageURL;
    }




    public String getPayType() {
        return payType;
    }




    public void setPayType(String payType) {
        this.payType = payType;
    }




    public String getRoyaltyFlag() {
        return royaltyFlag;
    }




    public void setRoyaltyFlag(String royaltyFlag) {
        this.royaltyFlag = royaltyFlag;
    }




    public String getMerchantUrl() {
        return merchantUrl;
    }




    public void setMerchantUrl(String merchantUrl) {
        this.merchantUrl = merchantUrl;
    }




    public String getExtraInfo() {
        return extraInfo;
    }




    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }




    public String getMergerInfo() {
        return mergerInfo;
    }




    public void setMergerInfo(String mergerInfo) {
        this.mergerInfo = mergerInfo;
    }




    public String getOrderType() {
        return orderType;
    }




    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }




    public String getVirtualCurrency() {
        return virtualCurrency;
    }




    public void setVirtualCurrency(String virtualCurrency) {
        this.virtualCurrency = virtualCurrency;
    }




    public String getDiscontInfo() {
        return discontInfo;
    }




    public void setDiscontInfo(String discontInfo) {
        this.discontInfo = discontInfo;
    }




    public String getAuthCode() {
        return authCode;
    }




    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }




    public String getPayTypes() {
        return payTypes;
    }




    public void setPayTypes(String payTypes) {
        this.payTypes = payTypes;
    }



    /**
     * putParamsToMap
     */
    @Override
    public boolean putParamsToMap(Map<String, String> map) {
        if(StringUtils.isNotBlank(accountType)){
            map.put("AccountType", accountType);
        }
        
        if(StringUtils.isNotBlank(accountCode)){
            map.put("AccountCode", accountCode);
        }
        
        if(StringUtils.isNotBlank(payInfo)){
            map.put("PayInfo", payInfo);
        }

        if(StringUtils.isNotBlank(orderId)){
            map.put("OrderId", orderId);
        }
        
        if(StringUtils.isNotBlank(merchantId)){
            map.put("MerchantId", merchantId);
        }
        
        if(StringUtils.isNotBlank(productId)){
            map.put("ProductId", productId);
        }
        
        if(StringUtils.isNotBlank(proCount)){
            map.put("ProCount", proCount);
        }
        
        if(StringUtils.isNotBlank(payAmount)){
            map.put("PayAmount", payAmount);
        }
        
        if(StringUtils.isNotBlank(payPeriod)){
            map.put("PayPeriod", payPeriod);
        }
        
        if(StringUtils.isNotBlank(payNotifyIntURL)){
            map.put("PayNotifyIntURL", payNotifyIntURL);
        }
        
        if(StringUtils.isNotBlank(payNotifyPageURL)){
            map.put("PayNotifyPageURL", payNotifyPageURL);
        }
        
        if(StringUtils.isNotBlank(payType)){
            map.put("PayType", payType);
        }
        
        if(StringUtils.isNotBlank(royaltyFlag)){
            map.put("RoyaltyFlag", royaltyFlag);
        }
        
        if(StringUtils.isNotBlank(merchantUrl)){
            map.put("MerchantUrl", merchantUrl);
        }
        
        if(StringUtils.isNotBlank(extraInfo)){
            map.put("ExtraInfo", extraInfo);
        }
        
        if(StringUtils.isNotBlank(mergerInfo)){
            map.put("MergerInfo", mergerInfo);
        }
        
        if(StringUtils.isNotBlank(orderType)){
            map.put("OrderType", orderType);
        }
        
        if(StringUtils.isNotBlank(virtualCurrency)){
            map.put("VirtualCurrency", virtualCurrency);
        }

        
        if(StringUtils.isNotBlank(discontInfo)){
            map.put("DiscontInfo", discontInfo);
        }
        
        if(StringUtils.isNotBlank(authCode)){
            map.put("AuthCode", authCode);
        }
        
        if(StringUtils.isNotBlank(payTypes)){
            map.put("PayTypes", payTypes);
        }
        return true;
    }

    /**
     * encode
     */
    @Override
    public void encode() {
        try {
            payInfo = URLEncoder.encode(payInfo, "UTF-8");
            
            
            payNotifyIntURL = URLEncoder.encode(payNotifyIntURL, "UTF-8");
            payNotifyPageURL = URLEncoder.encode(payNotifyPageURL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
    }
    
    /*public void encode(){
        try {
            payInfo = URLEncoder.encode(payInfo, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/
        
}
