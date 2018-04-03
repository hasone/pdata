package com.cmcc.vrp.queue.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.boss.sichuan.model.flowredpacket.FlowRedPacketReq;
import com.cmcc.vrp.boss.sichuan.model.flowredpacket.FlowRedPacketResp;
import com.cmcc.vrp.boss.sichuan.model.flowredpacket.FlowRedPacketRespOutdata;
import com.cmcc.vrp.boss.sichuan.service.ScFlowRedPacketService;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.pojo.ActivitiesWinPojo;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

@RunWith(MockitoJUnitRunner.class)
public class IndividualActivityWorkerTest {
    @InjectMocks
    IndividualActivityWorker worker = new IndividualActivityWorker();
    @Mock
    ActivityWinRecordService activityWinRecordService;
    @Mock
    GlobalConfigService globalConfigService;
    @Mock
    ActivitiesService activitiesService;
    @Mock
    ActivityPrizeService activityPrizeService;
    @Mock
    ScFlowRedPacketService scFlowRedPacketService;
    @Mock
    SerialNumService serialNumService;
    @Mock
    IndividualAccountService individualAccountService;
    @Mock
    IndividualProductService individualProductService;

    @Test
    public void exec(){

        worker.exec();
        
        worker.setTaskString(JSONObject.toJSONString(createInvalidatePojo()));
        worker.exec();
        
        ActivityWinRecord activityWinRecord = new ActivityWinRecord();
        activityWinRecord.setStatus(2);
        Mockito.when(activityWinRecordService.selectByRecordId(Mockito.anyString())).thenReturn(activityWinRecord);
        worker.setTaskString(JSONObject.toJSONString(createPojo()));
        worker.exec();
        
        activityWinRecord.setStatus(1);
        Mockito.when(activityWinRecordService.selectByRecordId(Mockito.anyString())).thenReturn(activityWinRecord);
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.DYNAMIC_PROXY_BOSS_FLAG.getKey())).thenReturn("true");
        worker.setTaskString(JSONObject.toJSONString(createPojo()));
        worker.exec();
        
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.DYNAMIC_PROXY_BOSS_FLAG.getKey())).thenReturn("false");
        worker.setTaskString(JSONObject.toJSONString(createPojo()));
        FlowRedPacketRespOutdata outData = new FlowRedPacketRespOutdata();
        outData.setOrderId("testOrderId");
        FlowRedPacketResp resp = new FlowRedPacketResp();
        resp.setOutData(outData);
        resp.setResCode("0000000");
        Mockito.when(scFlowRedPacketService.sendRequest(Mockito.any(FlowRedPacketReq.class))).thenReturn(resp);
        Mockito.when(serialNumService.insert(Mockito.any(SerialNum.class))).thenReturn(true); 
        worker.exec();
        
        
        resp.setResCode("0000001");
        Mockito.when(scFlowRedPacketService.sendRequest(Mockito.any(FlowRedPacketReq.class))).thenReturn(resp);        
        Mockito.when(activityWinRecordService.updateByPrimaryKeySelective(Mockito.any(ActivityWinRecord.class))).thenReturn(false);
        worker.exec();
        
        Mockito.when(scFlowRedPacketService.sendRequest(Mockito.any(FlowRedPacketReq.class))).thenReturn(null);               
        worker.exec();
    }
    
    private ActivitiesWinPojo createInvalidatePojo(){
        ActivitiesWinPojo pojo = new ActivitiesWinPojo();
        pojo.setActivitiesWinRecordId("activityWinRecordId");
        Activities activities = new Activities();
        activities.setActivityId("activityId");
        pojo.setActivities(activities);
        return pojo;
    }
    
    private ActivitiesWinPojo createPojo(){
        ActivitiesWinPojo pojo = new ActivitiesWinPojo();
        pojo.setActivitiesWinRecordId("activityWinRecordId");
        Activities activities = new Activities();
        activities.setActivityId("activityId");
        pojo.setActivities(activities);
        return pojo;
    }
}
