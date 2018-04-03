package com.cmcc.vrp.util;

public enum GDRegion {

    gz("200", "广州"),
    sw("660", "汕尾"),
    yj("662", "阳江"),
    jy("663", "揭阳"),
    mm("668", "茂名"),
    jm("750", "江门"),
    sg("751", "韶关"),
    hz("752", "惠州"),
    mz("753", "梅州"),
    st("754", "汕头"),
    sz("755", "深圳"),
    zh("756", "珠海"),
    fs("757", "佛山"),
    zq("758", "肇庆"),
    zj("759", "湛江"),
    zs("760", "中山"),
    hy("762", "河源"),
    qy("763", "清远"),
    yf("766", "云浮"),
    cz("768", "潮州"),
    dg("769", "东莞");
    
    
    private String areaCode;
    
    private String areaName;

    private GDRegion(String areaCode, String areaName) {
        this.areaCode = areaCode;
        this.areaName = areaName;
    }
    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
    
    public static String getAreaName(String areaCode) {
        if (areaCode == null) {
            return "未知";
        }
        for (GDRegion gdRegion : GDRegion.values()) {
            if (gdRegion.getAreaCode().equals(areaCode)) {
                return gdRegion.getAreaName();
            }
        }

        return "未知";
    }
}
