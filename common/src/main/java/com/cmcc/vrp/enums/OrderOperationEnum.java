package com.cmcc.vrp.enums;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @ClassName: OrderOperationEnum 
 * @Description: 山东订单推送接口操作类型
 * @author: Rowe
 * @date: 2017年5月26日 下午1:45:12
 */
public enum OrderOperationEnum {

    ORDER("01", "NEW"), //订购操作
    DELETE("02", "DELETE"), //退订操作
    CHNAGE("03", "UPDATE"), //变更操作
    PAUSE("04", "UPDATE"), //暂停操作
    RECOVER("05", "UPDATE");//恢复操作

    private String oprCode;

    private String type;

    public String getOprCode() {
        return oprCode;
    }

    public String getType() {
        return type;
    }

    private OrderOperationEnum(String oprCode, String type) {
        this.oprCode = oprCode;
        this.type = type;
    }

    /**
     * 
     * @Title: toMap 
     * @Description: 类型转换
     * @return
     * @return: Map<String,String>
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (OrderOperationEnum item : OrderOperationEnum.values()) {
            map.put(item.getOprCode(), item.getType());
        }
        return map;
    }

    public static OrderOperationEnum getOperation(String oprCode, String type) {
        if (StringUtils.isBlank(oprCode) || StringUtils.isBlank(type)) {
            return null;
        }
        for (OrderOperationEnum item : OrderOperationEnum.values()) {
            if (item.getOprCode().equals(oprCode) && item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

    public static OrderOperationEnum getOperationFromValue(String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        for (OrderOperationEnum item : OrderOperationEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

    public static boolean isValidType(String type) {
        if (StringUtils.isBlank(type)) {
            return false;
        }
        for (OrderOperationEnum item : OrderOperationEnum.values()) {
            if (item.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidOprCode(String oprCode) {
        if (StringUtils.isBlank(oprCode)) {
            return false;
        }
        for (OrderOperationEnum item : OrderOperationEnum.values()) {
            if (item.getOprCode().equals(oprCode)) {
                return true;
            }
        }
        return false;
    }

    public static String getAllValues() {
        StringBuffer buf = new StringBuffer();
        for (OrderOperationEnum item : OrderOperationEnum.values()) {
            buf.append(" " + item.getType());
        }
        return buf.toString();
    }

}
