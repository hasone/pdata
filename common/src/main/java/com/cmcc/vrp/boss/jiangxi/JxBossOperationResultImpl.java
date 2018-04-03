package com.cmcc.vrp.boss.jiangxi;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import com.cmcc.vrp.boss.jiangxi.model.JXReturnCode;

/**
 * Created by leelyn on 2016/7/7.
 */
public class JxBossOperationResultImpl extends AbstractBossOperationResultImpl {


    public JxBossOperationResultImpl(JXReturnCode returnCode) {
        this.resultCode = returnCode.getCode();
        this.resultDesc = returnCode.getMsg();

    }
    
    public JxBossOperationResultImpl(String resultCode, String resultDesc) {
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
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
        return this.resultCode.equals("00");
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public boolean isNeedQuery() {
        return false;
    }
}
