package com.cmcc.vrp.boss.heilongjiang.fee;

public enum HljFeeResultCode {
    SUCCESS("0", "充值成功"),
    FAILD("1", "充值失败"),
    PARA_ILLEGALITY("11", "参数缺失"),
    CODE_PARAM_JSON_ERROR("12", "数据库CODE字段json解析错误");

    private String code;
    private String msg;

    HljFeeResultCode(String code, String msg) {
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
