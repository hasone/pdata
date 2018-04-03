package com.cmcc.vrp.boss.hunan.model;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import org.apache.commons.lang.StringUtils;

/**
 * Created by sunyiwei on 2016/9/19.
 */
public class HNFreeOfChargeBossOperationResult extends AbstractBossOperationResultImpl {
    @Override
    public boolean isSuccess() {
        return StringUtils.isNotBlank(resultCode) && resultCode.equals("00000");
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
