package com.cmcc.vrp.boss.shanghai;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import com.cmcc.vrp.boss.shanghai.model.ShReturnCode;

/**
 * @author lgk8023
 *
 */
public class ShNewBossOperationResultImpl extends AbstractBossOperationResultImpl {

    public ShNewBossOperationResultImpl(ShReturnCode returnCode) {
        this.resultCode = returnCode.getCode();
        this.resultDesc = returnCode.getMsg();
    }
    
    public ShNewBossOperationResultImpl(String resultCode, String resultDesc) {
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
    }

    @Override
    public boolean isSuccess() {
        if (this.resultCode == null) {
            return false;
        }
        return this.resultCode.equals("0000");
    }

    @Override
    public Object getOperationResult() {
        return null;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public boolean isNeedQuery() {
        return false;
    }
}
