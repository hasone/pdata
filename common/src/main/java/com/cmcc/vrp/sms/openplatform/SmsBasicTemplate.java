package com.cmcc.vrp.sms.openplatform;

/**
 * 开放平台设置的短信模板基础结构
 *
 */
public abstract class SmsBasicTemplate {
    
    protected transient int templateId;
    
    /**
     * 模板类共用的设置参数方法
     */
    abstract protected void setParams(int templateId,String ... params);

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    /**
     * 共用设置属性类
     */
    public void setTemplateProperties(SmsBody smsBody){
        smsBody.setTemplateId(getTemplateId());
        smsBody.setSmsTemplate(this);
    }
}
