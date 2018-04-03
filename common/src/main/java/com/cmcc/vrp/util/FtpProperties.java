package com.cmcc.vrp.util;

/**
 * FtpProperties
 *
 */
public class FtpProperties {
    /**
     * url
     */
    private String url = "";
    
    /**
     * port
     */
    private int port = 21;
    
    /**
     * username
     */
    private String username = "";
    
    /**
     * password
     */
    private String password = "";
    
    /**
     * ftp端路径
     */
    private String remotePath = "";
    
    /**
     * 保存路径
     */
    private String localPath ="";
    
    
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRemotePath() {
        return remotePath;
    }
    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }
    public String getLocalPath() {
        return localPath;
    }
    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
    
    
}
