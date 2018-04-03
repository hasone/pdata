package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.PresentRecord;
import com.cmcc.vrp.province.model.PresentRecordResult;

/**
 * PresentRecordMapper
 * */
public interface PresentRecordMapper {
    /**
     * @Title:deleteByPrimaryKey
     * @Description:
     * */
    int deleteByPrimaryKey(Long id);

    /**
     * @Title:insert
     * @Description:
     * */
    int insert(PresentRecord record);

    /**
     * @Title:insertSelective
     * @Description:
     * */
    int insertSelective(PresentRecord record);

    /**
     * @Title:batchInsert
     * @Description:
     * */
    int batchInsert(List<PresentRecord> list);

    /**
     * @Title:selectByPrimaryKey
     * @Description:
     * */
    PresentRecord selectByPrimaryKey(Long id);

    /**
     * @Title:updateByPrimaryKeySelective
     * @Description:
     * */
    int updateByPrimaryKeySelective(PresentRecord record);

    /**
     * @Title:updateByPrimaryKey
     * @Description:
     * */
    int updateByPrimaryKey(PresentRecord record);

    /**
     * @Title:queryCount
     * @Description:
     * */
    int queryCount(Map map);

    /**
     * @Title:queryCountPlus
     * @Description:
     * */
    int queryCountPlus(Map map);

    /**
     * @Title:queryRecord
     * @Description:
     * */
    List<PresentRecordResult> queryRecord(Map map);

    /**
     * @Title:queryRecordPlus
     * @Description:
     * */
    List<PresentRecordResult> queryRecordPlus(Map map);

    /**
     * @Title:count
     * @Description:
     * */
    int count(long id);

    /**
     * @Title:selectByRuleId
     * @Description:
     * */
    List<PresentRecord> selectByRuleId(long ruleId);

    /**
     * @Title:selectStatus
     * @Description:
     * */
    int selectStatus(long ruleId);

    /**
     * @Title:queryByRuleStatus
     * @Description:
     * */
    List<PresentRecord> queryByRuleStatus(Map<String, Object> parameters);

    /**
     * @Title:deleteByRuleId
     * @Description:
     * */
    int deleteByRuleId(long ruleId);

    /**
     * 日志查询
     * @Title:listLogs
     * @Description:
     * */
    List<PresentRecord> listLogs(Map<String, Object> params);

    /**
     * @Title:countListLogs
     * @Description:
     * */
    Long countListLogs(Map<String, Object> params);

    /**
     * 批量更新记录状态
     * @Title:batchUpdateStatus
     * @Description:
     * */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("newStatus") int newStatus);

    /**
     * 批量更新充值结果
     * @Title:batchUpdateChargeResult
     * @Description:
     * */
    int batchUpdateChargeResult(@Param("records") List<PresentRecord> records);

    /**
     * 更新状态
     * @Title:updateStatus
     * @Description:
     * */
    int updateStatus(@Param("id") Long id, @Param("status") int status, @Param("errorMsg") String errorMsg);

    /**
     * @Title:updateStatusAndStatusCode
     * @Description:
     * */
    int updateStatusAndStatusCode(@Param("id") Long id,
                                  @Param("status") int status,
                                  @Param("statusCode") String statusCode,
                                  @Param("errorMsg") String errorMsg);

    /**
     * 批量更新记录状态码
     * @Title:batchUpdateStatusCode
     * @Description:
     * */
    int batchUpdateStatusCode(@Param("ids") List<Long> ids, @Param("newStatusCode") String newStatusCode);

    /**
     * 更新记录状态码
     * @Title:updateStatusCode
     * @Description:
     * */
    int updateStatusCode(@Param("recordId") Long recordId, @Param("statusCode") String statusCode);

    /**
     * selectBySysSerialNum
     * */
    PresentRecord selectBySysSerialNum(@Param("sysSerialNum")String sysSerialNum);
}