package com.cmcc.vrp.boss.bjym.pojos;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import com.cmcc.vrp.boss.bjym.enums.BjymChargeResponseCodeEnum;

/**
 * 北京云漫boss操作结果
 *
 * Created by sunyiwei on 2017/4/6.
 */
public class BjymBossOperationResult extends AbstractBossOperationResultImpl {
    private final BjymChargeResponse chargeResponse;

    public BjymBossOperationResult(BjymChargeResponse chargeResponse, BjymChargeResponseCodeEnum chargeResponseCodeEnum) {
        this.chargeResponse = chargeResponse;
        this.resultCode = chargeResponseCodeEnum.getCode();
        this.resultDesc = chargeResponseCodeEnum.getMessage();
    }

    public BjymBossOperationResult(BjymChargeResponse chargeResponse, String code, String desc) {
        this.chargeResponse = chargeResponse;
        this.resultCode =  code;
        this.resultDesc = desc;
    }

    /**
     * 判断当前操作结果是否成功
     *
     * @return true为成功，false为失败
     */
    @Override
    public boolean isSuccess() {
        return chargeResponse != null
                && BjymChargeResponseCodeEnum.OK.getCode().equals(chargeResponse.getCode());
    }

    /**
     * 是否为异步充值结果
     *
     * @return true表示当前结果为异步结果，否则为同步结果
     */
    @Override
    public boolean isAsync() {
        return true;
    }

    /**
     * 获取操作结果
     *
     * @return 返回操作结果，结果的内容和格式由各BOSS定义
     */
    @Override
    public Object getOperationResult() {
        return chargeResponse;
    }

    /**
     * 是否需要入队列去查询
     */
    @Override
    public boolean isNeedQuery() {
        return false;
    }
}
