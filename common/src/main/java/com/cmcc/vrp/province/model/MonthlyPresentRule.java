package com.cmcc.vrp.province.model;

import java.util.Date;
import java.util.List;

/**
 * 
 * @ClassName: MonthlyPresentRule 
 * @Description: TODO
 * @author: Rowe
 * @date: 2017年7月18日 下午4:16:02
 */
public class MonthlyPresentRule {

    private Long id;

    private Long entId;

    private Integer total;

    private Integer status;

    private Integer monthCount;

    private Date startTime;

    private Date endTime;

    private Date createTime;

    private Date updateTime;

    private Long creatorId;

    private Long updaterId;

    private Integer deleteFlag;

    private Integer version;

    private Integer useSms;

    private Long smsTemplateId;

    private String activityName;
    
    private Integer givenCount;
    
    private Long prdId;

    //以下为扩展属性
    private String entName;

    private String entCode;

    private String productName;

    private String productCode;
    
    private Long productSize;

    private String phones;//前端手机号

    private List<String> phonesList;//处理后的手机号码

    private String size;//封装前端传过来的参数，单位为MB，元或者个

    private Integer activityType;//活动类型，单个赠送or批量赠送
    
    /**
     * 拓展activityCreator
     */
    private ActivityCreator activityCreator;

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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(Integer monthCount) {
        this.monthCount = monthCount;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Long updaterId) {
        this.updaterId = updaterId;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getUseSms() {
        return useSms;
    }

    public void setUseSms(Integer useSms) {
        this.useSms = useSms;
    }

    public Long getSmsTemplateId() {
        return smsTemplateId;
    }

    public void setSmsTemplateId(Long smsTemplateId) {
        this.smsTemplateId = smsTemplateId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName == null ? null : activityName.trim();
    }

    public Integer getGivenCount() {
        return givenCount;
    }

    public void setGivenCount(Integer givenCount) {
        this.givenCount = givenCount;
    }

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName;
    }

    public String getEntCode() {
        return entCode;
    }

    public void setEntCode(String entCode) {
        this.entCode = entCode;
    }

    public Long getPrdId() {
        return prdId;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Long getProductSize() {
        return productSize;
    }

    public void setProductSize(Long productSize) {
        this.productSize = productSize;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public List<String> getPhonesList() {
        return phonesList;
    }

    public void setPhonesList(List<String> phonesList) {
        this.phonesList = phonesList;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getActivityType() {
        return activityType;
    }

    public void setActivityType(Integer activityType) {
        this.activityType = activityType;
    }

    public ActivityCreator getActivityCreator() {
        return activityCreator;
    }

    public void setActivityCreator(ActivityCreator activityCreator) {
        this.activityCreator = activityCreator;
    }
    
    
}