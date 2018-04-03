package com.cmcc.vrp.province.model;

import java.util.Date;
/**
 * 统计供应商产品每天成功充值对象（以充值成功统计）
 * 跑脚本进行统计，插入到相应表中
 * @author qinqinyan
 * */
public class SupplierProdSuccessUsePerDay {
    private Long id;

    private Long supplierProductId;

    private Double useMoney;

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

    public Double getUseMoney() {
        return useMoney;
    }

    public void setUseMoney(Double useMoney) {
        this.useMoney = useMoney;
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