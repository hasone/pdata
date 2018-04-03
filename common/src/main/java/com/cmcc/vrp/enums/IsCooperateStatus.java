package com.cmcc.vrp.enums;

public enum IsCooperateStatus {

    NOT_COOPERATE(1, "未签约"),
    COOPERATE(2, "已签约");


    private Integer code;

    private String name;

    IsCooperateStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return
     */
    public String getname() {
        return name;
    }

    /**
     * @param name
     */
    public void setname(String name) {
        this.name = name;
    }

}
