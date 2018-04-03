package com.cmcc.vrp.enums;

public enum SignAchieveCoinType {

    SIGN_COIN(0, "签到获得流量币"),
    SIGN_AWARD_COIN(1, "连续签到奖励的流量币"),
    OUTNUMBER_MAX_SERIAL_SIGN_DAY(2, "超过最大连续签到次数"),
    OUTNUMBER_MAX_SIGN_PER_DAY(3, "超过每天最大奖励签到人数");


    private Integer code;

    private String name;

    SignAchieveCoinType(Integer code, String name) {
        this.code = code;
        this.name = name;
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
