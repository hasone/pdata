package com.cmcc.vrp.ec.utils;

/**
 * Created by sunyiwei on 2016/7/6.
 */
public enum CallbackResult {
    SUCCESS("10000", "回调成功"),
    OTHERS("10001", "回调失败");

    private String code;
    private String message;

    CallbackResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @param code
     * @return
     */
    public static CallbackResult fromCode(String code) {
        for (CallbackResult callbackResult : CallbackResult.values()) {
            if (callbackResult.getCode().equals(code)) {
                return callbackResult;
            }
        }

        return null;
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
}
