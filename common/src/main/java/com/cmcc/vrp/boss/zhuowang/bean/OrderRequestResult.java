package com.cmcc.vrp.boss.zhuowang.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 订购请求结果（订购处理结果见#OrderHandleResult）
 *
 * @author qinpo
 */
public class OrderRequestResult implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 5357285719692788635L;
    /**
     * 流水号
     */
    private String transIDO;
    /**
     * 应答/错误类型
     */
    private String rspType;
    /**
     * 应答/错误代码
     */
    private String rspCode;
    /**
     * 应答/错误描述
     */
    private String rspDesc;
    /**
     * 请求状态
     * 00 处理成功
     */
    private String status;
    /**
     * BBOSS订单流水号列表（每个省份一个）
     */
    private List<String> operSeqList;

    public String getTransIDO() {
        return transIDO;
    }

    public void setTransIDO(String transIDO) {
        this.transIDO = transIDO;
    }

    public String getRspType() {
        return rspType;
    }

    public void setRspType(String rspType) {
        this.rspType = rspType;
    }

    public String getRspCode() {
        return rspCode;
    }

    public void setRspCode(String rspCode) {
        this.rspCode = rspCode;
    }

    public String getRspDesc() {
        return rspDesc;
    }

    public void setRspDesc(String rspDesc) {
        this.rspDesc = rspDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getOperSeqList() {
        return operSeqList;
    }

    public void setOperSeqList(List<String> operSeqList) {
        this.operSeqList = operSeqList;
    }

    public enum ResultCode {
        SUCCESS("00", "处理成功"),
        OTHERS("01", "处理失败");

        private String status;
        private String message;

        ResultCode(String status, String message) {
            this.status = status;
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }

}
