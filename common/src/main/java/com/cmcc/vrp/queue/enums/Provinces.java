package com.cmcc.vrp.queue.enums;

/**
 * Created by leelyn on 2016/11/15.
 */
public enum Provinces {

    BEIJING("BJ", "北京", "zhuowang.boss.province.beijing"),
    TIANJIN("TJ", "天津", "zhuowang.boss.province.tianjin"),
    CHONGQING("CQ", "重庆", "zhuowang.boss.province.chongqing"),
    HEBEI("HE", "河北", "zhuowang.boss.province.hebei"),
    SHANGHAI("SH", "上海", "zhuowang.boss.province.shanghai"),
    SHANXI1("SX", "山西", "zhuowang.boss.province.shanxi1"),
    NEIMENG("NM", "内蒙", "zhuowang.boss.province.neimeng"),
    LIAONING("LN", "辽宁", "zhuowang.boss.province.liaoning"),
    JINLIN("JL", "吉林", "zhuowang.boss.province.jilin"),
    HEILONGJIANG("HL", "黑龙江", "zhuowang.boss.province.heilongjiang"),
    JIANGSU("JS", "江苏", "zhuowang.boss.province.jiangsu"),
    ZHEJIANG("ZJ", "浙江", "zhuowang.boss.province.zhejiang"),
    ANHUI("AH", "安徽", "zhuowang.boss.province.anhui"),
    FUJIAN("FJ", "福建", "zhuowang.boss.province.fujian"),
    JIANGXI("JX", "江西", "zhuowang.boss.province.jiangxi"),
    SHANDONG("SD", "山东", "zhuowang.boss.province.shandong"),
    HENAN("HA", "河南", "zhuowang.boss.province.henan"),
    HUBEI("HB", "湖北", "zhuowang.boss.province.hubei"),
    HUNAN("HN", "湖南", "zhuowang.boss.province.hunan"),
    GUANGDONG("GD", "广东", "zhuowang.boss.province.guangdong"),
    GUANGXI("GX", "广西", "zhuowang.boss.province.guangxi"),
    HAINAN("HI", "海南", "zhuowang.boss.province.hainan"),
    SICHUAN("SC", "四川", "zhuowang.boss.province.sichuan"),
    GUIZHOU("GZ", "贵州", "zhuowang.boss.province.guizhou"),
    YUNNAN("YN", "云南", "zhuowang.boss.province.yunnan"),
    XIZANG("XZ", "西藏", "zhuowang.boss.province.xizang"),
    SHANXI2("SN", "陕西", "zhuowang.boss.province.shanxi2"),
    GANSU("GS", "甘肃", "zhuowang.boss.province.gansu"),
    QINGHAI("QH", "青海", "zhuowang.boss.province.qinghai"),
    NINGXIA("NX", "宁夏", "zhuowang.boss.province.ningxia"),
    XINJIANG("XJ", "新疆", "zhuowang.boss.province.xinjiang"),
    TAIWAN("TW", "台湾", "zhuowang.boss.province.taiwan"),
    HONGKONG("HK", "香港", "zhuowang.boss.province.hongkong"),
    MACAO("MO", "澳门", "zhuowang.boss.province.macao");

    private String code;

    private String name;

    private String queueName;

    Provinces(String code, String name, String queueName) {
        this.code = code;
        this.name = name;
        this.queueName = queueName;
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

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
}
