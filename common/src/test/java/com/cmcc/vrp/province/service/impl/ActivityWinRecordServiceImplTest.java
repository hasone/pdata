package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ActivityWinRecordStatus;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.FlowcardChargeChannelType;
import com.cmcc.vrp.province.dao.ActivityWinRecordMapper;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.CallbackPojo;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.JudgeIspService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.util.QueryObject;

/**
 * ActivityWinRecordServiceImplTest.java
 *
 * @author wujiamin
 * @date 2016年11月4日
 */
@RunWith(MockitoJUnitRunner.class)
public class ActivityWinRecordServiceImplTest {
    @InjectMocks
    ActivityWinRecordService service = new ActivityWinRecordServiceImpl();
    @Mock
    ActivityWinRecordMapper mapper;
    @Mock
    TaskProducer taskProducer;
    @Mock
    ActivitiesService activitiesService;
    @Mock
    GlobalConfigService globalConfigService;
    @Mock
    JudgeIspService judgeIspService;
    @Mock
    IndividualAccountService individualAccountService;
    @Mock
    ActivityPrizeService activityPrizeService;
    @Mock
    AdministerService administerService;

    @Test
    public void testInsertSelective() {
        assertFalse(service.insertSelective(null));

        when(mapper.insertSelective(Mockito.any(ActivityWinRecord.class))).thenReturn(1);
        assertTrue(service.insertSelective(new ActivityWinRecord()));

        when(mapper.insertSelective(Mockito.any(ActivityWinRecord.class))).thenReturn(0);
        assertFalse(service.insertSelective(new ActivityWinRecord()));

        verify(mapper, times(2)).insertSelective(Mockito.any(ActivityWinRecord.class));
    }

    @Test
    public void testSelectByPrimaryKey() {
        when(mapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(new ActivityWinRecord());
        assertNotNull(service.selectByPrimaryKey(1L));
        verify(mapper, times(1)).selectByPrimaryKey(Mockito.anyLong());
    }

    @Test
    public void testSelectByRecordId() {
        when(mapper.selectByRecordId(Mockito.anyString())).thenReturn(new ActivityWinRecord());
        assertNotNull(service.selectByRecordId("111"));
        verify(mapper, times(1)).selectByRecordId(Mockito.anyString());
    }

    @Test
    public void testUpdateStatus() {
        assertFalse(service.updateStatus(null, ChargeRecordStatus.COMPLETE, "信息", "18888888888"));
        assertFalse(service.updateStatus(1L, null, "信息", "18888888888"));

        when(mapper.updateStatus(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyString(), anyString())).thenReturn(1);
        assertTrue(service.updateStatus(1L, ChargeRecordStatus.COMPLETE, "信息", "18888888888"));

        when(mapper.updateStatus(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyString(), anyString())).thenReturn(0);
        assertFalse(service.updateStatus(1L, ChargeRecordStatus.COMPLETE, "信息", "18888888888"));

        verify(mapper, times(2)).updateStatus(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyString(), anyString());
    }

    @Test
    public void testUpdateActivityStatus() {
        assertFalse(service.updateActivityStatus(null, ActivityWinRecordStatus.SUCCESS, "信息", "18888888888"));
        assertFalse(service.updateActivityStatus(1L, null, "信息", "18888888888"));

        when(mapper.updateStatusAndStatusCode(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString(), anyString())).thenReturn(1);
        assertTrue(service.updateActivityStatus(1L, ActivityWinRecordStatus.SUCCESS, "信息", "18888888888"));

        when(mapper.updateStatusAndStatusCode(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString(), anyString())).thenReturn(0);
        assertFalse(service.updateActivityStatus(1L, ActivityWinRecordStatus.SUCCESS, "信息", "18888888888"));

        verify(mapper, times(2)).updateStatusAndStatusCode(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString(), anyString());
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        when(mapper.updateByPrimaryKeySelective(Mockito.any(ActivityWinRecord.class))).thenReturn(1);
        assertTrue(service.updateByPrimaryKeySelective(new ActivityWinRecord()));

        when(mapper.updateByPrimaryKeySelective(Mockito.any(ActivityWinRecord.class))).thenReturn(0);
        assertFalse(service.updateByPrimaryKeySelective(new ActivityWinRecord()));

        verify(mapper, times(2)).updateByPrimaryKeySelective(Mockito.any(ActivityWinRecord.class));
    }

    @Test
    public void testUpdateByPrimaryKey() {
        when(mapper.updateByPrimaryKey(Mockito.any(ActivityWinRecord.class))).thenReturn(1);
        assertTrue(service.updateByPrimaryKey(new ActivityWinRecord()));

        when(mapper.updateByPrimaryKey(Mockito.any(ActivityWinRecord.class))).thenReturn(0);
        assertFalse(service.updateByPrimaryKey(new ActivityWinRecord()));

        verify(mapper, times(2)).updateByPrimaryKey(Mockito.any(ActivityWinRecord.class));
    }

    @Test
    public void testBatchUpdateStatus() {
        when(mapper.batchUpdateStatus(Mockito.anyList(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any(Date.class)))
                .thenReturn(createRecordIds().size());
        assertTrue(service.batchUpdateStatus(createRecordIds(), "18867103685", 1, 1, new Date()));

        when(mapper.batchUpdateStatus(Mockito.anyList(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any(Date.class)))
                .thenReturn(createRecordIds().size() + 1);
        assertFalse(service.batchUpdateStatus(createRecordIds(), "18867103685", 1, 1, new Date()));

        verify(mapper, times(2)).batchUpdateStatus(Mockito.anyList(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any(Date.class));
    }

    @Test
    public void testGetCurrentActivityInfo0() {
        when(mapper.getCurrentActivityInfo(Mockito.anyString())).thenReturn(null);

        when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(createActivity());
        when(mapper.countChargeMobileByMap(Mockito.anyMap())).thenReturn(1);

        assertNotNull(service.getCurrentActivityInfo("123456"));

        when(mapper.getCurrentActivityInfo(Mockito.anyString())).thenReturn(new ArrayList());
        assertNotNull(service.getCurrentActivityInfo("123456"));

        verify(mapper, times(2)).getCurrentActivityInfo(Mockito.anyString());
        verify(activitiesService, times(2)).selectByActivityId(Mockito.anyString());
        verify(mapper, times(2)).countChargeMobileByMap(Mockito.anyMap());
    }

    @Test
    public void testGetCurrentActivityInfo1() {
        when(mapper.getCurrentActivityInfo(Mockito.anyString())).thenReturn(createRecords());

        Activities act = createActivity();
        act.setType(ActivityType.COMMON_REDPACKET.getCode());
        when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(act);

        assertNotNull(service.getCurrentActivityInfo("123456"));

        verify(mapper).getCurrentActivityInfo(Mockito.anyString());
        verify(activitiesService).selectByActivityId(Mockito.anyString());
    }

    @Test
    public void testShowForPageResultCount() {
        assertEquals(service.showForPageResultCount(null), 0);

        when(mapper.showForPageResultCount(Mockito.anyMap())).thenReturn(1);
        QueryObject map = new QueryObject();
        map.getQueryCriterias().put("status", "1,2,3");
        assertEquals(service.showForPageResultCount(map), 1);

        verify(mapper).showForPageResultCount(Mockito.anyMap());
    }

    @Test
    public void testShowForPageResult() {
        assertNull(service.showForPageResult(null));

        when(mapper.showForPageResult(Mockito.anyMap())).thenReturn(new ArrayList());
        QueryObject map = new QueryObject();
        map.getQueryCriterias().put("status", "1,2,3");
        assertNotNull(service.showForPageResult(map));

        verify(mapper).showForPageResult(Mockito.anyMap());
    }

    @Test
    public void testSelectByMap() {
        when(mapper.selectByMap(Mockito.anyMap())).thenReturn(new ArrayList());
        assertNotNull(service.selectByMap(new HashMap()));
        verify(mapper).selectByMap(Mockito.anyMap());
    }

    @Test
    public void testBatchInsertForFlowcard() {
        assertFalse(service.batchInsertForFlowcard(null, 1L, 1L, 1L, "18867103685", "18867103685", "18867103685"));

        assertFalse(service.batchInsertForFlowcard("123", 1L, 1L, 1L, "", "", ""));

        when(judgeIspService.judgeIsp(Mockito.anyString())).thenReturn("M");
        when(mapper.batchInsert(Mockito.anyList())).thenReturn(3);
        assertTrue(service.batchInsertForFlowcard("123", 1L, 1L, 1L, "18867103685", "18867103685", "18867103685"));

        when(mapper.batchInsert(Mockito.anyList())).thenReturn(4);
        assertFalse(service.batchInsertForFlowcard("123", 1L, 1L, 1L, "18867103685", "18867103685", "18867103685"));

        verify(mapper, times(2)).batchInsert(Mockito.anyList());
        verify(judgeIspService, times(2 * 3)).judgeIsp(Mockito.anyString());
    }


    @Test
    public void testBatchInsertForFlowcard1() {
        when(judgeIspService.judgeIsp(Mockito.anyString())).thenReturn("M");
        when(mapper.batchInsert(Mockito.anyList())).thenReturn(4);
        assertFalse(service.batchInsertForFlowcard("123", null, 1L, 1L, "18867103685", "18867103685", "18867103685"));

        verify(mapper).batchInsert(Mockito.anyList());
    }

    @Test
    public void testBatchInsertForQRcode() {
        assertFalse(service.batchInsertForQRcode(null, null));
        assertFalse(service.batchInsertForQRcode("123456", null));
        ActivityInfo actInfo = new ActivityInfo();
        actInfo.setPrizeCount(0L);
        assertFalse(service.batchInsertForQRcode("123456", actInfo));

        actInfo.setPrizeCount(10L);
        when(mapper.batchInsert(Mockito.anyList())).thenReturn(0);
        assertFalse(service.batchInsertForQRcode("123456", actInfo));

        when(mapper.batchInsert(Mockito.anyList())).thenReturn(10);
        assertTrue(service.batchInsertForQRcode("123456", actInfo));

        verify(mapper, times(2)).batchInsert(Mockito.anyList());
    }

    @Test
    public void testDownLoadPhones() {
        HttpServletResponse response = null;
        HttpServletRequest request = null;
        assertFalse(service.downLoadPhones(request, response, null, new ArrayList(), "fileName.txt", ActivityType.QRCODE.getCode().toString()));


        when(globalConfigService.get(Mockito.anyString())).thenReturn("test");
        assertFalse(service.downLoadPhones(request, response, 1L, createPhoneList(), "fileName.txt", ActivityType.QRCODE.getCode().toString()));

        when(globalConfigService.get(Mockito.anyString())).thenReturn("test");
        assertFalse(service.downLoadPhones(request, response, 1L, createPhoneList(), "fileName.txt", null));
    }

    @Test
    public void testSelectByActivityId() {
        assertNull(service.selectByActivityId(null));

        when(mapper.selectByActivityId(Mockito.anyString())).thenReturn(new ArrayList());
        assertNotNull(service.selectByActivityId("123"));
        verify(mapper).selectByActivityId(Mockito.anyString());
    }

    @Test
    public void testSelectByActivityIdAndIsp() {
        assertNull(service.selectByActivityIdAndIsp(null, "M"));
        assertNull(service.selectByActivityIdAndIsp("123", null));

        when(mapper.selectByActivityIdAndIsp(Mockito.anyString(), Mockito.anyString())).thenReturn(new ArrayList());
        assertNotNull(service.selectByActivityIdAndIsp("123", "M"));
        verify(mapper).selectByActivityIdAndIsp(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testDeleteByActivityId() {
        when(mapper.deleteByActivityId(Mockito.anyString())).thenReturn(0);
        assertFalse(service.deleteByActivityId(Mockito.anyString()));

        when(mapper.deleteByActivityId(Mockito.anyString())).thenReturn(1);
        assertTrue(service.deleteByActivityId(Mockito.anyString()));

        verify(mapper, times(2)).deleteByActivityId(Mockito.anyString());
    }

    @Test
    public void testSelectByMapForRedpacket() {
        when(mapper.selectByMapForRedpacket(Mockito.anyMap())).thenReturn(new ArrayList());
        assertNotNull(service.selectByMapForRedpacket(Mockito.anyMap()));
        verify(mapper).selectByMapForRedpacket(Mockito.anyMap());
    }

    @Test
    public void testCountByMapForRedpacket() {
        when(mapper.countByMapForRedpacket(Mockito.anyMap())).thenReturn(0L);
        assertNotNull(service.countByMapForRedpacket(Mockito.anyMap()));
        verify(mapper).countByMapForRedpacket(Mockito.anyMap());
    }

    @Test
    public void testBatchInsertFlowcoinPresent() {
        assertFalse(service.batchInsertFlowcoinPresent(null, "18867103685", 1L, "18867103581"));

        assertFalse(service.batchInsertFlowcoinPresent("123", null, 1L, "18867103581"));

        when(mapper.batchInsert(Mockito.anyList())).thenReturn(1);
        assertTrue(service.batchInsertFlowcoinPresent("123", "18867103685", 1L, "18867103581"));

        when(mapper.batchInsert(Mockito.anyList())).thenReturn(0);
        assertFalse(service.batchInsertFlowcoinPresent("123", "18867103685", 1L, "18867103581"));

        verify(mapper, times(2)).batchInsert(Mockito.anyList());
    }

    @Test
    public void testUpdateForIndividualRedpacket() {
        assertFalse(service.updateForIndividualRedpacket(null, "123", 1, "123"));
        assertFalse(service.updateForIndividualRedpacket(new CallbackPojo(), null, 1, "123"));
        assertFalse(service.updateForIndividualRedpacket(new CallbackPojo(), "123", null, "123"));
        assertFalse(service.updateForIndividualRedpacket(new CallbackPojo(), "123", 1, null));

        when(mapper.updateByPrimaryKeySelective(Mockito.any(ActivityWinRecord.class))).thenReturn(1);
        assertTrue(service.updateForIndividualRedpacket(new CallbackPojo(), "123", 1, "123"));

        when(mapper.updateByPrimaryKeySelective(Mockito.any(ActivityWinRecord.class))).thenReturn(0);
        assertFalse(service.updateForIndividualRedpacket(new CallbackPojo(), "123", 1, "123"));

        verify(mapper, times(2)).updateByPrimaryKeySelective(Mockito.any(ActivityWinRecord.class));
    }

    @Test
    public void testInsertForIndividualRedpacket1() {
        assertFalse(service.insertForIndividualRedpacket(null, "123"));
        assertFalse(service.insertForIndividualRedpacket(new CallbackPojo(), null));

        CallbackPojo pojo = new CallbackPojo();
        pojo.setPrizeCount(1);
        pojo.setEnterId("1");
        pojo.setPrizeId("1");

        when(mapper.insertSelective(Mockito.any(ActivityWinRecord.class))).thenReturn(0);
        when(administerService.selectAdministerById(Mockito.anyLong())).thenReturn(new Administer());

        assertFalse(service.insertForIndividualRedpacket(pojo, "123"));

        when(mapper.insertSelective(Mockito.any(ActivityWinRecord.class))).thenReturn(1);
        when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(null);
        assertFalse(service.insertForIndividualRedpacket(pojo, "123"));

    }

    @Test
    public void testInsertForIndividualRedpacket2() {
        CallbackPojo pojo = new CallbackPojo();
        pojo.setPrizeCount(1);
        pojo.setEnterId("1");
        pojo.setPrizeId("1");
        pojo.setActiveId("123");

        when(administerService.selectAdministerById(Mockito.anyLong())).thenReturn(new Administer());
        when(mapper.insertSelective(Mockito.any(ActivityWinRecord.class))).thenReturn(1);
        when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(createActivity());

        when(activityPrizeService.selectByActivityIdForIndividual(Mockito.anyString())).thenReturn(null);
        assertFalse(service.insertForIndividualRedpacket(pojo, "123"));


        when(activityPrizeService.selectByActivityIdForIndividual(Mockito.anyString())).thenReturn(new ArrayList());
        assertFalse(service.insertForIndividualRedpacket(pojo, "123"));

        when(activityPrizeService.selectByActivityIdForIndividual(Mockito.anyString())).thenReturn(createActivityPrize());

        when(individualAccountService.getAccountByOwnerIdAndProductId(Mockito.anyLong(),
                Mockito.anyLong(), Mockito.anyInt())).thenReturn(null);

        assertFalse(service.insertForIndividualRedpacket(pojo, "123"));

        when(individualAccountService.getAccountByOwnerIdAndProductId(Mockito.anyLong(),
                Mockito.anyLong(), Mockito.anyInt())).thenReturn(new IndividualAccount());
        when(individualAccountService.changeAccount(Mockito.any(IndividualAccount.class), Mockito.any(BigDecimal.class), Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(false);

        try{
            service.insertForIndividualRedpacket(pojo, "123");
        }catch(Exception e){
            
        }

    }

    @Test
    public void testInsertForIndividualRedpacket3() {
        CallbackPojo pojo = new CallbackPojo();
        pojo.setPrizeCount(1);
        pojo.setEnterId("1");
        pojo.setPrizeId("1");
        pojo.setActiveId("123");

        when(administerService.selectAdministerById(Mockito.anyLong())).thenReturn(new Administer());
        when(mapper.insertSelective(Mockito.any(ActivityWinRecord.class))).thenReturn(1);
        when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(createActivity());
        when(activityPrizeService.selectByActivityIdForIndividual(Mockito.anyString())).thenReturn(createActivityPrize());
        when(individualAccountService.getAccountByOwnerIdAndProductId(Mockito.anyLong(),
                Mockito.anyLong(), Mockito.anyInt())).thenReturn(new IndividualAccount());
        when(individualAccountService.changeAccount(Mockito.any(IndividualAccount.class), Mockito.any(BigDecimal.class), Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);

        assertTrue(service.insertForIndividualRedpacket(pojo, "123"));
    }

    @Test
    public void testShowForPageResultCountIndividualPresent() {
        assertEquals(service.showForPageResultCountIndividualPresent(null), 0);

        when(mapper.showForPageResultCountIndividualPresent(Mockito.anyMap())).thenReturn(1);
        assertEquals(service.showForPageResultCountIndividualPresent(new QueryObject()), 1);

        verify(mapper).showForPageResultCountIndividualPresent(Mockito.anyMap());
    }

    @Test
    public void testShowForPageResultIndividualPresent() {
        when(mapper.showForPageResultIndividualPresent(Mockito.anyMap())).thenReturn(new ArrayList());
        assertNotNull(service.showForPageResultIndividualPresent(new QueryObject()));

        verify(mapper).showForPageResultIndividualPresent(Mockito.anyMap());
    }

    @Test
    public void testBatchUpdate() {
        assertFalse(service.batchUpdate(null));

        List<ActivityWinRecord> records = new ArrayList();
        when(mapper.batchUpdate(Mockito.anyList())).thenReturn(records.size());
        assertTrue(service.batchUpdate(records));
        verify(mapper).batchUpdate(Mockito.anyList());
    }

    @Test
    public void testUpdateStatusCodeByRecordId() {
        String recordId = "123";
        String statusCode = "201";

        when(mapper.updateStatusCodeByRecordId(Mockito.anyString(), Mockito.anyString())).thenReturn(-1).thenReturn(1);
        assertFalse(service.updateStatusCodeByRecordId(null, null));
        assertFalse(service.updateStatusCodeByRecordId(recordId, statusCode));
        assertTrue(service.updateStatusCodeByRecordId(recordId, statusCode));
        verify(mapper, Mockito.times(2)).updateStatusCodeByRecordId(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testBatchUpdateStatusCodeByRecordId() {
        List<String> sns = new ArrayList();
        sns.add("111");
        String statusCode = "201";

        when(mapper.batchUpdateStatusCodeByRecordId(Mockito.anyList(), Mockito.anyString())).thenReturn(-1).thenReturn(1);
        assertFalse(service.batchUpdateStatusCodeByRecordId(null, null));
        assertFalse(service.batchUpdateStatusCodeByRecordId(new ArrayList<String>(), null));
        assertFalse(service.batchUpdateStatusCodeByRecordId(sns, statusCode));
        assertTrue(service.batchUpdateStatusCodeByRecordId(sns, statusCode));
        verify(mapper, Mockito.times(2)).batchUpdateStatusCodeByRecordId(Mockito.anyList(), Mockito.anyString());
    }
    
    @Test
    public void testSelectIndividualFlowRedpacketList() {       
        when(mapper.selectIndividualFlowRedpacketList(Mockito.anyMap())).thenReturn(new ArrayList());       
        assertNotNull(service.selectIndividualFlowRedpacketList(new HashMap()));
        verify(mapper, Mockito.times(1)).selectIndividualFlowRedpacketList(Mockito.anyMap());
    }


    private List<String> createRecordIds() {
        List<String> recordIds = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            recordIds.add(Integer.toString(i));
        }
        return recordIds;
    }

    private Activities createActivity() {
        Activities act = new Activities();
        act.setActivityId("1213456");
        act.setType(ActivityType.QRCODE.getCode());
        return act;
    }

    private List<ActivityWinRecord> createRecords() {
        List<ActivityWinRecord> records = new ArrayList<ActivityWinRecord>();
        for (int i = 0; i < 10; i++) {
            ActivityWinRecord record = new ActivityWinRecord();
            record.setProductSize(10240L);
            if (i % 2 == 0) {
                record.setPrice(1000L);
            }
            records.add(record);
        }
        return records;
    }

    private List<String> createPhoneList() {
        List<String> s = new ArrayList();
        s.add("18867103685");
        return s;
    }

    private List<ActivityPrize> createActivityPrize() {
        List<ActivityPrize> lists = new ArrayList();
        ActivityPrize p = new ActivityPrize();
        p.setProductId(1L);
        lists.add(p);
        return lists;
    }
    
    @Test
    public void testCountChargeMobileByActivityId(){
        Mockito.when(mapper.countChargeMobileByActivityId(Mockito.anyString()))
        .thenReturn(1);
        assertSame(1, service.countChargeMobileByActivityId("test"));
    }
    
    @Test
    public void testCountIndividualFlowRedpacketList(){
        Mockito.when(mapper.countIndividualFlowRedpacketList(Mockito.anyMap())).thenReturn(1);
        assertSame(1, service.countIndividualFlowRedpacketList(new HashMap<String, String>()));
    }
    
    @Test
    public void testCountWinRecords(){
        Mockito.when(mapper.countWinRecords(Mockito.anyMap())).thenReturn(1);
        HashMap map = new HashMap<String, String>();
        map.put("status", "1,2,3");
        map.put("payResult", "1,2,3");        
        assertSame(1, service.countWinRecords(map));
    }
    
    @Test
    public void testGetWinRecordsForCrowdFunding(){
        Mockito.when(mapper.getWinRecordsForCrowdFunding(Mockito.anyMap())).thenReturn(new ArrayList<ActivityWinRecord>());
        assertNotNull(service.getWinRecordsForCrowdFunding(new HashMap<String, String>()));
    }
    

    @Test
    public void testInsertForIndividualFlowRedpacket() throws RuntimeException{
        CallbackPojo pojo = createCallbackPojo();
        assertFalse(service.insertForIndividualFlowRedpacket(pojo, ""));
        
        Mockito.when(mapper.insertSelective(Mockito.any(ActivityWinRecord.class)))
        .thenReturn(0).thenReturn(1);
        assertFalse(service.insertForIndividualFlowRedpacket(pojo, "123"));
    
        Activities activities = new Activities();
        activities.setActivityId("test");
        activities.setId(1L);
        activities.setType(1);
        List<ActivityPrize> activityPrizes = new ArrayList<ActivityPrize>();
        ActivityPrize prize = new ActivityPrize();
        prize.setActivityId("test");
        prize.setProductId(1L);
        activityPrizes.add(prize);
        IndividualAccount individualAccount = new IndividualAccount();
        individualAccount.setId(1L);
        
        Mockito.when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(activities);
        Mockito.when(activityPrizeService.selectByActivityIdForIndividual(Mockito.anyString())).thenReturn(activityPrizes);
        Mockito.when(individualAccountService.getAccountByOwnerIdAndProductId(Mockito.anyLong(),
                Mockito.anyLong(), Mockito.anyInt())).thenReturn(individualAccount);
        try{
            assertFalse(service.insertForIndividualFlowRedpacket(pojo, "123"));
        }catch(Exception e){
            
        }
        
        when(individualAccountService.changeAccount(Mockito.any(IndividualAccount.class), Mockito.any(BigDecimal.class), Mockito.anyString(), 
                Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
        assertTrue(service.insertForIndividualFlowRedpacket(pojo, "123"));
    }
    

    private CallbackPojo createCallbackPojo(){
        CallbackPojo pojo = new CallbackPojo();
        pojo.setActiveId("test");
        pojo.setMobile("18867101234");
        pojo.setPrizeId("1");
        pojo.setPrizeCount(10);
        return pojo;
    }
    
    @Test
    public void testSelectByActivityIdAndMobile(){
        assertNull(service.selectByActivityIdAndMobile("test", ""));
        Mockito.when(mapper.selectByActivityIdAndMobile(Mockito.anyString(), Mockito.anyString())).thenReturn(new ActivityWinRecord());
        assertNotNull(service.selectByActivityIdAndMobile("test", "18867101234"));
    }
    
    @Test
    public void testShowWinRecords(){
        Mockito.when(mapper.showWinRecords(Mockito.anyMap())).thenReturn(new ArrayList<ActivityWinRecord>());
        HashMap map = new HashMap<String, String>();
        map.put("status", "1,2,3");
        map.put("payResult", "1,2,3");        
        assertNotNull(service.showWinRecords(map));
    }
    
    @Test
    public void testBatchUpdateForFlowcard(){
        Mockito.when(mapper.batchUpdateForFlowcard(Mockito.anyList(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any(Date.class), Mockito.anyString())).thenReturn(0);
        assertTrue(service.batchUpdateForFlowcard(new ArrayList<String>(), "18867103685", 1, 1, new Date(), FlowcardChargeChannelType.HJS.getCode()));
    }
    
    @Test
    public void testSelectAllWinRecords(){
        HashMap map = new HashMap<String, String>();
        map.put("status", "1,2,3");
        map.put("payResult", "1,2,3");        
        Mockito.when(mapper.selectAllWinRecords(Mockito.anyMap())).thenReturn(new ArrayList());
        assertNotNull(service.selectAllWinRecords(map));
    }
    
    @Test
    public void testGetWinRecordsForCrowdFundingByMap(){
        Mockito.when(mapper.getWinRecordsForCrowdFundingByMap(Mockito.anyMap())).thenReturn(new ArrayList());
        assertNotNull(service.getWinRecordsForCrowdFundingByMap(new HashMap()));
    }
    
    @Test
    public void testSelectActivityIdByMobile(){
        Mockito.when(mapper.selectActivityIdByMobile(Mockito.anyString())).thenReturn(new ArrayList());
        assertNotNull(service.selectActivityIdByMobile("18867103685"));
    }
}
