package com.cmcc.vrp.enums;

import com.cmcc.vrp.util.StringUtils;

/**
 * 流量券登录充值渠道
 * @author qinqinyan
 */
public enum FlowcardChargeChannelType {

    HJS("hjs", "合教授"),
    ZQWT("zqwt", "政企微厅");

    private String code;

    private String name;

    FlowcardChargeChannelType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * @param value
     * @return
     */
    public static FlowcardChargeChannelType fromValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        for (FlowcardChargeChannelType type : FlowcardChargeChannelType.values()) {
            if (type.getCode().equals(value)) {
                return type;
            }
        }

        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return
     */
    public String getname() {
        return name;
    }

    /**
     * @param name
     */
    public void setname(String name) {
        this.name = name;
    }

}
