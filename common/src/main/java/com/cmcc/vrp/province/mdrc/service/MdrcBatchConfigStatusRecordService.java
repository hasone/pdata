package com.cmcc.vrp.province.mdrc.service;

import java.util.List;

import com.cmcc.vrp.province.model.MdrcBatchConfigStatusRecord;

/**
 * 
 * @ClassName: MdrcBatchConfigStatusRecordService 
 * @Description: 营销卡规则状态变更记录表
 * @author: Rowe
 * @date: 2017年8月16日 下午12:16:17
 */
public interface MdrcBatchConfigStatusRecordService {

    /**
     * 
     * @Title: insertSelective 
     * @Description: TODO
     * @param record
     * @return
     * @return: boolean
     */
    boolean insertSelective(MdrcBatchConfigStatusRecord record);
    
    /**
     * 
     * @Title: selectByConfigId 
     * @Description: TODO
     * @param configId
     * @param statusList
     * @return
     * @return: List<MdrcBatchConfigStatusRecord>
     */
    List<MdrcBatchConfigStatusRecord> selectByConfigId(Long configId, List<Integer> statusList);

    /**
     * 
     * @Title: selectByPrimaryKey 
     * @Description: TODO
     * @param configId
     * @return
     * @return: MdrcBatchConfigStatusRecord
     */
    MdrcBatchConfigStatusRecord selectByPrimaryKey(Long configId);
}
