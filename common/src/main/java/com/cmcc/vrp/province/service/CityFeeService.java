package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.CityFee;

/**
 * 
 * @ClassName: CityFeeService 
 * @Description: 湖南地市编码与资费编码映射关系服务类
 * @author: Rowe
 * @date: 2017年5月25日 下午2:24:54
 */
public interface CityFeeService {

    /**
     * 
     * @Title: getByCityCodeAndFeecode 
     * @Description: TODO
     * @param cityCode
     * @param feeCode
     * @return
     * @return: CityFee
     */
    CityFee getByCityCodeAndFeecode(String cityCode, String feeCode);
}
