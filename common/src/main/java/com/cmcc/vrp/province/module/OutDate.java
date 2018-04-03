package com.cmcc.vrp.province.module;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/6/28.
 */
@XStreamAlias("OUT_DATA")
public class OutDate {

    @XStreamAlias("PHONE_NO")
    String phoneNo;

    @XStreamAlias("GPRS_CNT_INFO")
    GprsCntInfo gprsCntInfo;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public GprsCntInfo getGprsCntInfo() {
        return gprsCntInfo;
    }

    public void setGprsCntInfo(GprsCntInfo gprsCntInfo) {
        this.gprsCntInfo = gprsCntInfo;
    }
}
