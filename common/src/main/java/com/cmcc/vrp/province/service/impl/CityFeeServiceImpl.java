package com.cmcc.vrp.province.service.impl;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.province.dao.CityFeeMapper;
import com.cmcc.vrp.province.model.CityFee;
import com.cmcc.vrp.province.service.CityFeeService;

@Service
public class CityFeeServiceImpl implements CityFeeService {

    @Autowired
    CityFeeMapper cityFeeMapper;

    /**
     * 根据地市信息和资费编码查询
     */
    @Override
    public CityFee getByCityCodeAndFeecode(String cityCode, String feeCode) {
        if (StringUtils.isBlank(cityCode) || StringUtils.isBlank(feeCode)) {
            return null;
        }
        HashMap<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("cityCode", cityCode);
        queryMap.put("feeCode", feeCode);
        return cityFeeMapper.getByCityCodeAndFeecode(queryMap);
    }
}
