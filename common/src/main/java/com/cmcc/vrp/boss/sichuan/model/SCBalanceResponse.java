/**
 *
 */
package com.cmcc.vrp.boss.sichuan.model;

/**
 * <p>Description: </p>
 *
 * @author xj
 * @date 2016年4月25日
 */
public class SCBalanceResponse {

    private String resCode;

    private String resMsg;

    private SCBalanceResponseOutData outData;

    /**
     * @return the resCode
     */
    public String getResCode() {
        return resCode;
    }

    /**
     * @param resCode the resCode to set
     */
    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    /**
     * @return the resMsg
     */
    public String getResMsg() {
        return resMsg;
    }

    /**
     * @param resMsg the resMsg to set
     */
    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    /**
     * @return the outData
     */
    public SCBalanceResponseOutData getOutData() {
        return outData;
    }

    /**
     * @param outData the outData to set
     */
    public void setOutData(SCBalanceResponseOutData outData) {
        this.outData = outData;
    }


}
