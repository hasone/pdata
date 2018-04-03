package com.cmcc.vrp.boss.shanghai.model.queryallnet;

import com.cmcc.vrp.boss.shanghai.model.paymember.SoapDataContainer;

import java.util.List;

/**
 * Created by lilin on 2016/8/26.
 */
public class QanRetInfo {

    private SoapDataContainer soapDataContainer;

    private String returnCode;

    private String returnMsg;

    private List<QanItem> returnContent;

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

    public List<QanItem> getReturnContent() {
        return returnContent;
    }

    public void setReturnContent(List<QanItem> returnContent) {
        this.returnContent = returnContent;
    }
}
