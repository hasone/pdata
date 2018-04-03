package com.cmcc.vrp.yqx.model;

/**
 * 
 * YqxRespModel
 * 
 *
 */
public class YqxRespFailModel {
    private String error;//0成功 

    private String errorDescription;
    
    public YqxRespFailModel(String error, String errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
    
    
}
