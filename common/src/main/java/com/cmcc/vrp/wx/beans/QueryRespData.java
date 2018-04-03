package com.cmcc.vrp.wx.beans;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年5月9日 下午3:53:51
*/
@XStreamAlias("QueryResp")
public class QueryRespData {
    @XStreamAlias("Code")
    String code;
    
    @XStreamAlias("EntCode")
    String entCode;
    
    @XStreamAlias("EntName")
    String entName;
    
    @XStreamAlias("SystemNum")
    String systemNum;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEntCode() {
        return entCode;
    }

    public void setEntCode(String entCode) {
        this.entCode = entCode;
    }

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName;
    }

    public String getSystemNum() {
        return systemNum;
    }

    public void setSystemNum(String systemNum) {
        this.systemNum = systemNum;
    }
    
}
