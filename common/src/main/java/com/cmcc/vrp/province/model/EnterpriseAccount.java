package com.cmcc.vrp.province.model;

import com.cmcc.vrp.exception.SelfCheckException;

import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:46:13
*/
public class EnterpriseAccount {
    private Long id;

    private Long entId;

    private Long prdId;

    private Long account;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    private Long version;

    /**
     * @param ea
     * @return
     * @throws SelfCheckException
     */
    public static boolean selfCheck(EnterpriseAccount ea) throws SelfCheckException {
        if (ea == null || ea.getAccount() == null || ea.getAccount() < 0
            || ea.getCreateTime() == null || ea.getDeleteFlag() == null
            || ea.getEntId() == null || ea.getPrdId() == null
            || ea.getVersion() == null) {
            throw new SelfCheckException();
        }

        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public Long getPrdId() {
        return prdId;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}