package com.cmcc.vrp.boss.jiangsu.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("content")
public class ResponseContent {
    @XStreamAlias("ret_code")
    private String retCode;
    
    @XStreamAlias("cc_operating_srl")
    private String ccOperatingSrl;

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getCcOperatingSrl() {
        return ccOperatingSrl;
    }

    public void setCcOperatingSrl(String ccOperatingSrl) {
        this.ccOperatingSrl = ccOperatingSrl;
    }
    
}
