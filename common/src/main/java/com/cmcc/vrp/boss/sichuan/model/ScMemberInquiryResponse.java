package com.cmcc.vrp.boss.sichuan.model;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年4月13日 下午4:43:21
*/
public class ScMemberInquiryResponse {
    
    private String resCode;
    
    private String resMsg;
    
    private String detailMsg;
    
    private String promptMsg;
    
    private OutData outData;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public String getDetailMsg() {
        return detailMsg;
    }

    public void setDetailMsg(String detailMsg) {
        this.detailMsg = detailMsg;
    }

    public String getPromptMsg() {
        return promptMsg;
    }

    public void setPromptMsg(String promptMsg) {
        this.promptMsg = promptMsg;
    }

    public OutData getOutData() {
        return outData;
    }

    public void setOutData(OutData outData) {
        this.outData = outData;
    }
}
