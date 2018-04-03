package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.province.model.EntStatusRecord;

public interface EntStatusRecordMapper {
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
    int insert(EntStatusRecord record);

    /**
     * 
     * @Title: insertSelective 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int insertSelective(EntStatusRecord record);

    /**
     * 
     * @Title: selectByPrimaryKey 
     * @Description: TODO
     * @param id
     * @return
     * @return: EntStatusRecord
     */
    EntStatusRecord selectByPrimaryKey(Long id);

    /**
     * 
     * @Title: updateByPrimaryKeySelective 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int updateByPrimaryKeySelective(EntStatusRecord record);

    /**
     * 
     * @Title: updateByPrimaryKey 
     * @Description: TODO
     * @param record
     * @return
     * @return: int
     */
    int updateByPrimaryKey(EntStatusRecord record);
    

    /**
     * 
     * @Title: showEntStatusRecordForPageResult 
     * @Description: TODO
     * @param map
     * @return
     * @return: List<EntStatusRecord>
     */
    List<EntStatusRecord> showEntStatusRecordForPageResult(Map<String, Object> map);

    /**
     * 
     * @Title: showEntStatusRecordCount 
     * @Description: TODO
     * @param map
     * @return
     * @return: long
     */
    long showEntStatusRecordCount(Map<String, Object> map);
}