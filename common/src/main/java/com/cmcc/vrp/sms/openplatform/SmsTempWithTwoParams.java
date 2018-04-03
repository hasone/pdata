package com.cmcc.vrp.sms.openplatform;

import com.google.gson.annotations.SerializedName;

/**
* 开发平台模板，有两个参数时
*
*/
public class SmsTempWithTwoParams extends SmsBasicTemplate {
    @SerializedName(value = "para1")
    String parameterOne;
    
    @SerializedName(value = "para2")
    String parameterTwo;
    
    @Override
    protected void setParams(int templateId,String... params) {
        setTemplateId(templateId);
        if(params == null || params.length < 2){
            return;
        }else{
            parameterOne = params[0];
            parameterTwo = params[1];
        }    
    }

    public String getParameterOne() {
        return parameterOne;
    }

    public void setParameterOne(String parameterOne) {
        this.parameterOne = parameterOne;
    }

    public String getParameterTwo() {
        return parameterTwo;
    }

    public void setParameterTwo(String parameterTwo) {
        this.parameterTwo = parameterTwo;
    }
    
    

}
