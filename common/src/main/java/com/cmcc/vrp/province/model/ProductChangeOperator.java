/**
 *
 */
package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * <p>Title:ProductChangeOperator </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年7月21日
 */
public class ProductChangeOperator {

    private Long id;

    private Long enterId;

    private Long prdId;

    private int operator;//操作：0-删除;1-增加;2-变更折扣,3-产品模板变更

    private int discount;

    private int deleteFlag;
    
    private Long oldProductTemplateId;//变更前企业使用的产品模板
    
    private Long newProductTemplateId; //变更后企业的产品模板id

    private Date createTime;
    
    private Date updateTime;
    //extended

    private String prdName;

    private String prdCode;

    private int price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getEnterId() {
        return enterId;
    }

    public void setEnterId(Long enterId) {
        this.enterId = enterId;
    }

    public Long getPrdId() {
        return prdId;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    public int getOperator() {
        return operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getOldProductTemplateId() {
        return oldProductTemplateId;
    }

    public void setOldProductTemplateId(Long oldProductTemplateId) {
        this.oldProductTemplateId = oldProductTemplateId;
    }

    public Long getNewProductTemplateId() {
        return newProductTemplateId;
    }

    public void setNewProductTemplateId(Long newProductTemplateId) {
        this.newProductTemplateId = newProductTemplateId;
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

    public String getPrdName() {
        return prdName;
    }

    public void setPrdName(String prdName) {
        this.prdName = prdName;
    }

    public String getPrdCode() {
        return prdCode;
    }

    public void setPrdCode(String prdCode) {
        this.prdCode = prdCode;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
