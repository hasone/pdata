package com.cmcc.vrp.province.module;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/6/28.
 */
@XStreamAlias("ROOT")
public class PersonalFlowReq {

    @XStreamAlias("PHONE_NO")
    String phoneNo;

    @XStreamAlias("LOGIN_NO")
    String loginNo;

    @XStreamAlias("QRY_TYPE")
    String qryType;

    @XStreamAlias("YEAR_MONTH")
    String yearMonth;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getQryType() {
        return qryType;
    }

    public void setQryType(String qryType) {
        this.qryType = qryType;
    }

    public String getLoginNo() {
        return loginNo;
    }

    public void setLoginNo(String loginNo) {
        this.loginNo = loginNo;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }
}
