package com.cmcc.vrp.boss.liaoning.model;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月17日 下午12:04:32
*/
public class LnBossOperationResultImpl extends AbstractBossOperationResultImpl {

    public LnBossOperationResultImpl(String returnCode, String returnDesc) {
        this.resultCode = returnCode;
        this.resultDesc = returnDesc;
    }

    @Override
    public boolean isNeedQuery() {
        return true;
    }

    @Override
    public boolean isSuccess() {
        if (this.resultCode == null) {
            return false;
        }
        return this.resultCode.equals("0000");
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public Object getOperationResult() {
        return null;
    }
}
