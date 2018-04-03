package com.cmcc.vrp.boss.beijing;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import com.cmcc.vrp.boss.beijing.model.BjReturnCode;

/**
 * Created by leelyn on 2016/7/19.
 */
public class BjBossOperationResultImpl extends AbstractBossOperationResultImpl {

    public BjBossOperationResultImpl(BjReturnCode returnCode) {
        this.resultCode = returnCode.getCode();
        this.resultDesc = returnCode.getMsg();
    }

    @Override
    public boolean isSuccess() {
        if (this.resultCode == null) {
            return false;
        }
        return this.resultCode.equals("0");
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
