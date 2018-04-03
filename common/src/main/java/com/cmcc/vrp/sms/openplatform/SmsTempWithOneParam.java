package com.cmcc.vrp.sms.openplatform;

import com.google.gson.annotations.SerializedName;

/**
 * 开发平台模板，只有一个参数时
 *
 */
public class SmsTempWithOneParam extends SmsBasicTemplate {
    @SerializedName(value = "para1")
    String parameterOne;
    
    @Override
    protected void setParams(int templateId,String... params) {
        setTemplateId(templateId);
        if(params == null || params.length == 0){
            return;
        }else{
            parameterOne = params[0];
        }    
    }

    public String getParameterOne() {
        return parameterOne;
    }

    public void setParameterOne(String parameterOne) {
        this.parameterOne = parameterOne;
    }
}
