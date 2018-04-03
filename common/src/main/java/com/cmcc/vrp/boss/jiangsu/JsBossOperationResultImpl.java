package com.cmcc.vrp.boss.jiangsu;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;

/**
 * @author lgk8023
 *
 */
public class JsBossOperationResultImpl extends AbstractBossOperationResultImpl {
    public JsBossOperationResultImpl(String resultCode, String resultDesc) {
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
    }
    @Override
    public boolean isSuccess() {
        if (this.resultCode == null) {
            return false;
        }
        return this.resultCode.equals("0") 
                || this.resultDesc.equals("HTTP调用超时[url=http://10.33.216.241:9985/fcgi-bin/AbilityPlat_js]");
    }

    @Override
    public boolean isAsync() {
        return this.resultDesc.equals("HTTP调用超时[url=http://10.33.216.241:9985/fcgi-bin/AbilityPlat_js]");
    }

    @Override
    public Object getOperationResult() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isNeedQuery() {
        return this.resultDesc.equals("HTTP调用超时[url=http://10.33.216.241:9985/fcgi-bin/AbilityPlat_js]");
    }
}
