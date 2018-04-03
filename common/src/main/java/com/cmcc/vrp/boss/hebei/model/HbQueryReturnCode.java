package com.cmcc.vrp.boss.hebei.model;

/**
 * Created by leelyn on 2016/10/12.
 */
public enum HbQueryReturnCode {

    SUCCESS("0000", "执行成功"),
    PROCESSING("1000", "未执行,在处理中"),
    ENVIRONMENT_ERRO("1999", "环境问题,这种状态可能已充值成功,需继续查询"),
    FAILD("1001 ", "未找到任务,执行失败");


    private String code;
    private String msg;

    HbQueryReturnCode(String code, String msg) {
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
