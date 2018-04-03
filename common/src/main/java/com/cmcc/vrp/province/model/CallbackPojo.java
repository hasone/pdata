package com.cmcc.vrp.province.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 中奖记录
 * Created by nvarchar on 15/11/19.
 */
public class CallbackPojo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String mobile;
    private String wxOpenid;
    private String platName;
    private String activeId; //活动编码
    private Integer gameType;
    private String catName; //运行商名称
    private String rankName;
    private String prizeName;
    private String prizeSource; //奖品来源（省公司为奖品企业来源编码）
    private String prizeId; //奖品编码
    private String enterId;
    private Integer prizeCount; //流量池奖品数量
    private Integer prizeType; //奖品类型 0:流量池 | 1：流量包 | 2:其他奖品
    private Integer prizeResponse; //奖品兑换方式 0:稍后兑换 | 1：直接兑换（省公司为1）
    private Date validality; //奖品过期时间（自营平台使用）
    private String description; //活动名称
    private Date createTime;
    private Date updateTime;
    private Integer deleteFlag;
    private String serial;
    private Long recordId;
    private Long chargeRecordId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getGameType() {
        return gameType;
    }

    public void setGameType(Integer gameType) {
        this.gameType = gameType;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Integer getPrizeResponse() {
        return prizeResponse;
    }

    public void setPrizeResponse(Integer prizeResponse) {
        this.prizeResponse = prizeResponse;
    }

    public String getPrizeSource() {
        return prizeSource;
    }

    public void setPrizeSource(String prizeSource) {
        this.prizeSource = prizeSource;
    }

    public String getPlatName() {
        return platName;
    }

    public void setPlatName(String platName) {
        this.platName = platName;
    }

    public String getEnterId() {
        return enterId;
    }

    public void setEnterId(String enterId) {
        this.enterId = enterId;
    }

    public String getWxOpenid() {
        return wxOpenid;
    }

    public void setWxOpenid(String wxOpenid) {
        this.wxOpenid = wxOpenid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getActiveId() {
        return activeId;
    }

    public void setActiveId(String activeId) {
        this.activeId = activeId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public Date getValidality() {
        return validality;
    }

    public void setValidality(Date validality) {
        this.validality = validality;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getPrizeCount() {
        return prizeCount;
    }

    public void setPrizeCount(Integer prizeCount) {
        this.prizeCount = prizeCount;
    }

    public String getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(String prizeId) {
        this.prizeId = prizeId;
    }

    public Integer getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(Integer prizeType) {
        this.prizeType = prizeType;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getChargeRecordId() {
        return chargeRecordId;
    }

    public void setChargeRecordId(Long chargeRecordId) {
        this.chargeRecordId = chargeRecordId;
    }

    /**
     * 转化成string
     * */
    public String toString() {
        String returnString = "[id= " + getId()
            + ",mobile=" + getMobile()
            + ",wxOpenid=" + getWxOpenid()
            + ",platName=" + getPlatName()
            + ",activeId=" + getActiveId()
            + ",gameType=" + getGameType()
            + ",catName=" + getCatName()
            + ",rankName=" + getRankName()
            + ",prizeName=" + getPrizeName()
            + ",prizeSource=" + getPrizeSource()
            + ",prizeId=" + getPrizeId()
            + ",enterId=" + getEnterId()
            + ",prizeCount=" + getPrizeCount()
            + ",prizeType=" + getPrizeType()
            + ",prizeResponse=" + getPrizeResponse()
            + ",validality=" + getValidality()
            + ",description=" + getDescription()
            + ",createTime=" + getCreateTime()
            + ",updateTime=" + getUpdateTime()
            + ",deleteFlag=" + getDeleteFlag()
            + ",serial=" + getSerial()
            + ",recordId=" + getRecordId()
            + "chargeRecordId=" + getChargeRecordId()
            + "]";
        return returnString;
    }
}
