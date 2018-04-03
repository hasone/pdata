package com.cmcc.vrp.province.mdrc.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author luozuwu
 * @Description: 卡状态，1新制卡, 2已签收、3已激活、4已绑定、5已使用、6已过期、7正常、8已锁定、 9已销卡
 * <p>
 * 其中6、7、8、9为运营状态，只有当运营状态为7即正常时，普通状态才有意义.
 */
public enum MdrcCardStatus {
    NEW(1, "新制卡"),
    STORED(2, "已签收"),
    ACTIVATED(3, "已激活"),
    // BINDING(4, "已绑定"),
    USED(5, "已使用"),

    //这三个为运营状态，有更高的优先级
    EXPIRED(6, "已过期"),
    NORMAL(7, "正常"),
    LOCKED(8, "已锁定"),
    DELETE(9, "已销卡"),
    USELESS(10, "已失效");

    private Integer code;

    private String message;

    MdrcCardStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (MdrcCardStatus item : MdrcCardStatus.values()) {
            map.put(item.getCode().toString(), item.getMessage());
        }
        return map;
    }


    /**
     * 正常状态时的map
     */
    public static Map<String, String> toNormalStatusMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (MdrcCardStatus item : MdrcCardStatus.values()) {
            if (item.getCode() < 7) {
                map.put(item.getCode().toString(), item.getMessage());
            }
        }
        return map;
    }

    /**
     * 取opstatus的为锁定或销卡map
     */
    public static Map<String, String> toOpStatusUnNoramlMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (MdrcCardStatus item : MdrcCardStatus.values()) {
            if (item.getCode() > 7) {
                map.put(item.getCode().toString(), item.getMessage());
            }
        }
        return map;
    }

    /**
     * 已绑定企业的状态map
     */
    public static Map<String, String> toEntStatusMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (MdrcCardStatus item : MdrcCardStatus.values()) {
            if (item.getCode() > 2) {
                map.put(item.getCode().toString(), item.getMessage());
            }
        }
        return map;
    }

    /**
     * 已绑定企业正常状态时的map
     */
    public static Map<String, String> toEntNormalStatusMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (MdrcCardStatus item : MdrcCardStatus.values()) {
            if (item.getCode() > 2 && item.getCode() < 7) {
                map.put(item.getCode().toString(), item.getMessage());
            }
        }
        return map;
    }


    /**
     * @param value
     * @return
     */
    public static MdrcCardStatus fromValue(int value) {
        for (MdrcCardStatus status : MdrcCardStatus.values()) {
            if (status.getCode() == value) {
                return status;
            }
        }

        return null;
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
