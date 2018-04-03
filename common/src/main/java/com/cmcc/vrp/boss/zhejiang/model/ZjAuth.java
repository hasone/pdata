package com.cmcc.vrp.boss.zhejiang.model;


import com.cmcc.vrp.boss.zhejiang.utils.EncryptUtil;

/**
 * Auth
 * the common model for storage the aep appkey authentication header information
 */
public class ZjAuth {

    /**
     * The appkey.
     */
    private String appKey;

    /**
     * The appkey secret.
     */
    private String appSecret;

    /**
     * The encrypt type.
     */
    private String encryptType = EncryptUtil.ENCRYPT_SHA256_TYPE;

    /**
     * Instantiates a new auth.
     *
     * @param appKey    the app key
     * @param appSecret the app secret
     */
    public ZjAuth(String appKey, String appSecret) {
        this.appKey = appKey;
        this.appSecret = appSecret;
    }

    /**
     * Instantiates a new auth.
     *
     * @param appKey      the app key
     * @param appSecret   the app secret
     * @param encryptType the encrypt type
     */
    public ZjAuth(String appKey, String appSecret, String encryptType) {
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.encryptType = encryptType;
    }

    /**
     * Gets the app key.
     *
     * @return the app key
     */
    public String getAppKey() {
        return appKey;
    }

    /**
     * Gets the app secret.
     *
     * @return the app secret
     */
    public String getAppSecret() {
        return appSecret;
    }

    /**
     * Gets the encrypt type.
     *
     * @return the encrypt type
     */
    public String getEncryptType() {
        return encryptType;
    }

}
