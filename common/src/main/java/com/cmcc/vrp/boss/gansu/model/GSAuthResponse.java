/**
 *
 */
package com.cmcc.vrp.boss.gansu.model;

/**
 * <p>Description: </p>
 *
 * @author xj
 * @date 2016年6月2日
 */
public class GSAuthResponse {

    private String originalId;

    private String rspCode;

    private String rspDesc;

    private GSAuthResponseEshubReserve eshubReserve;

    public String getOriginalId() {
        return originalId;
    }

    public void setOriginalId(String originalId) {
        this.originalId = originalId;
    }

    public String getRspCode() {
        return rspCode;
    }

    public void setRspCode(String rspCode) {
        this.rspCode = rspCode;
    }

    public String getRspDesc() {
        return rspDesc;
    }

    public void setRspDesc(String rspDesc) {
        this.rspDesc = rspDesc;
    }

    public GSAuthResponseEshubReserve getEshubReserve() {
        return eshubReserve;
    }

    public void setEshubReserve(GSAuthResponseEshubReserve eshubReserve) {
        this.eshubReserve = eshubReserve;
    }


}
