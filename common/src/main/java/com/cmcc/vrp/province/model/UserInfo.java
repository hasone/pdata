package com.cmcc.vrp.province.model;

import com.cmcc.vrp.exception.SelfCheckException;
import com.cmcc.vrp.util.StringUtils;

import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午3:25:47
*/
public class UserInfo {
    private Long id;

    private String mobile;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    /*
     * 自我检查各属性值是否有效
     */
    /**
     * @param userInfo
     * @throws SelfCheckException
     */
    public static void selfCheck(UserInfo userInfo) throws SelfCheckException {
        if (userInfo == null
            || !StringUtils.isValidMobile(userInfo.getMobile())
            || userInfo.getCreateTime() == null || userInfo.getDeleteFlag() == null) {
            throw new SelfCheckException();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
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