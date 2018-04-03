package com.cmcc.vrp.boss.xiangshang;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import com.cmcc.vrp.boss.xiangshang.pojo.ErrCode;
import com.cmcc.vrp.boss.xiangshang.pojo.ResponsePojo;

/**
 * 向上渠道的请求操作结果 <p> Created by sunyiwei on 2016/12/9.
 */
public class XiangShangBossOperationResult extends AbstractBossOperationResultImpl {
    private final ErrCode errCode;
    private final ResponsePojo responsePojo;

    public XiangShangBossOperationResult(ResponsePojo responsePojo, ErrCode errCode) {
        this.responsePojo = responsePojo;
        this.errCode = errCode;
    }

    @Override
    public String getResultCode() {
        return errCode != null ? errCode.getMessage() : null;
    }

    @Override
    public String getResultDesc() {
        return responsePojo == null ? null : responsePojo.getErrinfo();
    }

    @Override
    public boolean isSuccess() {
        return ErrCode.OrderSended == errCode;
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public Object getOperationResult() {
        return responsePojo;
    }

    @Override
    public boolean isNeedQuery() {
        return false;
    }
}
