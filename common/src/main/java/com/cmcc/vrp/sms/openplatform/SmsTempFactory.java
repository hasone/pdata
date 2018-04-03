package com.cmcc.vrp.sms.openplatform;

/**
 * 短信模板工厂类
 *
 */
public class SmsTempFactory {
    
    /**
     * 根据参数个数的大小得到模板类，并设置完参数
     */
    public static SmsBasicTemplate getTemplate(int templateId,String... params){
        SmsBasicTemplate template = null;
        if(params == null || params.length == 0){
            return null;
        }else if(params.length == 1){
            template = new SmsTempWithOneParam();
        }else if(params.length == 2){
            template = new SmsTempWithTwoParams();
        }else{
            return null;
        }
        
        
        template.setParams(templateId, params);
        return template; 
    }
}
