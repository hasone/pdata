package com.cmcc.vrp.province.service;

import java.util.List;

import com.cmcc.vrp.enums.ActivityWinRecordStatus;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.model.MonthlyPresentRecord;
import com.cmcc.vrp.util.QueryObject;

/**
 * 
 * @ClassName: MonthlyPresentRecordService 
 * @Description: 包月赠送记录服务类
 * @author: Rowe
 * @date: 2017年7月5日 上午9:16:42
 */
public interface MonthlyPresentRecordService {
    
    /**
     * 
     * @Title: insert 
     * @Description: 生成记录
     * @param record
     * @return
     * @return: boolean
     */
    boolean insert(MonthlyPresentRecord record);
    
    /**
     * 
     * @Title: getRecords 
     * @Description: 查询记录
     * @param queryObject
     * @return
     * @return: List<MonthlyPresentRecord>
     */
    List<MonthlyPresentRecord> getRecords(QueryObject queryObject);
    
    /**
     * 
     * @Title: countRecords 
     * @Description: 统计记录
     * @param queryObject
     * @return
     * @return: Long
     */
    Long countRecords(QueryObject queryObject);
    
    /**
     * 
     * @Title: batchInsert 
     * @Description: 批量生成记录
     * @param list
     * @return
     * @return: boolean
     */
    boolean batchInsert(List<MonthlyPresentRecord> list);
    
    /**
     * @Title: updateStatus
     */
    boolean updateActivityStatus(Long id, ActivityWinRecordStatus status, String errorMsg);
    
    /**
     * 
     * @param records
     * @return
     */
    boolean batchUpdateChargeResult(List<MonthlyPresentRecord> records);
    
    /**
     * 
     * @param id
     * @param status
     * @param errorMsg
     * @return
     */
    boolean updatePresentStatus(Long id, ChargeRecordStatus status, String errorMsg);

    /**
     * @Title: updateStatusCode
     */
    boolean updateStatusCode(Long recordId, String statusCode);
    
    /**
     * @Title: batchUpdateStatusCode
     */
    boolean batchUpdateStatusCode(List<Long> ids, String statusCode);

}
