package com.cmcc.vrp.yqx.model;

/**
 * 
 * YqxRespModel
 * 
 *
 */
public class YqxRespSuccModel {
    private int error;//0成功 
    
    private String src;
    
    

    public YqxRespSuccModel(int error, String src) {
        this.error = error;
        this.src = src;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }   
}
