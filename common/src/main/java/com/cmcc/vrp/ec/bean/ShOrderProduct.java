package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("OrderProduct")
public class ShOrderProduct {

    @XStreamAlias("custServiceId")
    private String custServiceId;

    @XStreamAlias("offerId")
    private String offerId;

    @XStreamAlias("roleId")
    private String roleId;

    @XStreamAlias("orderType")
    private String orderType;

    @XStreamAlias("operateType")
    private String operateType;

    @XStreamAlias("orderName")
    private String orderName;

    @XStreamAlias("mainBillId")
    private String mainBillId;

    @XStreamAlias("corporateContactName")
    private String corporateContactName;

    @XStreamAlias("corporateContactTel")
    private String corporateContactTel;

    @XStreamAlias("depositedFee")
    private String depositedFee;

    @XStreamAlias("contractEffDate")
    private String contractEffDate;

    @XStreamAlias("contractExpDate")
    private String contractExpDate;

    @XStreamAlias("bbossInsOfferIds")
    private String bbossInsOfferId;
    
    @XStreamAlias("accId")
    private String accId;

    public String getCustServiceId() {
        return custServiceId;
    }

    public void setCustServiceId(String custServiceId) {
        this.custServiceId = custServiceId;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getMainBillId() {
        return mainBillId;
    }

    public void setMainBillId(String mainBillId) {
        this.mainBillId = mainBillId;
    }

    public String getCorporateContactName() {
        return corporateContactName;
    }

    public void setCorporateContactName(String corporateContactName) {
        this.corporateContactName = corporateContactName;
    }

    public String getCorporateContactTel() {
        return corporateContactTel;
    }

    public void setCorporateContactTel(String corporateContactTel) {
        this.corporateContactTel = corporateContactTel;
    }

    public String getDepositedFee() {
        return depositedFee;
    }

    public void setDepositedFee(String depositedFee) {
        this.depositedFee = depositedFee;
    }

    public String getContractEffDate() {
        return contractEffDate;
    }

    public void setContractEffDate(String contractEffDate) {
        this.contractEffDate = contractEffDate;
    }

    public String getContractExpDate() {
        return contractExpDate;
    }

    public void setContractExpDate(String contractExpDate) {
        this.contractExpDate = contractExpDate;
    }

    public String getBbossInsOfferId() {
        return bbossInsOfferId;
    }

    public void setBbossInsOfferId(String bbossInsOfferId) {
        this.bbossInsOfferId = bbossInsOfferId;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }
    
}
