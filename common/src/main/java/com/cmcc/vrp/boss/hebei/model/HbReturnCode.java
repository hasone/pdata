package com.cmcc.vrp.boss.hebei.model;

/**
 * Created by leelyn on 2016/8/19.
 */
public enum HbReturnCode {

    SUCCESS("0000", "充值成功"),
    SYSTEM_ERROR("1999", "环境问题,这种状态可能已充值成功,调接口查询流水状态"),
    PARA_ERROR("2998", "参数错误"),
    FAILD("xxxx", "未知错误失败");

    private String code;
    private String msg;

    HbReturnCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

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
}
