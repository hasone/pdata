package com.cmcc.vrp.boss.henan.model;

/**
 * Created by leelyn on 2016/8/17.
 */
public class HaStatusResult {

    private String BILL_ID;
    private String MEM_SRVPKG_DESC;
    private String VALID_DATE;
    private String STATE;
    private String DEAL_RESULT;

    public String getBILL_ID() {
        return BILL_ID;
    }

    public void setBILL_ID(String BILL_ID) {
        this.BILL_ID = BILL_ID;
    }

    public String getMEM_SRVPKG_DESC() {
        return MEM_SRVPKG_DESC;
    }

    public void setMEM_SRVPKG_DESC(String MEM_SRVPKG_DESC) {
        this.MEM_SRVPKG_DESC = MEM_SRVPKG_DESC;
    }

    public String getVALID_DATE() {
        return VALID_DATE;
    }

    public void setVALID_DATE(String VALID_DATE) {
        this.VALID_DATE = VALID_DATE;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getDEAL_RESULT() {
        return DEAL_RESULT;
    }

    public void setDEAL_RESULT(String DEAL_RESULT) {
        this.DEAL_RESULT = DEAL_RESULT;
    }
}
