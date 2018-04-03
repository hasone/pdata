package com.cmcc.vrp.boss.hebei;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import com.cmcc.vrp.boss.hebei.model.HbReturnCode;

/**
 * Created by leelyn on 2016/8/19.
 */
public class HbBossOperationResultImpl extends AbstractBossOperationResultImpl {

    public HbBossOperationResultImpl(HbReturnCode returnCode) {
        this.resultCode = returnCode.getCode();
        this.resultDesc = returnCode.getMsg();
    }

    @Override
    public Object getOperationResult() {
        return null;
    }

    @Override
    public boolean isSuccess() {
        if (this.resultCode == null) {
            return false;
        }
        return this.resultCode.equals(HbReturnCode.SUCCESS.getCode());
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
