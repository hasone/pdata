package com.cmcc.vrp.queue.task;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.cmcc.vrp.boss.zhuowang.bean.OrderRequestResult;
import com.cmcc.vrp.boss.zhuowang.bean.UserData;
import com.cmcc.vrp.boss.zhuowang.service.FlowPackageOrderService;
import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.CallbackPojo;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.cmcc.vrp.queue.pojo.ZwBossPojo;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;

/**
 * Created by leelyn on 2016/11/19.
 */
@RunWith(PowerMockRunner.class)
public class ZwChargeWorkerTest {

    @InjectMocks
    ZwChargeWorker zwChargeWorker = new ZwChargeWorker();

    @Mock
    ChargeRecordService chargeRecordService;

    @Mock
    FlowPackageOrderService<UserData> flowPackageOrderService;

    @Mock
    TaskProducer taskProducer;

    @Mock
    AccountService accountService;

    @Mock
    SupplierProductService supplierProductService;

    @Mock
    SerialNumService serialNumService;

    @Mock
    GlobalConfigService globalConfigService;


    @Before
    public void initMocks() {
        PowerMockito.when(chargeRecordService.batchUpdateStatus(anyList())).thenReturn(false);
        PowerMockito.when(chargeRecordService.updateActivityRecords(anyList(), any(ChargeResult.class))).thenReturn(false);
        PowerMockito.when(taskProducer.productPlatformCallbackMsg(any(CallbackPojo.class))).thenReturn(true);
        PowerMockito.when(accountService.returnFunds(anyString())).thenReturn(true);
        SupplierProduct supplierProduct = new SupplierProduct();
        supplierProduct.setCode("bossProductCode");
        PowerMockito.when(supplierProductService.selectByPrimaryKey(anyLong())).thenReturn(supplierProduct);
        PowerMockito.when(serialNumService.batchUpdate(anyList())).thenReturn(true);
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.DYNAMIC_PROXY_BOSS_FLAG.getKey())).thenReturn("false");
        PowerMockito.when(chargeRecordService.batchUpdateBossChargeTimeBySystemNum(anyList(), any(Date.class))).thenReturn(true);
    }

    @Test
    public void test() {
        zwChargeWorker.setTaskString("");
        List<ChargeRecord> records = new ArrayList<ChargeRecord>();
        PowerMockito.when(chargeRecordService.batchSelectBySystemNum(any(List.class))).thenReturn(records);
        zwChargeWorker.exec();

        zwChargeWorker.setTaskString(buildTaskString());
        List<ChargeRecord> records1 = new ArrayList<ChargeRecord>();
        records1.add(new ChargeRecord());
        PowerMockito.when(chargeRecordService.batchSelectBySystemNum(any(List.class))).thenReturn(records1);
        List<String> strings = new ArrayList<String>();
        strings.add("bossRespNum");

        OrderRequestResult or = new OrderRequestResult();
        or.setStatus("00");
        or.setOperSeqList(strings);
        PowerMockito.when(flowPackageOrderService.sendRequest(anyList(), anyString())).thenReturn(or);
        zwChargeWorker.exec();

        OrderRequestResult or1 = new OrderRequestResult();
        or1.setStatus("01");
        or1.setOperSeqList(strings);
        PowerMockito.when(flowPackageOrderService.sendRequest(anyList(), anyString())).thenReturn(or1);
        zwChargeWorker.exec();

        verify(flowPackageOrderService, times(2)).sendRequest(anyList(), anyString());
    }
    
    @Test
    public void test_invalidate() {

        zwChargeWorker.setTaskString(buildInvalidateTaskString());
        List<ChargeRecord> records1 = new ArrayList<ChargeRecord>();
        records1.add(new ChargeRecord());
        PowerMockito.when(chargeRecordService.batchSelectBySystemNum(any(List.class))).thenReturn(records1);
        List<String> strings = new ArrayList<String>();
        strings.add("bossRespNum");

        zwChargeWorker.exec();

       
    }

    private String buildTaskString() {
        ZwBossPojo pojo = new ZwBossPojo();
        List<ChargeDeliverPojo> pojos = new ArrayList<ChargeDeliverPojo>();
        for (int i = 0; i < 50; i++) {
            ChargeDeliverPojo deliverPojo = new ChargeDeliverPojo();
            deliverPojo.setSerialNum("xxx" + i);
            deliverPojo.setType("1");
            deliverPojo.setRecordId(1l);
            deliverPojo.setPrdId(12l);
            deliverPojo.setActivityName("EC");
            deliverPojo.setEntId(100l);
            deliverPojo.setMobile("18888888888");
            deliverPojo.setActivityType(ActivityType.INTERFACE);
            PhoneRegion pr = new PhoneRegion();
            pr.setProvince("浙江");
            deliverPojo.setPhoneRegion(pr);
            deliverPojo.setSplPrdId(123l);
            pojos.add(deliverPojo);
        }
        pojo.setPojos(pojos);
        return new Gson().toJson(pojo);
    }
    
    private String buildInvalidateTaskString() {
        ZwBossPojo pojo = new ZwBossPojo();
        List<ChargeDeliverPojo> pojos = new ArrayList<ChargeDeliverPojo>();
        for (int i = 0; i < 50; i++) {
            ChargeDeliverPojo deliverPojo = new ChargeDeliverPojo();
            deliverPojo.setSerialNum("xxx" + i);
            deliverPojo.setType("ec");
            deliverPojo.setRecordId(1l);
            deliverPojo.setPrdId(12l);
            deliverPojo.setActivityName("EC");

            deliverPojo.setMobile("18888888888");
            deliverPojo.setActivityType(ActivityType.INTERFACE);
            PhoneRegion pr = new PhoneRegion();
            pr.setProvince("浙江");
            deliverPojo.setPhoneRegion(pr);
            deliverPojo.setSplPrdId(123l);
            pojos.add(deliverPojo);
        }
        pojo.setPojos(pojos);
        return new Gson().toJson(pojo);
    }
    
    @Test
    public void testParse() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        ZwChargeWorker y = new ZwChargeWorker();
        Class y1 = ZwChargeWorker.class;//获得class类
        Method method = y1.getDeclaredMethod("parse", new Class[]{Gson.class, String.class});//获得method.注意,这里不能使用getMethod方法,因为这个方法只能获取public修饰的方法..
        method.setAccessible(true);//这个设置为true.可以无视java的封装..不设置这个也无法或者这个Method
        Object result = method.invoke(y, new Object[]{new Gson(), "test"});
        assertNull(result);//这里自定拆箱..

    }
    
    @Test
    public void testGetDynamicResult() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        ZwChargeWorker y = new ZwChargeWorker();
        Class y1 = ZwChargeWorker.class;//获得class类
        Method method = y1.getDeclaredMethod("getDynamicResult");//获得method.注意,这里不能使用getMethod方法,因为这个方法只能获取public修饰的方法..
        method.setAccessible(true);//这个设置为true.可以无视java的封装..不设置这个也无法或者这个Method
        Object result = method.invoke(y);
        assertNotNull(result);//这里自定拆箱..

    }
}
