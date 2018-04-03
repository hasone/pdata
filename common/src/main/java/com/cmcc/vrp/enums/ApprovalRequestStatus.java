package com.cmcc.vrp.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Title:ApprovalRequestStatus </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年10月24日
 */
public enum ApprovalRequestStatus {

    APPROVING(0, "审核中"), APPROVED(1, "已通过"), REJECT(2, "已驳回");

    private Integer code;

    private String message;

    ApprovalRequestStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(Integer code) {
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

    /**
     * 
     * @Title: toMap 
     * @Description: TODO
     * @return
     * @return: Map<String,String>
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (ApprovalRequestStatus item : ApprovalRequestStatus.values()) {
            map.put(item.getCode().toString(), item.getMessage());
        }
        return map;
    }

}
