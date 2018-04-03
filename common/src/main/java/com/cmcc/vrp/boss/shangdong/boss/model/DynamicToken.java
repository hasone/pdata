package com.cmcc.vrp.boss.shangdong.boss.model;

/**
 * @ClassName:	DynamicToken 
 * @Description:  云平台4.1.3	无登录获取Token，返回对象
 * @author:	qihang
 * @date:	2016年3月10日 上午9:23:15 
 * @author qihang
 *
 */
public class DynamicToken {
    private String code;
	
    private String msg;
    
    private String token;
    
    public String getCode() {
    	return code;
    }
    
    public void setCode(String code) {
    	this.code = code;
    }
    
    public String getMsg() {
    	return msg;
    }
    
    public void setMsg(String msg) {
    	this.msg = msg;
    }
    
    public String getToken() {
    	return token;
    }
    
    public void setToken(String token) {
    	this.token = token;
    }
    

}
