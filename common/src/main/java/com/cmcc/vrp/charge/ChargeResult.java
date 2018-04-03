package com.cmcc.vrp.charge;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: ChargeResult
 * @Description: 充值结果
 * @author: sunyiwei
 * @date: 2015年5月8日 上午11:36:04
 */
public class ChargeResult implements Serializable {
    private static final long serialVersionUID = 926907128946945843L;
    private ChargeResultCode code;
    private ChargeMsgCode msg;
    private String failureReason = "";
    private String statusCode;
    private Integer financeStatus;
    private Date updateChargeTime;

    public ChargeResult(ChargeResultCode code) {
        this.code = code;
    }

    public ChargeResult(ChargeResultCode code, String failureReason) {
        this.code = code;
        this.failureReason = failureReason;
    }

    public ChargeResult(ChargeMsgCode msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return code != null && (code.equals(ChargeResultCode.SUCCESS) || code.equals(ChargeResultCode.PROCESSING));
    }

    public ChargeResultCode getCode() {
        return code;
    }

    public void setCode(ChargeResultCode code) {
        this.code = code;
    }

    /**
     * @return the msg
     */
    public ChargeMsgCode getMsg() {
        return msg;
    }


    /**
     * @param msg the msg to set
     */
    public void setMsg(ChargeMsgCode msg) {
        this.msg = msg;
    }


    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public enum ChargeResultCode {
        SUCCESS("100"),
        FAILURE("101"),
        PROCESSING("102"),
        CONNECT_FAILURE("001"),
        UN_ACCEPT("002"),
        SYSTEM_ERROR("21");

        private String code;

        ChargeResultCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public enum ChargeMsgCode {
        refuseToEnterQueue("-1"),//拒绝入业务队列
        businessQueue("201"), //入业务队列
        deliverQueue("202"), //入分发队列
        supplierQueue("203"), //分拣成功进入渠道队列
        zwProviceQueue("204"), //入卓望省队列
        zwBossQueue("205"), //入卓望boss队列
        completed("301"); //已投递上游boss

        private String code;

        ChargeMsgCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getFinanceStatus() {
        return financeStatus;
    }

    public void setFinanceStatus(Integer financeStatus) {
        this.financeStatus = financeStatus;
    }

    public Date getUpdateChargeTime() {
        return updateChargeTime;
    }

    public void setUpdateChargeTime(Date updateChargeTime) {
        this.updateChargeTime = updateChargeTime;
    }
}
