package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.charge.ChargeResult.ChargeResultCode;
import com.cmcc.vrp.ec.bean.CallBackReq;
import com.cmcc.vrp.ec.bean.CallBackReqData;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ActivityWinRecordStatus;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.dao.ChargeRecordMapper;
import com.cmcc.vrp.province.dao.PresentRecordMapper;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.InterfaceRecord;
import com.cmcc.vrp.province.model.PresentRecord;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.InterfaceRecordService;
import com.cmcc.vrp.province.service.ManagerService;
import com.cmcc.vrp.province.service.MonthlyPresentRecordService;
import com.cmcc.vrp.province.service.MonthlyPresentRuleService;
import com.cmcc.vrp.province.service.PresentRecordService;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by sunyiwei on 16/8/29.
 */
@RunWith(MockitoJUnitRunner.class)
public class ChargeRecordServiceImplTest {
    @InjectMocks
    ChargeRecordService chargeRecordService = new ChargeRecordServiceImpl();

    @Mock
    ChargeRecordMapper chargeRecordMapper;

    @Mock
    PresentRecordService presentRecordService;

    @Mock
    ActivityWinRecordService activityWinRecordService;

    @Mock
    InterfaceRecordService interfaceRecordService;

    @Mock
    EnterprisesService enterpriseService;

    @Mock
    ManagerService managerService;

    @Mock
    PresentRecordMapper presentRecordMapper;
    
    @Mock
    MonthlyPresentRuleService monthlyPresentRuleService;
    
    @Mock
    MonthlyPresentRecordService monthlyPresentRecordService;
    
    @Mock
    GlobalConfigService globalConfigService;

    private static String randStr(int length) {
        StringBuilder sb = new StringBuilder();

        Random r = new Random();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + r.nextInt(26)));
        }

        return sb.toString();
    }

    @Test
    public void updateStatusAndStatusCodeTest() {
        Assert.assertFalse(chargeRecordService.updateStatusAndStatusCode(1l, "xxxx", 1, "",null, null));
        Assert.assertFalse(chargeRecordService.updateStatusAndStatusCode(1l, "xxxx", 1, "xxxx",null, null));
        when(chargeRecordMapper.updateStatusAndStatusCode(anyLong(), anyString(), anyInt(), anyString(), anyInt(), any(Date.class))).thenReturn(1);
    }

    @Test
    public void batchSelectBySystemNumTest() {
        when(chargeRecordMapper.batchSelectBySystemNum(anyList())).thenReturn(new ArrayList());
        Assert.assertNotNull(chargeRecordService.batchSelectBySystemNum(new ArrayList<String>()));
    }

    @Test
    public void testStatisticChargeList1() {
        QueryObject queryObject = new QueryObject();
        queryObject.getQueryCriterias().put("endTime", "2016-11-11");
        queryObject.getQueryCriterias().put("managerId", "1");

        when(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey())).thenReturn("gd");
        when(enterpriseService.getEnterIdByManagerId(1L)).thenReturn(new ArrayList());
        assertNotNull(chargeRecordService.statisticChargeList(queryObject));

        when(enterpriseService.getEnterIdByManagerId(1L)).thenReturn(null);
        assertNotNull(chargeRecordService.statisticChargeList(queryObject));

        List<Long> entIds = new ArrayList<Long>();
        entIds.add(1L);
        when(enterpriseService.getEnterIdByManagerId(1L)).thenReturn(entIds);
        when(chargeRecordMapper.statisticChargeList(Mockito.anyMap())).thenReturn(new ArrayList());
        assertNotNull(chargeRecordService.statisticChargeList(queryObject));
        
        when(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey())).thenReturn("sd");
        when(chargeRecordMapper.sdstatisticChargeList(Mockito.anyMap())).thenReturn(new ArrayList());
        assertNotNull(chargeRecordService.statisticChargeList(queryObject));
    }

    @Test
    public void testStatisticChargeList2() {
        QueryObject queryObject = new QueryObject();
        queryObject.getQueryCriterias().put("managerId", "1");

        when(enterpriseService.getEnterIdByManagerId(1L)).thenReturn(new ArrayList());
        when(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey())).thenReturn("gd");
        assertNotNull(chargeRecordService.statisticChargeList(queryObject));

        List<Long> entIds = new ArrayList<Long>();
        entIds.add(1L);
        when(enterpriseService.getEnterIdByManagerId(1L)).thenReturn(entIds);
        when(chargeRecordMapper.statisticChargeList(Mockito.anyMap())).thenReturn(new ArrayList());
        assertNotNull(chargeRecordService.statisticChargeList(queryObject));
        
        when(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey())).thenReturn("sd");
        when(chargeRecordMapper.sdstatisticChargeList(Mockito.anyMap())).thenReturn(new ArrayList());
        assertNotNull(chargeRecordService.statisticChargeList(queryObject));
    }

    @Test
    public void testStatisticChargeListCount1() {
        QueryObject queryObject = new QueryObject();
        queryObject.getQueryCriterias().put("endTime", "2016-11-11");
        queryObject.getQueryCriterias().put("managerId", "1");

        when(enterpriseService.getEnterIdByManagerId(1L)).thenReturn(new ArrayList());
        assertEquals(chargeRecordService.statisticChargeListCount(queryObject), 0);

        when(enterpriseService.getEnterIdByManagerId(1L)).thenReturn(null);
        assertEquals(chargeRecordService.statisticChargeListCount(queryObject), 0);

        List<Long> entIds = new ArrayList<Long>();
        entIds.add(1L);
        when(enterpriseService.getEnterIdByManagerId(1L)).thenReturn(entIds);
        when(chargeRecordMapper.statisticChargeListCount(Mockito.anyMap())).thenReturn(1);
        assertEquals(chargeRecordService.statisticChargeListCount(queryObject), 1);
    }

    @Test
    public void testStatisticChargeListCount2() {
        QueryObject queryObject = new QueryObject();
        queryObject.getQueryCriterias().put("managerId", "1");

        when(enterpriseService.getEnterIdByManagerId(1L)).thenReturn(new ArrayList());
        assertEquals(chargeRecordService.statisticChargeListCount(queryObject), 0);

        when(enterpriseService.getEnterIdByManagerId(1L)).thenReturn(null);
        assertEquals(chargeRecordService.statisticChargeListCount(queryObject), 0);

        List<Long> entIds = new ArrayList<Long>();
        entIds.add(1L);
        when(enterpriseService.getEnterIdByManagerId(1L)).thenReturn(entIds);
        when(chargeRecordMapper.statisticChargeListCount(Mockito.anyMap())).thenReturn(1);
        assertEquals(chargeRecordService.statisticChargeListCount(queryObject), 1);
    }

    @Test
    public void testStatistictByChargeDay1() {
        QueryObject queryObject = new QueryObject();
        queryObject.getQueryCriterias().put("endTime", "2016-11-11");
        queryObject.getQueryCriterias().put("managerId", "1");

        when(enterpriseService.getEnterIdByManagerId(1L)).thenReturn(new ArrayList());
        assertNotNull(chargeRecordService.statistictByChargeDay(queryObject));

        when(managerService.getSonTreeIdByManageId(1L)).thenReturn(null);
        assertNotNull(chargeRecordService.statistictByChargeDay(queryObject));

        List<Long> managerIds = new ArrayList<Long>();
        managerIds.add(1L);
        when(managerService.getSonTreeIdByManageId(1L)).thenReturn(managerIds);
        when(chargeRecordMapper.statistictByChargeDay(Mockito.anyMap())).thenReturn(new ArrayList());
        assertNotNull(chargeRecordService.statistictByChargeDay(queryObject));
    }

    @Test
    public void testStatistictByChargeDay2() {
        QueryObject queryObject = new QueryObject();
        queryObject.getQueryCriterias().put("managerId", "1");

        when(enterpriseService.getEnterIdByManagerId(1L)).thenReturn(new ArrayList());
        assertNotNull(chargeRecordService.statistictByChargeDay(queryObject));

        when(managerService.getSonTreeIdByManageId(1L)).thenReturn(null);
        assertNotNull(chargeRecordService.statistictByChargeDay(queryObject));

        List<Long> managerIds = new ArrayList<Long>();
        managerIds.add(1L);
        when(managerService.getSonTreeIdByManageId(1L)).thenReturn(managerIds);
        when(chargeRecordMapper.statistictByChargeDay(Mockito.anyMap())).thenReturn(new ArrayList());
        assertNotNull(chargeRecordService.statistictByChargeDay(queryObject));
    }


    @Test
    public void testCreate() {
        assertFalse(chargeRecordService.create(null));
        ChargeRecord chargeRecord = new ChargeRecord();
        assertFalse(chargeRecordService.create(chargeRecord));
        chargeRecord.setPrdId(1L);
        assertFalse(chargeRecordService.create(chargeRecord));
        chargeRecord.setEnterId(1L);
        assertFalse(chargeRecordService.create(chargeRecord));
        chargeRecord.setRecordId(1L);
        assertFalse(chargeRecordService.create(chargeRecord));
        chargeRecord.setChargeTime(new Date());
        assertFalse(chargeRecordService.create(chargeRecord));
        chargeRecord.setaName("11");
        assertFalse(chargeRecordService.create(chargeRecord));
        chargeRecord.setTypeCode(1);
        assertFalse(chargeRecordService.create(chargeRecord));
        chargeRecord.setStatus(1);
        assertFalse(chargeRecordService.create(chargeRecord));
        chargeRecord.setPhone("18867103685");
        assertFalse(chargeRecordService.create(chargeRecord));
        chargeRecord.setType("11");

        when(chargeRecordMapper.insert(Mockito.any(ChargeRecord.class))).thenReturn(0);
        assertFalse(chargeRecordService.create(chargeRecord));

        when(chargeRecordMapper.insert(Mockito.any(ChargeRecord.class))).thenReturn(1);
//        assertTrue(chargeRecordService.create(chargeRecord));

//        verify(chargeRecordMapper, times(2)).insert(Mockito.any(ChargeRecord.class));
    }

    @Test
    public void testBatchInsert() {
        assertFalse(chargeRecordService.batchInsert(null));
        assertFalse(chargeRecordService.batchInsert(new ArrayList()));
        List records = new ArrayList();
        records.add(new ChargeRecord());
        assertFalse(chargeRecordService.batchInsert(records));

        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setPrdId(1L);
        chargeRecord.setEnterId(1L);
        chargeRecord.setRecordId(1L);
        chargeRecord.setChargeTime(new Date());
        chargeRecord.setaName("11");
        chargeRecord.setTypeCode(1);
        chargeRecord.setStatus(1);
        chargeRecord.setPhone("18867103685");
        chargeRecord.setType("11");

        List records2 = new ArrayList();
        records2.add(chargeRecord);

        when(chargeRecordMapper.batchInsert(Mockito.anyList())).thenReturn(0);
        assertFalse(chargeRecordService.batchInsert(records2));

        when(chargeRecordMapper.batchInsert(Mockito.anyList())).thenReturn(1);
        assertTrue(chargeRecordService.batchInsert(records2));

        verify(chargeRecordMapper, times(2)).batchInsert(Mockito.anyList());
    }

    @Test
    public void testBatchUpdateStatus() throws Exception {
        assertFalse(chargeRecordService.batchUpdateStatus(null));
        assertFalse(chargeRecordService.batchUpdateStatus(new ArrayList()));

        List<ChargeRecord> records = build();
        assertFalse(chargeRecordService.batchUpdateStatus(records));

        for (ChargeRecord record : records) {
            record.setStatus(randStatus().getCode());
        }
        assertFalse(chargeRecordService.batchUpdateStatus(records));

        for (ChargeRecord record : records) {
            record.setId(1L);
        }
        when(chargeRecordMapper.batchUpdateStatus(Mockito.anyList(), Mockito.any(Date.class), Mockito.anyInt())).thenReturn(records.size());
        assertTrue(chargeRecordService.batchUpdateStatus(records));

        when(chargeRecordMapper.batchUpdateStatus(Mockito.anyList(), Mockito.any(Date.class), Mockito.anyInt())).thenReturn(0);
        assertFalse(chargeRecordService.batchUpdateStatus(records));
    }

    @Test
    public void testStatement() throws Exception {
        DateTime end = new DateTime();

        Date beginDate = DateUtil.getDateAfter(new Date(), 10);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateTime begin = DateTime.parse(format.format(beginDate));
        Long entId = 409L;

        assertNull(chargeRecordService.statement(null, end, entId));
        assertNull(chargeRecordService.statement(begin, null, entId));
        assertNull(chargeRecordService.statement(begin, end, entId));

        beginDate = DateUtil.getDateBefore(new Date(), 10);
        begin = DateTime.parse(format.format(beginDate));

        when(chargeRecordMapper.statement(Mockito.any(Date.class), Mockito.any(Date.class), Mockito.anyLong())).thenReturn(new ArrayList());
        assertNotNull(chargeRecordService.statement(begin, end, entId));

        verify(chargeRecordMapper).statement(Mockito.any(Date.class), Mockito.any(Date.class), Mockito.anyLong());

    }

    private ChargeRecordStatus randStatus() {
        return ChargeRecordStatus.fromValue(new Random().nextInt(6) - 1);
    }

    private List<ChargeRecord> build() {
        List<ChargeRecord> records = new LinkedList<ChargeRecord>();
        for (int i = 0; i < 10; i++) {
            records.add(buildRecord("18867102100", 1));
        }

        return records;
    }

    private ChargeRecord buildRecord(String mobile, int typeCode) {
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setaName(randStr(10));
        chargeRecord.setBossNum(randStr(15));
        chargeRecord.setChargeTime(new Date());
        chargeRecord.setErrorMessage(randStr(15));
        chargeRecord.setEnterId(53L);
        chargeRecord.setFingerprint(randStr(12));
        chargeRecord.setPhone(mobile);
        chargeRecord.setPrdId(324L);
        chargeRecord.setPrice(34L);
        chargeRecord.setRecordId(43L);
        chargeRecord.setSerialNum(randStr(12));
        chargeRecord.setStatus(ChargeRecordStatus.WAIT.getCode());
        chargeRecord.setSupplierProductId(34L);
        chargeRecord.setSystemNum(randStr(12));
        chargeRecord.setType("1");
        chargeRecord.setTypeCode(typeCode);

        return chargeRecord;
    }

    private CallBackReq validCallbackReq() {
        CallBackReq cbr = new CallBackReq();
        cbr.setDateTime(new DateTime().toString());

        CallBackReqData cbrd = new CallBackReqData();
        cbrd.setEcSerialNum("ecSerialNum");
        cbrd.setMobile("18867102100");
        cbrd.setSystemNum("systemSerialNum");
        cbrd.setStatus(ChargeRecordStatus.COMPLETE.getCode());
        cbrd.setDescription("fdsafd");

        cbr.setCallBackReqData(cbrd);

        return cbr;
    }

    private CallBackReq nullSystemSerialNum() {
        CallBackReq cbq = validCallbackReq();
        cbq.getCallBackReqData().setSystemNum(null);

        return cbq;
    }

    private CallBackReq nullEcSerialNum() {
        CallBackReq cbq = validCallbackReq();
        cbq.getCallBackReqData().setEcSerialNum(null);

        return cbq;
    }

    private CallBackReq nullMobile() {
        CallBackReq cbq = validCallbackReq();
        cbq.getCallBackReqData().setMobile(null);

        return cbq;
    }

    private CallBackReq nullStatus() {
        CallBackReq cbq = validCallbackReq();
        cbq.getCallBackReqData().setStatus(null);

        return cbq;
    }

    @Test
    public void testUpdateStatus_4() throws Exception {
        assertFalse(chargeRecordService.updateStatus(null, ChargeRecordStatus.FAILED, ""));
        assertFalse(chargeRecordService.updateStatus(3424L, null, ""));

        ChargeRecord chargeRecord = buildRecord("18867102100", ActivityType.GIVE.getCode());
        when(chargeRecordMapper.get(anyLong())).thenReturn(null).thenReturn(chargeRecord);
        when(chargeRecordMapper.updateStatus(anyLong(), anyInt(), anyString())).thenReturn(1);
        when(presentRecordService.updateStatus(anyLong(), any(ChargeRecordStatus.class), anyString())).thenReturn(true);

        assertFalse(chargeRecordService.updateStatus(342L, ChargeRecordStatus.COMPLETE, ""));

        verify(chargeRecordMapper, times(1)).get(anyLong());
    }

    @Test
    public void testUpdateStatus_5() throws Exception {
        ChargeRecord chargeRecord = buildRecord("18867102100", ActivityType.GIVE.getCode());
        when(chargeRecordMapper.get(anyLong())).thenReturn(chargeRecord);
        when(chargeRecordMapper.updateStatus(anyLong(), anyInt(), anyString()))
                .thenReturn(0).thenReturn(1);
        when(presentRecordService.updateStatus(anyLong(), any(ChargeRecordStatus.class), anyString())).thenReturn(true);

        assertFalse(chargeRecordService.updateStatus(342L, ChargeRecordStatus.COMPLETE, ""));
    }

    @Test
    public void testUpdateStatus_6() throws Exception {
        ChargeRecord chargeRecord = buildRecord("18867102100", ActivityType.GIVE.getCode());
        when(chargeRecordMapper.get(anyLong())).thenReturn(chargeRecord);
        when(chargeRecordMapper.updateStatus(anyLong(), anyInt(), anyString())).thenReturn(1);
        when(presentRecordService.updateStatus(anyLong(), any(ChargeRecordStatus.class), anyString()))
                .thenReturn(false).thenReturn(true);

        assertFalse(chargeRecordService.updateStatus(342L, ChargeRecordStatus.COMPLETE, ""));

    }

    @Test
    public void testUpdateStatus_7() throws Exception {
        int count = ActivityType.values().length;
        List<ChargeRecord> chargeRecords = new LinkedList<ChargeRecord>();
        for (ActivityType type : ActivityType.values()) {
            chargeRecords.add(buildRecord("18867102100", type.getCode()));
            chargeRecords.add(buildRecord("18867102100", type.getCode()));
        }

        when(chargeRecordMapper.get(anyLong())).thenAnswer(AdditionalAnswers.returnsElementsOf(chargeRecords));
        when(chargeRecordMapper.updateStatus(anyLong(), anyInt(), anyString())).thenReturn(1);
        when(presentRecordService.updateStatus(anyLong(), any(ChargeRecordStatus.class), anyString()))
                .thenReturn(false);
        when(activityWinRecordService.updateStatus(anyLong(), any(ChargeRecordStatus.class), anyString(), anyString()))
                .thenReturn(false);

        when(interfaceRecordService.updateChargeStatus(anyLong(), any(ChargeRecordStatus.class), anyString()))
                .thenReturn(false);

        for (ActivityType type : ActivityType.values()) {
            assertFalse(chargeRecordService.updateStatus(342L, ChargeRecordStatus.COMPLETE, ""));
        }

        when(chargeRecordMapper.get(anyLong())).thenAnswer(AdditionalAnswers.returnsElementsOf(chargeRecords));
        when(chargeRecordMapper.updateStatus(anyLong(), anyInt(), anyString())).thenReturn(1);
        when(presentRecordService.updateStatus(anyLong(), any(ChargeRecordStatus.class), anyString()))
                .thenReturn(true);

        when(activityWinRecordService.updateStatus(anyLong(), any(ChargeRecordStatus.class), anyString(), anyString()))
                .thenReturn(true);

        when(interfaceRecordService.updateChargeStatus(anyLong(), any(ChargeRecordStatus.class), anyString()))
                .thenReturn(true);
        assertFalse(chargeRecordService.updateStatus(342L, ChargeRecordStatus.COMPLETE, ""));
    }

    @Test
    public void testUpdateStatus_9() throws Exception {
        assertFalse(chargeRecordService.updateStatus(null, new ChargeResult(ChargeResultCode.FAILURE)));
        assertFalse(chargeRecordService.updateStatus(1L, null));
//        assertFalse(chargeRecordService.updateStatus(1L, new ChargeResult(null)));

        ChargeRecord chargeRecord = buildRecord("18867102100", ActivityType.GIVE.getCode());
        when(chargeRecordMapper.get(anyLong())).thenReturn(chargeRecord);
        when(chargeRecordMapper.updateStatus(anyLong(), anyInt(), anyString())).thenReturn(1);
        when(presentRecordService.updateStatus(anyLong(), any(ChargeRecordStatus.class), anyString())).thenReturn(true);

        assertFalse(chargeRecordService.updateStatus(1L, new ChargeResult(ChargeResultCode.FAILURE)));

        ChargeResult result = new ChargeResult(ChargeResultCode.FAILURE);
        result.setFailureReason("错误原因");
        assertFalse(chargeRecordService.updateStatus(1L, result));

        assertFalse(chargeRecordService.updateStatus(1L, new ChargeResult(ChargeResultCode.SUCCESS)));

        assertFalse(chargeRecordService.updateStatus(1L, new ChargeResult(ChargeResultCode.PROCESSING)));
    }
    
    @Test
    public void testUpdateStatusMonthly() throws Exception {
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setId(1L);
        chargeRecord.setRecordId(1L);
        chargeRecord.setTypeCode(ActivityType.MONTHLY_PRESENT.getCode());
        when(chargeRecordMapper.get(anyLong())).thenReturn(chargeRecord);
        when(chargeRecordMapper.updateStatusAndStatusCode(anyLong(), anyString(), anyInt(), anyString(), anyInt(), any(Date.class))).thenReturn(1);
        when(monthlyPresentRecordService.updateActivityStatus(Mockito.anyLong(),Mockito.any(ActivityWinRecordStatus.class),Mockito.anyString())).thenReturn(true);
        chargeRecordService.updateStatus(1L,ChargeRecordStatus.PROCESSING,"111");
        chargeRecordService.updateStatus(1L,ChargeRecordStatus.WAIT,"111");
        chargeRecordService.updateStatus(1L,ChargeRecordStatus.FAILED,"111");
        chargeRecordService.updateStatus(1L,ChargeRecordStatus.UNUSED,"111");
    }

    @Test
    public void testUpdateByRecordId() {
        assertFalse(chargeRecordService.updateByRecordId(null, 1, "123"));
        assertFalse(chargeRecordService.updateByRecordId(1L, null, "123"));

        when(chargeRecordMapper.updateByRecordId(1L, 1, "123")).thenReturn(1);
        assertTrue(chargeRecordService.updateByRecordId(1L, 1, "123"));

        when(chargeRecordMapper.updateByRecordId(1L, 1, "123")).thenReturn(0);
        assertFalse(chargeRecordService.updateByRecordId(1L, 1, "123"));

        verify(chargeRecordMapper, times(2)).updateByRecordId(1L, 1, "123");
    }

    @Test
    public void testUpdateBySystemNum() {
        assertFalse(chargeRecordService.updateBySystemNum(null, 1, "123", null, null));
        assertFalse(chargeRecordService.updateBySystemNum("11", null, "123", null, null));

        when(chargeRecordMapper.updateBySystemNum("11", 1, "123", null, null)).thenReturn(1);
        assertTrue(chargeRecordService.updateBySystemNum("11", 1, "123", null, null));

        when(chargeRecordMapper.updateBySystemNum("11", 1, "123", null, null)).thenReturn(0);
        assertFalse(chargeRecordService.updateBySystemNum("11", 1, "123", null, null));

        verify(chargeRecordMapper, times(2)).updateBySystemNum("11", 1, "123", null, null);
    }

    @Test
    public void testGetRecordBySN() {
        assertNull(chargeRecordService.getRecordBySN(null));

        when(chargeRecordMapper.selectRecordBySN("11")).thenReturn(new ChargeRecord());
        assertNotNull(chargeRecordService.getRecordBySN("11"));

        verify(chargeRecordMapper, times(1)).selectRecordBySN("11");
    }

    @Test
    public void testGetRecords() {
        assertNull(chargeRecordService.getRecords(null, 1, 1));
        assertNull(chargeRecordService.getRecords(1L, null, 1));
        assertNull(chargeRecordService.getRecords(1L, 1, null));

        when(chargeRecordMapper.selectRecords(1L, 1, 1)).thenReturn(new ArrayList());
        assertNotNull(chargeRecordService.getRecords(1L, 1, 1));

        verify(chargeRecordMapper, times(1)).selectRecords(1L, 1, 1);
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        when(chargeRecordMapper.updateByPrimaryKeySelective(Mockito.any(ChargeRecord.class))).thenReturn(1);
        assertTrue(chargeRecordService.updateByPrimaryKeySelective(new ChargeRecord()));

        when(chargeRecordMapper.updateByPrimaryKeySelective(Mockito.any(ChargeRecord.class))).thenReturn(0);
        assertFalse(chargeRecordService.updateByPrimaryKeySelective(new ChargeRecord()));

        verify(chargeRecordMapper, times(2)).updateByPrimaryKeySelective(Mockito.any(ChargeRecord.class));
    }

    @Test
    public void testUpdateByTypeCodeAndRecordId() {
        when(chargeRecordMapper.updateByTypeCodeAndRecordId(Mockito.anyMap())).thenReturn(1);
        assertTrue(chargeRecordService.updateByTypeCodeAndRecordId(Mockito.anyMap()));

        when(chargeRecordMapper.updateByTypeCodeAndRecordId(Mockito.anyMap())).thenReturn(0);
        assertFalse(chargeRecordService.updateByTypeCodeAndRecordId(Mockito.anyMap()));

        verify(chargeRecordMapper, times(2)).updateByTypeCodeAndRecordId(Mockito.anyMap());
    }

    @Test
    public void testSelectRecordByEnterIdAndSerialNum() {

        List<ChargeRecord> list = new ArrayList();
        when(chargeRecordMapper.selectRecordByEnterIdAndSerialNum(Mockito.anyLong(), Mockito.anyString())).thenReturn(list);
        assertNull(chargeRecordService.selectRecordByEnterIdAndSerialNum(null, null));
        assertNull(chargeRecordService.selectRecordByEnterIdAndSerialNum(1L, null));
        assertSame(list, chargeRecordService.selectRecordByEnterIdAndSerialNum(1L, "aaa"));
    }

    @Test
    public void testUpdateStatusCode() {
        Long id = 1L;
        String errorMsg = "203";
        when(chargeRecordMapper.updateStatusCode(Mockito.anyLong(), Mockito.anyString())).thenReturn(0).thenReturn(1);
        assertFalse(chargeRecordService.updateStatusCode(null, errorMsg));
        assertTrue(chargeRecordService.updateStatusCode(id, errorMsg));
        assertTrue(chargeRecordService.updateStatusCode(id, errorMsg));
    }

    @Test
    public void testUpdateActivityRecords() {
        List<ChargeRecord> records = new ArrayList<ChargeRecord>();
        records.add(getValidChargeRecord(ActivityType.INTERFACE.getCode()));
        records.add(getValidChargeRecord(ActivityType.GIVE.getCode()));
        records.add(getValidChargeRecord(ActivityType.REDPACKET.getCode()));
        records.add(getValidChargeRecord(ActivityType.LOTTERY.getCode()));
        records.add(getValidChargeRecord(ActivityType.GOLDENBALL.getCode()));
        records.add(getValidChargeRecord(ActivityType.FLOWCARD.getCode()));
        records.add(getValidChargeRecord(ActivityType.QRCODE.getCode()));
        records.add(getValidChargeRecord(ActivityType.MONTHLY_PRESENT.getCode()));

        when(interfaceRecordService.get(Mockito.anyLong())).thenReturn(new InterfaceRecord());
        when(presentRecordService.selectByRecordId(Mockito.anyLong())).thenReturn(new PresentRecord());
        when(activityWinRecordService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(new ActivityWinRecord());

        assertFalse(chargeRecordService.updateActivityRecords(records, new ChargeResult(ChargeResult.ChargeResultCode.PROCESSING)));
        assertFalse(chargeRecordService.updateActivityRecords(records, new ChargeResult(ChargeResult.ChargeResultCode.FAILURE)));

    }

    private ChargeRecord getValidChargeRecord(int code) {
        ChargeRecord record = new ChargeRecord();
        record.setTypeCode(code);
        record.setId(1L);
        record.setPrdId(2L);
        record.setEnterId(3L);
        record.setRecordId(4L);
        return record;
    }
    
    @Test
    public void testGetMdrcChargeRecords() {
        List<ChargeRecord> records = new ArrayList<ChargeRecord>();
        assertNull(chargeRecordService.getMdrcChargeRecords(null, null));

        when(chargeRecordMapper.getMdrcChargeRecords(Mockito.anyMap())).thenReturn(records);
        assertEquals(records, chargeRecordService.getMdrcChargeRecords("2017", null));
    }
    
    
    @Test
    public void testCountMdrcChargeRecords() {
        List<ChargeRecord> records = new ArrayList<ChargeRecord>();
        assertEquals(0, chargeRecordService.countMdrcChargeRecords(null, null));

        when(chargeRecordMapper.countMdrcChargeRecords(Mockito.anyMap())).thenReturn(1L);
        assertEquals(1L, chargeRecordService.countMdrcChargeRecords("2017", null));
    }
    @Test
    public void testUpdateQueryTime() {
        Mockito.when(chargeRecordMapper.updateQueryTime(Mockito.anyString(), Mockito.any(Date.class)))
            .thenReturn(1).thenReturn(0);
        Assert.assertTrue(chargeRecordService.updateQueryTime("1234", new Date()));
        Assert.assertFalse(chargeRecordService.updateQueryTime("1234", new Date()));
    }
    
    @Test
    public void testGetRecordsByMobileAndPrd() {

        Assert.assertNull(chargeRecordService.getRecordsByMobileAndPrd(null, 1l, new Date()));
        Assert.assertNull(chargeRecordService.getRecordsByMobileAndPrd("18867105766", null, new Date()));
        Assert.assertNull(chargeRecordService.getRecordsByMobileAndPrd("18867105766", 1l, null));
        Mockito.when(chargeRecordMapper.getRecordsByMobileAndPrd(Mockito.anyString(), Mockito.anyLong(), Mockito.any(Date.class)))
            .thenReturn(null);
        Assert.assertNull(chargeRecordService.getRecordsByMobileAndPrd("18867105766", 1l, new Date()));
    }
}

