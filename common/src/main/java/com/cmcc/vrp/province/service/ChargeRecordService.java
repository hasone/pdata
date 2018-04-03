package com.cmcc.vrp.province.service;

import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.statement.FingerprintStat;
import com.cmcc.vrp.province.module.ChargeStatisticLineModule;
import com.cmcc.vrp.province.module.ChargeStatisticListModule;
import com.cmcc.vrp.util.QueryObject;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 充值记录服务类
 * */
public interface ChargeRecordService {

    /**
     * 统计充值记录
     * @param queryObject
     */
    List<ChargeStatisticListModule> statisticChargeList(QueryObject queryObject);

    /**
     * 统计充值记录
     * @param queryObject
     */
    int statisticChargeListCount(QueryObject queryObject);

    /**
     * 统计充值记录
     * @param queryObject
     */
    List<ChargeStatisticLineModule> statistictByChargeDay(QueryObject queryObject);

    /**
     * 构建充值记录
     * @Title: create
     * @param chargeRecord
     */
    boolean create(ChargeRecord chargeRecord);

    /**
     * 批量插入充值记录
     *
     * @param records 批量插入记录
     * @return 插入成功返回true, 否则false
     */
    boolean batchInsert(List<ChargeRecord> records);

    /**
     * 更新接口充值状态
     * @param errorMsg
     * @param id
     * @param status
     */
    boolean updateStatus(Long id, ChargeRecordStatus status, String errorMsg);

    /**
     * 根据充值结果更新充值状态
     * @param chargeResult
     * @param id
     */
    boolean updateStatus(Long id, ChargeResult chargeResult);

    /**
     * 批量更新充值状态
     *
     * @param records 充值记录
     * @return 更新成功返回true, 否则false
     */
    boolean batchUpdateStatus(List<ChargeRecord> records);

    /**
     * 根据recordId更新充值记录
     * @param recordId
     * @param status
     * @param errMsg
     */
    boolean updateByRecordId(Long recordId, Integer status, String errMsg);

    /**
     * 根据systemNum更新充值记录
     * @param systemNum
     * @param status
     * @param errMsg
     * @param financeStatus
     * @param updateChargeTime
     */
    boolean updateBySystemNum(String systemNum, Integer status, String errMsg, Integer financeStatus, Date updateChargeTime);


    /**
     * @param systemNum
     */
    ChargeRecord getRecordBySN(String systemNum);

    /**
     * @param entId
     * @param pageOff
     * @param pageSize
     */
    List<ChargeRecord> getRecords(Long entId, Integer pageOff, Integer pageSize);


    /**
     * @param record
     */
    boolean updateByPrimaryKeySelective(ChargeRecord record);

    /**
     * @param map
     */
    boolean updateByTypeCodeAndRecordId(Map map);

    /**
     * 返回指定企业指定时间内的记录
     * @param start
     * @param end
     * @param entId
     */
    List<FingerprintStat> statement(DateTime start, DateTime end, Long entId);

    /**
     * @param enterId
     * @param serialNum
     * @return
     * @Title: selectRecordByEnterIdAndSerialNum
     * @Description: TODO
     * @return: List<ChargeRecord>
     */
    List<ChargeRecord> selectRecordByEnterIdAndSerialNum(Long enterId, String serialNum);

    /**
     * 批量查找充值记录
     * @param systemNums
     */
    List<ChargeRecord> batchSelectBySystemNum(List<String> systemNums);

    /**
     * @param records
     * @param chargeResult
     */
    boolean updateActivityRecords(List<ChargeRecord> records, ChargeResult chargeResult);

    /**
     * 更新充值状态码
     * @param id
     * @param statusCode
     */
    boolean updateStatusCode(Long id, String statusCode);

    /**
     * @param statusCode
     * @param systemNums
     */
    boolean batchUpdateStatusCode(String statusCode, List<String> systemNums);

    /**
     * @param recordId
     * @param statusCode
     * @param status
     * @param errorMsg
     * @param financeStatus
     * @param updateChargeTime
     */
    boolean updateStatusAndStatusCode(Long recordId, String statusCode, Integer status, String errorMsg, Integer financeStatus, Date updateChargeTime);

    /**
     * @param statusCode
     * @param statusCode
     */
    boolean updateStatusCodeBySystemNum(String systemNum, String statusCode);

    /** 
     * @Title: updateBossChargeTimeBySystemNum 
     */
    boolean updateBossChargeTimeBySystemNum(String systemNum, Date date);

    /** 
     * @Title: batchUpdateBossChargeTimeBySystemNum
     */
    boolean batchUpdateBossChargeTimeBySystemNum(List<String> buildSystemNums, Date date);

    /**
     * 报表查询
     * @param queryObject
     * @author qinqinyan
     * */
    List<ChargeRecord> queryChargeRecord(QueryObject queryObject);

    /**
     * 报表查询
     * @param queryObject
     * @author qinqinyan
     * */
    Long countChargeRecord(QueryObject queryObject);
    
    /**
     * 江西前一天充值统计
     * @param start
     * @param end
     * @param supplierId
     * @return
     */
    List<ChargeRecord> getJxChargeRecords(Date start, Date end, Long supplierId);
    
    /**
     * 
     * @Title: getMdrcChargeRecords 
     * @Description: TODO
     * @param year
     * @param map
     * @return
     * @return: List<ChargeRecord>
     */
    List<ChargeRecord> getMdrcChargeRecords(String year, Map map);
    
    /**
     * 
     * @Title: countMdrcChargeRecords 
     * @Description: TODO
     * @param year
     * @param map
     * @return
     * @return: long
     */
    long countMdrcChargeRecords(String year, Map map);
    
    /**
     * @param systemNum
     * @param date
     * @return
     */
    boolean updateQueryTime(String systemNum, Date date);

    /**
     * 
     * */
    List<ChargeRecord> getRecordsByMobileAndPrd(String mobile, Long splPid, Date date);
}
