package com.cmcc.vrp.sms.openplatform;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * 开放平台短信相关类
 *
 */
public class SmsBody {
    
    /**
     * 短信内容签名，系统会自动添加【】，无需自行添加
     */
    @SerializedName(value = "messageSign")
    private String messageSign;
    
    
    /**
     * 可选。分配的短信客户代码，
     */
    @SerializedName(value = "smsCustomCode")
    private String smsCustomCode;
    
    /**
     * 手机号，支持群发
     */
    @SerializedName(value = "mobiles")
    private List<String> mobiles;
    
    /**
     * 是否回执  0：不回执；1：回执；为空表示不要回执
     */
    @SerializedName(value = "needReceipt")
    private Integer needReceipt = 0;//是否回执  0：不回执；1：回执；为空表示不要回执

    /**
     * 当needReceipt=1时，非空（为接受回执的地址，需要开发者提供），其余值时可以为空
     */
    @SerializedName(value = "receiptNotificationURL")
    private String receiptNotificationURL;//当needReceipt=1时，非空（为接受回执的地址，需要开发者提供），其余值时可以为空
    
    /**
     * 模板Id，非空
     */
    @SerializedName(value = "templateId")
    private Integer templateId = 1;
    
    /**
     * 短信模板
     */
    @SerializedName(value = "templateParameter")
    private SmsBasicTemplate smsTemplate;

    public String getMessageSign() {
        return messageSign;
    }

    public void setMessageSign(String messageSign) {
        this.messageSign = messageSign;
    }

    public String getSmsCustomCode() {
        return smsCustomCode;
    }

    public void setSmsCustomCode(String smsCustomCode) {
        this.smsCustomCode = smsCustomCode;
    }

    public List<String> getMobiles() {
        return mobiles;
    }

    public void setMobiles(List<String> mobiles) {
        this.mobiles = mobiles;
    }

    public Integer getNeedReceipt() {
        return needReceipt;
    }

    public void setNeedReceipt(Integer needReceipt) {
        this.needReceipt = needReceipt;
    }

    public String getReceiptNotificationURL() {
        return receiptNotificationURL;
    }

    public void setReceiptNotificationURL(String receiptNotificationURL) {
        this.receiptNotificationURL = receiptNotificationURL;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public SmsBasicTemplate getSmsTemplate() {
        return smsTemplate;
    }

    public void setSmsTemplate(SmsBasicTemplate smsTemplate) {
        this.smsTemplate = smsTemplate;
    }
    
}
