/**
 * @Title: UssmsCardErrType.java
 * @Package com.cmcc.vrp.enums
 * @author: qihang
 * @date: 2015年8月18日 下午2:30:00
 * @version V1.0
 */
package com.cmcc.vrp.enums;

/**
 * @ClassName: UssmsCardErrType
 * @Description: 短信上行激活营销卡的错误类型
 * @author: qihang
 * @date: 2015年8月18日 下午2:30:00
 *
 */
public enum UssmsCardErrType {
    SUCCESS(0, "激活成功"),
    FORMAT_ERR(1, "指令格式错误，请重新输入。"),//收到的上传指令不是"卡号#卡密"
    CARDINFO_ERR(2, "卡号或密码错误，请重新确认后重新充值。"),//卡号卡密不正确，或者状态不是已激活
    CHARGE_ERR(3, "网络异常，请稍后再试。");//卡的状态正确，但是连接boss充值时返回的信息是充值失败

    private int code;

    private String message;

    private UssmsCardErrType(int code, String message) {
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
