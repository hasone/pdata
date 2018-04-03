/**
 *
 */
package com.cmcc.vrp.enums;

/**
 * <p>
 * Title:FlowControlType
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author xujue
 * @date 2016年9月21日
 */
public enum FlowControlType {

    setCountUpper(0, "设置日上限金额"), setCountAddition(1, "设置日追加金额"), setCountCharge(
        2, "充值金额");

    int code;

    String message;

    private FlowControlType(int code, String message) {
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
