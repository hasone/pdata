package com.cmcc.vrp.boss.xinjiang.request.json.model;

import com.google.gson.annotations.SerializedName;

/**
 * NewSend
 *
 */
public class NewSend {
    @SerializedName(value = "ACCESS_NUM")
    private String accessNum;
    
    @SerializedName(value = "GROUP_ID")
    private String groupId;
    
    @SerializedName(value = "INFO_VALUE")
    private String infoValue;
    
    @SerializedName(value = "PAY_MODE")
    private String payMode = "00";
    
    @SerializedName(value = "OPR_NUMB")
    private String oprNumb;
    
    

    public NewSend(String accessNum, String groupId, String infoValue,
            String oprNumb) {
        this.accessNum = accessNum;
        this.groupId = groupId;
        this.infoValue = infoValue;
        this.oprNumb = oprNumb;
    }

    public String getAccessNum() {
        return accessNum;
    }

    public void setAccessNum(String accessNum) {
        this.accessNum = accessNum;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getInfoValue() {
        return infoValue;
    }

    public void setInfoValue(String infoValue) {
        this.infoValue = infoValue;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getOprNumb() {
        return oprNumb;
    }

    public void setOprNumb(String oprNumb) {
        this.oprNumb = oprNumb;
    } 
    
    
    
 

}
