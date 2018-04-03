package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("Info")
public class ShEnterpriseInfo {
    @XStreamAlias("EnterCode")
    private String enterCode;
    
    @XStreamAlias("Name")
    private String name;
    
    @XStreamAlias("Province")
    private String province;
    
    @XStreamAlias("City")
    private String city;
    
    @XStreamAlias("County")
    private String county;
    
    @XStreamAlias("AccountManagerName")
    private String accountManagerName;
    
    @XStreamAlias("AccountManagerTel")
    private String accountManagerTel;
    
    @XStreamAlias("EntManagerName")
    private String entManagerName;
    
    @XStreamAlias("EntManagerTel")
    private String entManagerTel;

    public String getEnterCode() {
        return enterCode;
    }

    public void setEnterCode(String enterCode) {
        this.enterCode = enterCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getAccountManagerName() {
        return accountManagerName;
    }

    public void setAccountManagerName(String accountManagerName) {
        this.accountManagerName = accountManagerName;
    }

    public String getAccountManagerTel() {
        return accountManagerTel;
    }

    public void setAccountManagerTel(String accountManagerTel) {
        this.accountManagerTel = accountManagerTel;
    }

    public String getEntManagerName() {
        return entManagerName;
    }

    public void setEntManagerName(String entManagerName) {
        this.entManagerName = entManagerName;
    }

    public String getEntManagerTel() {
        return entManagerTel;
    }

    public void setEntManagerTel(String entManagerTel) {
        this.entManagerTel = entManagerTel;
    }


    
}
