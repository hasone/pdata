package com.cmcc.vrp.boss.sichuan.model;

/**
 * 附加资费办理返回
 *
 * @author wujiamin
 * @date 2016年9月20日下午2:23:57
 */
public class SCShortAddModeResponse {
    private String resCode;

    private String resMsg;

    private SCShortAddModeResponseOutData outData;

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

    public SCShortAddModeResponseOutData getOutData() {
        return outData;
    }

    public void setOutData(SCShortAddModeResponseOutData outData) {
        this.outData = outData;
    }

}
