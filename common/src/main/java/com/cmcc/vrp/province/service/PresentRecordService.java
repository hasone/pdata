package com.cmcc.vrp.province.service;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.enums.ActivityWinRecordStatus;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.model.PresentRecord;
import com.cmcc.vrp.province.model.PresentRecordResult;
import com.cmcc.vrp.province.model.PresentRule;
import com.cmcc.vrp.province.model.json.post.PresentRecordJson;
import com.cmcc.vrp.util.QueryObject;

/**
 * PresentRecordService.java
 */

public interface PresentRecordService {

    /**
     * 创建规则记录
     *
     * @param presentRule
     * @param mobile
     * @Title:create
     * @author: hexinxu
     */
    boolean create(PresentRule presentRule, List<PresentRecordJson> presentRecordJson);

    /**
     * @Title: queryCount
     */
    int queryCount(QueryObject queryObject);

    /**
     * @Title: queryCountPlus
     */
    int queryCountPlus(Map<String, Object> map);

    /**
     * @Title: updateStatus
     */
    boolean updateStatus(Long id, ChargeRecordStatus status, String errorMsg);

    /**
     * @Title: updateStatus
     */
    boolean updateActivityStatus(Long id, ActivityWinRecordStatus status, String errorMsg);

    /**
     * @Title: queryRecord
     */
    List<PresentRecordResult> queryRecord(QueryObject queryObject);

    /**
     * @Title: queryRecordPlus
     */
    List<PresentRecordResult> queryRecordPlus(Map<String, Object> map);

    /**
     * @Title: selectByRuleId
     */
    List<PresentRecord> selectByRuleId(long ruleId);

    /**
     * @Title: selectByRecordId
     */
    PresentRecord selectByRecordId(long id);
    
    /**
     * @Title: selectBySysSerialNum
     */
    PresentRecord selectBySysSerialNum(String sysSerialNum);

    /**
     * @Title: count
     */
    int count(long id);

    /**
     * 批量更新状态
     *
     * @Title: batchUpdateStatus
     */
    boolean batchUpdateStatus(List<Long> recordIds, ChargeRecordStatus chargeRecordStatus);

    /**
     * 批量更新充值结果
     *
     * @Title: batchUpdateChargeResult
     */
    boolean batchUpdateChargeResult(List<PresentRecord> records);

    /**
     * @Title: batchUpdateStatusCode
     */
    boolean batchUpdateStatusCode(List<Long> ids, String statusCode);

    /**
     * @Title: updateStatusCode
     */
    boolean updateStatusCode(Long recordId, String statusCode);

    /**
     * @Title: updatePresentStatus
     */
    boolean updatePresentStatus(Long id, ChargeRecordStatus status, String errorMsg);
    
    
}
