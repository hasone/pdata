package com.cmcc.vrp.boss.shanghai.model.querycount;

import com.cmcc.vrp.boss.shanghai.model.common.ErrorInfo;

/**
 * @author lgk8023
 *
 */
public class QueryUsableBalanceOfFlowResponse {
    private ErrorInfo ErrorInfo;
    private QueryUsableBalanceOfFlowRetInfo RetInfo;
    public ErrorInfo getErrorInfo() {
        return ErrorInfo;
    }
    public void setErrorInfo(ErrorInfo errorInfo) {
        ErrorInfo = errorInfo;
    }
    public QueryUsableBalanceOfFlowRetInfo getRetInfo() {
        return RetInfo;
    }
    public void setRetInfo(QueryUsableBalanceOfFlowRetInfo retInfo) {
        RetInfo = retInfo;
    }
    
}
