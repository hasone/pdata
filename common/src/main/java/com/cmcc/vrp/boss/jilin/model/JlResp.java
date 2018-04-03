package com.cmcc.vrp.boss.jilin.model;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年4月5日 上午9:01:42
*/
public class JlResp {

    private String RETURN_CODE;
    
    private String RETURN_MSG;
    
    private String TRANSIDO;
    
    private String BOSSTRANSIDD;
    
    private String BALANCE;

    public String getRETURN_CODE() {
        return RETURN_CODE;
    }

    public void setRETURN_CODE(String rETURN_CODE) {
        RETURN_CODE = rETURN_CODE;
    }

    public String getRETURN_MSG() {
        return RETURN_MSG;
    }

    public void setRETURN_MSG(String rETURN_MSG) {
        RETURN_MSG = rETURN_MSG;
    }

    public String getTRANSIDO() {
        return TRANSIDO;
    }

    public void setTRANSIDO(String tRANSIDO) {
        TRANSIDO = tRANSIDO;
    }

    public String getBOSSTRANSIDD() {
        return BOSSTRANSIDD;
    }

    public void setBOSSTRANSIDD(String bOSSTRANSIDD) {
        BOSSTRANSIDD = bOSSTRANSIDD;
    }

    public String getBALANCE() {
        return BALANCE;
    }

    public void setBALANCE(String bALANCE) {
        BALANCE = bALANCE;
    }
    
}
