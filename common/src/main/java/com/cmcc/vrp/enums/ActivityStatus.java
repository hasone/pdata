package com.cmcc.vrp.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 活动统一的状态枚举类
 *
 * @author wujiamin
 * @date 2016年8月19日上午11:26:50
 */
public enum ActivityStatus {

    SAVED(0, "已保存"),
    ON(1, "已上架"),
    PROCESSING(2, "活动进行中"),
    DOWN(3, "已下架"),
    END(4, "活动已结束"),
    SUBMIT_APPROVAL(5, "审核中"),
    FINISH_APPROVAL(6, "审核完成"),
    REJECT(7, "已驳回");

    private Integer code;

    private String message;

    ActivityStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (ActivityStatus item : ActivityStatus.values()) {
            map.put(item.getCode().toString(), item.getMessage());
        }
        return map;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
