package com.cmcc.vrp.province.model;


/**
 * ecApprovalRequest 用于山东的EC审批记录
 *
 */
public class EcApprovalRequest extends ApprovalRequest{
    
    private String ip1;

    private String ip2;

    private String ip3;

    private String callbackUrl;

    public String getIp1() {
        return ip1;
    }

    public void setIp1(String ip1) {
        this.ip1 = ip1;
    }

    public String getIp2() {
        return ip2;
    }

    public void setIp2(String ip2) {
        this.ip2 = ip2;
    }

    public String getIp3() {
        return ip3;
    }

    public void setIp3(String ip3) {
        this.ip3 = ip3;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
    
    /**
     * 初始化
     * 暂时不用反射实现
     */
    public static EcApprovalRequest init(ApprovalRequest req){
        EcApprovalRequest ecApprovalRequest = new EcApprovalRequest();
        ecApprovalRequest.setId(req.getId());
        ecApprovalRequest.setEntName(req.getEntName());
        ecApprovalRequest.setEntCode(req.getEntCode());
        ecApprovalRequest.setEntDistrictName(req.getEntDistrictName());
        ecApprovalRequest.setCreateTime(req.getCreateTime());
        ecApprovalRequest.setResult(req.getResult());
        return ecApprovalRequest;
    }

}
