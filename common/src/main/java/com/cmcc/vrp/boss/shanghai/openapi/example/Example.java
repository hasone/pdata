package com.cmcc.vrp.boss.shanghai.openapi.example;

import com.cmcc.vrp.boss.shanghai.model.common.AsiaDTO;
import com.cmcc.vrp.boss.shanghai.model.querycount.QueryUsableBalanceOfFlow;
import com.cmcc.vrp.boss.shanghai.service.impl.QueryProductServiceIml;
import com.cmcc.vrp.boss.shanghai.service.impl.QueryUsableBalanceOfFlowServiceImpl;

/**
 * @author lgk8023
 *
 */
public class Example {
    public static void main(String[] args) throws Exception {
        //订购组余额查询接口
        QueryUsableBalanceOfFlow queryUsableBalanceOfFlow = new QueryUsableBalanceOfFlow();
        queryUsableBalanceOfFlow.setAccId("10030458657");
        queryUsableBalanceOfFlow.setOfferId("390000023260");
        queryUsableBalanceOfFlow.setMainBillId("30000441995501");
        
        QueryUsableBalanceOfFlowServiceImpl boss = new QueryUsableBalanceOfFlowServiceImpl();
        System.out.println(boss.queryUsableBalanceOfFlow(queryUsableBalanceOfFlow));
        
        AsiaDTO asiaDTO = new AsiaDTO();
        asiaDTO.setRoleId("182000014240");
        asiaDTO.setOfferId("390000023260");
        QueryProductServiceIml productServiceIml = new QueryProductServiceIml();
        System.out.println(productServiceIml.queryProductByOfferIdAndRoleId(asiaDTO));
        
        

    }
}
