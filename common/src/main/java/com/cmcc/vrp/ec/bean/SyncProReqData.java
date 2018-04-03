package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("SyncInfo")
public class SyncProReqData {
	
    @XStreamAlias("EnterpriseId")
	String enterpriseId;
	
    @XStreamAlias("EntProCode")
	String entProCode;

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEntProCode() {
        return entProCode;
    }

    public void setEntProCode(String entProCode) {
        this.entProCode = entProCode;
    }

}
