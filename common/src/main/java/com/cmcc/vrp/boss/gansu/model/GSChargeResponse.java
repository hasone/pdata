/**
 *
 */
package com.cmcc.vrp.boss.gansu.model;


/**
 * <p>Description: </p>
 *
 * @author xj
 * @date 2016年5月16日
 */
public class GSChargeResponse {
    private String rspCode;

    private String rspDesc;

    private String originalId;

    private GSChargeResponseEshubReserve eshubReserve;

    private GSChargeResponseServiceContent serviceContent;


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


    public String getOriginalId() {
        return originalId;
    }


    public void setOriginalId(String originalId) {
        this.originalId = originalId;
    }


    public GSChargeResponseEshubReserve getEshubReserve() {
        return eshubReserve;
    }


    public void setEshubReserve(GSChargeResponseEshubReserve eshubReserve) {
        this.eshubReserve = eshubReserve;
    }

    public GSChargeResponseServiceContent getServiceContent() {
        return serviceContent;
    }

    public void setServiceContent(GSChargeResponseServiceContent serviceContent) {
        this.serviceContent = serviceContent;
    }
}
