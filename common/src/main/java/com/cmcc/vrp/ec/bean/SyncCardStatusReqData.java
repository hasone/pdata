package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("SyncCardStatus")
public class SyncCardStatusReqData {
    
    @XStreamAlias("StartTime")
    String startTime;
    
    @XStreamAlias("EndTime")
    String endTime;
    
    @XStreamAlias("Type")
    String type;
    
    @XStreamAlias("SerialNumber")
    String serialNumber;

    @XStreamAlias("Year")
    String year;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
    
}
