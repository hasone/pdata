package com.cmcc.vrp.boss.fujian;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import com.cmcc.vrp.boss.fujian.model.FjReturnCode;

/**
 * Created by leelyn on 2016/7/27.
 */
public class FjBossOperationResultImpl extends AbstractBossOperationResultImpl {

    public FjBossOperationResultImpl(FjReturnCode returnCode) {
        this.resultCode = returnCode.getCode();
        this.resultDesc = returnCode.getMsg();
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
