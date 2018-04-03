package com.cmcc.vrp.enums;

/**
 * 算术验证码的操作类型
 * <p>
 * Created by sunyiwei on 2016/11/16.
 */
public enum CalcRandomCodeOperationType {
    Add("+"),
    Minus("-");

    private String oper;

    CalcRandomCodeOperationType(String oper) {
        this.oper = oper;
    }

    public String getOper() {
        return oper;
    }
}
