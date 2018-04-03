package com.cmcc.vrp.province.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.enums.PROVINCETYPE;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.google.gson.Gson;

@Service("phoneRegionService")
public class PhoneRegionServiceImpl implements PhoneRegionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneRegionServiceImpl.class);

    @Autowired
    private GlobalConfigService globalConfigService;

    @Override
    public PhoneRegion query(String mobile) {
        if (StringUtils.isBlank(mobile) || mobile.length() != 11) {
            LOGGER.error("无效的手机号码, Mobile = {}.", mobile);
            return null;
        }

        String requestUrl = getRequestUrl();

        LOGGER.info("号段查询服务的请求地址为{}.", requestUrl);
        String resp = HttpUtils.get(requestUrl, build("mobile", mobile));
        LOGGER.info("号段查询服务的响应内容为{}.", resp);

        return buildPhoneRegion(resp);
    }

    private Map<String, String> build(String key, String value) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put(key, value);

        return map;
    }

    private PhoneRegion buildPhoneRegion(String resp) {
        PhoneRegion phoneRegion = new Gson().fromJson(resp, PhoneRegion.class);
        LOGGER.info("响应内容对象为{}.", new Gson().toJson(phoneRegion));

        return phoneRegion;
    }

    public String getRequestUrl() {
        return globalConfigService.get(GlobalConfigKeyEnum.PHONE_REGION_QUERY_URL.getKey());
    }

    @Override
    public String checkPhoneRegionService(String mobile) {

        String provinceFlag = globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey());
        PROVINCETYPE province = PROVINCETYPE.fromValue(provinceFlag);
        String errMsg = "请输入正确的移动手机号码";
        if (province != null) {
            errMsg = "请输入" + province.getDesc() + "移动手机号码";
        }
        if (StringUtils.isBlank(mobile) || !com.cmcc.vrp.util.StringUtils.isValidMobile(mobile)) {
            return errMsg;
        }
        PhoneRegion phoneRegion = query(mobile);
        if (phoneRegion != null && province.getDesc().equalsIgnoreCase(phoneRegion.getProvince())
                && "M".equalsIgnoreCase(phoneRegion.getSupplier())) {
            return null;
        }
        LOGGER.info("号码归属地信息校验不通过，mobile = {}, phoneRegion = {}, province = {}.", mobile,
                new Gson().toJson(phoneRegion), new Gson().toJson(province));
        return errMsg;
    }
}
