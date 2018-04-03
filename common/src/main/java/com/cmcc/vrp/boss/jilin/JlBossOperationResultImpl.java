package com.cmcc.vrp.boss.jilin;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年4月5日 上午10:17:18
*/
public class JlBossOperationResultImpl extends AbstractBossOperationResultImpl {

    
    public JlBossOperationResultImpl(String resultCode, String resultDesc) {
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
    }

    @Override
    public boolean isNeedQuery() {
        return false;
    }

    @Override
    public boolean isSuccess() {
        if (this.resultCode == null) {
            return false;
        }
        return this.resultCode.equals("0");
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public Object getOperationResult() {
        return null;
    }
}
