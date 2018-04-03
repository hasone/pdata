package com.cmcc.vrp.boss.shanghai.service.impl;

import com.cmcc.vrp.boss.shanghai.model.common.AsiaDTO;
import com.cmcc.vrp.boss.shanghai.model.common.BusiParams;
import com.cmcc.vrp.boss.shanghai.model.common.InMap;
import com.cmcc.vrp.boss.shanghai.model.common.Request;
import com.cmcc.vrp.boss.shanghai.model.querymemberrole.QueryMemberRoleInMap;
import com.cmcc.vrp.boss.shanghai.service.AbstractShBossService;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by lilin on 2016/8/23.
 */
@Service("QueryMemberRoleImpl")
public class QueryMemberRoleImpl extends AbstractShBossService {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(QueryMemberRoleImpl.class);

    @Autowired
    private Gson gson;

    public static void main(String[] args) {
        QueryMemberRoleImpl memberRole = new QueryMemberRoleImpl();
        memberRole.queryMemberRoleByOfferId(new AsiaDTO());
    }

    @Override
    protected InMap getMap(Map map) {
        QueryMemberRoleInMap inMap = new QueryMemberRoleInMap();
        String offerId;
        if (StringUtils.isNotBlank(offerId = map.get("offerId").toString())) {
            inMap.setOfferId(offerId); //"390000001511"
        }
        LOGGER.info("策划编号查询成员角色 inMap:", gson.toJson(inMap));
        return inMap;
    }

    @Override
    protected Request getRequest(InMap inMap) {
        Request request = new Request();
        request.setBusiCode("PT-SH-FS-OI4186");
        BusiParams busiParams = new BusiParams();
        busiParams.setFlag("2");
        busiParams.setInMap(inMap);
        request.setBusiParams(busiParams);
        return request;
    }
}
