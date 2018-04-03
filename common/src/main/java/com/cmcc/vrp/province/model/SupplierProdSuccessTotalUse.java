package com.cmcc.vrp.province.model;

import java.util.Date;
/**
 * 供应商产品成功充值总额度对象（以成功充值为统计依据）
 * 跑脚本进行统计，插入到相应表中
 * @author qinqinyan
 * */
public class SupplierProdSuccessTotalUse {
    private Long id;

    private Long supplierProductId;

    private Double totalUse;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSupplierProductId() {
        return supplierProductId;
    }

    public void setSupplierProductId(Long supplierProductId) {
        this.supplierProductId = supplierProductId;
    }

    public Double getTotalUse() {
        return totalUse;
    }

    public void setTotalUse(Double totalUse) {
        this.totalUse = totalUse;
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