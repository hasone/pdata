package com.cmcc.vrp.ec.bean.gd;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("ContactInfo")
public class ContactInfo {

    @XStreamAlias("UserName")
    private String userName;
    
    @XStreamAlias("Mobile")
    private String mobile;
    
    @XStreamAlias("EMail")
    private String email;
    
    @XStreamAlias("MainContact")
    private String mainContact;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMainContact() {
        return mainContact;
    }

    public void setMainContact(String mainContact) {
        this.mainContact = mainContact;
    }

}
