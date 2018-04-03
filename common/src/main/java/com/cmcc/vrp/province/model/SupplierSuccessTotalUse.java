package com.cmcc.vrp.province.model;

import java.util.Date;
/**
 * 统计供应商成功充值的金额（以充值成功为统计依据）
 * 跑脚本进行统计，插入到相应表中
 * @author qinqinyan
 * */
public class SupplierSuccessTotalUse {
    private Long id;

    private Long supplierId;

    private Double totalUseMoney;

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

    public Double getTotalUseMoney() {
        return totalUseMoney;
    }

    public void setTotalUseMoney(Double totalUseMoney) {
        this.totalUseMoney = totalUseMoney;
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