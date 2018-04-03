package com.cmcc.vrp.ec.bean.gd;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("ECSyncInfoReq")
public class ECSyncInfoReq {

    @XStreamAlias("ECCode")
    private String ecCode;
    
    @XStreamAlias("OprCode")
    private String oprCode;
    
    @XStreamAlias("ECName")
    private String ecName;
    
    @XStreamAlias("Region")
    private String region;
    
    @XStreamAlias("LegalPerson")
    private String legalPerson;
    
    @XStreamAlias("EntPermit")
    private String entPermit;
       
    @XStreamImplicit(itemFieldName="ContactInfo")
    private List<ContactInfo> contactInfo;
    
    @XStreamAlias("ECLevel")
    private String ecLevel;
    
    @XStreamAlias("UnitKind")
    private String unitKind;
    
    @XStreamAlias("District")
    private String district;
    
    @XStreamAlias("InnetDate")
    private String innetDate;
    
    @XStreamAlias("VipType")
    private String vipType;
    
    @XStreamAlias("VipTypeStateDate")
    private String vipTypeStateDate;
    
    @XStreamAlias("CreditLevel")
    private String creditLevel;
    
    @XStreamAlias("DevChannel")
    private String devChannel;
    
    @XStreamAlias("DevUserId")
    private String devUserId;

    public String getEcCode() {
        return ecCode;
    }

    public void setEcCode(String ecCode) {
        this.ecCode = ecCode;
    }

    public String getOprCode() {
        return oprCode;
    }

    public void setOprCode(String oprCode) {
        this.oprCode = oprCode;
    }

    public String getEcName() {
        return ecName;
    }

    public void setEcName(String ecName) {
        this.ecName = ecName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getEntPermit() {
        return entPermit;
    }

    public void setEntPermit(String entPermit) {
        this.entPermit = entPermit;
    }

    public List<ContactInfo> getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(List<ContactInfo> contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getEcLevel() {
        return ecLevel;
    }

    public void setEcLevel(String ecLevel) {
        this.ecLevel = ecLevel;
    }

    public String getUnitKind() {
        return unitKind;
    }

    public void setUnitKind(String unitKind) {
        this.unitKind = unitKind;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getInnetDate() {
        return innetDate;
    }

    public void setInnetDate(String innetDate) {
        this.innetDate = innetDate;
    }

    public String getVipType() {
        return vipType;
    }

    public void setVipType(String vipType) {
        this.vipType = vipType;
    }

    public String getVipTypeStateDate() {
        return vipTypeStateDate;
    }

    public void setVipTypeStateDate(String vipTypeStateDate) {
        this.vipTypeStateDate = vipTypeStateDate;
    }

    public String getCreditLevel() {
        return creditLevel;
    }

    public void setCreditLevel(String creditLevel) {
        this.creditLevel = creditLevel;
    }

    public String getDevChannel() {
        return devChannel;
    }

    public void setDevChannel(String devChannel) {
        this.devChannel = devChannel;
    }

    public String getDevUserId() {
        return devUserId;
    }

    public void setDevUserId(String devUserId) {
        this.devUserId = devUserId;
    }

}
