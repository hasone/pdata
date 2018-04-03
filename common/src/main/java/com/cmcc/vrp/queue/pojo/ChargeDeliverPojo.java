/**
 *
 */
package com.cmcc.vrp.queue.pojo;

import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.province.model.PhoneRegion;

/**
 * <p>Title:ChargeDeliverPojo </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月11日
 */
public class ChargeDeliverPojo {

    private Long entId;

    private Long prdId;

    private String mobile;

    private String serialNum;

    private String type;//区分是否是EC充值，chargeType枚举

    private Long splPrdId;

    private String activityName;

    private Long recordId;

    private ActivityType activityType;

    private PhoneRegion phoneRegion; //手机号码归属地
    
    private Integer count = 1;//充值个数，默认为1,山东包月请自行设置


    /**
     * @return the recordId
     */
    public Long getRecordId() {
        return recordId;
    }

    /**
     * @param recordId the recordId to set
     */
    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    /**
     * @return the activityType
     */
    public ActivityType getActivityType() {
        return activityType;
    }

    /**
     * @param activityType the activityType to set
     */
    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    /**
     * @return the activityName
     */
    public String getActivityName() {
        return activityName;
    }

    /**
     * @param activityName the activityName to set
     */
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    /**
     * @return the splPrdId
     */
    public Long getSplPrdId() {
        return splPrdId;
    }

    /**
     * @param splPrdId the splPrdId to set
     */
    public void setSplPrdId(Long splPrdId) {
        this.splPrdId = splPrdId;
    }

    /**
     * @return the entId
     */
    public Long getEntId() {
        return entId;
    }

    /**
     * @param entId the entId to set
     */
    public void setEntId(Long entId) {
        this.entId = entId;
    }

    /**
     * @return the prdId
     */
    public Long getPrdId() {
        return prdId;
    }

    /**
     * @param prdId the prdId to set
     */
    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the serialNum
     */
    public String getSerialNum() {
        return serialNum;
    }

    /**
     * @param serialNum the serialNum to set
     */
    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the phoneRegion
     */
    public PhoneRegion getPhoneRegion() {
        return phoneRegion;
    }

    /**
     * @param phoneRegion the phoneRegion to set
     */
    public void setPhoneRegion(PhoneRegion phoneRegion) {
        this.phoneRegion = phoneRegion;
    }

    /**
     * getCount() 
     */
    public Integer getCount() {
        return count;
    }

    /**
     * setCount(Integer count)
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    
}
