package com.cmcc.vrp.wx;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年5月9日 下午3:25:19
*/
public interface EntUserQueryService {
    /**
     * @param mobile
     * @param entId
     * @return
     */
    public boolean checkMobile(String mobile, Long crowdfundingActivityDetailId);
}
