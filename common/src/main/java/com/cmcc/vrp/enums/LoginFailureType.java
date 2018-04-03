/**
 * @Title: LoginFailureType.java
 * @Package com.cmcc.vrp.enums
 * @author: qihang
 * @date: 2015年5月12日 下午2:46:53
 * @version V1.0
 */
package com.cmcc.vrp.enums;

import java.util.HashMap;
import java.util.Map;

public enum LoginFailureType {
    VIRIFYCODE_ERR(1, "短信验证证码错误，请重新查看"),
    UNKNOWN_ERR(9, "未知错误");


    private Integer code;  //错误编码

    private String message;  //页面上显示的错误信息

    LoginFailureType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (LoginFailureType item : LoginFailureType.values()) {
            map.put(String.valueOf(item.getCode()), item.getMessage());
        }
        return map;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
