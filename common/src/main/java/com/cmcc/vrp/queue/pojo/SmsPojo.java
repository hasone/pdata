/**
 * @Title: SmsPojo.java
 * @Package com.cmcc.vrp.province.sms.login
 * @author: qihang
 * @date: 2015年8月11日 下午2:18:44
 * @version V1.0
 */
package com.cmcc.vrp.queue.pojo;

/**
 * @ClassName: SmsPojo
 * @Description: TODO
 * @author: qihang
 * @date: 2015年8月11日 下午2:18:44
 */
public class SmsPojo {
    private String mobile;

    private String content;

    private String prdName;

    private String entName;

    private String type;//短信类型

    private String templateName;//短信模板名称

    public SmsPojo() {

    }

    public SmsPojo(String mobile, String content, String prdName, String entName, String type, String templateName) {
        this.mobile = mobile;
        this.content = content;
        this.prdName = prdName;
        this.entName = entName;
        this.type = type;
        this.templateName = templateName;
    }

    public SmsPojo(String mobile, String content, String prdName, String entName, String type) {
        this(mobile, content, prdName, entName, type, null);
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPrdName() {
        return prdName;
    }

    public void setPrdName(String prdName) {
        this.prdName = prdName;
    }

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}

