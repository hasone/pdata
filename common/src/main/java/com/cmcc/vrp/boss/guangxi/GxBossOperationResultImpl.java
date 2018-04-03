package com.cmcc.vrp.boss.guangxi;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import com.cmcc.vrp.boss.guangxi.model.GxReturnCode;

/**
 * Created by leelyn on 2016/9/13.
 */
public class GxBossOperationResultImpl extends AbstractBossOperationResultImpl {

    public GxBossOperationResultImpl(GxReturnCode returnCode) {
        this.resultCode = returnCode.getCode();
        this.resultDesc = returnCode.getMsg();
    }

    @Override
    public boolean isSuccess() {
        if (this.resultCode == null) {
            return false;
        }
        return this.resultCode.equals(GxReturnCode.SUCCESS.getCode());
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public Object getOperationResult() {
        return null;
    }

    @Override
    public boolean isNeedQuery() {
        return false;
    }
}
