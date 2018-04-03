package com.cmcc.vrp.boss.shanghai.service;


import com.cmcc.vrp.boss.shanghai.model.common.AsiaDTO;
import com.cmcc.vrp.boss.shanghai.model.querycount.InfoList;
import com.cmcc.vrp.boss.shanghai.model.querycount.QueryUsableBalanceOfFlow;
import com.cmcc.vrp.boss.shanghai.model.queryproduct.ProductItem;

import java.util.List;

/**
 * Created by lilin on 2016/8/22.
 */
public interface ShBossService {

    /**
     * @param custServiceId
     * @return
     */
    AsiaDTO queryAllGroupOrderInfo(String custServiceId);

    /**
     * @param asiaDTO
     * @return
     */
    AsiaDTO queryMemberRoleByOfferId(AsiaDTO asiaDTO);

    /**
     * @param asiaDTO
     * @return
     */
    List<ProductItem> queryProductByOfferIdAndRoleId(AsiaDTO asiaDTO);
    
    /**
     * @param queryUsableBalanceOfFlow
     * @return
     */
    List<InfoList> queryUsableBalanceOfFlow(QueryUsableBalanceOfFlow queryUsableBalanceOfFlow);

}
