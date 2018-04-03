package com.cmcc.vrp.boss.xinjiang.request.json.model;

import com.google.gson.annotations.SerializedName;

/**
 * TradeInfo
 *
 */
public class TradeInfo {
    @SerializedName(value = "OPR_NUMB")
    private String oprNumb;
 
    public TradeInfo(String oprNumb) {
        this.oprNumb = oprNumb;
    }

    public String getOprNumb() {
        return oprNumb;
    }

    public void setOprNumb(String oprNumb) {
        this.oprNumb = oprNumb;
    }
    
    
}
