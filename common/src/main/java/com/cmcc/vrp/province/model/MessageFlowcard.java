/**
 * @Title: MessageFlowcard.java
 * @Package com.cmcc.vrp.province.model
 * @author: qihang
 * @date: 2015年5月21日 上午10:51:08
 * @version V1.0
 */
package com.cmcc.vrp.province.model;

/**
 * @ClassName: MessageFlowcard
 * @Description: TODO
 * @author: qihang
 * @date: 2015年5月21日 上午10:51:08
 *
 */
public class MessageFlowcard {
    private String mobile;

    private String message;

    private Integer status;


    public MessageFlowcard() {
        super();
    }

    public MessageFlowcard(String message, Integer status) {
        super();
        this.message = message;
        this.status = status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
