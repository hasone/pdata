package com.cmcc.vrp.boss.tianjin.model;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月17日 下午12:04:32
*/
public class TjBossOperationResultImpl extends AbstractBossOperationResultImpl {

    public TjBossOperationResultImpl(String returnCode, String returnDesc) {
        this.resultCode = returnCode;
        this.resultDesc = returnDesc;
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
