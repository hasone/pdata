package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.EntECRecord;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * */
public interface EntECRecordMapper {
    /**
     *
     *
     * */
    int deleteByPrimaryKey(Long id);

    /**
     *
     *
     * */
    int insert(EntECRecord record);

    /**
     *
     *
     * */
    int insertSelective(EntECRecord record);

    /**
     *
     *
     * */
    EntECRecord selectByPrimaryKey(Long id);

    /**
     *
     *
     * */
    int updateByPrimaryKeySelective(EntECRecord record);

    /**
     *
     *
     * */
    int updateByPrimaryKey(EntECRecord record);

    /**
     *
     *
     * */
    List<EntECRecord> showEntEcRecordForPageResult(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    long showEntEcRecordCount(Map<String, Object> map);
    
    /**
     * getLatestEntEcRecords 按时间倒序得到企业所有变更记录
     */
    List<EntECRecord> getLatestEntEcRecords(Long enterId);
}