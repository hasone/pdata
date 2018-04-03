package com.cmcc.vrp.pay.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 请求头
 *
 */
public class PayHeader {
    @XStreamAlias("Version")
    private String version = "1.00";
    
    @XStreamAlias("TransactionId")
    private String transactionId;
    
    @XStreamAlias("TransactionDate")
    private String transactionDate;
    
    @XStreamAlias("OriginId")
    private String originId;
    
    @XStreamAlias("VerifyCode")
    private String verifyCode;
    
    @XStreamAlias("DigestAlg")
    private String digestAlg = "MD5";

    
    public PayHeader(String originId,String transactionId){
        this.originId = originId;
        this.transactionId = transactionId;
        
        /**
         * 14位组包时间YYYYMMDDHH24MISS
         */
        SimpleDateFormat dateFormate = new SimpleDateFormat("YYYYMMddHHmmss");
        transactionDate = dateFormate.format(new Date());
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getDigestAlg() {
        return digestAlg;
    }

    public void setDigestAlg(String digestAlg) {
        this.digestAlg = digestAlg;
    }
    
    /**
     * 
     * putParamsToMap
     */
    public boolean putParamsToMap(Map<String, String> map){
        if(StringUtils.isNotBlank(version)){
            map.put("Version", version);
        }
        
        if(StringUtils.isNotBlank(transactionId)){
            map.put("TransactionId", transactionId);
        }
        
        if(StringUtils.isNotBlank(transactionDate)){
            map.put("TransactionDate", transactionDate);
        }
        
        if(StringUtils.isNotBlank(originId)){
            map.put("OriginId", originId);
        }
        return true;
    }
    
    
}
