package com.cmcc.vrp.boss.ecinvoker;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;

/**
 * Created by leelyn on 2016/7/14.
 */
public class EcBossOperationResultImpl extends AbstractBossOperationResultImpl {
    public final static EcBossOperationResultImpl SUCCESS = new EcBossOperationResultImpl("success", null);
    public final static EcBossOperationResultImpl FAIL = new EcBossOperationResultImpl("fail", null);

    public EcBossOperationResultImpl(String code, String desc) {
        this.resultCode = code;
        this.resultDesc = desc;
    }

    public static void main(String[] args) {
        System.out.println(EcBossOperationResultImpl.SUCCESS.isSuccess());
    }

    @Override
    public boolean isSuccess() {
        return this.resultCode.equalsIgnoreCase("SUCCESS");
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
        return false;
    }
}
