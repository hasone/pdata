package com.cmcc.vrp.sms.jilin.model;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年4月25日 上午8:45:47
*/
public class Header {
    private String TRACE_ID;
    
    private String PARENT_CALL_ID;
    
    private String KEEP_LIVE;

    public String getTRACE_ID() {
        return TRACE_ID;
    }

    public void setTRACE_ID(String tRACE_ID) {
        TRACE_ID = tRACE_ID;
    }

    public String getPARENT_CALL_ID() {
        return PARENT_CALL_ID;
    }

    public void setPARENT_CALL_ID(String pARENT_CALL_ID) {
        PARENT_CALL_ID = pARENT_CALL_ID;
    }

    public String getKEEP_LIVE() {
        return KEEP_LIVE;
    }

    public void setKEEP_LIVE(String kEEP_LIVE) {
        KEEP_LIVE = kEEP_LIVE;
    }
}
