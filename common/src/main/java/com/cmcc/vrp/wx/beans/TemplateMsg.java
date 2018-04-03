package com.cmcc.vrp.wx.beans;

import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 微信模板消息使用
 * TemplateMsg.java
 * @author wujiamin
 * @date 2017年3月28日
 */
public class TemplateMsg {
    private String touser;
    @JSONField(name="template_id")
    private String templateId;
    private String url;
    private Map<String, TemplateData> data;

    public Map<String, TemplateData> getData() {
        return data;
    }

    public void setData(Map<String, TemplateData> data) {
        this.data = data;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}
