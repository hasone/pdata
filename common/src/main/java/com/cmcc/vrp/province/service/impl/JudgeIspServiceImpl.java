package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.enums.IspType;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.JudgeIspService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 3tqjh8 on 2016/8/28.
 */
@Service("judgeIspService")
public class JudgeIspServiceImpl implements JudgeIspService {
    @Autowired
    GlobalConfigService globalConfigService;

    @Override
    public String judgeIsp(String mobile) {

        String[] cmMobile = getCMCCMobileFlag().split(",");
        String[] cuMobile = getUNICOMMobileFlag().split(",");
        String[] ctMobile = getTELECOMMobileFlag().split(",");

        String mobileStr = mobile.substring(0, 3);

        //判断是否为移动号
        for (int i = 0; i < cmMobile.length; i++) {
            if (mobileStr.equals(cmMobile[i])) {
                return IspType.CMCC.getValue();
            }
        }
        //判断是否为联通号
        for (int i = 0; i < cuMobile.length; i++) {
            if (mobileStr.equals(cuMobile[i])) {
                return IspType.UNICOM.getValue();
            }
        }

        //判断是否为电信号
        for (int i = 0; i < ctMobile.length; i++) {
            if (mobileStr.equals(ctMobile[i])) {
                return IspType.TELECOM.getValue();
            }
        }

        return null;
    }

    @Override
    public String judgeIsp(String mobile, String[] cmMobile, String[] cuMobile, String[] ctMobile) {
        String mobileStr = mobile.substring(0, 3);

        //判断是否为移动号
        for (int i = 0; i < cmMobile.length; i++) {
            if (mobileStr.equals(cmMobile[i])) {
                return IspType.CMCC.getValue();
            }
        }
        //判断是否为联通号
        for (int i = 0; i < cuMobile.length; i++) {
            if (mobileStr.equals(cuMobile[i])) {
                return IspType.UNICOM.getValue();
            }
        }

        //判断是否为电信号
        for (int i = 0; i < ctMobile.length; i++) {
            if (mobileStr.equals(ctMobile[i])) {
                return IspType.TELECOM.getValue();
            }
        }

        return null;
    }

    /**
     * 获取移动手机号段（前三位）
     */
    private String getCMCCMobileFlag() {
        return globalConfigService.get(GlobalConfigKeyEnum.CMCC_MOBILE_FLAG.getKey());
    }

    /**
     * 获取联通手机号段（前三位）
     */
    private String getUNICOMMobileFlag() {
        return globalConfigService.get(GlobalConfigKeyEnum.UNICOM_MOBILE_FLAG.getKey());
    }

    /**
     * 获取电信手机号段（前三位）
     */
    private String getTELECOMMobileFlag() {
        return globalConfigService.get(GlobalConfigKeyEnum.TELECOM_MOBILE_FLAG.getKey());
    }

}
