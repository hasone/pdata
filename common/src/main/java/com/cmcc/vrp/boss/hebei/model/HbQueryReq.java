package com.cmcc.vrp.boss.hebei.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/10/11.
 */
@XStreamAlias("IntMsg")
public class HbQueryReq {

    @XStreamAlias("CommandID")
    private String commandID;

    @XStreamAlias("ProcessTime")
    private String processTime;

    @XStreamAlias("ProvinceId")
    private String provinceId;

    @XStreamAlias("OperId")
    private String operId;

    @XStreamAlias("Password")
    private String password;

    @XStreamAlias("Channel")
    private String channel;

    @XStreamAlias("TestFlag")
    private String testFlag;

    @XStreamAlias("SvcCont")
    private String SvcCont;

    public String getCommandID() {
        return commandID;
    }

    public void setCommandID(String commandID) {
        this.commandID = commandID;
    }

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getOperId() {
        return operId;
    }

    public void setOperId(String operId) {
        this.operId = operId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTestFlag() {
        return testFlag;
    }

    public void setTestFlag(String testFlag) {
        this.testFlag = testFlag;
    }

    public String getSvcCont() {
        return SvcCont;
    }

    public void setSvcCont(String svcCont) {
        SvcCont = svcCont;
    }
}
