package com.cmcc.vrp.queue.task.channel;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.PhoneRegionService;
import com.cmcc.vrp.queue.DistributeProducer;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;

/**
 * Created by leelyn on 2016/11/17.
 */
@Ignore
@RunWith(PowerMockRunner.class)
@PrepareForTest(AbstractChannelWorker.class)
public class BaseChannelWorkerTest {

    @Mock
    PhoneRegionService phoneRegionService;

    @Mock
    TaskProducer taskProducer;

    @Mock
    ChargeRecordService chargeRecordService;

    @Mock
    AccountService accountService;

    @Mock
    DistributeProducer distributeProducer;

    @Mock
    GlobalConfigService globalConfigService;


    @Before
    public void initMocks() {
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.DYNAMIC_PROXY_BOSS_FLAG.getKey())).thenReturn("false");
    }

    public String getTaskStr() {
        ChargeDeliverPojo pojo = new ChargeDeliverPojo();
        pojo.setActivityName("单元测试");
        pojo.setActivityType(null);
        pojo.setEntId(1l);
        pojo.setMobile("1888888888");
        pojo.setPhoneRegion(new PhoneRegion());
        pojo.setPrdId(1l);
        pojo.setRecordId(1l);
        pojo.setType("xxxx");
        pojo.setSerialNum("xxxx");
        return new Gson().toJson(pojo);
    }
}
