package com.cmcc.vrp.province.model;
/**
 * 激活审核详情类
 * */
public class MdrcActiveDetail {
    private Long id;

    private Long requestId;//激活请求ID

    private Long configId;//规则ID

    private String startCardNumber;//起始卡号

    private String endCardNumber;//终止卡号

    private Integer count;//激活数量

    private String image;

    private String imageKey;

    private Integer deleteFlag;

    private Long entId;//企业ID

    private Long templateId;//模板ID

    private Long productId;//产品ID
    
    /**
     * 以下为扩展属性
     */
    private Integer result;//审批状态
    
    private String serialNumber;//批次号
    
    private Long amount;//制卡数量

    private Integer activeStatus;
    
    private String configName;//卡名称

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public String getStartCardNumber() {
        return startCardNumber;
    }

    public void setStartCardNumber(String startCardNumber) {
        this.startCardNumber = startCardNumber == null ? null : startCardNumber.trim();
    }

    public String getEndCardNumber() {
        return endCardNumber;
    }

    public void setEndCardNumber(String endCardNumber) {
        this.endCardNumber = endCardNumber == null ? null : endCardNumber.trim();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey == null ? null : imageKey.trim();
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }
}