package com.cmcc.vrp.province.model;

/**
 * s3配置文件
 *
 * Created by sunyiwei on 2017/4/18.
 */
public class S3Config {
    private String s3AccessKey;

    private String s3SecretKey;

    private String host;

    private String bucketName;

    private boolean useHttps = true;

    public String getS3AccessKey() {
        return s3AccessKey;
    }

    public void setS3AccessKey(String s3AccessKey) {
        this.s3AccessKey = s3AccessKey;
    }

    public String getS3SecretKey() {
        return s3SecretKey;
    }

    public void setS3SecretKey(String s3SecretKey) {
        this.s3SecretKey = s3SecretKey;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public boolean isUseHttps() {
        return useHttps;
    }

    public void setUseHttps(boolean useHttps) {
        this.useHttps = useHttps;
    }
}
