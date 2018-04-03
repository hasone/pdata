package com.cmcc.vrp.boss.virtualCharge.webService;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import com.cmcc.vrp.boss.virtualCharge.model.VirtualChargeReturnCode;

/**
 * Created by qinqinyan on 2017/5/9.
 */
public class VirtualChargeBossOperationResultImpl extends AbstractBossOperationResultImpl {

    public VirtualChargeBossOperationResultImpl(VirtualChargeReturnCode virtualChargeReturnCode){
        this.resultCode = virtualChargeReturnCode.getCode();
        this.resultDesc = virtualChargeReturnCode.getMsg();
    }

    @Override
    public boolean isSuccess() {
        if (this.resultCode == null) {
            return false;
        }
        return this.resultCode.equals("0000");
    }

    @Override
    public boolean isAsync() {
        return false;
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
