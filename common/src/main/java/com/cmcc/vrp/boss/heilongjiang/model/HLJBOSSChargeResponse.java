package com.cmcc.vrp.boss.heilongjiang.model;

import org.apache.commons.lang.StringUtils;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;

/**
 * 
 * @ClassName: HLJBOSSChargeResponse 
 * @Description: 黑龙江BOSS充值封装类
 * @author: Rowe
 * @date: 2017年3月30日 下午2:41:53
 */
public class HLJBOSSChargeResponse extends AbstractBossOperationResultImpl {

    /**
     * BOSS端接口返回接口调用成功的状态码:Y为成功,其余失败。    
     */
    private static final String BOSS_RESPONSE_SUCCESS_CODE = "Y";

    private String resultCode;

    private String resultDesc;

    private HLJBOSSChargePojo charge;//BOSS返回报文包装类

    /**
     * 
     */
    @Override
    public boolean isSuccess() {
        if (!StringUtils.isBlank(resultCode) && resultCode.equals(BOSS_RESPONSE_SUCCESS_CODE)) {
            return true;
        }
        return false;
    }
    /**
     * 
     */
    @Override
    public boolean isAsync() {
        // TODO Auto-generated method stub
        return false;
    }
    /**
     * 
     */
    @Override
    public HLJBOSSChargePojo getOperationResult() {
        // TODO Auto-generated method stub
        return charge;
    }
    /**
     * 
     */
    @Override
    public boolean isNeedQuery() {
        // TODO Auto-generated method stub
        return false;
    }
    /**
     * 
     */
    public String getResultCode() {
        return resultCode;
    }
    /**
     * 
     */
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
    /**
     * 
     */
    public String getResultDesc() {
        return resultDesc;
    }
    /**
     * 
     */
    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }
    /**
     * 
     */
    public HLJBOSSChargePojo getCharge() {
        return charge;
    }
    /**
     * 
     */
    public void setCharge(HLJBOSSChargePojo charge) {
        this.charge = charge;
    }

}
