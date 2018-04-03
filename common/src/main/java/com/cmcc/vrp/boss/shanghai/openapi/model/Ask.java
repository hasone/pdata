package com.cmcc.vrp.boss.shanghai.openapi.model;

import java.util.Date;

import com.cmcc.vrp.boss.shanghai.openapi.util.JsonUtil;


/**
 * @author yan.jy@primeton.com createTime锛�2014骞�7鏈�22鏃�
 * 
 *         绛惧悕瀵嗛挜妯″瀷
 */
public class Ask {

    /**
     * 鍏挜鍊�
     */
    private String publicKeyStr;

    /**
     * 绉侀挜鍊�
     */
    private String privateKeyStr;

    /**
     * 鍓╀綑鏈夋晥鏈熸椂闂� 鍗曚綅姣
     */
    private Long restMills;

    /**
     * 澶辨晥鏃堕棿
     */
    private Date endTime;

    /**
     * 鑾峰緱澶辨晥鏃堕棿锛岀涓�娆¤幏鍙栨椂閫氳繃鍓╀綑鏃堕棿鐢熸垚
     * 
     * @return
     */
    public Date getEndTime() {
        if (endTime == null) {
            endTime = new Date(System.currentTimeMillis() + restMills);
        }
        return endTime;
    }

    /**
     *  (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return JsonUtil.toJsonString(this);
    }

    public String getPublicKeyStr() {
        return publicKeyStr;
    }

    public void setPublicKeyStr(String publicKeyStr) {
        this.publicKeyStr = publicKeyStr;
    }

    public String getPrivateKeyStr() {
        return privateKeyStr;
    }

    public void setPrivateKeyStr(String privateKeyStr) {
        this.privateKeyStr = privateKeyStr;
    }

    public void setRestMills(Long restMills) {
        this.restMills = restMills;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

}
