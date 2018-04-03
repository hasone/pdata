/**
 *
 */
package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * <p>
 * Title:EntFlowControl
 * </p>
 * <p>
 * Description:   企业流控信息
 * </p>
 *
 * @author xujue
 * @date 2016年9月20日
 *  
 */
public class EntFlowControl {

    private Long enterId;

    private String name;

    private String code;

    private String cmManagerName; // 企业管理员的父节点

    private Long countUpper;

    private Long countNow;

    private Long countAddition;

    private Integer fcSmsFlag;

    private Long creatorId;

    private Long updatorId;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    public Long getEnterId() {
        return enterId;
    }

    public void setEnterId(Long enterId) {
        this.enterId = enterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCmManagerName() {
        return cmManagerName;
    }

    public void setCmManagerName(String cmManagerName) {
        this.cmManagerName = cmManagerName;
    }

    public Long getCountUpper() {
        return countUpper;
    }

    public void setCountUpper(Long countUpper) {
        this.countUpper = countUpper;
    }

    public Long getCountNow() {
        return countNow;
    }

    public void setCountNow(Long countNow) {
        this.countNow = countNow;
    }

    public Long getCountAddition() {
        return countAddition;
    }

    public void setCountAddition(Long countAddition) {
        this.countAddition = countAddition;
    }

    public Integer getFcSmsFlag() {
        return fcSmsFlag;
    }

    public void setFcSmsFlag(Integer fcSmsFlag) {
        this.fcSmsFlag = fcSmsFlag;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
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
