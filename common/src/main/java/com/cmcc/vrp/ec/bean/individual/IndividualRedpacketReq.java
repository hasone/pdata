package com.cmcc.vrp.ec.bean.individual;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * IndividualRedpacketReq.java
 * @author wujiamin
 * @date 2017年1月13日
 */
@XStreamAlias("CreateRequest")
public class IndividualRedpacketReq {
    @XStreamAlias("ActivityParam")
    private IndividualRedpacketActivityParam param;
    
    @XStreamAlias("SerialNum")
    private String ecSerialNumber;

    public IndividualRedpacketActivityParam getParam() {
        return param;
    }

    public void setParam(IndividualRedpacketActivityParam param) {
        this.param = param;
    }

    public String getEcSerialNumber() {
        return ecSerialNumber;
    }

    public void setEcSerialNumber(String ecSerialNumber) {
        this.ecSerialNumber = ecSerialNumber;
    }   
}
