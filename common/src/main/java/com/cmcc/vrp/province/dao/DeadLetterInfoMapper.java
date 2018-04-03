package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.DeadLetterInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * DeadLetterInfoMapper.java
 */
public interface DeadLetterInfoMapper {
    /** 
     * @Title: insert 
     */
    int insert(DeadLetterInfo record);

    /** 
     * @Title: getAllUndeletedRecords 
     */
    List<DeadLetterInfo> getAllUndeletedRecords();

    /** 
     * @Title: batchDelete 
     */
    int batchDelete(@Param("ids") List<Long> ids);
}