package com.cmcc.vrp.boss.shanghai.model.queryorderstate;

import com.cmcc.vrp.boss.shanghai.model.paymember.SoapDataContainer;

/**
 * Created by lilin on 2016/9/14.
 */
public class QosRetInfo {

    private SoapDataContainer soapDataContainer;

    private String returnCode;

    private String returnMsg;

    private QosReturnContent returnContent;

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

    public QosReturnContent getReturnContent() {
        return returnContent;
    }

    public void setReturnContent(QosReturnContent returnContent) {
        this.returnContent = returnContent;
    }
}
