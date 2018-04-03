package com.cmcc.vrp.boss.shyc.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * 上海月呈定义的状态回调请求
 *
 * Created by sunyiwei on 2017/3/14.
 */
public class ShycCallbackReq {
    private Integer type;

    @SerializedName("orderno")
    private String orderNo;

    @SerializedName("taskid")
    private String taskId;

    private String phone;
    private String code;
    private String message;
    private String sign;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
