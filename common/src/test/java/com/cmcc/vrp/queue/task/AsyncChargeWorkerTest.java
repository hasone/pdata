/**
 *
 */
package com.cmcc.vrp.queue.task;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.charge.ChargeService;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EntCallbackAddrService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.InterfaceRecordService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.cmcc.vrp.queue.pojo.ChargePojo;
import com.cmcc.vrp.queue.queue.busi.DeliverByBossQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.ApplicationContext;

/**
 * <p>Title:AsyncChargeWorkerTest </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月16日
 */
@RunWith(PowerMockRunner.class)
public class AsyncChargeWorkerTest {
    @InjectMocks
    AsyncChargeWorker asyncChargeWorker = new AsyncChargeWorker();

    @Mock
    ProductService productService;

    @Mock
    ChargeService chargeService;

    @Mock
    InterfaceRecordService interfaceRecordService;

    @Mock
    EnterprisesService enterprisesService;

    @Mock
    ChargeRecordService chargeRecordService;

    @Mock
    EntCallbackAddrService entCallbackAddrService;

    @Mock
    SerialNumService serialNumService;

    @Mock
    AccountService accountService;

    @Mock
    TaskProducer taskProducer;

    @Mock
    ApplicationContext applicationContext;

    @Test
    public void testExec1() {

        asyncChargeWorker.setTaskString("  ");
        asyncChargeWorker.exec();
    }

    @Test
    @PrepareForTest(DeliverByBossQueue.class)
    public void testExec2() {
        ChargePojo chargePojo = createChargePojo();
        String taskString = JSON.toJSONString(chargePojo);
        asyncChargeWorker.setTaskString(taskString);
        ChargeRecord cr = new ChargeRecord();
        cr.setId(1L);

        DeliverByBossQueue dbq = PowerMockito.mock(DeliverByBossQueue.class);
        Mockito.when(applicationContext.getBean(DeliverByBossQueue.class)).thenReturn(dbq);
        PowerMockito.when(dbq.publish(Mockito.any(ChargeDeliverPojo.class))).thenReturn(false);
        Mockito.when(accountService.returnFunds(Mockito.anyString())).thenReturn(false);
        Mockito.when(chargeRecordService.getRecordBySN(Mockito.anyString())).thenReturn(cr);
        Mockito.when(interfaceRecordService.updateChargeStatus(Mockito.anyLong(),
            Mockito.any(ChargeRecordStatus.class), Mockito.anyString())).thenReturn(false);
        Mockito.when(chargeRecordService.updateStatus(Mockito.anyLong(), Mockito.any(ChargeResult.class))).thenReturn(false);
        asyncChargeWorker.exec();
    }

    @Test
    @PrepareForTest(DeliverByBossQueue.class)
    public void testExec3() {
        ChargePojo chargePojo = createChargePojo();
        String taskString = JSON.toJSONString(chargePojo);
        asyncChargeWorker.setTaskString(taskString);
        ChargeRecord cr = new ChargeRecord();
        cr.setId(1L);

        DeliverByBossQueue dbq = PowerMockito.mock(DeliverByBossQueue.class);
        Mockito.when(applicationContext.getBean(DeliverByBossQueue.class)).thenReturn(dbq);
        Mockito.when(chargeRecordService.getRecordBySN(Mockito.anyString())).thenReturn(cr);
        PowerMockito.when(dbq.publish(Mockito.any(ChargeDeliverPojo.class))).thenReturn(true);
        Mockito.when(accountService.returnFunds(Mockito.anyString())).thenReturn(true);
        Mockito.when(interfaceRecordService.updateStatusCode(Mockito.anyLong(),
            Mockito.anyString())).thenReturn(false).thenReturn(true);
        Mockito.when(chargeRecordService.updateStatusCode(Mockito.anyLong(), Mockito.anyString())).thenReturn(false).thenReturn(true);

        asyncChargeWorker.exec();
        asyncChargeWorker.exec();
    }

    private ChargePojo createChargePojo() {
        ChargePojo pojo = new ChargePojo();
        pojo.setAppKey("weqrwgfaw");
        pojo.setChargeRecordId(1L);
        pojo.setEcSerialNum("45233t34");
        pojo.setEnterpriseId(1L);
        pojo.setFingerprint("qqq");
        pojo.setMobile("18867101111");
        pojo.setProductId(1L);
        pojo.setRemoteIpAddr("127.0.0.1");
        pojo.setSystemNum("124235423");
        return pojo;
    }
}
