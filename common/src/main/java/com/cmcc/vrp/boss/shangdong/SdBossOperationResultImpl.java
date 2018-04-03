package com.cmcc.vrp.boss.shangdong;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import com.cmcc.vrp.boss.shangdong.model.SDReturnCode;

/**
 * Created by leelyn on 2016/6/28.
 */
public class SdBossOperationResultImpl extends AbstractBossOperationResultImpl {

    public SdBossOperationResultImpl() {
    }

    public SdBossOperationResultImpl(SDReturnCode returnCode) {
        this.resultCode = returnCode.getCode();
        this.resultDesc = returnCode.getMsg();
    }


    @Override
    public boolean isSuccess() {
        if (this.resultCode != null) {
            return this.resultCode.equals("100");
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
