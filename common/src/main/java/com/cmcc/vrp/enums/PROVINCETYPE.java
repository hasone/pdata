package com.cmcc.vrp.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @ClassName: PROVINCETYPE 
 * @Description: TODO
 * @author: Rowe
 * @date: 2017年4月1日 下午3:16:45
 */
public enum PROVINCETYPE {
    SICHUAN("sc","四川"),
    SHANDONG("sd", "山东"),
    GUANGDONG_MDRC("gd_mdrc","广东"),
    CHONGQING("chongqing", "重庆"),
    HUNAN("hun","湖南"),
    XINJIANG("xj", "新疆"),
    HEILONGJIANG("hlj","黑龙江"),
    NEIMENGGU("neimenggu", "内蒙古"),
    HAINAN("hainan", "海南"),
    GANSU("gansu", "甘肃"),
    SHANGHAI("shanghai", "上海"),
    TIANJIN("tianjin", "天津"),
    ZIYING("ziying","自营平台"),
    DEFAULT("local","本省");

    private String value;
    private String desc;

    PROVINCETYPE(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * @param value
     * @return
     */
    public static PROVINCETYPE fromValue(String value) {
        if(StringUtils.isBlank(value)){
            return DEFAULT;
        }
        for (PROVINCETYPE tpye : PROVINCETYPE.values()) {
            if (tpye.getValue().equalsIgnoreCase(value)) {
                return tpye;
            }
        }
        return DEFAULT;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
