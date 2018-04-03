package com.cmcc.vrp.boss.heilongjiang.model;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.cmcc.vrp.boss.BossOperationResult;

/**
 * 
 * @ClassName: HLJChargeResult 
 * @Description: 行业应用流量包实时赠送接口结果封装报文
 * @author: Rowe
 * @date: 2017年8月28日 下午5:04:13
 * @约定报文格式：{"code":"0000000","message":"Rest Request Success","promptMsg":"User Operate Success","data":{"TRANS_ID":"20170828165500520"}}.

 */
public class HLJChargeResult implements BossOperationResult {

    private static String CHARGE_SUCCESS_CODE = "0000000"; //充值成功状态码，0000000表示成功，其他表示失败

    @JSONField(name = "code")
    private String code;

    @JSONField(name = "message")
    private String message;

    @JSONField(name = "promptMsg")
    private String promptMsg;
    
    private boolean isAsync;//是否异步

    @Override
    public String getResultCode() {
        return code;
    }

    @Override
    public boolean isSuccess() {
        if (StringUtils.isNotBlank(code) && CHARGE_SUCCESS_CODE.equalsIgnoreCase(code)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isAsync() {
        return isAsync;
    }

    @Override
    public String getResultDesc() {
        return promptMsg;
    }

    @Override
    public Object getOperationResult() {
        return null;
    }

    @Override
    public boolean isNeedQuery() {
        return false;
    }

    @Override
    public String getFingerPrint() {
        return null;
    }

    @Override
    public String getSystemNum() {
        return null;
    }

    @Override
    public Long getEntId() {
        return null;
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

    public String getPromptMsg() {
        return promptMsg;
    }

    public void setPromptMsg(String promptMsg) {
        this.promptMsg = promptMsg;
    }

    public void setAsync(boolean isAsync) {
        this.isAsync = isAsync;
    }

}
