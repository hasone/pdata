package com.cmcc.vrp.util;

/**
 * Created by sunyiwei on 2016/6/27.
 */
public enum Provinces {
    AnHui("AH", "安徽"),
    BeiJing("BJ", "北京"),
    FuJian("FJ", "福建"),
    GanSu("GS", "甘肃"),
    GangDong("GD", "广东"),
    Guangxi("GX", "广西"),
    Guizhou("GZ", "贵州"),
    Hainan("HI", "海南"),
    Hebei("HE", "河北"),
    Henan("HA", "河南"),
    Heilongjiang("HL", "黑龙江"),
    Hubei("HB", "湖北"),
    Hunan("HN", "湖南"),
    Jilin("JL", "吉林"),
    Jiangsu("JS", "江苏"),
    Jiangxi("JX", "江西"),
    Liaoning("LN", "辽宁"),
    InnerMongoria("INM", "内蒙古"),
    Ningxia("NX", "宁夏"),
    Qinghai("QH", "青海"),
    Shandong("SD", "山东"),
    Shanxi("SX", "山西"),
    Shaanxi("SN", "陕西"),
    Shanghai("SH", "上海"),
    Sichuan("SC", "四川"),
    Tianjing("TJ", "天津"),
    Tibet("XZ", "西藏"),
    Xinjiang("XJ", "新疆"),
    Yunnan("YN", "云南"),
    Zhejiang("ZJ", "浙江"),
    Chongqing("CQ", "重庆"),
    Unknown("Unknown", "未知");


    private String code;
    private String name;

    Provinces(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * @param name
     * @return
     */
    public static Provinces fromName(String name) {
        if (name == null) {
            return Unknown;
        }
        for (Provinces provinces : Provinces.values()) {
            if (provinces.getName().equals(name)) {
                return provinces;
            }
        }

        return Unknown;
    }
    
    /**
     * @param code
     * @return
     */
    public static Provinces fromCode(String code) {
        if (code == null) {
            return Unknown;
        }
        for (Provinces provinces : Provinces.values()) {
            if (provinces.getCode().equals(code)) {
                return provinces;
            }
        }

        return Unknown;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
