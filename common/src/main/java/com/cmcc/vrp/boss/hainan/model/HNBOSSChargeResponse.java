package com.cmcc.vrp.boss.hainan.model;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import org.apache.commons.lang.StringUtils;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午10:19:42
*/
public class HNBOSSChargeResponse extends AbstractBossOperationResultImpl {

    private static String BOSS_RESPONSE_SUCCESS_CODE = "0";//BOSS端接口返回接口调用成功的状态码

    private String code;//状态码

    private String desc;//状态描述

    private HNBOSSCharge charge;//充值结果包装类

    @Override
    public String getResultCode() {
        // TODO Auto-generated method stub
        return code;
    }

    @Override
    public boolean isSuccess() {
        if (!StringUtils.isBlank(code) && code.equals(BOSS_RESPONSE_SUCCESS_CODE)) {
            return true;
        }
        return false;
    }

    @Override
    public String getResultDesc() {
        // TODO Auto-generated method stub
        return desc;
    }

    @Override
    public Object getOperationResult() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public HNBOSSCharge getCharge() {
        return charge;
    }

    public void setCharge(HNBOSSCharge charge) {
        this.charge = charge;
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
