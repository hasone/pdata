package com.cmcc.vrp.wx.enums;

public enum TemplateMsgTypeEnum {
    /**
     * 验证成功通知
     **/
    TYPE1(1),
    /**
     * 验证失败通知
     **/
    TYPE2(2),
    /**
     * 活动结果通知
     **/
    TYPE3(3),
    /**
     * 支付结果通知
     **/
    TYPE4(4),
    /**
     * 办理结果通知
     **/
    TYPE5(5),
    /**
     * 个人二维码通知
     **/
    TYPE6(6);
    private int type;

    private TemplateMsgTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
