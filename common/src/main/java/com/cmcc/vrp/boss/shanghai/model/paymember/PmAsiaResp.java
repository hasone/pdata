package com.cmcc.vrp.boss.shanghai.model.paymember;

import com.cmcc.vrp.boss.shanghai.model.common.ErrorInfo;

/**
 * Created by lilin on 2016/8/26.
 */
public class PmAsiaResp {

    private ErrorInfo ErrorInfo;

    private PmRetInfo RetInfo;

    public com.cmcc.vrp.boss.shanghai.model.common.ErrorInfo getErrorInfo() {
        return ErrorInfo;
    }

    public void setErrorInfo(com.cmcc.vrp.boss.shanghai.model.common.ErrorInfo errorInfo) {
        ErrorInfo = errorInfo;
    }

    public PmRetInfo getRetInfo() {
        return RetInfo;
    }

    public void setRetInfo(PmRetInfo retInfo) {
        RetInfo = retInfo;
    }
}
