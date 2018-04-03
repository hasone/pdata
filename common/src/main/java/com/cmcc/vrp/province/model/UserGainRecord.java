package com.cmcc.vrp.province.model;

import com.cmcc.vrp.exception.SelfCheckException;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午3:25:18
*/
public class UserGainRecord {
    private Long id;

    private Long entId;

    private Long userId;

    private Long size;

    private Long prdId;

    private Integer status;

    private String sourceName;

    private String extendParam;

    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    /**
     * @param record
     * @return
     * @throws SelfCheckException
     */
    public static boolean selfCheck(UserGainRecord record) throws SelfCheckException {
        if (record == null
            || record.getStatus() == null
            || record.getCreateTime() == null
            || record.getDeleteFlag() == null || record.getEntId() == null
            || StringUtils.isBlank(record.getSourceName())
            || record.getUserId() == null) {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPrdId() {
        return prdId;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName == null ? null : sourceName.trim();
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

    public String getExtendParam() {
        return extendParam;
    }

    public void setExtendParam(String extendParam) {
        this.extendParam = extendParam;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}