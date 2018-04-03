package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.MonthlyPresentRecord;

/**
 * 
 * @ClassName: MonthlyPresentRecordMapper 
 * @Description: TODO
 * @author: Rowe
 * @date: 2017年7月18日 下午4:14:45
 */
public interface MonthlyPresentRecordMapper {
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
    int insert(MonthlyPresentRecord record);

    /**
     * 
     * @Title: insertSelective 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int insertSelective(MonthlyPresentRecord record);

    /**
     * 
     * @Title: selectByPrimaryKey 
     * @Description: TODO
     * @param id
     * @return
     * @return: MonthlyPresentRecord
     */
    MonthlyPresentRecord selectByPrimaryKey(Long id);

    /**
     * 
     * @Title: updateByPrimaryKeySelective 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int updateByPrimaryKeySelective(MonthlyPresentRecord record);

    /**
     * 
     * @Title: updateByPrimaryKey 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int updateByPrimaryKey(MonthlyPresentRecord record);

    /**
     * 
     * @Title: getRecords 
     * @Description: TODO
     * @param map
     * @return
     * @return: List<MonthlyPresentRecord>
     */
    List<MonthlyPresentRecord> getRecords(Map map);

    /**
     * 
     * @Title: countRecords 
     * @Description: TODO
     * @param map
     * @return
     * @return: Long
     */
    Long countRecords(Map map);

    /**
     * 
     * @Title: batchInsert 
     * @Description: TODO
     * @param list
     * @return
     * @return: int
     */
    int batchInsert(List<MonthlyPresentRecord> list);
    
    /**
     * 
     * @param id
     * @param status
     * @param statusCode
     * @param errorMsg
     * @return
     */
    int updateStatusAndStatusCode(@Param("id") Long id,
            @Param("status") int status,
            @Param("statusCode") String statusCode,
            @Param("errorMsg") String errorMsg);
    
    /**
     * 批量更新充值结果
     * @Title:batchUpdateChargeResult
     * @Description:
     * */
    int batchUpdateChargeResult(@Param("records") List<MonthlyPresentRecord> records);
    
    /**
     * 更新状态
     * @Title:updateStatus
     * @Description:
     * */
    int updateStatus(@Param("id") Long id, @Param("status") int status, @Param("errorMsg") String errorMsg);
    
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

}