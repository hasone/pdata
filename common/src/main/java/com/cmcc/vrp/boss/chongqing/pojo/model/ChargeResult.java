package com.cmcc.vrp.boss.chongqing.pojo.model;

import java.io.Serializable;

/**
 * @ClassName: ChargeResult
 * @Description: 充值结果
 * @author: sunyiwei
 * @date: 2015年5月8日 上午11:36:04
 */
public class ChargeResult implements Serializable {

    private static final long serialVersionUID = 926907128946945843L;
    private String code;
    private String failureReason = "";
    public ChargeResult(String code) {
        this.code = code;
    }

    public ChargeResult(String code, String failureReason) {
        this.code = code;

        this.failureReason = failureReason;
    }

    public boolean isSuccess() {
        if (code == null) {
            return false;
        }
        return code.equals(RESULTCODE.SUCCESS.getCode());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public static enum RESULTCODE {
        SUCCESS("100"),
        FAILURE("101"),
        CONNECT_FAILURE("001");

        private String code;

        private RESULTCODE(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}