package com.cmcc.vrp.queue.task;

import static org.mockito.Matchers.anyString;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import com.cmcc.vrp.boss.BossQueryResult;
import com.cmcc.vrp.boss.beijing.BjBossQueryServiceImpl;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.BaseBossQuery;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ChargeQueryPojo;
import com.google.gson.Gson;

/**
 * Created by leelyn on 2016/11/19.
 */
@RunWith(PowerMockRunner.class)
@ContextConfiguration("classpath:conf/applicationContext.xml")
@PrepareForTest(AsynChargeQueryWorker.class)
public class AsynChargeQueryWorkerTest {

    @InjectMocks
    AsynChargeQueryWorker chargeQueryWorker = new AsynChargeQueryWorker();

    @Mock
    ChargeRecordService chargeRecordService;

    @Mock
    BjBossQueryServiceImpl bjBossQueryService;

    @Mock
    AccountService accountService;

    @Mock
    TaskProducer taskProducer;
    
    @Mock
    ApplicationContext applicationContext;

//    @Before
//    public void initMocks() {
//        Gson gson = new Gson();
//        ReflectionTestUtils.setField(chargeQueryWorker, "gson", gson);
//        ReflectionTestUtils.setField(chargeQueryWorker, "applicationContext", applicationContext);
//        chargeQueryWorker.setTaskString(buildTaskString());
//
//        ChargeRecord record = new ChargeRecord();
//        record.setStatus(ChargeRecordStatus.PROCESSING.getCode());
//        PowerMockito.when(chargeRecordService.getRecordBySN(anyString())).thenReturn(record);
//        PowerMockito.when(chargeRecordService.updateStatus(anyLong(), any(ChargeResult.class))).thenReturn(false);
//
//
//    }

    @Test
    @PrepareForTest(BjBossQueryServiceImpl.class)
    public void test() throws Exception {
	
	BjBossQueryServiceImpl service = PowerMockito.mock(BjBossQueryServiceImpl.class);
	Map<String, BaseBossQuery> map = new HashMap<String, BaseBossQuery>();
	map.put("aa", service);
	
	ChargeRecord record = new ChargeRecord();
	record.setStatus(ChargeRecordStatus.PROCESSING.getCode());
	
	PowerMockito.when(applicationContext.getBeansOfType(BaseBossQuery.class)).thenReturn(map);
	PowerMockito.when(service.queryStatus(anyString())).thenReturn(BossQueryResult.SUCCESS);
	
	chargeQueryWorker.setTaskString(buildTaskString());
        chargeQueryWorker.exec();

    }

    private String buildTaskString() {
        ChargeQueryPojo pojo = new ChargeQueryPojo();
        pojo.setFingerPrint("beijing123456789");
        pojo.setSystemNum("xxxxxxxxxx");
        pojo.setEntId(1l);
        return new Gson().toJson(pojo);
    }


}
