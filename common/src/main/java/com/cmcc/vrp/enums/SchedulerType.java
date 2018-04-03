package com.cmcc.vrp.enums;

import java.util.HashMap;
import java.util.Map;

public enum SchedulerType {

    MP("MP", "月流量赠送"),
    MDRCEXPIRE("BatchConfigExpireTask", "营销卡过期"),
    RESTORE("RESTORE", "到期流量返还"),
    LOTTERY_QUERY("LotteryQueryPhoneListTask", "大转盘定期查询中奖名单"),
    ENT_REDPACKET_CHANGE("EntRedpacketStatusTask", "定期更改红包活动状态"),
    LOTTERY_CHANGE("ChangeLotteryStatusTask", "定期更改大转盘活动状态"),
    GOLDENBALL_CHANGE("ChangeGoldenballStatusTask", "定期更改砸金蛋活动状态"),
    ENTERPRISE_EXPIRE("EnterpriseExpireTask", "企业合同到期暂停"),
    ENTERPRISE_LICENCE_EXPIRE("EnterpriseLicenceExpireTask", "企业营业执照到期回退至体验企业"),
    FLOWCARD_START("FlowcardStartTask", "流量券活动开始"),
    FLOWCARD_END("FlowcardEndTask", "流量券活动结束"),
    QRCODE_START("FlowcardStartTask", "二维码活动开始"),
    QRCODE_END("FlowcardEndTask", "二维码活动结束"),
    COMMON_REDPACKET_START("CommonRedpacketStartTask", "个人红包活动开始"),
    COMMON_REDPACKET_END("CommonRedpacketEndTask", "个人红包活动结束"),
    LUCKY_REDPACKET_START("LuckyRedpacketStartTask", "拼手气红包活动开始"),
    LUCKY_REDPACKET_END("LuckyRedpacketEndTask", "拼手气红包活动结束"),
    ENTERPRISE_INTERFACE_EXPIRE("EnterpriseInterfaceExpire", "接口appkey及secret过期提醒"),
    LOTTERY_START("LotteryStartTask", "大转盘活动开始"),
    LOTTERY_END("LotteryEndTask", "大转盘活动结束"),
    REDPACKET_START("RedpacketStartTask", "红包活动开始"),
    REDPACKET_END("RedpacketEndTask", "红包活动结束"),
    GOLDENEGG_START("GoldenEggStartTask", "红包活动开始"),
    GOLDENEGG_END("GoldenEggEndTask", "红包活动结束"),
    CROWDFUNDING_START("CrowdFundingStartTask", "流量众筹活动开始"),
    CROWDFUNDING_END("CrowdFundingEndTask", "流量众筹活动结束"),
    MONTHLY_PRESENT("monthlyPresent", "包月赠送");

    private String code;

    private String message;

    SchedulerType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (SchedulerType item : SchedulerType.values()) {
            map.put(item.getCode(), item.getMessage());
        }
        return map;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}