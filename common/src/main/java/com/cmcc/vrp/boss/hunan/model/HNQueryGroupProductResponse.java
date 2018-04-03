package com.cmcc.vrp.boss.hunan.model;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;

/**
 * @ClassName: HNQueryGroupProductResponse
 * @Description: 湖南省BOSS查询集团产品列表接口返回报文封装类
 * @author: Rowe
 * @date: 2016年4月7日 下午5:00:34
 */
public class HNQueryGroupProductResponse extends AbstractBossOperationResultImpl {

    public static final String BOSS_RESPONSE_SUCCESS_CODE = "0";//BOSS端接口调用成功后返回的状态码

    private String X_RESULTCODE;//状态码

    private String X_RESULTINFO;//状态描述

    private List<HNBOSSProduct> DISCNT_DATA;//产品列表

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

    public List<HNBOSSProduct> getDISCNT_DATA() {
        return DISCNT_DATA;
    }

    public void setDISCNT_DATA(List<HNBOSSProduct> dISCNT_DATA) {
        DISCNT_DATA = dISCNT_DATA;
    }

    @Override
    public List<HNBOSSProduct> getOperationResult() {
        return DISCNT_DATA;
    }
}
