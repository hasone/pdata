/**
 * 
 */
package com.cmcc.vrp.enums;

/**
 * <p>Title:InterfaceApprovalStatus </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年10月24日
 */
public enum InterfaceApprovalStatus {
    
    APPROVING(0, "申请中"),
    APPROVED(1, "已通过"),
    REJECT(2, "已驳回");

    private int code;
    private String message;

    InterfaceApprovalStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    
}
