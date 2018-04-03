package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("Request")
public class QryLeftTrafficReq {

    @XStreamAlias("QryLeftTraffic")
    private QryLeftTraffic qryLeftTraffic;

    public QryLeftTraffic getQryLeftTraffic() {
        return qryLeftTraffic;
    }

    public void setQryLeftTraffic(QryLeftTraffic qryLeftTraffic) {
        this.qryLeftTraffic = qryLeftTraffic;
    }
    
    
}
