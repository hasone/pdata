package com.cmcc.vrp.ec.bean.individual;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年4月14日 下午1:49:07
*/
@XStreamAlias("OutData")
public class MemberInqueryRespData {
    
    @XStreamAlias("Mobile")
    private String mobile;
    
    @XStreamAlias("EffDate")
    private String effDate;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEffDate() {
        return effDate;
    }

    public void setEffDate(String effDate) {
        this.effDate = effDate;
    }

    
}
