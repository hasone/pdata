package com.cmcc.vrp.boss.shanghai.model.paymember;


/**
 * Created by lilin on 2016/8/26.
 */
public class PmRetInfo {

    private SoapDataContainer soapDataContainer;

    private String returnCode;

    private String returnMsg;

    private PmReturnContent returnContent;

    public SoapDataContainer getSoapDataContainer() {
        return soapDataContainer;
    }

    public void setSoapDataContainer(SoapDataContainer soapDataContainer) {
        this.soapDataContainer = soapDataContainer;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public PmReturnContent getReturnContent() {
        return returnContent;
    }

    public void setReturnContent(PmReturnContent returnContent) {
        this.returnContent = returnContent;
    }
}
