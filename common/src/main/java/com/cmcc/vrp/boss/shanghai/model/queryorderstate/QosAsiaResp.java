package com.cmcc.vrp.boss.shanghai.model.queryorderstate;

/**
 * Created by lilin on 2016/9/14.
 */
public class QosAsiaResp {

    private com.cmcc.vrp.boss.shanghai.model.common.ErrorInfo ErrorInfo;

    private QosRetInfo RetInfo;

    public com.cmcc.vrp.boss.shanghai.model.common.ErrorInfo getErrorInfo() {
        return ErrorInfo;
    }

    public void setErrorInfo(com.cmcc.vrp.boss.shanghai.model.common.ErrorInfo errorInfo) {
        ErrorInfo = errorInfo;
    }

    public QosRetInfo getRetInfo() {
        return RetInfo;
    }

    public void setRetInfo(QosRetInfo retInfo) {
        RetInfo = retInfo;
    }
}
