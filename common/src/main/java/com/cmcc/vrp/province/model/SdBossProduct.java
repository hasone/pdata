package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * 
 * @ClassName: SdBossProduct 
 * @Description: 山东产品列表
 * @author: Rowe
 * @date: 2017年8月31日 下午4:24:46
 */
public class SdBossProduct {

    private Long id;//自增ID

    private String name;//产品名称

    private String code;//产品编码

    private Long price;//产品价格，单位分

    private Long size;//产品大小，单位KB

    private Integer type;//产品类型

    private Date createTime;//创建时间

    private Date updateTime;//更新时间

    private Integer deleteFlag;//删除标识

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
     * @Title: getName 
     * @Description: TODO
     * @return
     * @return: String
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @Title: setName 
     * @Description: TODO
     * @param name
     * @return: void
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 
     * @Title: getCode 
     * @Description: TODO
     * @return
     * @return: String
     */
    public String getCode() {
        return code;
    }

    /**
     * 
     * @Title: setCode 
     * @Description: TODO
     * @param code
     * @return: void
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 
     * @Title: getPrice 
     * @Description: TODO
     * @return
     * @return: Long
     */
    public Long getPrice() {
        return price;
    }

    /**
     * 
     * @Title: setPrice 
     * @Description: TODO
     * @param price
     * @return: void
     */
    public void setPrice(Long price) {
        this.price = price;
    }

    /**
     * 
     * @Title: getSize 
     * @Description: TODO
     * @return
     * @return: Long
     */
    public Long getSize() {
        return size;
    }

    /**
     * 
     * @Title: setSize 
     * @Description: TODO
     * @param size
     * @return: void
     */
    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * 
     * @Title: getType 
     * @Description: TODO
     * @return
     * @return: Integer
     */
    public Integer getType() {
        return type;
    }

    /**
     * 
     * @Title: setType 
     * @Description: TODO
     * @param type
     * @return: void
     */
    public void setType(Integer type) {
        this.type = type;
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
     * @Title: getupdateTime 
     * @Description: TODO
     * @return
     * @return: Date
     */
    public Date getupdateTime() {
        return updateTime;
    }

    /**
     * 
     * @Title: setupdateTime 
     * @Description: TODO
     * @param updateTime
     * @return: void
     */
    public void setupdateTime(Date updateTime) {
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