package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * 短信模板
 *
 * @author kok
 */
public class SmsTemplate {
    private long id;
    private String name;
    private String content;
    private Date createTime;
    private Integer defaultHave;
    private Integer defaultUse;

    public SmsTemplate() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDefaultHave() {
        return defaultHave;
    }

    public void setDefaultHave(Integer defaultHave) {
        this.defaultHave = defaultHave;
    }

    public Integer getDefaultUse() {
        return defaultUse;
    }

    public void setDefaultUse(Integer defaultUse) {
        this.defaultUse = defaultUse;
    }


}
