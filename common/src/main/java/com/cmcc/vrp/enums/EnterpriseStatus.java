package com.cmcc.vrp.enums;

/**
 * 企业状态编码
 */
public enum EnterpriseStatus {

    NORMAL(0, "正常"),
    DELETED(1, "已删除"),
    PAUSE(2, "已暂停"),
    OFFLINE(3, "已下线"),
    SUBMIT_BASIC(4, "提交企业基本信息"),
    COOPERATE_INFO(5, "完成合作信息填写"),
    SUBMIT_APPROVAL(6, "审核中"),
    SAVED(7, "已保存"),
    REJECTED(8, "已驳回"),
    PROBATION(9, "体验企业"),
    QUALIFICATION_APPROVAL(10, "完成企业认证信息填写"),
    QUALIFICATION(11, "认证企业"),
    QUALIFICATION_REJECT(12, "认证驳回"),
    COOPERATE(13, "合作企业"),
    CONFIRM(14,"待确认"); //广东专属

    private Integer code;

    private String name;

    EnterpriseStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(Integer code) {
        String name = null;
        switch (code) {
            case 0:
                name = "正常";
                break;
            case 1:
                name = "已删除";
                break;
            case 2:
                name = "已暂停";
                break;
            case 3:
                name = "已下线";
                break;
            case 4:
                name = "等待市级管理员审核";
                break;
            case 5:
                name = "等待省级管理员审核";
                break;
            case 6:
                name = "等待省级管理员审核";
                break;
            case 7:
                name = "已保存";
                break;
            case 8:
                name = "已驳回";
                break;
            default:
                name = "";
                break;
        }
        return name;
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
