package com.cmcc.vrp.boss.shanghai.model.queryallnet;

import com.cmcc.vrp.boss.shanghai.model.common.AsiaResp;

/**
 * Created by lilin on 2016/8/26.
 */
public class QanAsiaResp extends AsiaResp {

    private com.cmcc.vrp.boss.shanghai.model.common.ErrorInfo ErrorInfo;

    private QanRetInfo RetInfo;

    public com.cmcc.vrp.boss.shanghai.model.common.ErrorInfo getErrorInfo() {
        return ErrorInfo;
    }

    public void setErrorInfo(com.cmcc.vrp.boss.shanghai.model.common.ErrorInfo errorInfo) {
        ErrorInfo = errorInfo;
    }

    public QanRetInfo getRetInfo() {
        return RetInfo;
    }

    public void setRetInfo(QanRetInfo retInfo) {
        RetInfo = retInfo;
    }
}
