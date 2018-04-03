package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.EnterpriseApprovalRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:13:05
*/
public interface EnterpriseApprovalRecordMapper {

    /**
     * @param entId
     * @return
     */
    List<EnterpriseApprovalRecord> selectByEntId(Long entId);

    /**
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);


    /**
     * @param record
     * @return
     */
    int insertSelective(EnterpriseApprovalRecord record);

    /**
     * @param id
     * @return
     */
    EnterpriseApprovalRecord selectByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(EnterpriseApprovalRecord record);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKey(EnterpriseApprovalRecord record);


    /**
     * 企业审核：
     * 获取企业列表
     * 获取企业数量
     */
    List<EnterpriseApprovalRecord> getApprovalEntList(Map<String, Object> map);

    int getApprovalEntCount(Map<String, Object> map);

    /**
     * 获取某个企业历史审核记录
     */
    List<EnterpriseApprovalRecord> getHistoryRecordByEntId(Map<String, Object> map);

    /**
     * 出入记录列表
     */
    int insertList(@Param("list") List<EnterpriseApprovalRecord> list);

    /**
     * 将isnew状态转化为0
     */
    int changeIsNewToZero(@Param("approvalRecordId") Long approvalRecordId);

    /**
     * @param record
     * @return
     */
    int insert(EnterpriseApprovalRecord record);

    /*
     * 将企业的审核原始记录的isNew标记设为0
     */
    /**
     * @param entId
     * @return
     */
    int updateIsNew(Long entId);

    int getApprovalEntCountForCustomerManager(Map<String, Object> map);

    List<EnterpriseApprovalRecord> getApprovalEntListForCustomerManager(
        Map<String, Object> map);

    /**
     * 获取待更新的记录
     */
    List<EnterpriseApprovalRecord> getEnterpriseApprovalRecordNeedUpdate(@Param("entId") Long entId,
                                                                         @Param("newStatus") Integer newStatus);

    /**
     * 更新记录
     */
    int updateLastRecord(EnterpriseApprovalRecord newRecord);

    /**
     * 根据企业id获取未审核的记录
     */
    List<EnterpriseApprovalRecord> getNeedApprovalByEntId(@Param("entId") Long entId);

    /**
     * @param record
     * @return
     */
    int deleteExpireRecord(EnterpriseApprovalRecord record);

    List<EnterpriseApprovalRecord> getEnterpriseApprovalForEntMans(Map<String, Object> map);

    int getEnterpriseApprovalCountForEntMans(Map<String, Object> map);

}