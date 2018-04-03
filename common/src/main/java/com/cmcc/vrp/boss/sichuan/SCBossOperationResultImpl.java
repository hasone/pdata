package com.cmcc.vrp.boss.sichuan;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import com.cmcc.webservice.sichuan.pojo.ReturnCode;

/**
 * Created by sunyiwei on 2016/4/8.
 */
public class SCBossOperationResultImpl extends AbstractBossOperationResultImpl {
    public SCBossOperationResultImpl() {
    }

    public SCBossOperationResultImpl(ReturnCode returnCode) {
        this.resultCode = returnCode.getCode();
        this.resultDesc = returnCode.getMsg();
    }

    @Override
    public boolean isSuccess() {
        if (this.resultCode != null) {
            return this.resultCode.equals("0000000");
        }
        return false;
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
