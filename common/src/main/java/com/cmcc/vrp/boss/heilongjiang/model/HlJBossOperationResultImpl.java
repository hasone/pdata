package com.cmcc.vrp.boss.heilongjiang.model;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;

import java.util.List;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午10:42:09
*/
public class HlJBossOperationResultImpl extends AbstractBossOperationResultImpl {

    public HlJBossOperationResultImpl(List<String> list) {
        if (list == null || list.isEmpty()) {
            resultCode = "000001";
            resultDesc = "连接黑龙江boss失败";
        } else if (list.size() < 3) {
            resultCode = "000002";
            resultDesc = "黑龙江boss返回信息缺失";
        } else {
            resultCode = list.get(1);
            resultDesc = list.get(2);
        }
    }


    @Override
    public boolean isSuccess() {
        return this.resultCode.equals("000000");
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
