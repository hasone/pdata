package com.cmcc.vrp.boss.shanxi.model;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;

/**
 * Created by leelyn on 2016/8/28.
 */
public class SxBossOperationResultImpl extends AbstractBossOperationResultImpl {

    public SxBossOperationResultImpl(SxReturnCode returnCode) {
        this.resultCode = returnCode.getCode();
        this.resultDesc = returnCode.getMsg();
    }
    
    public SxBossOperationResultImpl(String resultCode, String resultDesc) {
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
    }

    @Override
    public boolean isNeedQuery() {
        return false;
    }

    @Override
    public boolean isSuccess() {
        if (this.resultCode == null) {
            return false;
        }
        return this.resultCode.equals("0");
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public Object getOperationResult() {
        return null;
    }
}
