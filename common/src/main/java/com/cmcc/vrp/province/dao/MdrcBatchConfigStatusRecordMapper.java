package com.cmcc.vrp.province.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmcc.vrp.province.model.MdrcBatchConfigStatusRecord;

/**
 * 
 * @ClassName: MdrcBatchConfigStatusRecordMapper 
 * @Description: TODO
 * @author: Rowe
 * @date: 2017年8月16日 下午5:09:40
 */
public interface MdrcBatchConfigStatusRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mdrc_batch_config_status_record
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mdrc_batch_config_status_record
     *
     * @mbggenerated
     */
    int insert(MdrcBatchConfigStatusRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mdrc_batch_config_status_record
     *
     * @mbggenerated
     */
    int insertSelective(MdrcBatchConfigStatusRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mdrc_batch_config_status_record
     *
     * @mbggenerated
     */
    MdrcBatchConfigStatusRecord selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mdrc_batch_config_status_record
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(MdrcBatchConfigStatusRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mdrc_batch_config_status_record
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(MdrcBatchConfigStatusRecord record);

    /**
     * 
     * @Title: selectByConfigId 
     * @Description: TODO
     * @param configId
     * @param statusList
     * @return
     * @return: List<MdrcBatchConfigStatusRecord>
     */
    List<MdrcBatchConfigStatusRecord> selectByConfigId(@Param("configId") Long configId, @Param("statusList") List<Integer> statusList);
}