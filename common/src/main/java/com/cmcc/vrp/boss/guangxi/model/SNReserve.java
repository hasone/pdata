package com.cmcc.vrp.boss.guangxi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/9/13.
 */
@XStreamAlias("SNReserve")
public class SNReserve {

    @XStreamAlias("TransIDC")
    private String transIDC;
    @XStreamAlias("ConvID")
    private String convID;
    @XStreamAlias("CutOffDay")
    private String cutOffDay;
    @XStreamAlias("OSNTime")
    private String oSNTime;
    @XStreamAlias("OSNDUNS")
    private String oSNDUNS;
    @XStreamAlias("HSNDUNS")
    private String hSNDUNS;
    @XStreamAlias("MsgSender")
    private String msgSender;
    @XStreamAlias("MsgReceiver")
    private String msgReceiver;
    @XStreamAlias("Priority")
    private String priority;
    @XStreamAlias("ServiceLevel")
    private String serviceLevel;


    public String getTransIDC() {
        return transIDC;
    }

    public void setTransIDC(String transIDC) {
        this.transIDC = transIDC;
    }

    public String getConvID() {
        return convID;
    }

    public void setConvID(String convID) {
        this.convID = convID;
    }

    public String getCutOffDay() {
        return cutOffDay;
    }

    public void setCutOffDay(String cutOffDay) {
        this.cutOffDay = cutOffDay;
    }

    /**
     * @return
     */
    public String getoSNTime() {
        return oSNTime;
    }

    /**
     * @param oSNTime
     */
    public void setoSNTime(String oSNTime) {
        this.oSNTime = oSNTime;
    }

    /**
     * @return
     */
    public String getoSNDUNS() {
        return oSNDUNS;
    }

    /**
     * @param oSNDUNS
     */
    public void setoSNDUNS(String oSNDUNS) {
        this.oSNDUNS = oSNDUNS;
    }

    /**
     * @return
     */
    public String gethSNDUNS() {
        return hSNDUNS;
    }

    /**
     * @param hSNDUNS
     */
    public void sethSNDUNS(String hSNDUNS) {
        this.hSNDUNS = hSNDUNS;
    }

    public String getMsgSender() {
        return msgSender;
    }

    public void setMsgSender(String msgSender) {
        this.msgSender = msgSender;
    }

    public String getMsgReceiver() {
        return msgReceiver;
    }

    public void setMsgReceiver(String msgReceiver) {
        this.msgReceiver = msgReceiver;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(String serviceLevel) {
        this.serviceLevel = serviceLevel;
    }
}
