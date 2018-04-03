package com.cmcc.vrp.boss.guangdong.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/10/12.
 */
@XStreamAlias("Member")
public class GdRespMember {

    @XStreamAlias("Mobile")
    private String mobile;

    @XStreamAlias("ResultCode")
    private String resultCode;

    @XStreamAlias("ResultMsg")
    private String resultMsg;

    @XStreamAlias("CRMApplyCode")
    private String crmApplyCode;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getCrmApplyCode() {
        return crmApplyCode;
    }

    public void setCrmApplyCode(String crmApplyCode) {
        this.crmApplyCode = crmApplyCode;
    }
}
