package com.cmcc.vrp.enums;

/**
 * Created by qinqinyan on 2017/1/6.
 * 广东流量众筹支付状态枚举
 */
public enum PaymentStatus {

    Not_Pay(0, "未支付"),
    Paying(1, "支付中"),
    Pay_Success(2, "支付成功"),
    Pay_Fail(3, "支付失败"),
    Refund_Success(4, "退款成功"),
    Refund_Fail(5, "退款失败"),
    Unkown_Exception(6, "未知异常");

    private int status;
    private String message;

    PaymentStatus(int status, String message){
        this.status = status;
        this.message = message;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * @param code
     * @return
     */
    public static PaymentStatus fromValue(int status) {
        for (PaymentStatus paymentStatus : PaymentStatus.values()) {
            if (paymentStatus.getStatus() == status) {
                return paymentStatus;
            }
        }

        return null;
    }
}
