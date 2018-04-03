package com.cmcc.vrp.enums;

import org.springframework.util.StringUtils;

/**
 * 财务状态
 * FinancialStatus.java
 * @author wujiamin
 * @date 2016年12月22日
 */
public enum FinanceStatus {
    OUT(0, "出账记录","cz"),//企业账户的流量充值支出、流量充值支出（冻结）
    IN(1, "未出账记录", "wcz"),//企业账户的流量充值退款收入
    ACCOUNT_CHANGE_OUT(2, "调账出账记录", "dzcz"),//调账退款支出
    ACCOUNT_CHANGE_IN(3, "调账未出账记录", "dzwcz");//调账退款收入

    private Integer code;

    private String name;
    
    private String type; //前端页面的类型
    
    FinanceStatus(Integer code, String name, String type){
        this.setCode(code);
        this.setName(name);
        this.setType(type);
    }
    
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    /** 
     * 根据页面传递的类型，获取账单类型
     * @Title: fromType 
     */
    public static Integer fromType(String type) {
        if (StringUtils.isEmpty(type)) {
            return null;
        }

        for (FinanceStatus status : FinanceStatus.values()) {
            if (type.equals(status.getType())) {
                return status.getCode();
            }
        }

        return null;
    }
}
