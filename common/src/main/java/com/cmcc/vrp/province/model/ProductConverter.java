package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * 产品转化，兼容前端和后端
 *
 */
public class ProductConverter {
    private Long id;

    private Long sourcePrdId;

    private Long destPrdId;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;
    
    private String sourcePrdName;
    
    private String destPrdName;
    
    private String sourcePrdCode;
    
    private Integer sourcePrdSize;
    
    private Integer sourcePrdPrice;
    
    private String sourceIsp;
    
    private Integer sourcePrdType;
    
    private Integer destPrdType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSourcePrdId() {
        return sourcePrdId;
    }

    public void setSourcePrdId(Long sourcePrdId) {
        this.sourcePrdId = sourcePrdId;
    }

    public Long getDestPrdId() {
        return destPrdId;
    }

    public void setDestPrdId(Long destPrdId) {
        this.destPrdId = destPrdId;
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

    public String getSourcePrdName() {
        return sourcePrdName;
    }

    public void setSourcePrdName(String sourcePrdName) {
        this.sourcePrdName = sourcePrdName;
    }

    public String getDestPrdName() {
        return destPrdName;
    }

    public void setDestPrdName(String destPrdName) {
        this.destPrdName = destPrdName;
    }

    public String getSourcePrdCode() {
        return sourcePrdCode;
    }

    public void setSourcePrdCode(String sourcePrdCode) {
        this.sourcePrdCode = sourcePrdCode;
    }

    public Integer getSourcePrdSize() {
        return sourcePrdSize;
    }

    public void setSourcePrdSize(Integer sourcePrdSize) {
        this.sourcePrdSize = sourcePrdSize;
    }

    public Integer getSourcePrdPrice() {
        return sourcePrdPrice;
    }

    public void setSourcePrdPrice(Integer sourcePrdPrice) {
        this.sourcePrdPrice = sourcePrdPrice;
    }

    public String getSourceIsp() {
        return sourceIsp;
    }

    public void setSourceIsp(String sourceIsp) {
        this.sourceIsp = sourceIsp;
    }

    public Integer getSourcePrdType() {
        return sourcePrdType;
    }

    public void setSourcePrdType(Integer sourcePrdType) {
        this.sourcePrdType = sourcePrdType;
    }

    public Integer getDestPrdType() {
        return destPrdType;
    }

    public void setDestPrdType(Integer destPrdType) {
        this.destPrdType = destPrdType;
    }
    
    
    
    
}