package com.cmcc.vrp.province.model;

import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午3:00:37
*/
public class MdrcCardInfo {

    /**
     * 以下为基本属性，与数据库一一对应
     */
    private Long id;

    private Long configId;

    private String cardNumber;

    private String cardPassword;

    private Integer status;

    private Integer opStatus;

    private String userMobile;

    private String userIp;

    private Date deadline;

    private Date createTime;

    private Date storedTime;

    private Date activatedTime;

    private Date boundTime;

    private Date usedTime;

    private Date lockedTime;

    private Date unlockTime;

    private Date deleteTime;

    private Date extendTime;

    private Date deactivateTime;

    private Long enterId;

    private Long productId;

    private String chargeMsg;

    private Integer chargeStatus;

    private String requestSerialNumber;

    private String reponseSerialNumber;

    private String salt; //加密用的盐

    private Date startTime;

    private String clearPsw; //明文的密码，用来下载
       
    /**
     * 以下为扩展属性
     */
    private String productSize;

    //年份
    private int year;
    
    
    private String enterpriseName; //企业名称
    
    private Integer serialNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardPassword() {
        return cardPassword;
    }

    public void setCardPassword(String cardPassword) {
        this.cardPassword = cardPassword;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStoredTime() {
        return storedTime;
    }

    public void setStoredTime(Date storedTime) {
        this.storedTime = storedTime;
    }

    public Date getActivatedTime() {
        return activatedTime;
    }

    public void setActivatedTime(Date activatedTime) {
        this.activatedTime = activatedTime;
    }

    public Date getBoundTime() {
        return boundTime;
    }

    public void setBoundTime(Date boundTime) {
        this.boundTime = boundTime;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }

    public Date getLockedTime() {
        return lockedTime;
    }

    public void setLockedTime(Date lockedTime) {
        this.lockedTime = lockedTime;
    }

    public Long getEnterId() {
        return enterId;
    }

    public void setEnterId(Long enterId) {
        this.enterId = enterId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Date getUnlockTime() {
        return unlockTime;
    }

    public void setUnlockTime(Date unlockTime) {
        this.unlockTime = unlockTime;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Date getExtendTime() {
        return extendTime;
    }

    public void setExtendTime(Date extendTime) {
        this.extendTime = extendTime;
    }

    public Integer getOpStatus() {
        return opStatus;
    }

    public void setOpStatus(Integer opStatus) {
        this.opStatus = opStatus;
    }

    public Date getDeactivateTime() {
        return deactivateTime;
    }

    public void setDeactivateTime(Date deactivateTime) {
        this.deactivateTime = deactivateTime;
    }

    public String getChargeMsg() {
        return chargeMsg;
    }

    public void setChargeMsg(String chargeMsg) {
        this.chargeMsg = chargeMsg;
    }

    public Integer getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(Integer chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public String getRequestSerialNumber() {
        return requestSerialNumber;
    }

    public void setRequestSerialNumber(String requestSerialNumber) {
        this.requestSerialNumber = requestSerialNumber;
    }

    public String getReponseSerialNumber() {
        return reponseSerialNumber;
    }

    public void setReponseSerialNumber(String reponseSerialNumber) {
        this.reponseSerialNumber = reponseSerialNumber;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getClearPsw() {
        return clearPsw;
    }

    public void setClearPsw(String clearPsw) {
        this.clearPsw = clearPsw;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }
    
}
