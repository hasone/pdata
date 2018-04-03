package com.cmcc.vrp.boss.shangdong.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/3/30.
 */
@XStreamAlias("PubInfo")
public class OrderReqPubInfo {

    @XStreamAlias("Version")
    String version;

    @XStreamAlias("EnterpriseBossId")
    String enterpriseBossId;

    @XStreamAlias("VerifyCode")
    String verifyCode;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEnterpriseBossId() {
        return enterpriseBossId;
    }

    public void setEnterpriseBossId(String enterpriseBossId) {
        this.enterpriseBossId = enterpriseBossId;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
