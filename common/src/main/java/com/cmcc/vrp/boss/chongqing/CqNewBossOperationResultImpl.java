package com.cmcc.vrp.boss.chongqing;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;

/**
 * @ClassName: CQBOSSChargeResponse
 * @author: qihang
 * @date: 2015年4月27日 下午3:18:33
 *
 */
public class CqNewBossOperationResultImpl extends AbstractBossOperationResultImpl {

    public String retCode = "";// "100"为正确，其它为错误

    public String retMsg = "";


    public CqNewBossOperationResultImpl(String retCode, String retMsg) {
        this.retCode = retCode;
        this.retMsg = retMsg;
    }

    @Override
    public String getResultCode() {

        return retCode;
    }

    @Override
    public boolean isSuccess() {
        if (retCode == null || !"0".equals(retCode)) {
            return false;
        }
        return true;
    }

    @Override
    public String getResultDesc() {

        return retMsg;
    }

    /**
     * cqboss没有返回任何信息
     */
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
