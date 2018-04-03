package com.cmcc.vrp.ec.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("Response")
public class QryLeftTrafficResp {
    
    @XStreamAlias("res_code")
    private String resCode;
    
    @XStreamAlias("res_desc")
    private String resDesc;
    
    @XStreamAlias("result")
    private List<DetailInfo> result;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResDesc() {
        return resDesc;
    }

    public void setResDesc(String resDesc) {
        this.resDesc = resDesc;
    }

    public List<DetailInfo> getResult() {
        return result;
    }

    public void setResult(List<DetailInfo> result) {
        this.result = result;
    }
}
