package com.cmcc.vrp.boss.core;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;

/**
 * Created by leelyn on 2016/8/17.
 */
public class CoreBossOperationResultImpl extends AbstractBossOperationResultImpl {

    public CoreBossOperationResultImpl(String resultCode, String resultDesc) {
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
    }

    @Override
    public boolean isSuccess() {
        return this.resultCode != null && CoreResultCodeEnum.SUCCESS.getCode().equals(this.resultCode);
    }

    @Override
    public Object getOperationResult() {
        return null;
    }

    @Override
    public boolean isNeedQuery() {
        return false;
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
