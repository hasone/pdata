package com.cmcc.vrp.province.model;

import com.cmcc.vrp.exception.SelfCheckException;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午3:11:00
*/
public class SmsRecord {
    private Long id;

    private String mobile;

    private String content;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    /**
     * @param record
     * @return
     * @throws SelfCheckException
     */
    public static boolean selfCheck(SmsRecord record) throws SelfCheckException {
        if (record == null
            || StringUtils.isBlank(record.getContent())
            || !com.cmcc.vrp.util.StringUtils.isValidMobile(record.getMobile())
            || record.getCreateTime() == null
            || record.getDeleteFlag() == null) {
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
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