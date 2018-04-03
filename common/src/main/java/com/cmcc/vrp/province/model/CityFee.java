package com.cmcc.vrp.province.model;

import java.util.Date;

public class CityFee {

    private Long id;

    private String cityCode;
  
    private String preFeeCode;

    private String feeCode;

    private String description;
    
    private Date createTime;

    private Date updateTime;

    private Integer deleteFlag;

    /**
     * 
     * @Title: getId 
     * @Description: TODO
     * @return
     * @return: Long
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     * @Title: setId 
     * @Description: TODO
     * @param id
     * @return: void
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * @Title: getCityCode 
     * @Description: TODO
     * @return
     * @return: String
     */
    public String getCityCode() {
        return cityCode;
    }

    /**
     * 
     * @Title: setCityCode 
     * @Description: TODO
     * @param cityCode
     * @return: void
     */
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    /**
     * 
     * @Title: getPreFeeCode 
     * @Description: TODO
     * @return
     * @return: String
     */
    public String getPreFeeCode() {
        return preFeeCode;
    }

    /**
     * 
     * @Title: setPreFeeCode 
     * @Description: TODO
     * @param preFeeCode
     * @return: void
     */
    public void setPreFeeCode(String preFeeCode) {
        this.preFeeCode = preFeeCode;
    }

    /**
     * 
     * @Title: getFeeCode 
     * @Description: TODO
     * @return
     * @return: String
     */
    public String getFeeCode() {
        return feeCode;
    }

    /**
     * 
     * @Title: setFeeCode 
     * @Description: TODO
     * @param feeCode
     * @return: void
     */
    public void setFeeCode(String feeCode) {
        this.feeCode = feeCode;
    }

    /**
     * 
     * @Title: getDescription 
     * @Description: TODO
     * @return
     * @return: String
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @Title: setDescription 
     * @Description: TODO
     * @param description
     * @return: void
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @Title: getCreateTime 
     * @Description: TODO
     * @return
     * @return: Date
     */
    public Date getCreateTime() {
        return createTime;
    }
    
    /**
     * 
     * @Title: setCreateTime 
     * @Description: TODO
     * @param createTime
     * @return: void
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 
     * @Title: getUpdateTime 
     * @Description: TODO
     * @return
     * @return: Date
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 
     * @Title: setUpdateTime 
     * @Description: TODO
     * @param updateTime
     * @return: void
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 
     * @Title: getDeleteFlag 
     * @Description: TODO
     * @return
     * @return: Integer
     */
    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    /**
     * 
     * @Title: setDeleteFlag 
     * @Description: TODO
     * @param deleteFlag
     * @return: void
     */
    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

  
}