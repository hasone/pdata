package com.cmcc.vrp.boss.shanghai.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.vrp.boss.shanghai.model.common.BusiParams;
import com.cmcc.vrp.boss.shanghai.model.common.InMap;
import com.cmcc.vrp.boss.shanghai.model.common.Request;
import com.cmcc.vrp.boss.shanghai.model.querycount.QueryUsableBalanceOfFlowInMap;
import com.cmcc.vrp.boss.shanghai.service.AbstractShBossService;
import com.google.gson.Gson;

/**
 * @author lgk8023
 *
 */
@Service("QueryUsableBalanceOfFlowServiceImpl")
public class QueryUsableBalanceOfFlowServiceImpl extends AbstractShBossService{
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(QueryUsableBalanceOfFlowServiceImpl.class);
    @Autowired
    private Gson gson = new Gson();
    @Override
    protected InMap getMap(Map map) {
        QueryUsableBalanceOfFlowInMap inMap = new QueryUsableBalanceOfFlowInMap();
        String acctId;
        String mainBillId;
        String offerId;
        if (StringUtils.isNotBlank(acctId = map.get("acctId").toString())) {
            inMap.setAcctId(acctId);
        }
        if (StringUtils.isNotBlank(mainBillId = map.get("mainBillId").toString())) {
            inMap.setMainBillId(mainBillId);
        }
        if (StringUtils.isNotBlank(offerId = map.get("offerId").toString())) {
            inMap.setOfferId(offerId);
        }
        LOGGER.info("集团全网订购查询 inMap:", gson.toJson(inMap));
        return inMap;
    }

    @Override
    protected Request getRequest(InMap inMap) {
        Request request = new Request();
        request.setBusiCode("PT-SH-FS-OI4731");
        BusiParams busiParams = new BusiParams();
        busiParams.setInMap(inMap);
        request.setBusiParams(busiParams);
        return request;
    }

}
