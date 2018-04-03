/**
 *
 */
package com.cmcc.vrp.province.model.SCMessage;

/**
 * @author JamieWu
 *         短信接口系统级输入参数
 */
public class SystemParam {

    private String app_id;

    private String app_key;

    private String timestamp;

    private String version;

    private String method;

    private String status;

    private String end_seller;

    private String sign;

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEnd_seller() {
        return end_seller;
    }

    public void setEnd_seller(String end_seller) {
        this.end_seller = end_seller;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
