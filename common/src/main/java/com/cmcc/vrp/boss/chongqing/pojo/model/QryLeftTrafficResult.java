package com.cmcc.vrp.boss.chongqing.pojo.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * @author lgk8023
 *
 */
public class QryLeftTrafficResult {

    @SerializedName("DETAILINFO")
    private List<DetailInfo> detailInfo;

    public List<DetailInfo> getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(List<DetailInfo> detailInfo) {
        this.detailInfo = detailInfo;
    }
    
}
