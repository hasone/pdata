package com.cmcc.vrp.enums;

import java.util.HashMap;
import java.util.Map;

public enum ApprovalType {

    Enterprise_Approval(0, "企业开户审核流程"), Product_Change_Approval(1, "产品变更审核流程"), Account_Change_Approval(2, "账户变更审核流程"), Activity_Approval(
            3, "营销活动审核"), Ent_Information_Change_Approval(4, "企业信息变更审核"), Ec_Approval(5, "企业EC审核"), MDRC_Active_Approval(
            6, "营销卡激活审批流程"), MDRC_MakeCard_Approval(7, "营销卡制卡审批流程"), Admin_Change_Approval(8, "用户变更流程"), Account_Min_Change_Approval(
            9, "企业最小额度变更审批"), Account_Alert_Change_Approval(10, "企业预警值变更审批"), Account_Stop_Change_Approval(11,
            "企业暂停值变更审批");

    private Integer code;

    private String message;

    ApprovalType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (ApprovalType item : ApprovalType.values()) {
            map.put(item.getCode().toString(), item.getMessage());
        }
        return map;
    }

    /**
     * 
     * @Title: getByCode 
     * @Description: 根据编码获取审批类型
     * @param code
     * @return
     * @return: ApprovalType
     */
    public static ApprovalType getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ApprovalType item : ApprovalType.values()) {
            if (item.getCode().intValue() == code.intValue()) {
                return item;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
