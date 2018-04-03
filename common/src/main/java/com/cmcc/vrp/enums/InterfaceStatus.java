package com.cmcc.vrp.enums;

/**
 * 企业的EC状态
 * <p>
 * Created by sunyiwei on 2016/9/12.
 */
public enum InterfaceStatus {
    CLOSE(0, "关闭"),
    OPEN(1, "开通"),
    UNAPPROVAL(2, "未申请"),
    APPROVING(3, "申请中"),
    REJECT(4, "已驳回");

    private int code;
    private String message;

    InterfaceStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
