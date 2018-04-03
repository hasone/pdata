package com.cmcc.vrp.province.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cmcc.vrp.ec.bean.Constants.ProductType;
import com.cmcc.vrp.province.model.DailyStatistic;
import com.cmcc.vrp.province.model.DailyStatisticResult;
import com.cmcc.vrp.province.model.Enterprise;

/**
 * @author lgk8023
 *
 */
public interface DailyStatisticService {
    
    /**
     * 企业分析- 企业充值个数
     * qihang
     */
    List<DailyStatisticResult> getEntSumDailyResult(Long managerId,Map<String, Object> map);
    
    /**
     * 企业分析-企业排行榜
     * qihang
     */
    List<DailyStatisticResult> getEntSumSortResult(Long managerId,Map<String, Object> map);
    
    /**
     * 企业分析-企业充值折前金额
     * qihang
     */
    List<DailyStatisticResult> getEntSumSoldeResult(Long managerId,Map<String, Object> map);
    
    /**
     * 企业分析-企业产品分布
     * qihang
     * 
     * prdType 为类型 ，请填写 1,2,6
     */
    List<DailyStatisticResult> getEntPrdSizeDistribution(Long managerId,Map<String, Object> map,
            ProductType prdType);
    
    /**
     * 企业分析-企业充值类型
     * qihang
     */
    List<DailyStatisticResult> getEntChargeTypeResult(Long managerId,Map<String, Object> map);
    

    /**
     * 
     * @Title: countSuccessCountByCity 
     * @Description: 按地市统计充值成功产品个数
     * @param city
     * @param chargeType
     * @param listEnters
     * @param prdId
     * @param productTypeList
     * @param startTime
     * @param endTime
     * @return
     * @return: List
     */
    List<DailyStatistic> countSuccessCountByCity(String city, String chargeType, List<Enterprise> listEnters, Long prdId,
            List<Byte> productTypeList, Date startTime, Date endTime);

    /**
     * 
     * @Title: countSuccessMoneyByCity 
     * @Description: 按地市统计充值成功产品折扣前总金额
     * @param city
     * @param chargeType
     * @param listEnters
     * @param prdId
     * @param productTypeList
     * @param startTime
     * @param endTime
     * @return
     * @return: List
     */
    List<DailyStatistic> countSuccessMoneyByCity(String city, String chargeType, List<Enterprise> listEnters, Long prdId,
            List<Byte> productTypeList, Date startTime, Date endTime);

    /**
     * 
     * @Title: countSuccessCapacityByCity 
     * @Description: 按地市统计充值成功产品总大小
     * @param city
     * @param chargeType
     * @param listEnters
     * @param prdId
     * @param productTypeList
     * @param startTime
     * @param endTime
     * @return
     * @return: List
     */
    List<DailyStatistic> countSuccessCapacityByCity(String city, String chargeType, List<Enterprise> listEnters, Long prdId,
            List<Byte> productTypeList, Date startTime, Date endTime);

    /**
     * 
     * @Title: countSuccessCountByDate 
     * @Description: 按日期统计充值成功总个数
     * @param city
     * @param chargeType
     * @param listEnters
     * @param prdId
     * @param productTypeList
     * @param startTime
     * @param endTime
     * @return
     * @return: List
     */
    List<DailyStatistic> countSuccessCountByDate(String city, String chargeType, List<Enterprise> listEnters, Long prdId,
            List<Byte> productTypeList, Date startTime, Date endTime);

    /**
     * 
     * @Title: countSuccessCapacityByDate 
     * @Description: 按日期统计充值成功总额度
     * @param city
     * @param chargeType
     * @param listEnters
     * @param prdId
     * @param productTypeList
     * @param startTime
     * @param endTime
     * @return
     * @return: List<DailyStatistic>
     */
    List<DailyStatistic> countSuccessCapacityByDate(String city, String chargeType, List<Enterprise> listEnters, Long prdId,
            List<Byte> productTypeList, Date startTime, Date endTime);

    /**
     * 
     * @Title: getDailyStatisticByCityAndSize 
     * @Description: 获取充值产品明细:三维，地市-》 产品大小 —》数量
     * @param city
     * @param chargeType
     * @param listEnters
     * @param prdId
     * @param productTypeList
     * @param startTime
     * @param endTime
     * @return
     * @return: List
     */
    List<DailyStatistic> getDailyStatisticByCityAndSize(String city, String chargeType, List<Enterprise> listEnters, Long prdId,
            List<Byte> productTypeList, Date startTime, Date endTime);
    
    /**
     * 
     * @Title: getDailyStatisticBySize 
     * @Description: 获取充值产品明细：二维，产品大小 —》数量
     * @param city
     * @param chargeType
     * @param listEnters
     * @param prdId
     * @param productTypeList
     * @param startTime
     * @param endTime
     * @return
     * @return: List
     */
    List<DailyStatistic> getDailyStatisticBySize(String city, String chargeType, List<Enterprise> listEnters, Long prdId,
            List<Byte> productTypeList, Date startTime, Date endTime);

    /**
     * 平台产品充值个数 按日期统计
     * @param id
     * @param params
     * @return
     */
    List<DailyStatisticResult> getPlatformChargeCountDate(Long managerId, Map<String, Object> map);

    /**
     * 平台产品充值总量 按日期统计
     * @param id
     * @param params
     * @return
     */
    List<DailyStatisticResult> getPlatformChargeCapacityDate(Long managerId, Map<String, Object> map);

    /**
     * 平台产品充值类型 按日期统计
     * @param id
     * @param params
     * @return
     */
    List<DailyStatisticResult> getPlatformChargeTypeDate(Long managerId, Map<String, Object> map);

    /**
     * 平台产品成功率 按日期统计
     * @param managerId
     * @param map
     * @return
     */
    List<DailyStatisticResult> getPlatformChargeSuccessDate(Long managerId, Map<String, Object> map);

    /**
     * 产品创收金额
     * @param managerId
     * @param map
     * @return
     */
    List<DailyStatisticResult> getPlatformChargeMoneyDate(Long managerId, Map<String, Object> map);

    List<DailyStatistic> getPlatformKeyDate(Long managerId, Map<String, Object> map);

    List<DailyStatistic> getPlatformData(Long managerId, Map<String, Object> map);

    /**
     * @param id
     * @param map
     * @return
     */
    Integer countGetPlatformData(Long managerId, Map<String, Object> map);

    /**
     * 
     * @param managerId
     * @param map
     * @return
     */
    DailyStatistic getSumPlatformData(Long managerId, Map<String, Object> map);
    
    /**
     * @param id
     * @param map
     * @return
     */
    Integer countGetEnterformData(Long managerId, Map<String, Object> map);

    /**
     * 
     * @param managerId
     * @param map
     * @return
     */
    List<DailyStatistic> getEnterformData(Long managerId, Map<String, Object> map);

    List<DailyStatistic> getPlatformDataByMap(Long managerId, Map<String, Object> map);

    DailyStatistic getSumPlatformDataByMap(Long managerId, Map<String, Object> map);
    
    /**
     * @param date
     * @return
     */
    int insertDailyStatistic(String beginTime, String endTime);
    
    /**
     * 
     * @Title: getDailyStatisticGroupProCode 
     * @Description: TODO
     * @param map
     * @return
     * @return: List<DailyStatistic>
     */
    List<DailyStatistic> getDailyStatisticGroupProCode(Map<String, Object> map);
    
    /**
     * 
     * @Title: countDailyStatisticGroupProCode 
     * @Description: TODO
     * @param map
     * @return
     * @return: Long
     */
    Long countDailyStatisticGroupProCode(Map<String, Object> map);
    
    /**
     * 
     * @Title: getDailyStatisticGroupByCity 
     * @Description: TODO
     * @param map
     * @return
     * @return: List<DailyStatistic>
     */
    List<DailyStatistic> getDailyStatisticGroupByCity(Map<String, Object> map);
    
    /**
     * 
     * @Title: countDailyStatisticGroupByCity 
     * @Description: TODO
     * @param map
     * @return
     * @return: Long
     */
    Long countDailyStatisticGroupByCity(Map<String, Object> map);
}
