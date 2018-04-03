package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.InterfaceRecord;


/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:27:02
*/
public interface InterfaceRecordMapper {
    /**
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    int insert(InterfaceRecord record);

    /**
     * @param record
     * @return
     */
    int insertSelective(InterfaceRecord record);

    /**
     * @param id
     * @return
     */
    InterfaceRecord selectByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(InterfaceRecord record);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKey(InterfaceRecord record);

    /**
     * @param map
     * @return
     */
    List<InterfaceRecord> findExistRecord(Map<String, Object> map);

    //日志查询
    /**
     * @param params
     * @return
     */
    List<InterfaceRecord> listLogs(Map<String, Object> params);

    /**
     * @param params
     * @return
     */
    Long countListLogs(Map<String, Object> params);

    //获取所有记录ID
    List<Long> getAllRecordIds();

    //获取所有记录的键值
    List<String> getAllCacheKeys();

    /**
     * @param records
     * @return
     */
    int batchUpdateStatus(@Param("records") List<InterfaceRecord> records);
    
    /**
     * @param id
     * @param statusCode
     * @return
     */
    int updateStatusCode(@Param("id")Long id, @Param("statusCode")String statusCode);

    /** 
     * @Title: selectBySerialNum 
     */
    List<InterfaceRecord> selectBySerialNum(String serialNum);
}