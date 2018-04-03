package com.cmcc.vrp.boss.jiangsu.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("package_user_id")
public class PackageUserId {
    @XStreamAlias("cplanpackagedt")
    private Cplanpackagedt cplanpackagedt;

    public Cplanpackagedt getCplanpackagedt() {
        return cplanpackagedt;
    }

    public void setCplanpackagedt(Cplanpackagedt cplanpackagedt) {
        this.cplanpackagedt = cplanpackagedt;
    }
}
