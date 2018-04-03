package com.cmcc.vrp.boss.jiangsu.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("operation_out")
public class OperationOut {
    @XStreamAlias("process_code")
    private String processCode;
    
    @XStreamAlias("sysfunc_id")
    private String sysfuncId;
    
    @XStreamAlias("response_time")
    private String responseTime;
    
    @XStreamAlias("request_source")
    private String requestSource;
    
    @XStreamAlias("request_seq")
    private String requestSeq;
    
    @XStreamAlias("response_seq")
    private String responseSeq;
    
    @XStreamAlias("request_type")
    private String requestType;
    
    @XStreamAlias("emergency_status")
    private String emergencyStatus;
    
    @XStreamAlias("response")
    private Response response;
    
    @XStreamAlias("content")
    private ResponseContent content;

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public String getSysfuncId() {
        return sysfuncId;
    }

    public void setSysfuncId(String sysfuncId) {
        this.sysfuncId = sysfuncId;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getRequestSource() {
        return requestSource;
    }

    public void setRequestSource(String requestSource) {
        this.requestSource = requestSource;
    }

    public String getRequestSeq() {
        return requestSeq;
    }

    public void setRequestSeq(String requestSeq) {
        this.requestSeq = requestSeq;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getEmergencyStatus() {
        return emergencyStatus;
    }

    public void setEmergencyStatus(String emergencyStatus) {
        this.emergencyStatus = emergencyStatus;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public ResponseContent getContent() {
        return content;
    }

    public void setContent(ResponseContent content) {
        this.content = content;
    }

    public String getResponseSeq() {
        return responseSeq;
    }

    public void setResponseSeq(String responseSeq) {
        this.responseSeq = responseSeq;
    }

}
