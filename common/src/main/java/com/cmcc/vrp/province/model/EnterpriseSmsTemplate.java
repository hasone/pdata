package com.cmcc.vrp.province.model;

/**
 * 企业短信模板
 *
 * @author JamieWu
 */
public class EnterpriseSmsTemplate {

    private Long enterId;

    private Long smsTemplateId;

    private Integer status = 0;

    private String name;

    private String content;

    public Long getEnterId() {
        return enterId;
    }

    public void setEnterId(Long enterId) {
        this.enterId = enterId;
    }

    public Long getSmsTemplateId() {
        return smsTemplateId;
    }

    public void setSmsTemplateId(Long smsTemplateId) {
        this.smsTemplateId = smsTemplateId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
