package com.cmcc.vrp.boss.shanghai.model.charge;

import com.cmcc.vrp.boss.shanghai.model.common.ErrorInfo;

/**
 * @author lgk8023
 *
 */
public class ChargeResp {
    
    private ErrorInfo ErrorInfo;

    private ChargeRetInfo RetInfo;

    public ErrorInfo getErrorInfo() {
        return ErrorInfo;
    }

    public void setErrorInfo(ErrorInfo errorInfo) {
        ErrorInfo = errorInfo;
    }

    public ChargeRetInfo getRetInfo() {
        return RetInfo;
    }

    public void setRetInfo(ChargeRetInfo retInfo) {
        RetInfo = retInfo;
    }

}
