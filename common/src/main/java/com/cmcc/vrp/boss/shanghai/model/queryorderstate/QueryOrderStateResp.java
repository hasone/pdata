package com.cmcc.vrp.boss.shanghai.model.queryorderstate;

/**
 * Created by lilin on 2016/8/25.
 */
public class QueryOrderStateResp {

    private String status;

    private QosAsiaResult result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public QosAsiaResult getResult() {
        return result;
    }

    public void setResult(QosAsiaResult result) {
        this.result = result;
    }
}
