package com.cmcc.vrp.enums;

/**
 * 营销卡模板类型
 * create by qinqinyan on 2017/08/09
 */
public enum MdrcTemplateType {

    COMMON_TEMPLATE(0, "模板库"),
    INDIVIDUATION_TEMPLATE(1, "自定义模板");
    

    private Integer code;

    private String name;
    

    MdrcTemplateType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * @param value
     * @return
     */
    public static MdrcTemplateType fromValue(Integer value) {
        if (value == null) {
            return null;
        }

        for (MdrcTemplateType type : MdrcTemplateType.values()) {
            if (type.getCode().equals(value)) {
                return type;
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
