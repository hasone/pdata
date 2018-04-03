package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("Response")
public class UploadFileResp {
    @XStreamAlias("Datetime")
    String responseTime;

    @XStreamAlias("FileInfo")
    UploadFileRespData uploadFileRespData;

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public UploadFileRespData getUploadFileRespData() {
        return uploadFileRespData;
    }

    public void setUploadFileRespData(UploadFileRespData uploadFileRespData) {
        this.uploadFileRespData = uploadFileRespData;
    }
}
