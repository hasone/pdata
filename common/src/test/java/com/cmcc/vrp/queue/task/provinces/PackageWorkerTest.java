package com.cmcc.vrp.queue.task.provinces;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.cmcc.vrp.queue.pojo.ZwBossPojo;
import com.google.gson.Gson;

/**
 * Created by leelyn on 2016/11/17.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(PackageWorker.class)
public class PackageWorkerTest {

    @InjectMocks
    PackageWorker worker = new PackageWorker();

    @Mock
    TaskProducer taskProducer;

    @Mock
    ChargeRecordService chargeRecordService;

    @Before
    public void init() {
        Gson gson = new Gson();
        ReflectionTestUtils.setField(worker, "gson", gson);
        PowerMockito.when(taskProducer.produceZwPackage(any(ZwBossPojo.class))).thenReturn(true);
        PowerMockito.when(chargeRecordService.batchSelectBySystemNum(anyList())).thenReturn(buildRecodrds(10));
        PowerMockito.when(chargeRecordService.updateStatusCode(anyLong(), anyString())).thenReturn(true);
    }

    @Test
    public void packageTest() {
        worker.setTaskString(getTaskStr(10));
        worker.exec();
        Mockito.verify(taskProducer, times(1)).produceZwPackage(any(ZwBossPojo.class));
    }

    @Test
    public void packageNoMsgTest() {
        worker.setTaskString(getNullTaskStr());
        worker.exec();
    }

    private List<ChargeRecord> buildRecodrds(Integer size) {
        List<ChargeRecord> records = new ArrayList<ChargeRecord>();
        for (int i = 0; i < size; i++) {
            ChargeRecord record = new ChargeRecord();
            record.setSerialNum(String.valueOf(i));
            record.setTypeCode(ActivityType.GIVE.getCode());
        }
        return records;
    }

    public String getTaskStr(Integer size) {
        ZwBossPojo zwBossPojo = new ZwBossPojo();
        List<ChargeDeliverPojo> list = new ArrayList<ChargeDeliverPojo>();
        for (int i = 0; i < size; i++) {
            ChargeDeliverPojo pojo = new ChargeDeliverPojo();
            pojo.setActivityName("单元测试");
            pojo.setActivityType(null);
            pojo.setEntId(1l);
            pojo.setMobile("1888888888");
            pojo.setPhoneRegion(new PhoneRegion());
            pojo.setPrdId(1l);
            pojo.setRecordId(1l);
            pojo.setType("xxxx");
            pojo.setSerialNum(String.valueOf(i));
            list.add(pojo);
        }
        zwBossPojo.setPojos(list);
        return new Gson().toJson(zwBossPojo);
    }

    public String getNullTaskStr() {
        return "";
    }
}
