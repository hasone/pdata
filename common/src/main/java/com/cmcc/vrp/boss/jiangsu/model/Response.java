package com.cmcc.vrp.boss.jiangsu.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("response")
public class Response {

    @XStreamAlias("resp_type")
    private String respType;
    
    @XStreamAlias("resp_result")
    private String respTresult;
    
    @XStreamAlias("resp_code")
    private String respCode;
    
    @XStreamAlias("resp_desc")
    private String respDesc;

    public String getRespType() {
        return respType;
    }

    public void setRespType(String respType) {
        this.respType = respType;
    }

    public String getRespTresult() {
        return respTresult;
    }

    public void setRespTresult(String respTresult) {
        this.respTresult = respTresult;
    }

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

}
