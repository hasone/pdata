package com.cmcc.vrp.boss.guangxi.enums;

/**
 * Created by leelyn on 2016/9/23.
 */
public enum CallbackStatus {

    SUCCESS("00", "Success"),
    FAILURE1("01", "ProductID未找到、已注销或者与发起方机构不匹配"),
    FAILURE2("02", "ProductID指定的流量统付订购关系不是个人流量包模式，不支持个人叠加包订购"),
    FAILURE3("03", "距离月底不足48小时，不允许再订购个人叠加包"),
    FAILURE4("04", "无效成员，不能订购叠加包"),
    FAILURE5("05", "成员数量过多，超过50个限制"),
    FAILURE6("06", "在同一个叠加包请求内，不允许对同一成员进行多次赠送"),
    FAILURE7("07", "主办省已经暂停此订购的成员和叠加包操作，请咨询省公司"),
    FAILURE99("99", "其他错误"),
    UnKnown("unknown", "充值失败");
    private String rspCode;

    private String rspDsc;

    public static String getRspDsc(String rspCode) {
        if (rspCode == null) {
            return "充值失败";
        }
        for (CallbackStatus callbackStatus : CallbackStatus.values()) {
            if (callbackStatus.getRspCode().equals(rspCode)) {
                return callbackStatus.getRspDsc();
            }
        }

        return "充值失败";
    } 
    CallbackStatus(String rspCode, String rspDsc) {
        this.rspCode = rspCode;
        this.rspDsc = rspDsc;
    }

    public String getRspCode() {
        return rspCode;
    }

    public void setRspCode(String rspCode) {
        this.rspCode = rspCode;
    }

    public String getRspDsc() {
        return rspDsc;
    }

    public void setRspDsc(String rspDsc) {
        this.rspDsc = rspDsc;
    }
}
