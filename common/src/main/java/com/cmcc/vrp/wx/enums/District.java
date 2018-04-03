package com.cmcc.vrp.wx.enums;

public enum District {
    GZ("广州", "020"),
    SW("汕尾", "0660"),
    YJ("阳江", "0662"),
    JY("揭阳", "0663"),
    MM("茂名", "0668"),
    JM("江门", "0750"),
    SG("韶关", "0751"),
    HZ("惠州", "0752"),
    MZ("梅州", "0753"),
    ST("汕头", "0754"),
    SZ("深圳", "0755"),
    ZH("珠海", "0756"),
    FS("佛山", "0757"),
    ZQ("肇庆", "0758"),
    ZJ("湛江", "0759"),
    ZS("中山", "0760"),
    HY("河源", "0762"),
    QY("清远", "0763"),
    YF("云浮", "0766"),
    CZ("潮州", "0768"),
    DG("东莞", "0769");
    private String district;

    private String code;

    District(String district, String code) {
        this.district = district;
        this.code = code;
    }

    public String getDistrict() {
        return district;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public static String getCode(String name) {
        if (name == null) {
            return null;
        }
        for (District district : District.values()) {
            if (name.contains(district.getDistrict())) {
                return district.getCode();
            }
        }
        return null;
    }
    
    public static void main(String[] args) {
        String name = "广东省东莞市某某区";
        System.out.println(District.getCode(name));
    }
}
