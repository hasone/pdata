package com.cmcc.vrp.boss.shanghai.service.impl;

import com.cmcc.vrp.boss.shanghai.model.common.BusiParams;
import com.cmcc.vrp.boss.shanghai.model.common.InMap;
import com.cmcc.vrp.boss.shanghai.model.common.Request;
import com.cmcc.vrp.boss.shanghai.model.queryorderstate.QueryOrderStateInMap;
import com.cmcc.vrp.boss.shanghai.service.AbstractShBossService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by lilin on 2016/8/25.
 */
@Service("QueryOrderStateServiceImpl")
public class QueryOrderStateServiceImpl extends AbstractShBossService {

    @Override
    protected InMap getMap(Map map) {
        QueryOrderStateInMap inMap = new QueryOrderStateInMap();
        String bossRespNum;
        if (StringUtils.isNotBlank(bossRespNum = map.get("bossReqNum").toString())) {
            inMap.setTransIDO(bossRespNum);
        }
        return inMap;
    }

    @Override
    protected Request getRequest(InMap inMap) {
        Request request = new Request();
        request.setBusiCode("PT-SH-FS-OI4186");
        BusiParams busiParams = new BusiParams();
        busiParams.setFlag("10");
        busiParams.setInMap(inMap);
        request.setBusiParams(busiParams);
        return request;
    }
}
