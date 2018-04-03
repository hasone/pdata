package com.cmcc.vrp.boss.xinjiang.request.json.model;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * OldSend
 *
 */
public class OldSend {
    
    @SerializedName(value = "GROUP_ID")
    private String groupId;
    
    @SerializedName(value = "USER_ID")
    private String userId;
    
    @SerializedName(value = "SERIAL_NUMBER")
    private String serialNumber;
    
    @SerializedName(value = "X_SUBTRANS_CODE")
    private String xSubtransCode = "ProcessGroupTransTrafficApp";
    
    @SerializedName(value = "INFO_VALUE")
    private String infoValue;
    
    @SerializedName(value = "OPR_NUMB")
    private String oprNumb;
    
  

    public OldSend(String groupId, String userId, String serialNumber,
            String infoValue, String oprNumb) {
        super();
        this.groupId = groupId;
        this.userId = userId;
        this.serialNumber = serialNumber;
        this.infoValue = infoValue;
        this.oprNumb = oprNumb;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getxSubtransCode() {
        return xSubtransCode;
    }

    public void setxSubtransCode(String xSubtransCode) {
        this.xSubtransCode = xSubtransCode;
    }

    public String getInfoValue() {
        return infoValue;
    }

    public void setInfoValue(String infoValue) {
        this.infoValue = infoValue;
    }

    public String getOprNumb() {
        return oprNumb;
    }

    public void setOprNumb(String oprNumb) {
        this.oprNumb = oprNumb;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    
}
