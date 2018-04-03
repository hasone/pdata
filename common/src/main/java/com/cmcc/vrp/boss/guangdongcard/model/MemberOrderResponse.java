package com.cmcc.vrp.boss.guangdongcard.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 成员订购响应内容
 *
 * Created by sunyiwei on 2016/11/18.
 */
@XStreamAlias("MemberOrderResponse")
public class MemberOrderResponse {
    @XStreamAlias("ResultCode")
    private String resultCode;

    @XStreamAlias("ResultMessage")
    private String resultMessage;

    @XStreamAlias("SyAppCode")
    private String syAppCode;

    @XStreamAlias("CrmApplyCode")
    private String crmApplyCode;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getSyAppCode() {
        return syAppCode;
    }

    public void setSyAppCode(String syAppCode) {
        this.syAppCode = syAppCode;
    }

    public String getCrmApplyCode() {
        return crmApplyCode;
    }

    public void setCrmApplyCode(String crmApplyCode) {
        this.crmApplyCode = crmApplyCode;
    }
}
