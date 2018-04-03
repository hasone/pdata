package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("Request")
public class ShEnterprise {
    @XStreamAlias("opr")
    private String opr;
    
    @XStreamAlias("type")
    private String type;
    
    @XStreamAlias("Info")
    private ShEnterpriseInfo info;

    public String getOpr() {
        return opr;
    }

    public void setOpr(String opr) {
        this.opr = opr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ShEnterpriseInfo getInfo() {
        return info;
    }

    public void setInfo(ShEnterpriseInfo info) {
        this.info = info;
    }
}
