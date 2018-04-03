/**
 * @Title: 	WarnStatus.java 
 * @Package com.cmcc.shandong.vrp.enums 
 * @author:	qihang
 * @date:	2016年4月8日 下午2:18:21 
 * @version	V1.0   
 */
package com.cmcc.vrp.enums;

/**
 * @ClassName: WarnStatus
 * @Description: 企业状态
 * @author: qihang
 * @date: 2016年4月8日 下午2:18:21
 * 
 */
public enum WarnStatus {
    NORMAL(0, "正常"), // 包括余额充足，没有相关记录和关闭状态
    ALERT(1, "已预警"), STOP(2, "已停止"), ERROR(-1, "未知状态");

    private Integer code;

    private String message;

    private WarnStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static WarnStatus getByCode(int code) {
        for (WarnStatus item : WarnStatus.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return ERROR;
    }

}
