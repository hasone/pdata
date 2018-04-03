package com.cmcc.vrp.enums;

/**
 * Created by kunrong on 15/6/3.
 */
public enum ProductStatus {
    OFF(0, "下架"),
    NORMAL(1, "上架"),;

    private Integer code;

    private String message;

    ProductStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
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
