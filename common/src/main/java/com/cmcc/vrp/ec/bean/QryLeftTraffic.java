package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("QryLeftTraffic")
public class QryLeftTraffic {

    @XStreamAlias("TELNUM")
    private String telnum;
    
    @XStreamAlias("CYCLE")
    private String cycle;
    
    @XStreamAlias("TYPE")
    private String type;

    public String getTelnum() {
        return telnum;
    }

    public void setTelnum(String telnum) {
        this.telnum = telnum;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
}
