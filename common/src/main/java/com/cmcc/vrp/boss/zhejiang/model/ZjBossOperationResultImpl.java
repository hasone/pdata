package com.cmcc.vrp.boss.zhejiang.model;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;

/**
 * Created by leelyn on 2016/8/2.
 */
public class ZjBossOperationResultImpl extends AbstractBossOperationResultImpl {

    public ZjBossOperationResultImpl(ZjErrorCode errorCode) {
        this.resultCode = errorCode.getCode();
        this.resultDesc = errorCode.getMsg();
    }

    @Override
    public boolean isSuccess() {
        if (this.resultCode == null) {
            return false;
        }
        return this.resultCode.equals(ZjErrorCode.SUCCESS.getCode());
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
