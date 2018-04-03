package com.cmcc.vrp.boss.jiangsu.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("cplanpackagedt")
public class Cplanpackagedt {
    @XStreamAlias("package_type")
    private String packageType;
    
    @XStreamAlias("package_code")
    private String packageCode;
    
    @XStreamAlias("package_old_code")
    private String packageOldCode;
    
    @XStreamAlias("package_inure_mode")
    private String packageInureMode;
    
    @XStreamAlias("package_cmd_code")
    private String packageCmdCode;
    
    @XStreamAlias("package_add_attr")
    private String packageAddAttr;
    

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getPackageOldCode() {
        return packageOldCode;
    }

    public void setPackageOldCode(String packageOldCode) {
        this.packageOldCode = packageOldCode;
    }

    public String getPackageInureMode() {
        return packageInureMode;
    }

    public void setPackageInureMode(String packageInureMode) {
        this.packageInureMode = packageInureMode;
    }

    public String getPackageCmdCode() {
        return packageCmdCode;
    }

    public void setPackageCmdCode(String packageCmdCode) {
        this.packageCmdCode = packageCmdCode;
    }

    public String getPackageAddAttr() {
        return packageAddAttr;
    }

    public void setPackageAddAttr(String packageAddAttr) {
        this.packageAddAttr = packageAddAttr;
    }

}
