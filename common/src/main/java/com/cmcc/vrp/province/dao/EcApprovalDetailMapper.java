package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.EcApprovalDetail;

/**
 * <p>Title:EcApprovalDetailMapper </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年10月24日
*/
public interface EcApprovalDetailMapper {
    /**
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    int insert(EcApprovalDetail record);

    /**
     * @param record
     * @return
     */
    int insertSelective(EcApprovalDetail record);

    /**
     * @param id
     * @return
     */
    EcApprovalDetail selectByPrimaryKey(Long id);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(EcApprovalDetail record);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKey(EcApprovalDetail record);
    
    /**
     * @param requestId
     * @return
     */
    EcApprovalDetail selectByRequestId(Long requestId);
}