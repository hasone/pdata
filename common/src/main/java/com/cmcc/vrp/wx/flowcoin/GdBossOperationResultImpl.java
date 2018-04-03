package com.cmcc.vrp.wx.flowcoin;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import com.cmcc.vrp.wx.flowcoin.model.GdReturnCode;

/**
 * Created by leelyn on 2016/7/8.
 */
public class GdBossOperationResultImpl extends AbstractBossOperationResultImpl {

    public GdBossOperationResultImpl(GdReturnCode returnCode) {
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
