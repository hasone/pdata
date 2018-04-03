package com.cmcc.vrp.boss.jsof;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;

/**
 * @author lgk8023
 *
 */
public class JsofBossOperationResultImpl extends AbstractBossOperationResultImpl {
    public JsofBossOperationResultImpl(String resultCode, String resultDesc) {
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
    }
    @Override
    public boolean isSuccess() {
        if (this.resultCode == null) {
            return false;
        }
        return this.resultCode.equals("0")
                || this.resultCode.equals("1");
    }

    @Override
    public boolean isAsync() {
        if (this.resultCode == null) {
            return false;
        }
        return this.resultCode.equals("0");
    }

    @Override
    public Object getOperationResult() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isNeedQuery() {
        // TODO Auto-generated method stub
        return false;
    }
}
