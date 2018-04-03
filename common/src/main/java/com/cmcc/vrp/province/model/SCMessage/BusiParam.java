/**
 *
 */
package com.cmcc.vrp.province.model.SCMessage;

/**
 * @author JamieWu
 *         短信接口业务请求参数
 */
public class BusiParam {

    private String phone_no;

    private String templateId;

    private String params;

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }


}
