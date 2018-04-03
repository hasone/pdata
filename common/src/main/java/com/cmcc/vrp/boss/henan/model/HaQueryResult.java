package com.cmcc.vrp.boss.henan.model;

/**
 * Created by leelyn on 2016/8/16.
 */
public class HaQueryResult {

    private String GROUP_NAME;
    private String MEM_SRVPKG_DESC;
    private String VALID_DATE;
    private String EXPIRE_DATE;
    private String DISCOUNT;

    public String getGROUP_NAME() {
        return GROUP_NAME;
    }

    public void setGROUP_NAME(String GROUP_NAME) {
        this.GROUP_NAME = GROUP_NAME;
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

    public String getEXPIRE_DATE() {
        return EXPIRE_DATE;
    }

    public void setEXPIRE_DATE(String EXPIRE_DATE) {
        this.EXPIRE_DATE = EXPIRE_DATE;
    }

    public String getDISCOUNT() {
        return DISCOUNT;
    }

    public void setDISCOUNT(String DISCOUNT) {
        this.DISCOUNT = DISCOUNT;
    }
}
