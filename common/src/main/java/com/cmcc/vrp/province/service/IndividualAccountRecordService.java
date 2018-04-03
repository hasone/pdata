package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.IndividualAccountRecord;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * IndividualAccountRecordService.java
 */
public interface IndividualAccountRecordService {
    /** 
     * @Title: create 
     */
    public boolean create(IndividualAccountRecord record);

    /** 
     * @Title: updateByPrimaryKeySelective 
     */
    public boolean updateByPrimaryKeySelective(IndividualAccountRecord record);

    /** 
     * @Title: selectByMap 
     */
    public List<IndividualAccountRecord> selectByMap(Map<String, Object> map);

    /** 
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
