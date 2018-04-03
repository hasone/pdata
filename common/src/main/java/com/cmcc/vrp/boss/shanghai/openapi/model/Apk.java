package com.cmcc.vrp.boss.shanghai.openapi.model;

import com.cmcc.vrp.boss.shanghai.openapi.util.JsonUtil;

/**
 * @author yan.jy@primeton.com createTime锛�2014骞�7鏈�22鏃�
 * 
 *         涓诲瘑閽ユā鍨�
 */
public class Apk {

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

    public Apk() {

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

    /**
     *  (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}
