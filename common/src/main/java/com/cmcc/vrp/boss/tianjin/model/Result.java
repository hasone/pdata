package com.cmcc.vrp.boss.tianjin.model;

import java.util.List;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月17日 上午10:49:37
*/
public class Result {
    private List<SvcContent> SVC_CONTENT;
    
    private String X_RESULTCODE;
    
    private String X_RESULTINFO;

    public List<SvcContent> getSVC_CONTENT() {
        return SVC_CONTENT;
    }

    public void setSVC_CONTENT(List<SvcContent> sVC_CONTENT) {
        SVC_CONTENT = sVC_CONTENT;
    }

    public String getX_RESULTCODE() {
        return X_RESULTCODE;
    }

    public void setX_RESULTCODE(String x_RESULTCODE) {
        X_RESULTCODE = x_RESULTCODE;
    }

    public String getX_RESULTINFO() {
        return X_RESULTINFO;
    }

    public void setX_RESULTINFO(String x_RESULTINFO) {
        X_RESULTINFO = x_RESULTINFO;
    }

    @Override
    public String toString() {
        return "Result [SVC_CONTENT=" + SVC_CONTENT + ", X_RESULTCODE=" + X_RESULTCODE + ", X_RESULTINFO="
                + X_RESULTINFO + "]";
    }

}
