package com.cmcc.vrp.boss.jiangsu.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("operation_in")
public class OperationIn {
    @XStreamAlias("process_code")
    private String processCode;
    
    @XStreamAlias("app_id")
    private String appId;
    
    @XStreamAlias("access_token")
    private String accessToken;
    
    @XStreamAlias("sign")
    private String sign;
    
    @XStreamAlias("verify_code")
    private String verifyCode;
    
    @XStreamAlias("request_type")
    private String requestType;
    
    @XStreamAlias("sysfunc_id")
    private String sysfuncId;
    
    @XStreamAlias("operator_id")
    private String operatorId;
    
    @XStreamAlias("organ_id")
    private String organId;
    
    @XStreamAlias("route_type")
    private String routeType;
    
    @XStreamAlias("route_value")
    private String routeValue;
    
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
    private String userPasswd;
    
    @XStreamAlias("operator_ip")
    private String operatorIp;
    
    @XStreamAlias("login_msisdn")
    private String loginMsisdn;
    
    @XStreamAlias("content")
    private Content content;

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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
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

    public String getUserPasswd() {
        return userPasswd;
    }

    public void setUserPasswd(String userPasswd) {
        this.userPasswd = userPasswd;
    }

    public String getOperatorIp() {
        return operatorIp;
    }

    public void setOperatorIp(String operatorIp) {
        this.operatorIp = operatorIp;
    }

    public String getLoginMsisdn() {
        return loginMsisdn;
    }

    public void setLoginMsisdn(String loginMsisdn) {
        this.loginMsisdn = loginMsisdn;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

}
