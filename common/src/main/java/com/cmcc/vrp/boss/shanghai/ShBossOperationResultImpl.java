package com.cmcc.vrp.boss.shanghai;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import com.cmcc.vrp.boss.shanghai.model.ShReturnCode;

/**
 * Created by leelyn on 2016/7/12.
 */
public class ShBossOperationResultImpl extends AbstractBossOperationResultImpl {

    public ShBossOperationResultImpl(ShReturnCode returnCode) {
        this.resultCode = returnCode.getCode();
        this.resultDesc = returnCode.getMsg();
    }
    
    public ShBossOperationResultImpl(String resultCode, String resultDesc) {
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
        return true;
    }

    @Override
    public boolean isNeedQuery() {
        return true;
    }
}
