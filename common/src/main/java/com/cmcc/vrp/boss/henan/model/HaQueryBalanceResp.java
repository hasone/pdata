package com.cmcc.vrp.boss.henan.model;

import java.util.Map;

/**
 * Created by leelyn on 2016/8/17.
 */
public class HaQueryBalanceResp {

    private String respCode;
    private String respDesc;
    private Map<String, String> result;

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

    public Map<String, String> getResult() {
        return result;
    }

    public void setResult(Map<String, String> result) {
        this.result = result;
    }
}
