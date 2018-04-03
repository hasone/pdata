package com.cmcc.vrp.boss.shanghai.service.impl;

import com.cmcc.vrp.boss.shanghai.model.common.BusiParams;
import com.cmcc.vrp.boss.shanghai.model.common.InMap;
import com.cmcc.vrp.boss.shanghai.model.common.Request;
import com.cmcc.vrp.boss.shanghai.model.queryallnet.QueryAllNetInMap;
import com.cmcc.vrp.boss.shanghai.service.AbstractShBossService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by lilin on 2016/8/23.
 */
@Service("QueryAllGroupServiceImpl")
public class QueryAllGroupServiceImpl extends AbstractShBossService {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(QueryAllGroupServiceImpl.class);

    @Autowired
    private Gson gson = new Gson();

    @Autowired
    private GlobalConfigService globalConfigService;

    public static void main(String[] args) {
        QueryAllGroupServiceImpl groupService = new QueryAllGroupServiceImpl();
        groupService.queryAllGroupOrderInfo("");
    }

    @Override
    protected InMap getMap(Map map) {
        QueryAllNetInMap inMap = new QueryAllNetInMap();
        String custServiceId;
        if (StringUtils.isNotBlank(custServiceId = map.get("custServiceId").toString())) {
            inMap.setCustServiceId(custServiceId);//测试：2100289519  生产：2100287639
        }
        inMap.setPoSpecId(getPoSpecId());//测试：010190004 生产：010190004
        LOGGER.info("集团全网订购查询 inMap:", gson.toJson(inMap));
        return inMap;
    }

    @Override
    protected Request getRequest(InMap inMap) {
        Request request = new Request();
        request.setBusiCode("PT-SH-FS-OI4186");
        BusiParams busiParams = new BusiParams();
        busiParams.setFlag("1");
        busiParams.setInMap(inMap);
        request.setBusiParams(busiParams);
        return request;
    }

    private String getPoSpecId() {
        return globalConfigService.get(GlobalConfigKeyEnum.BOSS_SHANGHAI_POSPECID.getKey());
    }

}
