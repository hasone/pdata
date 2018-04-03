package com.cmcc.vrp.province.model;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午3:03:52
*/
public class PresentDTO {

    private String sbType;

    private String str;

    private String eId;

    private String pId;

    public String getSbType() {
        return sbType;
    }

    public void setSbType(String sbType) {
        this.sbType = sbType;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    /**
     * @return
     */
    public String geteId() {
        return eId;
    }

    /**
     * @param eId
     */
    public void seteId(String eId) {
        this.eId = eId;
    }

    /**
     * @return
     */
    public String getpId() {
        return pId;
    }

    /**
     * @param pId
     */
    public void setpId(String pId) {
        this.pId = pId;
    }
}
