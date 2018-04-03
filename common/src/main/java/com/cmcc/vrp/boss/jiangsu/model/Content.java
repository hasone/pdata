package com.cmcc.vrp.boss.jiangsu.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("content")
public class Content {
    @XStreamAlias("msisdn")
    private String msisdn;
    
    @XStreamAlias("user_id")
    private String userId;
    
    @XStreamAlias("package_user_id")
    private PackageUserId packageUserId;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PackageUserId getPackageUserId() {
        return packageUserId;
    }

    public void setPackageUserId(PackageUserId packageUserId) {
        this.packageUserId = packageUserId;
    }
}
