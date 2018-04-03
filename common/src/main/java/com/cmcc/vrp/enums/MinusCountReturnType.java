package com.cmcc.vrp.enums;

/**
 * AccountServiceImpl.minuscount   返回错误信息
 * @author qihang
 *
 */
public enum MinusCountReturnType {
    OK("OK","扣减成功"),
    PARAM_ERR("PARAM_ERR","此次充值参数错误"),
    PASS_LIMITFLOW("PASS_LIMITFLOW","此次充值请求超过企业流控金额"),
    PRD_ERR("PRD_ERR","此次充值获取产品错误"),
    ACCOUNT_NOTEXIST("ACCOUNT_NOTEXIST","账户信息不存在"),
    PASS_STOPVALUE("PASS_STOPVALUE","此次充值超过企业设置的暂停值"),
    MINUS_FAILURE("MINUS_FAILURE","此次充值扣减余额失败"),
    NO_PRDMATCH("NO_PRDMATCH","此次充值账户余额不足"),
    NO_SUFFIT("NO_SUFFIT","此次充值账户余额不足"); 
    
    private String type;
    
    private String msg;
    
    private MinusCountReturnType(String type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    
}
