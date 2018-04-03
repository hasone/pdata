package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("SyncResult")
public class SyncProRespData {
    @XStreamAlias("SyncCode")
	String syncCode;
	
    @XStreamAlias("SyncInfo")
	String syncInfo;

    public String getSyncCode() {
        return syncCode;
    }

    public void setSyncCode(String syncCode) {
        this.syncCode = syncCode;
    }

    public String getSyncInfo() {
        return syncInfo;
    }

    public void setSyncInfo(String syncInfo) {
        this.syncInfo = syncInfo;
    }

}
