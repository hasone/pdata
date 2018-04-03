package com.cmcc.vrp.boss.chongqing.pojo.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author lgk8023
 *
 */
public class Token {

    @SerializedName("access_token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
}
