package com.cmcc.vrp.province.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.DailyStatistic;
import com.cmcc.vrp.province.model.DailyStatisticResult;
import com.cmcc.vrp.province.model.Enterprise;

/**
 * mysql-generator
 *
 */
public interface DailyStatisticMapper {
    /**
     * 
     * @Title: deleteByPrimaryKey 
     * @Description: TODO
     * @param id
     * @return
     * @return: int
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 
     * @Title: insert 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int insert(DailyStatistic record);

    /**
     * 
     * @Title: insertSelective 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int insertSelective(DailyStatistic record);

    /**
     * 
     * @Title: selectByPrimaryKey 
     * @Description: TODO
     * @param id
     * @return
     * @return: DailyStatistic
     */
    DailyStatistic selectByPrimaryKey(Long id);

    /**
     * 
     * @Title: updateByPrimaryKeySelective 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int updateByPrimaryKeySelective(DailyStatistic record);

    /**
     * 
     * @Title: updateByPrimaryKey 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int updateByPrimaryKey(DailyStatistic record);
    
    /**
     * getEntSumDailyResult
     */
    List<DailyStatisticResult> getEntSumDailyResult(Map<String, Object> map);
    
    /**
     * getEntSumSortResult
     */
    List<DailyStatisticResult> getEntSumSortResult(Map<String, Object> map);
    
    /**
     * getEntSumSoldeResult
     */
    List<DailyStatisticResult> getEntSumSoldeResult(Map<String, Object> map);
    
    /**
     * getEntPrdSizeDistribution
     */
    List<DailyStatisticResult> getEntPrdSizeDistribution(Map<String, Object> map);
    
    /**
     * getEntChargeTypeResult
     */
    List<DailyStatisticResult> getEntChargeTypeResult(Map<String, Object> map);
    
    /**
     * 
     * @Title: countSuccessCount 
     * @Description: 统计充值成功产品个数
     * @param city
     * @param chargeType
     * @param enterId
     * @param prdId
     * @param productTypeList
     * @param startTime
     * @param endTime
     * @return
     * @return: List
     */
    List<DailyStatistic> countSuccessCountByCity(@Param("city") String city, @Param("chargeType") String chargeType,
            @Param("listEnters") List<Enterprise> listEnters, @Param("prdId") Long prdId,
            @Param("productTypeList") List<Byte> productTypeList, @Param("startTime") Date startTime,
            @Param("endTime") Date endTime);

    /**
     * 
     * @Title: countSuccessMoney 
     * @Description: 统计充值成功产品折扣前总金额
     * @param city
     * @param chargeType
     * @param enterId
     * @param prdId
     * @param productTypeList
     * @param startTime
     * @param endTime
     * @return
     * @return: List
     */
    List<DailyStatistic> countSuccessMoneyByCity(@Param("city") String city, @Param("chargeType") String chargeType,
            @Param("listEnters") List<Enterprise> listEnters, @Param("prdId") Long prdId,
            @Param("productTypeList") List<Byte> productTypeList, @Param("startTime") Date startTime,
            @Param("endTime") Date endTime);

    /**
     * 
     * @Title: countSuccessCapacity 
     * @Description: 统计充值成功产品总大小
     * @param city
     * @param chargeType
     * @param enterId
     * @param prdId
     * @param productTypeList
     * @param startTime
     * @param endTime
     * @return
     * @return: List
     */
    List<DailyStatistic> countSuccessCapacityByCity(@Param("city") String city, @Param("chargeType") String chargeType,
            @Param("listEnters") List<Enterprise> listEnters, @Param("prdId") Long prdId,
            @Param("productTypeList") List<Byte> productTypeList, @Param("startTime") Date startTime,
            @Param("endTime") Date endTime);
   
    /**
     * 
     * @Title: countSuccessCountByDate 
     * @Description: 按日期统计充值成功总个数
     * @param city
     * @param chargeType
     * @param enterId
     * @param prdId
     * @param productTypeList
     * @param startTime
     * @param endTime
     * @return
     * @return: List
     */
    List<DailyStatistic> countSuccessCountByDate(@Param("city") String city, @Param("chargeType") String chargeType,
            @Param("listEnters") List<Enterprise> listEnters, @Param("prdId") Long prdId,
            @Param("productTypeList") List<Byte> productTypeList, @Param("startTime") Date startTime,
            @Param("endTime") Date endTime);
    
    /**
     * 
     * @Title: countSuccessCapacityByDate 
     * @Description: 按日期统计充值成功流量总额度
     * @param city
     * @param chargeType
     * @param enterId
     * @param prdId
     * @param productTypeList
     * @param startTime
     * @param endTime
     * @return
     * @return: List
     */
    List<DailyStatistic> countSuccessCapacityByDate(@Param("city") String city, @Param("chargeType") String chargeType,
            @Param("listEnters") List<Enterprise> listEnters, @Param("prdId") Long prdId,
            @Param("productTypeList") List<Byte> productTypeList, @Param("startTime") Date startTime,
            @Param("endTime") Date endTime);
    
    /**
     * 
     * @Title: getDailyStatisticByCityAndSize 
     * @Description: 获取充值产品明细
     * @param city
     * @param chargeType
     * @param enterId
     * @param prdId
     * @param productTypeList
     * @param startTime
     * @param endTime
     * @return
     * @return: List<DailyStatistic>
     */
    List<DailyStatistic> getDailyStatisticByCityAndSize(@Param("city") String city, @Param("chargeType") String chargeType,
            @Param("listEnters") List<Enterprise> listEnters, @Param("prdId") Long prdId,
            @Param("productTypeList") List<Byte> productTypeList, @Param("startTime") Date startTime,
            @Param("endTime") Date endTime);
    
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
     * @return: List<DailyStatistic>
     */
    List<DailyStatistic> getDailyStatisticBySize(@Param("city") String city, @Param("chargeType") String chargeType,
            @Param("listEnters") List<Enterprise> listEnters, @Param("prdId") Long prdId,
            @Param("productTypeList") List<Byte> productTypeList, @Param("startTime") Date startTime,
            @Param("endTime") Date endTime);


    /**
     * @param map
     * @return
     */
    List<DailyStatisticResult> getPlatformChargeCountDate(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    List<DailyStatisticResult> getPlatformChargeCapacityDate(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    List<DailyStatisticResult> getPlatformChargeTypeDate(Map<String, Object> map);

    List<DailyStatisticResult> getPlatformChargeSuccessDate(Map<String, Object> map);

    List<DailyStatisticResult> getPlatformChargeMoneyDate(Map<String, Object> map);

    List<DailyStatistic> getPlatformKeyDate(Map<String, Object> map);

    List<DailyStatistic> getPlatformData(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    Integer countGetPlatformData(Map<String, Object> map);

    /**
     * 
     * @param map
     * @return
     */
    DailyStatistic getSumPlatformData(Map<String, Object> map);
    
    /**
     * @param map
     * @return
     */
    Integer countGetEnterformData(Map<String, Object> map);

    /**
     * 
     * @param map
     * @return
     */
    List<DailyStatistic> getEnterformData(Map<String, Object> map);

    List<DailyStatistic> getPlatformDataByMap(Map<String, Object> map);

    DailyStatistic getSumPlatformDataByMap(Map<String, Object> map);

    /**
     * @param date
     * @param endTime 
     * @return
     */
    int insertDailyStatistic(@Param("beginTime")String beginTime, @Param("endTime")String endTime);

    
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