package com.cmcc.vrp.boss.shanghai.model.queryproduct;

import com.cmcc.vrp.boss.shanghai.model.common.ErrorInfo;

/**
 * Created by lilin on 2016/8/26.
 */
public class QpAsiaResp {

    private ErrorInfo ErrorInfo;

    private QpRetInfo RetInfo;

    public com.cmcc.vrp.boss.shanghai.model.common.ErrorInfo getErrorInfo() {
        return ErrorInfo;
    }

    public void setErrorInfo(com.cmcc.vrp.boss.shanghai.model.common.ErrorInfo errorInfo) {
        ErrorInfo = errorInfo;
    }

    public QpRetInfo getRetInfo() {
        return RetInfo;
    }

    public void setRetInfo(QpRetInfo retInfo) {
        RetInfo = retInfo;
    }
}
