package com.cmcc.vrp.province.mdrc.model;

import com.cmcc.vrp.province.mdrc.enums.DateTimeConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.util.Date;

/**
 * Created by sunyiwei on 2016/6/1.
 */
@XStreamAlias("Request")
public class MdrcEcRequest {
    @XStreamAlias("Datetime")
    @XStreamConverter(DateTimeConverter.class)
    private Date requestTime;

    @XStreamAlias("Card")
    private MdrcEcRequestData mdrcEcRequestData;

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public MdrcEcRequestData getMdrcEcRequestData() {
        return mdrcEcRequestData;
    }

    public void setMdrcEcRequestData(MdrcEcRequestData mdrcEcRequestData) {
        this.mdrcEcRequestData = mdrcEcRequestData;
    }
}
