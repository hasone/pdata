package com.cmcc.vrp.province.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.MonthlyPresentRecordCopy;

/**
 * @author lgk8023
 *
 */
public interface MonthlyPresentRecordCopyMapper {
    /**
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    int insert(MonthlyPresentRecordCopy record);

    /**
     * @param record
     * @return
     */
    int insertSelective(MonthlyPresentRecordCopy record);

    /**
     * @param id
     * @return
     */
    MonthlyPresentRecordCopy selectByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(MonthlyPresentRecordCopy record);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKey(MonthlyPresentRecordCopy record);

    /**
     * @param monthlyPresentRecordCopyList
     * @return
     */
    int batchInsert(List<MonthlyPresentRecordCopy> list);

    List<MonthlyPresentRecordCopy> getByRuleId(@Param("ruleId") Long ruleId);
}