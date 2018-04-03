package com.cmcc.vrp.province.mdrc.model;

import com.cmcc.vrp.province.mdrc.enums.DateTimeConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.util.Date;

/**
 * Created by sunyiwei on 2016/6/1.
 */
@XStreamAlias("Response")
public class MdrcEcChargeResponse {
    @XStreamAlias("DateTime")
    @XStreamConverter(DateTimeConverter.class)
    private Date dateTime;

    @XStreamAlias("Card")
    private MdrcEcChargeResponseData mdrcEcResponseData;

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public MdrcEcChargeResponseData getMdrcEcResponseData() {
        return mdrcEcResponseData;
    }

    public void setMdrcEcResponseData(MdrcEcChargeResponseData mdrcEcResponseData) {
        this.mdrcEcResponseData = mdrcEcResponseData;
    }
}
