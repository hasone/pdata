package com.cmcc.vrp.province.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.enums.ActivityWinRecordStatus;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.cache.CacheService;
import com.cmcc.vrp.province.dao.InterfaceRecordMapper;
import com.cmcc.vrp.province.model.InterfaceRecord;
import com.cmcc.vrp.province.service.InterfaceRecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by sunyiwei on 2016/9/20.
 */
@RunWith(MockitoJUnitRunner.class)
public class InterfaceRecordServiceImplTest {
    @InjectMocks
    @Spy
    private InterfaceRecordService service = new InterfaceRecordServiceImpl();

    @Mock
    private InterfaceRecordMapper mapper;

    @Mock
    private CacheService cacheService;

    @Test
    public void testGet() {
        String enterCode = "123456";
        String serialNum = "123";
        String phoneNum = "18867103685";

        String valueStr = JSONObject.toJSONString(createInterfaceRecord());

        when(cacheService.sExist(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        assertNull(service.get(enterCode, serialNum, phoneNum));

        when(cacheService.sExist(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(cacheService.get(Mockito.anyString())).thenReturn(null);
        assertNull(service.get(enterCode, serialNum, phoneNum));


        when(cacheService.get(Mockito.anyString())).thenReturn(valueStr);

        when(mapper.findExistRecord(Mockito.anyMap())).thenReturn(null);
        assertNotNull(service.get(enterCode, serialNum, phoneNum));

        when(mapper.findExistRecord(Mockito.anyMap())).thenReturn(new ArrayList());
        assertNotNull(service.get(enterCode, serialNum, phoneNum));
    }

    @Test
    public void testGet1() {
        String enterCode = "123456";
        String serialNum = "123";
        String phoneNum = "18867103685";

        when(cacheService.sExist(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(cacheService.get(Mockito.anyString())).thenReturn(null);
        List<InterfaceRecord> interfaceRecords = new ArrayList();
        interfaceRecords.add(new InterfaceRecord());
        when(mapper.findExistRecord(Mockito.anyMap())).thenReturn(interfaceRecords);
        assertNotNull(service.get(enterCode, serialNum, phoneNum));

        when(cacheService.sExist(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(mapper.findExistRecord(Mockito.anyMap())).thenReturn(null);
        assertNull(service.get(enterCode, serialNum, phoneNum));
    }

    @Test
    public void testGet2() {
        Long id = 1L;
        assertNull(service.get(null));
        when(cacheService.sExist(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        assertNull(service.get(id));

        String valueStr = JSONObject.toJSONString(createInterfaceRecord());
        when(cacheService.sExist(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(cacheService.get(Mockito.anyString())).thenReturn(valueStr);
        assertNotNull(service.get(id));

        when(cacheService.sExist(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(cacheService.get(Mockito.anyString())).thenReturn(null);
        when(mapper.selectByPrimaryKey(id)).thenReturn(null);
        assertNull(service.get(id));

        when(cacheService.sExist(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(cacheService.get(Mockito.anyString())).thenReturn(null);
        when(mapper.selectByPrimaryKey(id)).thenReturn(new InterfaceRecord());
        assertNotNull(service.get(id));
    }


    @Test
    public void testInsert() {
        when(cacheService.sExist(Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        when(mapper.insert(Mockito.any(InterfaceRecord.class))).thenReturn(1);
        assertTrue(service.insert(createInterfaceRecord()));

        when(mapper.insert(Mockito.any(InterfaceRecord.class))).thenReturn(0);
        assertFalse(service.insert(createInterfaceRecord()));

        when(cacheService.sExist(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(cacheService.get(Mockito.anyString())).thenReturn(null);
        List<InterfaceRecord> interfaceRecords = new ArrayList();
        interfaceRecords.add(new InterfaceRecord());
        when(mapper.findExistRecord(Mockito.anyMap())).thenReturn(interfaceRecords);
        assertFalse(service.insert(createInterfaceRecord()));
    }

    @Test
    public void testUpdateChargeStatus() throws Exception {
        assertFalse(service.updateChargeStatus(null, ChargeRecordStatus.FAILED, ""));
        assertFalse(service.updateChargeStatus(234L, null, ""));

        doReturn(new InterfaceRecord()).when(service).get(anyLong());
        when(cacheService.update(anyList(), anyString())).thenReturn(true);
        when(mapper.updateByPrimaryKeySelective(any(InterfaceRecord.class)))
                .thenReturn(1).thenReturn(0);

        assertTrue(service.updateChargeStatus(234L, ChargeRecordStatus.FAILED, ""));
        assertFalse(service.updateChargeStatus(234L, ChargeRecordStatus.FAILED, ""));

        verify(cacheService, times(2)).update(anyList(), anyString());
        verify(mapper, times(2)).updateByPrimaryKeySelective(any(InterfaceRecord.class));
    }

    @Test
    public void testUpdateChargeStatus2() throws Exception {
        assertFalse(service.updateChargeStatus(null, ChargeRecordStatus.FAILED, ""));
        assertFalse(service.updateChargeStatus(234L, null, ""));

        doReturn(null).when(service).get(anyLong());
        assertFalse(service.updateChargeStatus(234L, ChargeRecordStatus.FAILED, ""));
    }

    @Test
    public void testUpdateChargeStatus3() throws Exception {
        doReturn(new InterfaceRecord()).when(service).get(anyLong());
        when(cacheService.update(anyList(), anyString())).thenReturn(false);

        assertFalse(service.updateChargeStatus(234L, ChargeRecordStatus.FAILED, ""));
        verify(cacheService, times(1)).update(anyList(), anyString());
    }

    @Test
    public void testUpdateActivityStatus() {
        assertFalse(service.updateActivityStatus(null, ActivityWinRecordStatus.FALURE, ""));
        assertFalse(service.updateActivityStatus(234L, null, ""));

        doReturn(new InterfaceRecord()).when(service).get(anyLong());
        when(cacheService.update(anyList(), anyString())).thenReturn(true);
        when(mapper.updateByPrimaryKeySelective(any(InterfaceRecord.class)))
                .thenReturn(1).thenReturn(0);

        assertTrue(service.updateActivityStatus(234L, ActivityWinRecordStatus.FALURE, ""));
        assertFalse(service.updateActivityStatus(234L, ActivityWinRecordStatus.FALURE, ""));

        verify(cacheService, times(2)).update(anyList(), anyString());
        verify(mapper, times(2)).updateByPrimaryKeySelective(any(InterfaceRecord.class));
    }

    @Test
    public void testUpdateStatusCode() {
        Long id = 1L;
        String statusCode = "203";
        when(mapper.updateStatusCode(id, statusCode)).thenReturn(0).thenReturn(1);
        assertFalse(service.updateStatusCode(null, statusCode));
        assertFalse(service.updateStatusCode(id, statusCode));
        assertTrue(service.updateStatusCode(id, statusCode));
    }

    private InterfaceRecord createInterfaceRecord() {
        InterfaceRecord record = new InterfaceRecord();
        record.setBossSerialNum("111");
        record.setEnterpriseCode("11");
        record.setProductCode("111");
        record.setPhoneNum("18867103685");

        return record;
    }
}