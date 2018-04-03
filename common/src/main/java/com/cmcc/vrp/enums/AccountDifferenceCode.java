package com.cmcc.vrp.enums;

import java.util.HashMap;
import java.util.Map;

public enum AccountDifferenceCode {

    /**
     * @author qinqinyan
     * @description 对账差异表差异代码
     * <p>
     * 差异代码定义规则：采用二进制
     * AutSerialNum|EntSerialNum|Phone|EnterpriseCode|ProductCode|ReturnCode
     * 100000:32,"企业缺失记录"
     * 010000:16,"企业流水号不一致"
     * 001000:8,"充值手机号不一致"
     * 000100:4,"企业编码不一致"
     * 000010:2,"产品代码不一致"
     * 100001:1,"充值返回代码不一致"
     * 100001:33,"系统缺失记录"
     */

//	EntSerialNum_Diff("16","企业流水号不一致"),
    Phone_Diff("8", "充值手机号不一致"),
    EnterpriseCode_Diff("4", "不是指定对账的企业编码"),
    ProductCode_Diff("2", "产品代码不一致"),
    ReturnCode_Diff("1", "充值返回代码不一致"),
    EntRecord_Lost("32", "企业缺失记录"),
    SysRecord_Lost("33", "系统缺失记录"),
    Ent_Fail_Record("34", "企业上传的失败记录"),
    Response_Time_Error("35", "时间格式错误或不在对账时间范围内"),
    Diff_Num_Of_Item_Per_Line("36", "记录中字段数目不为指定数量"),
    Invalid_Record("37", "无效记录,企业编码、返回代码或返回时间不符合要求");


    private String code;

    private String message;

    AccountDifferenceCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (AccountDifferenceCode item : AccountDifferenceCode.values()) {
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
