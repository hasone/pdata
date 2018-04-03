package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.SdAccountChargeRecordMapper;
import com.cmcc.vrp.province.model.SdAccountChargeRecord;
import com.cmcc.vrp.province.service.SdAccountChargeRecordService;

/**
 * 
 * @ClassName: SdAccountChargeRecordServiceImplTest 
 * @Description: TODO
 * @author: Rowe
 * @date: 2017年9月21日 下午2:36:07
 */
@RunWith(MockitoJUnitRunner.class)
public class SdAccountChargeRecordServiceImplTest {
    @InjectMocks
    SdAccountChargeRecordService sdAccountChargeRecordService = new SdAccountChargeRecordServiceImpl();
    @Mock
    SdAccountChargeRecordMapper sdAccountChargeRecordMapper;

    @Test
    public void testInsertSelective() {
        assertFalse(sdAccountChargeRecordService.insertSelective(null));
        Mockito.when(sdAccountChargeRecordMapper.insertSelective(any(SdAccountChargeRecord.class))).thenReturn(1);
        assertTrue(sdAccountChargeRecordService.insertSelective(new SdAccountChargeRecord()));
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        assertFalse(sdAccountChargeRecordService.updateByPrimaryKeySelective(null));

        SdAccountChargeRecord record = new SdAccountChargeRecord();
        assertFalse(sdAccountChargeRecordService.updateByPrimaryKeySelective(record));

        record.setId(1L);
        Mockito.when(sdAccountChargeRecordMapper.updateByPrimaryKeySelective(any(SdAccountChargeRecord.class)))
                .thenReturn(1);
        assertTrue(sdAccountChargeRecordService.updateByPrimaryKeySelective(record));
    }

}
