package com.cmcc.vrp.boss.shanghai.service.impl;

import com.cmcc.vrp.boss.shanghai.model.common.AsiaDTO;
import com.cmcc.vrp.boss.shanghai.model.common.BusiParams;
import com.cmcc.vrp.boss.shanghai.model.common.InMap;
import com.cmcc.vrp.boss.shanghai.model.common.Request;
import com.cmcc.vrp.boss.shanghai.model.queryproduct.QueryProductInMap;
import com.cmcc.vrp.boss.shanghai.service.AbstractShBossService;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by lilin on 2016/8/23.
 */
@Service("QueryProductServiceIml")
public class QueryProductServiceIml extends AbstractShBossService {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(QueryProductServiceIml.class);

    @Autowired
    private Gson gson;

    public static void main(String[] args) {
        QueryProductServiceIml productServiceIml = new QueryProductServiceIml();
        productServiceIml.queryProductByOfferIdAndRoleId(new AsiaDTO());
    }

    @Override
    protected InMap getMap(Map map) {
        QueryProductInMap inMap = new QueryProductInMap();
        String offerId;
        String roleId;
        if (StringUtils.isNotBlank(offerId = map.get("offerId").toString())
            && StringUtils.isNotBlank(roleId = map.get("roleId").toString())) {
            inMap.setOfferId(offerId);//"390000001511"
            inMap.setRoleId(roleId);//"182000013026"
        }
        LOGGER.info("策划编号及成员角色查询套餐下挂产品 inMap:", gson.toJson(inMap));
        return inMap;
    }

    @Override
    protected Request getRequest(InMap inMap) {
        Request request = new Request();
        request.setBusiCode("PT-SH-FS-OI4186");
        BusiParams busiParams = new BusiParams();
        busiParams.setFlag("3");
        busiParams.setInMap(inMap);
        request.setBusiParams(busiParams);
        return request;
    }
}
