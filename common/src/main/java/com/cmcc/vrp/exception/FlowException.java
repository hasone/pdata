package com.cmcc.vrp.exception;

import java.util.List;

/**
 * 本类是自己定义的异常类
 *
 * @author 陈德伟
 * @version Ver 1.00 2014-10-02
 */
public class FlowException extends RuntimeException {

    private static final long serialVersionUID = 8156464015933076496L;

    private String errorKey;//异常信息
    private List<?> args;//信息参数

    public FlowException() {
    }

    public FlowException(String errorKey) {
        super();
        this.errorKey = errorKey;
    }

    public FlowException(String errorKey, String message) {
        super(message);
        this.errorKey = errorKey;
    }

    public FlowException(String errorKey, String message, Throwable cause) {
        super(message, cause);
        this.errorKey = errorKey;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public List<?> getArgs() {
        return args;
    }

    public void setArgs(List<?> args) {
        this.args = args;
    }
}
