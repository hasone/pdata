package com.cmcc.vrp.boss.xinjiang.request.json.model;

import com.google.gson.annotations.SerializedName;

/**
 * ResourcePool
 *
 */
public class ResourcePool {
    @SerializedName(value = "GROUP_ID")
    private String groupId;

    public ResourcePool(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    
}
