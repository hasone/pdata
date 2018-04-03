package com.cmcc.vrp.boss.hunan.model;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;

import org.apache.commons.lang.StringUtils;

/**
 * @ClassName: HNChargeResponse
 * @Description: 湖南省BOSS充值接口返回报文的封装类
 * @author: Rowe
 * @date: 2016年4月7日 下午4:55:16
 */
public class HNChargeResponse extends AbstractBossOperationResultImpl {

    private static final String BOSS_RESPONSE_SUCCESS_CODE = "0";//BOSS端接口返回接口调用成功的状态码:0为成功,其余失败。

    private String X_RESULTCODE;//状态码

    private String X_RESULTINFO;//状态描述

    private HNBOSSCharge charge;//BOSS返回的额外信息

    @Override
    public String getResultCode() {
        return X_RESULTCODE;
    }

    @Override
    public boolean isSuccess() {
        if (!StringUtils.isBlank(X_RESULTCODE) && X_RESULTCODE.equals(BOSS_RESPONSE_SUCCESS_CODE)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public boolean isNeedQuery() {
        return false;
    }

    public String getX_RESULTCODE() {
        return X_RESULTCODE;
    }

    public void setX_RESULTCODE(String x_RESULTCODE) {
        X_RESULTCODE = x_RESULTCODE;
    }

    public String getX_RESULTINFO() {
        return X_RESULTINFO;
    }

    public void setX_RESULTINFO(String x_RESULTINFO) {
        X_RESULTINFO = x_RESULTINFO;
    }

    @Override
    public HNBOSSCharge getOperationResult() {
        return charge;
    }

    public HNBOSSCharge getCharge() {
        return charge;
    }

    public void setCharge(HNBOSSCharge charge) {
        this.charge = charge;
    }

}
