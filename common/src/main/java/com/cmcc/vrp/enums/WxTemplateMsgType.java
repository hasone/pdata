package com.cmcc.vrp.enums;

/**
 * 微信模板消息
 * WxTemplateType.java
 * @author wujiamin
 * @date 2017年3月28日
 */
public enum WxTemplateMsgType {
    CROWDFUNDING_SUCCESS(1, "众筹成功", "3haF8Ma9jO7-RtwRYLQoG5Dt8Rz-QOOB9xHmMrhnNqM"),
    PAY_SUCCESS(2, "支付成功", "BplXa86MlDlUPhaOqe7hf1E3E1tpFEuzUrHHNQKirHQ"), 
    PAY_FAIL(3, "支付失败", "DnGjEbLfJxAxH3EJYFOOS0Wh_-o0TQ0z_ML8K84GuIc"),
    REFUND(4, "退款成功", "DoxroSVC73_ih1bMLmr5LJwrIWIzfIYqd7PyGJrTpec"),
    EXCHANGE_SUCCESS(5, "兑换成功", "mWUEv-82fUbXRqizyRXtWe5_xP4F1VESs1T5pXx7QG8"),
    EXCHANGE_FAIL(6, "兑换失败", "X9Tc6A1QcuWGhcwfxWUyjI7lgg6nSMk5BtW_6TTIa-U");

    private Integer code;

    private String message;
    
    private String templateId;

    private WxTemplateMsgType(Integer code, String message, String templateId) {
        this.code = code;
        this.message = message;
        this.templateId = templateId;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    

    public static WxTemplateMsgType getByCode(int code) {
        for (WxTemplateMsgType item : WxTemplateMsgType.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }

}
