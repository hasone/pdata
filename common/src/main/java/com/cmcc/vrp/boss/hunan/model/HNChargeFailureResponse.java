package com.cmcc.vrp.boss.hunan.model;

/**
 * @ClassName: HNChargeResponse
 * @Description: 湖南省BOSS充值接口失败：失败返回报文格式{"res_code":"3CRM_COMM_103","res_message":"[该号码信息不存在！]"}
 * @author: Rowe
 * @date: 2016年4月7日 下午4:55:16
 */
public class HNChargeFailureResponse {

    private String res_code;//状态码

    private String res_message;//失败原因

    public String getRes_code() {
        return res_code;
    }

    public void setRes_code(String res_code) {
        this.res_code = res_code;
    }

    public String getRes_message() {
        return res_message;
    }

    public void setRes_message(String res_message) {
        this.res_message = res_message;
    }

}
