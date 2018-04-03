package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.IndividualAccountRecord;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:23:54
*/
public interface IndividualAccountRecordMapper {
    /**
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    int insert(IndividualAccountRecord record);

    /**
     * @param record
     * @return
     */
    int insertSelective(IndividualAccountRecord record);

    /**
     * @param id
     * @return
     */
    IndividualAccountRecord selectByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(IndividualAccountRecord record);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKey(IndividualAccountRecord record);

    /**
     * @param map
     * @return
     */
    List<IndividualAccountRecord> selectByMap(Map<String, Object> map);
    
    /** 
     * 累积积分
     * @Title: selectAccumulateAccount 
     */
    BigDecimal selectAccumulateAccount(Long accountId);
    
    
    /** 
     * @Title: countDetailRecordByMap 
     */
    int countDetailRecordByMap(Map<String, Object> map);

    /** 
     * @Title: selectDetailRecordByMap 
     */
    List<IndividualAccountRecord> selectDetailRecordByMap(Map<String, Object> map);

}