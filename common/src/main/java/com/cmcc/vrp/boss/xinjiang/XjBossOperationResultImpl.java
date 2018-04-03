package com.cmcc.vrp.boss.xinjiang;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import com.cmcc.vrp.boss.xinjiang.response.SendResp;

/**
 * 新疆充值结果
 * XjBossOperationResultImpl.java
 * @author wujiamin
 * @date 2016年11月18日
 */
public class XjBossOperationResultImpl extends AbstractBossOperationResultImpl {

    public XjBossOperationResultImpl(SendResp sendResp) {
        this.resultCode = sendResp.getResultCode();
        this.resultDesc = sendResp.getResultInfo();
    }
    
    public XjBossOperationResultImpl(String resultCode,String resultDesc) {
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
    }

    @Override
    public Object getOperationResult() {
        return null;
    }

    @Override
    public boolean isSuccess() {        
        if(resultCode.equals("0")){
            return true;
        }
        return false;
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
