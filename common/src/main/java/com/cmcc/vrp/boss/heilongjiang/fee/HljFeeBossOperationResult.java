package com.cmcc.vrp.boss.heilongjiang.fee;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午10:36:35
*/
public class HljFeeBossOperationResult extends AbstractBossOperationResultImpl {

    public HljFeeBossOperationResult(HljFeeResultCode returnCode) {
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
        return false;
    }

    @Override
    public boolean isNeedQuery() {
        return false;
    }
}
