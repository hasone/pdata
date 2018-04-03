package com.cmcc.vrp.queue.task;

import com.alibaba.fastjson.JSONArray;
import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.charge.ChargeService;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.pojo.PresentPojo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;

/**
 * Created by qinqinyan on 2016/11/8.
 */
@RunWith(MockitoJUnitRunner.class)
public class FlowCardWorkerTest {
    @InjectMocks
    FlowCardWorker flowCardWorker = new FlowCardWorker();
    @Mock
    ChargeService chargeService;
    @Mock
    ChargeRecordService chargeRecordService;
    @Mock
    SerialNumService serialNumService;
    @Mock
    ActivityWinRecordService activityWinRecordService;
    @Mock
    ActivitiesService activitiesService;

    @Test
    public void testExec1() {
        PresentPojo presentPojo = new PresentPojo();
        presentPojo.setRuleId(1L);
        presentPojo.setRecordId(1L);
        presentPojo.setMobile("18867101111");
        presentPojo.setProductId(1L);
        presentPojo.setEnterpriseId(1L);
        presentPojo.setRequestSerialNum("test");
        String presentPojoJson = JSONArray.toJSONString(presentPojo);

        flowCardWorker.setTaskString(presentPojoJson);
        Mockito.when(activityWinRecordService.selectByPrimaryKey(anyLong())).thenReturn(null);
        flowCardWorker.exec();
    }

    @Test
    public void testExec2() {
        PresentPojo presentPojo = new PresentPojo();
        presentPojo.setRuleId(1L);
        presentPojo.setRecordId(1L);
        presentPojo.setMobile("18867101111");
        presentPojo.setProductId(1L);
        presentPojo.setEnterpriseId(1L);
        presentPojo.setRequestSerialNum("test");
        String presentPojoJson = JSONArray.toJSONString(presentPojo);

        ActivityWinRecord record = new ActivityWinRecord();
        record.setActivityId("test");
        record.setId(1L);

        Activities activity = new Activities();

        ChargeResult.ChargeResultCode code = ChargeResult.ChargeResultCode.SUCCESS;
        ChargeResult chargeResult = new ChargeResult(code);

        flowCardWorker.setTaskString(presentPojoJson);

        Mockito.when(activityWinRecordService.selectByPrimaryKey(anyLong())).thenReturn(record);
        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(activity);
        Mockito.when(chargeRecordService.create(any(ChargeRecord.class))).thenReturn(true);
        Mockito.when(serialNumService.insert(any(SerialNum.class))).thenReturn(true);
        Mockito.when(chargeService.charge(anyLong(), anyLong(), anyLong(),
            anyLong(), any(AccountType.class), anyString(), anyString())).thenReturn(chargeResult);
        Mockito.when(chargeRecordService.updateByPrimaryKeySelective(any(ChargeRecord.class))).thenReturn(false);
        Mockito.when(activityWinRecordService.updateByPrimaryKeySelective(any(ActivityWinRecord.class))).thenReturn(false);
        flowCardWorker.exec();
        Mockito.verify(activityWinRecordService).selectByPrimaryKey(anyLong());
        Mockito.verify(activitiesService).selectByActivityId(anyString());
        Mockito.verify(chargeRecordService).create(any(ChargeRecord.class));
        Mockito.verify(serialNumService).insert(any(SerialNum.class));
        Mockito.verify(chargeService).charge(anyLong(), anyLong(), anyLong(),
            anyLong(), any(AccountType.class), anyString(), anyString());
        Mockito.verify(chargeRecordService).updateByPrimaryKeySelective(any(ChargeRecord.class));
        Mockito.verify(activityWinRecordService).updateByPrimaryKeySelective(any(ActivityWinRecord.class));
    }

    @Test
    public void testExec3() {
        PresentPojo presentPojo = new PresentPojo();
        presentPojo.setRuleId(1L);
        presentPojo.setRecordId(1L);
        presentPojo.setMobile("18867101111");
        presentPojo.setProductId(1L);
        presentPojo.setEnterpriseId(1L);
        presentPojo.setRequestSerialNum("test");
        String presentPojoJson = JSONArray.toJSONString(presentPojo);

        ActivityWinRecord record = new ActivityWinRecord();
        record.setActivityId("test");
        record.setId(1L);

        Activities activity = new Activities();


        ChargeResult.ChargeResultCode code = ChargeResult.ChargeResultCode.PROCESSING;
        ChargeResult chargeResult = new ChargeResult(code);


        flowCardWorker.setTaskString(presentPojoJson);

        Mockito.when(activityWinRecordService.selectByPrimaryKey(anyLong())).thenReturn(record);
        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(activity);
        Mockito.when(chargeRecordService.create(any(ChargeRecord.class))).thenReturn(true);
        Mockito.when(serialNumService.insert(any(SerialNum.class))).thenReturn(true);
        Mockito.when(chargeService.charge(anyLong(), anyLong(), anyLong(),
            anyLong(), any(AccountType.class), anyString(), anyString())).thenReturn(chargeResult);
        Mockito.when(chargeRecordService.updateByPrimaryKeySelective(any(ChargeRecord.class))).thenReturn(false);
        Mockito.when(activityWinRecordService.updateByPrimaryKeySelective(any(ActivityWinRecord.class))).thenReturn(false);
        flowCardWorker.exec();
        Mockito.verify(activityWinRecordService).selectByPrimaryKey(anyLong());
        Mockito.verify(activitiesService).selectByActivityId(anyString());
        Mockito.verify(chargeRecordService).create(any(ChargeRecord.class));
        Mockito.verify(serialNumService).insert(any(SerialNum.class));
        Mockito.verify(chargeService).charge(anyLong(), anyLong(), anyLong(),
            anyLong(), any(AccountType.class), anyString(), anyString());
        Mockito.verify(chargeRecordService).updateByPrimaryKeySelective(any(ChargeRecord.class));
        Mockito.verify(activityWinRecordService).updateByPrimaryKeySelective(any(ActivityWinRecord.class));
    }

    @Test
    public void testExec4() {
        PresentPojo presentPojo = new PresentPojo();
        presentPojo.setRuleId(1L);
        presentPojo.setRecordId(1L);
        presentPojo.setMobile("18867101111");
        presentPojo.setProductId(1L);
        presentPojo.setEnterpriseId(1L);
        presentPojo.setRequestSerialNum("test");
        String presentPojoJson = JSONArray.toJSONString(presentPojo);

        ActivityWinRecord record = new ActivityWinRecord();
        record.setActivityId("test");
        record.setId(1L);

        Activities activity = new Activities();


        ChargeResult.ChargeResultCode code = ChargeResult.ChargeResultCode.FAILURE;
        ChargeResult chargeResult = new ChargeResult(code);


        flowCardWorker.setTaskString(presentPojoJson);

        Mockito.when(activityWinRecordService.selectByPrimaryKey(anyLong())).thenReturn(record);
        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(activity);
        Mockito.when(chargeRecordService.create(any(ChargeRecord.class))).thenReturn(true);
        Mockito.when(serialNumService.insert(any(SerialNum.class))).thenReturn(true);
        Mockito.when(chargeService.charge(anyLong(), anyLong(), anyLong(),
            anyLong(), any(AccountType.class), anyString(), anyString())).thenReturn(chargeResult);
        Mockito.when(chargeRecordService.updateByPrimaryKeySelective(any(ChargeRecord.class))).thenReturn(false);
        Mockito.when(activityWinRecordService.updateByPrimaryKeySelective(any(ActivityWinRecord.class))).thenReturn(false);
        flowCardWorker.exec();
        Mockito.verify(activityWinRecordService).selectByPrimaryKey(anyLong());
        Mockito.verify(activitiesService).selectByActivityId(anyString());
        Mockito.verify(chargeRecordService).create(any(ChargeRecord.class));
        Mockito.verify(serialNumService).insert(any(SerialNum.class));
        Mockito.verify(chargeService).charge(anyLong(), anyLong(), anyLong(),
            anyLong(), any(AccountType.class), anyString(), anyString());
        Mockito.verify(chargeRecordService).updateByPrimaryKeySelective(any(ChargeRecord.class));
        Mockito.verify(activityWinRecordService).updateByPrimaryKeySelective(any(ActivityWinRecord.class));
    }

    @Test
    public void testExec5() {
        PresentPojo presentPojo = new PresentPojo();
        presentPojo.setRuleId(1L);
        presentPojo.setRecordId(1L);
        presentPojo.setMobile("18867101111");
        presentPojo.setProductId(1L);
        presentPojo.setEnterpriseId(1L);
        presentPojo.setRequestSerialNum("test");
        String presentPojoJson = JSONArray.toJSONString(presentPojo);

        ActivityWinRecord record = new ActivityWinRecord();
        record.setActivityId("test");
        record.setId(1L);

        Activities activity = new Activities();

        flowCardWorker.setTaskString(presentPojoJson);

        Mockito.when(activityWinRecordService.selectByPrimaryKey(anyLong())).thenReturn(record);
        Mockito.when(activitiesService.selectByActivityId(anyString())).thenReturn(activity);
        Mockito.when(chargeRecordService.create(any(ChargeRecord.class))).thenReturn(true);
        Mockito.when(serialNumService.insert(any(SerialNum.class))).thenReturn(false);
        Mockito.when(activityWinRecordService.updateByPrimaryKeySelective(any(ActivityWinRecord.class)))
            .thenReturn(false);
        flowCardWorker.exec();
        Mockito.verify(activityWinRecordService).selectByPrimaryKey(anyLong());
        Mockito.verify(activitiesService).selectByActivityId(anyString());
        Mockito.verify(chargeRecordService).create(any(ChargeRecord.class));
        Mockito.verify(serialNumService).insert(any(SerialNum.class));
        Mockito.verify(activityWinRecordService).updateByPrimaryKeySelective(any(ActivityWinRecord.class));
    }
}
