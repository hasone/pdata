package com.cmcc.vrp.province.model;

import java.util.Date;
/**
 * 统计供应商每天发送充值请求的金额（以成功发送请求充值为统计依据）
 * @author qinqinyan
 * */
public class SupplierReqUsePerDay {
    private Long id;

    private Long supplierId;

    private Double usedMoney;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Double getUsedMoney() {
        return usedMoney;
    }

    public void setUsedMoney(Double usedMoney) {
        this.usedMoney = usedMoney;
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
}