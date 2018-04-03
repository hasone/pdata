package com.cmcc.vrp.province.service;

import com.cmcc.vrp.province.model.CrowdfundingQueryUrl;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年5月5日 上午10:05:14
*/
public interface CrowdfundingQueryUrlService {

    /**
     * @param record
     * @return
     */
    boolean insert(CrowdfundingQueryUrl record);
    
    /**
     * 根据id删除数据
     * @param id
     * @return
     */
    boolean delete(Long id);
    
    CrowdfundingQueryUrl getByCrowdfundingActivityDetailId(Long crowdfundingActivityDetailId);
    
    CrowdfundingQueryUrl getById(Long id);
    
    /**
     * @param crowdfundingActivityDetailId
     * @return
     */
    boolean updateByCrowdfundingActivityDetailId(CrowdfundingQueryUrl record);
}
