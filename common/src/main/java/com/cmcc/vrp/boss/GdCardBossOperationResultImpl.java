package com.cmcc.vrp.boss;

import com.cmcc.vrp.boss.guangdongcard.enums.ReturnInfo;
import com.cmcc.vrp.boss.guangdongcard.model.MemberOrderResponse;
import org.apache.commons.lang.StringUtils;

/**
 * 广东流量卡充值结果
 * <p>
 * Created by sunyiwei on 2016/11/23.
 */
public class GdCardBossOperationResultImpl extends AbstractBossOperationResultImpl {
    private MemberOrderResponse memberOrderResponse;

    public GdCardBossOperationResultImpl(MemberOrderResponse memberOrderResponse) {
        this.memberOrderResponse = memberOrderResponse;

        if (memberOrderResponse != null) {
            this.resultCode = memberOrderResponse.getResultCode();
            this.resultDesc = memberOrderResponse.getResultMessage();
        }
    }

    public GdCardBossOperationResultImpl(ReturnInfo returnInfo) {
        this.memberOrderResponse = null;
        this.resultCode = String.valueOf(returnInfo.getValue());
        this.resultDesc = returnInfo.getMessage();
    }

    /**
     * 判断当前操作结果是否成功
     *
     * @return true为成功，false为失败
     */
    @Override
    public boolean isSuccess() {
        return StringUtils.isNotBlank(resultCode)
            && String.valueOf(ReturnInfo.SUCCESS.getValue()).equals(resultCode);
    }

    /**
     * 是否为异步充值结果
     *
     * @return true表示当前结果为异步结果，否则为同步结果
     */
    @Override
    public boolean isAsync() {
        return false;
    }

    /**
     * 获取操作结果
     *
     * @return 返回操作结果，结果的内容和格式由各BOSS定义
     */
    @Override
    public Object getOperationResult() {
        return memberOrderResponse;
    }

    /**
     * 是否需要入队列去查询
     *
     * @return
     */
    @Override
    public boolean isNeedQuery() {
        return false;
    }
}
