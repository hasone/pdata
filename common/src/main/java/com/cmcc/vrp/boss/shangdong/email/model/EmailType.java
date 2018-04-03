package com.cmcc.vrp.boss.shangdong.email.model;

import java.util.LinkedHashMap;
import java.util.Map;
/**
 * 
 * @ClassName: EmailType 
 * @Description: 邮件类型
 * @author: Rowe
 * @date: 2016年8月15日 下午8:48:01
 */
public enum EmailType {
    
    DAY_EMAIL_PACKAGE(0, "发送每天流量包对账文件"),
    MONTH_EMAIL_PACKAGE(1, "发送每月流量包对账文件"),
    DAY_EMAIL_CELL(2, "发送每天流量池用户使用情况对账文件"),
    DAY_EMAIL_CELL_BOSS(3, "发送每天流量池boss订购情况对账文件");
    
    private Integer code;
    
    private String message;
    
    EmailType(Integer code, String message){
        this.code = code;
        this.message = message;
    }
    
    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
    
    /**
     * 转化为map
     * @return
     */
    public static Map<String, String> toMap(){
        Map<String, String> map = new LinkedHashMap<String, String>();
        for(EmailType item : EmailType.values()){
            map.put(item.getCode().toString(), item.getMessage());
        }
        return map;
    }
}
