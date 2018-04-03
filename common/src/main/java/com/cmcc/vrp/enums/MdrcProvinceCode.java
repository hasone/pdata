package com.cmcc.vrp.enums;

/**
 * @ClassName: MdrcProvinceCode
 * @Description: 营销卡操作省份编码
 * @author: Rowe
 * @date: 2016年7月11日 下午3:43:24
 */
public enum MdrcProvinceCode {
    DEFALUT("50", "默认值"),
    HAINAN("23", "海南省");

    private String code;

    private String message;

    MdrcProvinceCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
