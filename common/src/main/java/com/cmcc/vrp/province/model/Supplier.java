package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author lgk8023
 * @date 2017年1月22日 下午3:11:19
 */
public class Supplier {
    private Long id;

    private String name;

    private String isp;

    private String enterName;

    private String enterCode;

    private String fingerprint;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    private Integer sync;

    private Integer status; // 供应商上下架状态。1：上架；0：下架

    private String payType;// 付款方式

    private String contractCode; // 合同编码

    private Double limitMoney; // 全量每日限额（开发理解：就是该供应商每日充值限制额度）,单位分，-1则不限额

    private Integer limitMoneyFlag; // 全量限额标志位，0：不开启；1:开启
    
    private Date limitUpdateTime; //操作时间，记录包括供应商限额或者供应商产品限额修改的操作

    // extend
    private Integer productLimitMoneyFlag; // 是否有产品控制
    
    private Double nowUsedMoney; //已使用金额
    
    private Integer isQueryChargeResult; //是否有充值结果查询接口

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp == null ? null : isp.trim();
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

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getSync() {
        return sync;
    }

    public void setSync(Integer sync) {
        this.sync = sync;
    }

    public String getEnterName() {
        return enterName;
    }

    public void setEnterName(String enterName) {
        this.enterName = enterName;
    }

    public String getEnterCode() {
        return enterCode;
    }

    public void setEnterCode(String enterCode) {
        this.enterCode = enterCode;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Double getLimitMoney() {
        return limitMoney;
    }

    public void setLimitMoney(Double limitMoney) {
        this.limitMoney = limitMoney;
    }

    public Integer getLimitMoneyFlag() {
        return limitMoneyFlag;
    }

    public void setLimitMoneyFlag(Integer limitMoneyFlag) {
        this.limitMoneyFlag = limitMoneyFlag;
    }


    public Integer getProductLimitMoneyFlag() {
        return productLimitMoneyFlag;
    }

    public void setProductLimitMoneyFlag(Integer productLimitMoneyFlag) {
        this.productLimitMoneyFlag = productLimitMoneyFlag;
    }

    public Date getLimitUpdateTime() {
        return limitUpdateTime;
    }

    public void setLimitUpdateTime(Date limitUpdateTime) {
        this.limitUpdateTime = limitUpdateTime;
    }

    public Double getNowUsedMoney() {
        return nowUsedMoney;
    }

    public void setNowUsedMoney(Double nowUsedMoney) {
        this.nowUsedMoney = nowUsedMoney;
    }

    public Integer getIsQueryChargeResult() {
        return isQueryChargeResult;
    }

    public void setIsQueryChargeResult(Integer isQueryChargeResult) {
        this.isQueryChargeResult = isQueryChargeResult;
    }

}
