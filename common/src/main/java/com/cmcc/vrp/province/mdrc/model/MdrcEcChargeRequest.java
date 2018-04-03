package com.cmcc.vrp.province.mdrc.model;

import com.cmcc.vrp.province.mdrc.enums.DateTimeConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.util.Date;

/**
 * Created by sunyiwei on 2016/6/21.
 */
@XStreamAlias("Request")
public class MdrcEcChargeRequest {
    @XStreamAlias("Datetime")
    @XStreamConverter(DateTimeConverter.class)
    private Date requestTime;

    @XStreamAlias("Card")
    private MdrcEcChargeRequestData mdrcEcChargeRequestData;

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public MdrcEcChargeRequestData getMdrcEcChargeRequestData() {
        return mdrcEcChargeRequestData;
    }

    public void setMdrcEcChargeRequestData(MdrcEcChargeRequestData mdrcEcChargeRequestData) {
        this.mdrcEcChargeRequestData = mdrcEcChargeRequestData;
    }
}
