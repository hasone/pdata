package com.cmcc.vrp.boss.beijing.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/8/11.
 */
@XStreamAlias("RetInfo")
public class QueryRetInfo {

    @XStreamAlias("PhoneList")
    private PhoneList phoneList;

    @XStreamAlias("Total")
    private String total;

    public PhoneList getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(PhoneList phoneList) {
        this.phoneList = phoneList;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
