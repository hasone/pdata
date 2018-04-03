/**
 * @Title: ChargeType.java
 * @Package com.cmcc.vrp.enums
 * @author: qihang
 * @date: 2015年9月17日 下午2:01:19
 * @version V1.0
 */
package com.cmcc.vrp.enums;

/**
 * @ClassName: ChargeType
 * @Description: TODO
 * @author: qihang
 * @date: 2015年9月17日 下午2:01:19
 *
 */
public enum ChargeType {

    MONTHLY_TASK("mt", "包月任务充值"),
    PRESENT_TASK("pt", "批量任务充值"),
    REDPACKET_TASK("rt", "红包任务充值"),
    FLOWCARD_TASK("ft", "流量卡充值"),
    QRCODE_TASK("qr", "二维码充值"),
    MDRCCARD_PAGE("mp", "营销卡页面充值"),
    MDRCCARD_SMS("ms", "营销卡短信充值"),
    GAME_TASK("gt", "游戏充值"),
    LOTTERY_TASK("lt", "大转盘中奖纪录充值"),
    GOLDENBALL_TASK("bt", "砸金蛋中奖纪录充值"),
    INDIVIDUAL_FLOWCOIN_EXCHANGE("ife", "个人流量币兑换充值"),
    INDIVIDUAL_FLOW_REDPACKET("ifr", "个人流量红包"),
    EC_TASK("ec", "EC充值"),
    CROWDFUNDING_TASK("crowdfunding", "众筹活动充值"),
    OTHERS("ot", "其它");

    private String code;

    private String name;

    private ChargeType(String code, String name) {
        this.code = code;
        this.name = name;
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
