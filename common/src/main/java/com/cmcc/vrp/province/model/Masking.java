package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * @author lgk8023
 *
 */
public class Masking {
    
    private Long id;
    
    private Long adminId;
    
    private Integer crowdfundingMask;
    
    private Integer mdrcMask;
    
    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Integer getCrowdfundingMask() {
        return crowdfundingMask;
    }

    public void setCrowdfundingMask(Integer crowdfundingMask) {
        this.crowdfundingMask = crowdfundingMask;
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

    public Integer getMdrcMask() {
        return mdrcMask;
    }

    public void setMdrcMask(Integer mdrcMask) {
        this.mdrcMask = mdrcMask;
    }
    
    

}
