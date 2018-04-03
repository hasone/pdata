package com.cmcc.vrp.province.model;

/**
 * <p>Title:EcApprovalDetail </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年10月24日
*/
public class EcApprovalDetail {
    private Long id;

    private Long requestId;

    private Long entId;

    private String ip1;

    private String ip2;

    private String ip3;

    private String callbackUrl;

    private Integer deleteFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public String getIp1() {
        return ip1;
    }

    public void setIp1(String ip1) {
        this.ip1 = ip1 == null ? null : ip1.trim();
    }

    public String getIp2() {
        return ip2;
    }

    public void setIp2(String ip2) {
        this.ip2 = ip2 == null ? null : ip2.trim();
    }

    public String getIp3() {
        return ip3;
    }

    public void setIp3(String ip3) {
        this.ip3 = ip3 == null ? null : ip3.trim();
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl == null ? null : callbackUrl.trim();
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}