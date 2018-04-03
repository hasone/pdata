package com.cmcc.vrp.province.dao;

import java.util.List;
import java.util.Map;

import com.cmcc.vrp.province.model.YqxOrderRecord;

/**
 * YqxOrderRecordMapper.java
 * @author wujiamin
 * @date 2017年5月4日
 */
public interface YqxOrderRecordMapper {
    /** 
     * @Title: deleteByPrimaryKey 
     */
    int deleteByPrimaryKey(Long id);

    /** 
     * @Title: insert 
     */
    int insert(YqxOrderRecord record);

    /** 
     * @Title: insertSelective 
     */
    int insertSelective(YqxOrderRecord record);

    /** 
     * @Title: selectByPrimaryKey 
     */
    YqxOrderRecord selectByPrimaryKey(Long id);

    /** 
     * @Title: updateByPrimaryKeySelective 
     */
    int updateByPrimaryKeySelective(YqxOrderRecord record);

    /** 
     * @Title: updateByPrimaryKey 
     */
    int updateByPrimaryKey(YqxOrderRecord record);

    /** 
     * @Title: selectByMap 
     */
    List<YqxOrderRecord> selectByMap(Map map);

    /** 
     * @Title: selectBySerialNum 
     */
    YqxOrderRecord selectBySerialNum(String serialNum);

    /** 
     * @Title: countByMap 
     */
    Integer countByMap(Map<String, Object> map);
}