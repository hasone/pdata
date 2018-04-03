package com.cmcc.vrp.enums;

/**
 * 活动类型，平台内部使用
 */
public enum ActivityType {

    INTERFACE(0, "接口调用"),
    GIVE(1, "赠送"),
    REDPACKET(2, "红包"),
    LOTTERY(3, "转盘"),
    GOLDENBALL(4, "砸金蛋"),
    FLOWCARD(5, "流量券"),
    QRCODE(6, "二维码"),
    COMMON_REDPACKET(7, "个人普通红包"),
    LUCKY_REDPACKET(8, "拼手气红包"),
    INDIVIDUAL_FLOWCOIN_EXCHANGE(9, "个人流量币兑换"),
    FLOWCOIN_PRESENT(10, "流量币赠送"),
    INDIVIDUAL_FLOWCOIN_PURCHASE(11, "个人流量币购买"),
    INDIVIDUAL_REDPACKAGE_ORDER(12, "个人红包流量订购"),
    CROWD_FUNDING(13, "流量众筹"),
    SIGN_IN(14, "签到"),
    INVITE(15, "邀请"),
    INVITED(16, "被邀请"),
    POINTS_EXCHANGR(17,"积分兑换"),
    MONTHLY_PRESENT(18,"包月赠送"),
    MDRC_CHARGE(19,"流量卡");

    private Integer code;

    private String name;
    //活动充值流程状态
    private String statusCode;

    ActivityType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * @param value
     * @return
     */
    public static ActivityType fromValue(Integer value) {
        if (value == null) {
            return null;
        }

        for (ActivityType type : ActivityType.values()) {
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

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
