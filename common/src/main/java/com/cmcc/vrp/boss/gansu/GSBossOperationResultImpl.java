/**
 *
 */
package com.cmcc.vrp.boss.gansu;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import com.cmcc.vrp.boss.gansu.model.GSInterBossResponse;

/**
 * <p>Description: </p>
 *
 * @author xj
 * @date 2016年5月16日
 */
public class GSBossOperationResultImpl extends AbstractBossOperationResultImpl {


    public GSBossOperationResultImpl(GSInterBossResponse datas) {
        this.resultCode = datas.getInterBOSS().getRspCode();
        this.resultDesc = datas.getInterBOSS().getRspDesc();
    }

    @Override
    public boolean isSuccess() {
        return this.resultCode.equals("A0000");
    }


    @Override
    public Object getOperationResult() {
        return this;
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
