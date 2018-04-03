package com.cmcc.vrp.wx.beans;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年5月9日 下午3:37:22
*/
@XStreamAlias("QueryInfo")
public class QueryInfoReqData {
    @XStreamAlias("SystemNum")
    String systemNum;
    
    @XStreamAlias("Mobile")
    String mobile;

    public String getSystemNum() {
        return systemNum;
    }

    public void setSystemNum(String systemNum) {
        this.systemNum = systemNum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
