package com.cmcc.vrp.boss.jiangsu.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("operation_in")
public class QryOrderOperationIn {

    @XStreamAlias("process_code")
    private String processCode;
    
    @XStreamAlias("app_id")
    private String appId;
    
    @XStreamAlias("access_token")
    private String accessToken;
    
    @XStreamAlias("verify_code")
    private String verifyCode;
    
    @XStreamAlias("request_type")
    private String requestType;
    
    @XStreamAlias("sysfunc_id")
    private String sysfuncId;
    
    @XStreamAlias("organ_id")
    private String organId;
    
    @XStreamAlias("route_type")
    private String routeType;
    
    @XStreamAlias("route_value")
    private String routeValue;
    
    @XStreamAlias("operator_id")
    private String operatorId;
    
    @XStreamAlias("channelid")
    private String channelid;
    
    @XStreamAlias("request_time")
    private String requestTime;
    
    @XStreamAlias("request_seq")
    private String requestSeq;
    
    @XStreamAlias("request_source")
    private String requestSource;
    
    @XStreamAlias("request_target")
    private String requestTarget;
    
    @XStreamAlias("msg_version")
    private String msgVersion;
    
    @XStreamAlias("cont_version")
    private String contVersion;
    
    @XStreamAlias("user_passwd")
    private UserPasswd userPasswd;
    
    @XStreamAlias("perfcol")
    private Perfcol perfcol;
    
    @XStreamAlias("content")
    private QryOrderContent qryOrderContent;

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getSysfuncId() {
        return sysfuncId;
    }

    public void setSysfuncId(String sysfuncId) {
        this.sysfuncId = sysfuncId;
    }

    public String getOrganId() {
        return organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public String getRouteValue() {
        return routeValue;
    }

    public void setRouteValue(String routeValue) {
        this.routeValue = routeValue;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getRequestSeq() {
        return requestSeq;
    }

    public void setRequestSeq(String requestSeq) {
        this.requestSeq = requestSeq;
    }

    public String getRequestSource() {
        return requestSource;
    }

    public void setRequestSource(String requestSource) {
        this.requestSource = requestSource;
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public void setRequestTarget(String requestTarget) {
        this.requestTarget = requestTarget;
    }

    public String getMsgVersion() {
        return msgVersion;
    }

    public void setMsgVersion(String msgVersion) {
        this.msgVersion = msgVersion;
    }

    public String getContVersion() {
        return contVersion;
    }

    public void setContVersion(String contVersion) {
        this.contVersion = contVersion;
    }

    public UserPasswd getUserPasswd() {
        return userPasswd;
    }

    public void setUserPasswd(UserPasswd userPasswd) {
        this.userPasswd = userPasswd;
    }

    public Perfcol getPerfcol() {
        return perfcol;
    }

    public void setPerfcol(Perfcol perfcol) {
        this.perfcol = perfcol;
    }

    public QryOrderContent getQryOrderContent() {
        return qryOrderContent;
    }

    public void setQryOrderContent(QryOrderContent qryOrderContent) {
        this.qryOrderContent = qryOrderContent;
    }

}
