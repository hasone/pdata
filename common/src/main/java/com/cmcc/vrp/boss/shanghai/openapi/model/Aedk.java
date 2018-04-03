package com.cmcc.vrp.boss.shanghai.openapi.model;

import java.util.Date;

import com.cmcc.vrp.boss.shanghai.openapi.util.JsonUtil;


/**
 * @author yan.jy@primeton.com createTime锛�2014骞�7鏈�22鏃�
 * 
 *         鍔犺В瀵嗗瘑閽ユā鍨�
 */
public class Aedk {

    /**
     * 鍔犺В瀵嗗瘑閽d
     */
    private Long id;

    /**
     * 搴旂敤缂栫爜
     */
    private String appCode;

    /**
     * 瀵嗛挜绠楁硶
     */
    private String algorithm;

    /**
     * 瀵嗛挜鍊�
     */
    private String value;

    /**
     * 澶辨晥鏃堕棿
     */
    private Date endTime;

    /**
     * 鏈夋晥鏈熷墿浣欐椂闂� 锛� expireTime - currentTime
     */
    private Long restTime;

    public Aedk() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public Long getRestTime() {
        return restTime;
    }

    public void setRestTime(Long restTime) {
        this.restTime = restTime;
    }

    /**
     *  (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return JsonUtil.toJsonString(this);
    }

    public Date getEndTime() {
        if (endTime == null) {
            endTime = new Date(System.currentTimeMillis() + restTime);
        }
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}