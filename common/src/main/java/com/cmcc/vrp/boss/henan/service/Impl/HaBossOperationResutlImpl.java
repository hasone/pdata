package com.cmcc.vrp.boss.henan.service.Impl;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import com.cmcc.vrp.boss.henan.model.HaErrorCode;

/**
 * Created by leelyn on 2016/6/26.
 */
public class HaBossOperationResutlImpl extends AbstractBossOperationResultImpl {

    private boolean queryFlag = false; // 默认不需要去BOSS侧查询

    public HaBossOperationResutlImpl() {
    }

    public HaBossOperationResutlImpl(HaErrorCode errorCode) {
        this.resultCode = errorCode.getCode();
        this.resultDesc = errorCode.getMsg();
    }

    @Override
    public boolean isSuccess() {
        if (this.resultCode != null) {
            return this.resultCode.equals("00000");
        }
        return false;
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
        return queryFlag;
    }

    public boolean isQueryFlag() {
        return queryFlag;
    }

    public void setQueryFlag(boolean queryFlag) {
        this.queryFlag = queryFlag;
    }
}
