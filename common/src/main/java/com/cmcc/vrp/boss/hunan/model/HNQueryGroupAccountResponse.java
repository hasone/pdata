package com.cmcc.vrp.boss.hunan.model;

import org.apache.commons.lang.StringUtils;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;

/**
 * @ClassName: HNQueryGroupAccountResponse
 * @Description: 湖南省BOSS查询集团产品余额接口返回报文封装类
 * @author: Rowe
 * @date: 2016年4月7日 下午4:58:00
 */
public class HNQueryGroupAccountResponse extends AbstractBossOperationResultImpl {

    public static final String BOSS_RESPONSE_SUCCESS_CODE = "0";//BOSS端接口返回接口调用成功的状态码

    private String X_RESULTCODE;//状态码

    private String X_RESULTINFO;//状态描述

    private HNBOSSAccount bossAccount;//账户余额

    @Override
    public boolean isSuccess() {
        if (!StringUtils.isBlank(X_RESULTCODE) && X_RESULTCODE.equals(BOSS_RESPONSE_SUCCESS_CODE)) {
            return true;
        }
        return false;
    }

    @Override
    public HNBOSSAccount getOperationResult() {
        // TODO Auto-generated method stub
        return bossAccount;
    }

    public HNBOSSAccount getBossAccount() {
        return bossAccount;
    }

    public void setBossAccount(HNBOSSAccount bossAccount) {
        this.bossAccount = bossAccount;
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
}
