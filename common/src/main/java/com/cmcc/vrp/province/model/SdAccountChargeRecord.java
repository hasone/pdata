package com.cmcc.vrp.province.model;

import java.util.Date;
/**
 * 
 * @ClassName: SdAccountChargeRecord 
 * @Description: 山东预付费账户信息封装类
 * @author: Rowe
 * @date: 2017年9月4日 上午10:32:52
 */
public class SdAccountChargeRecord {

    private Long id;

    private String opr;

    private String type;

    private String pkgSeq;

    private String ecid;

    private String acctId;

    private String userId;

    private String productId;

    private Long pay;

    private Date oprEffTime;

    private String acctSeq;

    private String acctNo;

    private String paramName;

    private String paramValue;

    private String requestBody;

    private Date createTime;

    private Date updateTime;

    private Long platAccountId;

    private Integer status;

    private String message;

    private Integer deleteFlag;
    
    private Long changeDetailId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpr() {
        return opr;
    }

    public void setOpr(String opr) {
        this.opr = opr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPkgSeq() {
        return pkgSeq;
    }

    public void setPkgSeq(String pkgSeq) {
        this.pkgSeq = pkgSeq;
    }

    public String getEcid() {
        return ecid;
    }

    public void setEcid(String ecid) {
        this.ecid = ecid;
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Long getPay() {
        return pay;
    }

    public void setPay(Long pay) {
        this.pay = pay;
    }

    public Date getOprEffTime() {
        return oprEffTime;
    }

    public void setOprEffTime(Date oprEffTime) {
        this.oprEffTime = oprEffTime;
    }

    public String getAcctSeq() {
        return acctSeq;
    }

    public void setAcctSeq(String acctSeq) {
        this.acctSeq = acctSeq;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getPlatAccountId() {
        return platAccountId;
    }

    public void setPlatAccountId(Long platAccountId) {
        this.platAccountId = platAccountId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getChangeDetailId() {
        return changeDetailId;
    }

    public void setChangeDetailId(Long changeDetailId) {
        this.changeDetailId = changeDetailId;
    }

}