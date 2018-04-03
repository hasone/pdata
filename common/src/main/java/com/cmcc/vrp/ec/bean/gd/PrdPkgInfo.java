package com.cmcc.vrp.ec.bean.gd;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("PrdPkgInfo")
public class PrdPkgInfo {

    @XStreamAlias("PrdCode")
    private String prdCode; //产品包编号
    
    @XStreamAlias("PrdOrdCode")
    private String prdOrdCode; //集团产品号码

    public String getPrdCode() {
        return prdCode;
    }

    public void setPrdCode(String prdCode) {
        this.prdCode = prdCode;
    }

    public String getPrdOrdCode() {
        return prdOrdCode;
    }

    public void setPrdOrdCode(String prdOrdCode) {
        this.prdOrdCode = prdOrdCode;
    }
 
}
