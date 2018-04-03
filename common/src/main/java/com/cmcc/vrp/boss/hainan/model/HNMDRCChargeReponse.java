/**
 *
 */
package com.cmcc.vrp.boss.hainan.model;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import org.apache.commons.lang.StringUtils;

/**
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年4月14日
 */
public class HNMDRCChargeReponse extends AbstractBossOperationResultImpl {

    private static String BOSS_RESPONSE_SUCCESS_CODE = "0";//BOSS端接口返回接口调用成功的状态码

    private String res_code;//状态码

    private String res_desc;//状态描述

    private HNBOSSMDRCCharge mdrcCharge;//充值结果包装类

    @Override
    public String getResultCode() {
        return res_code;
    }

    @Override
    public boolean isSuccess() {
        if (!StringUtils.isBlank(res_code) && res_code.equals(BOSS_RESPONSE_SUCCESS_CODE)) {
            return true;
        }
        return false;
    }

    @Override
    public String getResultDesc() {
        return res_desc;
    }

    @Override
    public HNBOSSMDRCCharge getOperationResult() {
        return mdrcCharge;
    }

    public String getRes_code() {
        return res_code;
    }

    public void setRes_code(String res_code) {
        this.res_code = res_code;
    }

    public String getRes_desc() {
        return res_desc;
    }

    public void setRes_desc(String res_desc) {
        this.res_desc = res_desc;
    }

    public HNBOSSMDRCCharge getMdrcCharge() {
        return mdrcCharge;
    }

    public void setMdrcCharge(HNBOSSMDRCCharge mdrcCharge) {
        this.mdrcCharge = mdrcCharge;
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

