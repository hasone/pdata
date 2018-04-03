package com.cmcc.vrp.province.dao;

import com.cmcc.vrp.province.model.CrowdfundingQueryUrl;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年5月5日 上午10:13:26
*/
public interface CrowdfundingQueryUrlMapper {
    /**
     * @param record
     * @return
     */
    int insert(CrowdfundingQueryUrl record);
    
    /**
     * @param id
     * @return
     */
    int delete(Long id);
    
    CrowdfundingQueryUrl getByCrowdfundingActivityDetailId(Long crowdfundingActivityDetailId);
    
    CrowdfundingQueryUrl getById(Long id);
    /**
     * @param record
     * @return
     */
    int updateByCrowdfundingActivityDetailId(CrowdfundingQueryUrl record);

}
