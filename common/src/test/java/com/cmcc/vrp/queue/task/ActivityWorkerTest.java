package com.cmcc.vrp.queue.task;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.charge.ChargeService;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.pojo.ActivitiesWinPojo;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.cmcc.vrp.queue.queue.busi.DeliverByBossQueue;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.ApplicationContext;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;

/**
 * Created by qinqinyan on 2016/11/8.
 */
@RunWith(PowerMockRunner.class)
public class ActivityWorkerTest {
    @InjectMocks
    ActivityWorker activityWorker = new ActivityWorker();
    @Mock
    ActivityWinRecordService activityWinRecordService;
    @Mock
    ChargeRecordService chargeRecordService;
    @Mock
    ChargeService chargeService;
    @Mock
    SerialNumService serialNumService;
    @Mock
    ApplicationContext applicationContext;
    @Mock
    AccountService accountService;

    @Test
    public void testExec1() {
        ActivitiesWinPojo activitiesWinPojo = createActivitiesWinPojo(ActivityType.LOTTERY.getCode());
        activitiesWinPojo.setActivitiesWinRecordId(null);
        String jsonStr = JSON.toJSONString(activitiesWinPojo);
        activityWorker.setTaskString(jsonStr);
        activityWorker.exec();
    }

    @Test
    public void testExec2() {
        ActivitiesWinPojo activitiesWinPojo = createActivitiesWinPojo(ActivityType.QRCODE.getCode());
        String jsonStr = JSON.toJSONString(activitiesWinPojo);
        activityWorker.setTaskString(jsonStr);
        Mockito.when(activityWinRecordService.selectByRecordId(anyString())).thenReturn(null);
        activityWorker.exec();
        Mockito.verify(activityWinRecordService).selectByRecordId(anyString());
    }

//    @Test
//    public void testExec8(){
//        ActivitiesWinPojo activitiesWinPojo = createActivitiesWinPojo(ActivityType.LOTTERY.getCode());
//        ActivityWinRecord record = new ActivityWinRecord();
//        String jsonStr = JSON.toJSONString(activitiesWinPojo);
//        activityWorker.setTaskString(jsonStr);
//        Mockito.when(activityWinRecordService.selectByRecordId(anyString())).thenReturn(record);
//        
//        activityWorker.exec();
//        Mockito.verify(activityWinRecordService).selectByRecordId(anyString());
//    }

    @Test
    @Ignore
    @PrepareForTest(DeliverByBossQueue.class)
    public void testExec9() {
        ActivitiesWinPojo activitiesWinPojo = createActivitiesWinPojo(ActivityType.FLOWCARD.getCode());
        ActivityWinRecord record = new ActivityWinRecord();
        String jsonStr = JSON.toJSONString(activitiesWinPojo);
        activityWorker.setTaskString(jsonStr);
        Mockito.when(activityWinRecordService.selectByRecordId(anyString())).thenReturn(record);

        ChargeRecord cr = new ChargeRecord();
        cr.setId(1L);
        DeliverByBossQueue dbq = PowerMockito.mock(DeliverByBossQueue.class);
        Mockito.when(chargeRecordService.getRecordBySN(Mockito.anyString())).thenReturn(cr);
        Mockito.when(applicationContext.getBean(DeliverByBossQueue.class)).thenReturn(dbq);
        PowerMockito.when(dbq.publish(Mockito.any(ChargeDeliverPojo.class))).thenReturn(false);
        Mockito.when(accountService.returnFunds(Mockito.anyString())).thenReturn(false);
        Mockito.when(chargeRecordService.updateStatus(Mockito.anyLong(), Mockito.any(ChargeRecordStatus.class), Mockito.anyString())).thenReturn(false);
        Mockito.when(activityWinRecordService.updateByPrimaryKeySelective(Mockito.any(ActivityWinRecord.class))).thenReturn(false);
        activityWorker.exec();
        Mockito.verify(activityWinRecordService).selectByRecordId(anyString());
    }

    @Test
    @PrepareForTest(DeliverByBossQueue.class)
    public void testExec10() {
        ActivitiesWinPojo activitiesWinPojo = createActivitiesWinPojo(ActivityType.QRCODE.getCode());
        ActivityWinRecord record = new ActivityWinRecord();
        String jsonStr = JSON.toJSONString(activitiesWinPojo);
        activityWorker.setTaskString(jsonStr);
        Mockito.when(activityWinRecordService.selectByRecordId(anyString())).thenReturn(record);

        ChargeRecord cr = new ChargeRecord();
        cr.setId(1L);
        DeliverByBossQueue dbq = PowerMockito.mock(DeliverByBossQueue.class);
        Mockito.when(chargeRecordService.getRecordBySN(Mockito.anyString())).thenReturn(cr);
        Mockito.when(applicationContext.getBean(DeliverByBossQueue.class)).thenReturn(dbq);
        PowerMockito.when(dbq.publish(Mockito.any(ChargeDeliverPojo.class))).thenReturn(true);
        Mockito.when(chargeRecordService.updateStatus(Mockito.anyLong(), Mockito.any(ChargeRecordStatus.class), Mockito.anyString())).thenReturn(false);
        Mockito.when(activityWinRecordService.updateByPrimaryKeySelective(Mockito.any(ActivityWinRecord.class))).thenReturn(true);
        Mockito.when(activityWinRecordService.updateStatusCodeByRecordId(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        Mockito.when(chargeRecordService.updateStatusCode(Mockito.anyLong(), Mockito.anyString())).thenReturn(false);
        activityWorker.exec();
        Mockito.verify(activityWinRecordService).selectByRecordId(anyString());
    }

    @Ignore
    @Test
    public void testExec3() {
        ActivitiesWinPojo activitiesWinPojo = createActivitiesWinPojo(ActivityType.LOTTERY.getCode());
        ActivityWinRecord record = new ActivityWinRecord();

        String jsonStr = JSON.toJSONString(activitiesWinPojo);
        activityWorker.setTaskString(jsonStr);
        Mockito.when(activityWinRecordService.selectByRecordId(anyString())).thenReturn(record);
        activityWorker.exec();
        Mockito.verify(activityWinRecordService).selectByRecordId(anyString());
    }

    @Ignore
    @Test
    public void testExec4() {
        ActivitiesWinPojo activitiesWinPojo = createActivitiesWinPojo(ActivityType.LOTTERY.getCode());
        ActivityWinRecord record = new ActivityWinRecord();
        record.setId(1L);

        String jsonStr = JSON.toJSONString(activitiesWinPojo);
        activityWorker.setTaskString(jsonStr);
        Mockito.when(activityWinRecordService.selectByRecordId(anyString())).thenReturn(record);
        Mockito.when(serialNumService.insert(any(SerialNum.class))).thenReturn(true);
        Mockito.when(chargeRecordService.create(any(ChargeRecord.class))).thenReturn(false);
        activityWorker.exec();
        Mockito.verify(activityWinRecordService).selectByRecordId(anyString());
        Mockito.verify(serialNumService).insert(any(SerialNum.class));
        Mockito.verify(chargeRecordService).create(any(ChargeRecord.class));
    }

    @Ignore
    @Test
    public void testExec5() {
        ActivitiesWinPojo activitiesWinPojo = createActivitiesWinPojo(ActivityType.LOTTERY.getCode());
        ActivityWinRecord record = createActivityWinRecord();
        ChargeResult chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.SUCCESS);


        String jsonStr = JSON.toJSONString(activitiesWinPojo);
        activityWorker.setTaskString(jsonStr);
        Mockito.when(activityWinRecordService.selectByRecordId(anyString())).thenReturn(record);
        Mockito.when(serialNumService.insert(any(SerialNum.class))).thenReturn(true);
        Mockito.when(chargeRecordService.create(any(ChargeRecord.class))).thenReturn(true);
        Mockito.when(chargeService.charge(anyLong(), anyLong(), anyLong(), anyLong(),
            any(AccountType.class), anyString(), anyString())).thenReturn(chargeResult);
        Mockito.when(chargeRecordService.updateByPrimaryKeySelective(any(ChargeRecord.class)))
            .thenReturn(false);
        Mockito.when(activityWinRecordService.updateByPrimaryKeySelective(any(ActivityWinRecord.class)))
            .thenReturn(false);
        activityWorker.exec();
        Mockito.verify(activityWinRecordService).selectByRecordId(anyString());
        Mockito.verify(serialNumService).insert(any(SerialNum.class));
        Mockito.verify(chargeRecordService).create(any(ChargeRecord.class));
        Mockito.verify(chargeService).charge(anyLong(), anyLong(), anyLong(), anyLong(),
            any(AccountType.class), anyString(), anyString());
        Mockito.verify(chargeRecordService).updateByPrimaryKeySelective(any(ChargeRecord.class));
        Mockito.verify(activityWinRecordService).updateByPrimaryKeySelective(any(ActivityWinRecord.class));
    }

    @Ignore
    @Test
    public void testExec6() {
        ActivitiesWinPojo activitiesWinPojo = createActivitiesWinPojo(ActivityType.FLOWCARD.getCode());
        ActivityWinRecord record = createActivityWinRecord();
        ChargeResult chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.PROCESSING);


        String jsonStr = JSON.toJSONString(activitiesWinPojo);
        activityWorker.setTaskString(jsonStr);
        Mockito.when(activityWinRecordService.selectByRecordId(anyString())).thenReturn(record);
        Mockito.when(serialNumService.insert(any(SerialNum.class))).thenReturn(true);
        Mockito.when(chargeRecordService.create(any(ChargeRecord.class))).thenReturn(true);
        Mockito.when(chargeService.charge(anyLong(), anyLong(), anyLong(), anyLong(),
            any(AccountType.class), anyString(), anyString())).thenReturn(chargeResult);
        Mockito.when(chargeRecordService.updateByPrimaryKeySelective(any(ChargeRecord.class)))
            .thenReturn(false);
        Mockito.when(activityWinRecordService.updateByPrimaryKeySelective(any(ActivityWinRecord.class)))
            .thenReturn(false);
        activityWorker.exec();
        Mockito.verify(activityWinRecordService).selectByRecordId(anyString());
        Mockito.verify(serialNumService).insert(any(SerialNum.class));
        Mockito.verify(chargeRecordService).create(any(ChargeRecord.class));
        Mockito.verify(chargeService).charge(anyLong(), anyLong(), anyLong(), anyLong(),
            any(AccountType.class), anyString(), anyString());
        Mockito.verify(chargeRecordService).updateByPrimaryKeySelective(any(ChargeRecord.class));
        Mockito.verify(activityWinRecordService).updateByPrimaryKeySelective(any(ActivityWinRecord.class));
    }

    @Ignore
    @Test
    public void testExec7() {
        ActivitiesWinPojo activitiesWinPojo = createActivitiesWinPojo(ActivityType.QRCODE.getCode());
        ActivityWinRecord record = createActivityWinRecord();
        ChargeResult chargeResult = new ChargeResult(ChargeResult.ChargeResultCode.FAILURE);


        String jsonStr = JSON.toJSONString(activitiesWinPojo);
        activityWorker.setTaskString(jsonStr);
        Mockito.when(activityWinRecordService.selectByRecordId(anyString())).thenReturn(record);
        Mockito.when(serialNumService.insert(any(SerialNum.class))).thenReturn(true);
        Mockito.when(chargeRecordService.create(any(ChargeRecord.class))).thenReturn(true);
        Mockito.when(chargeService.charge(anyLong(), anyLong(), anyLong(), anyLong(),
            any(AccountType.class), anyString(), anyString())).thenReturn(chargeResult);
        Mockito.when(chargeRecordService.updateByPrimaryKeySelective(any(ChargeRecord.class)))
            .thenReturn(false);
        Mockito.when(activityWinRecordService.updateByPrimaryKeySelective(any(ActivityWinRecord.class)))
            .thenReturn(false);
        activityWorker.exec();
        Mockito.verify(activityWinRecordService).selectByRecordId(anyString());
        Mockito.verify(serialNumService).insert(any(SerialNum.class));
        Mockito.verify(chargeRecordService).create(any(ChargeRecord.class));
        Mockito.verify(chargeService).charge(anyLong(), anyLong(), anyLong(), anyLong(),
            any(AccountType.class), anyString(), anyString());
        Mockito.verify(chargeRecordService).updateByPrimaryKeySelective(any(ChargeRecord.class));
        Mockito.verify(activityWinRecordService).updateByPrimaryKeySelective(any(ActivityWinRecord.class));
    }

    private ActivityWinRecord createActivityWinRecord() {
        ActivityWinRecord record = new ActivityWinRecord();
        record.setId(1L);
        record.setPrizeId(1L);
        record.setChargeMobile("18867103333");
        return record;
    }

    private ActivitiesWinPojo createActivitiesWinPojo(Integer type) {
        ActivitiesWinPojo activitiesWinPojo = new ActivitiesWinPojo();
        Activities activities = new Activities();
        activities.setId(1L);
        activities.setActivityId("test");
        activities.setType(type);
        activities.setEntId(1L);
        activitiesWinPojo.setActivities(activities);
        activitiesWinPojo.setActivitiesWinRecordId("test");
        return activitiesWinPojo;
    }


}
