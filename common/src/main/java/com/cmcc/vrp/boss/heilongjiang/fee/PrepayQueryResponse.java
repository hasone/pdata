package com.cmcc.vrp.boss.heilongjiang.fee;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午11:25:28
*/
public class PrepayQueryResponse {
    private String resCode;
    private String resMsg;
    private String deposit;

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

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    @Override
    public String toString() {
        return "BalanceQueryResponse [resCode=" + resCode + ", resMsg=" + resMsg + ", deposit=" + deposit + "]";
    }
}
