package com.cmcc.vrp.queue.task.channel;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.test.util.ReflectionTestUtils;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.zhejiang.model.ZjBossOperationResultImpl;
import com.cmcc.vrp.boss.zhejiang.model.ZjErrorCode;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.queue.enums.Provinces;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.google.gson.Gson;

/**
 * Created by leelyn on 2016/11/17.
 */
@Ignore
public class ZwChannelWorkerTest extends BaseChannelWorkerTest {

    @InjectMocks
    ZwChannelWorker zwChannelWorker = new ZwChannelWorker();


    @Before
    public void initMocks() {
        Gson gson = new Gson();
        ReflectionTestUtils.setField(zwChannelWorker, "gson", gson);
        PowerMockito.when(distributeProducer.distibute(anyString(), anyObject())).thenReturn(true);
    }

    @Test
    public void testCqChannel() {

        for (Provinces province : Provinces.values()) {
            zwChannelWorker.setTaskString(this.getTaskStr(province.getName()));
            zwChannelWorker.exec();
        }

        Mockito.verify(distributeProducer, times(31)).distibute(anyString(), anyObject());
    }

    public BossOperationResult buildResult(ZjErrorCode code) {
        BossOperationResult result = new ZjBossOperationResultImpl(code);
        return result;
    }

    public String getTaskStr(String province) {
        ChargeDeliverPojo pojo = new ChargeDeliverPojo();
        pojo.setActivityName("单元测试");
        pojo.setActivityType(null);
        pojo.setEntId(1l);
        pojo.setMobile("1888888888");
        PhoneRegion phoneRegion = new PhoneRegion();
        phoneRegion.setProvince(province);
        pojo.setPhoneRegion(phoneRegion);
        pojo.setPrdId(1l);
        pojo.setRecordId(1l);
        pojo.setType("xxxx");
        pojo.setSerialNum("xxxx");
        return new Gson().toJson(pojo);
    }
}
