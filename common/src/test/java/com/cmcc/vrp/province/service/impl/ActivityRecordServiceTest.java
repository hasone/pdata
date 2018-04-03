package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.InterfaceRecordMapper;
import com.cmcc.vrp.province.model.InterfaceRecord;
import com.cmcc.vrp.province.service.InterfaceRecordService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Matchers.anyList;

/**
 * Created by leelyn on 2016/11/26.
 */
@RunWith(PowerMockRunner.class)
public class ActivityRecordServiceTest {

    @InjectMocks
    InterfaceRecordService interfaceRecordService = new InterfaceRecordServiceImpl();

    @Mock
    InterfaceRecordMapper interfaceRecordMapper;

    @Test
    public void testBatchUpdate() {
        PowerMockito.when(interfaceRecordMapper.batchUpdateStatus(anyList())).thenReturn(10);
        Assert.assertTrue(interfaceRecordService.batchUpdateStatus(records()));
    }

    private List<InterfaceRecord> records() {
        List<InterfaceRecord> records = new LinkedList<InterfaceRecord>();
        for (int i = 1; i <= 10; i++) {
            InterfaceRecord interfaceRecord = new InterfaceRecord();
            interfaceRecord.setId((long) i);
            interfaceRecord.setStatus(3);
            interfaceRecord.setStatusCode("301");
            interfaceRecord.setErrMsg("充值成功");
            records.add(interfaceRecord);
        }
        return records;
    }
}
