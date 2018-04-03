package com.cmcc.vrp.boss.zhuowang;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import com.cmcc.vrp.boss.zhuowang.bean.OrderRequestResult;

/**
 * Created by sunyiwei on 2016/9/19.
 */
public class ZWBossOperationResultImpl extends AbstractBossOperationResultImpl {
    private OrderRequestResult orr;

    public ZWBossOperationResultImpl(OrderRequestResult orr) {
        this.orr = orr;
    }

    @Override
    public String getResultCode() {
        return null;
    }

    @Override
    public boolean isSuccess() {
        return orr != null && OrderRequestResult.ResultCode.SUCCESS.getStatus().equals(orr.getStatus());
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String getResultDesc() {
        return orr != null ? orr.getRspDesc() : null;
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
