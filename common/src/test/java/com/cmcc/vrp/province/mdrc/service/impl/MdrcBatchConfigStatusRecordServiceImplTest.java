package com.cmcc.vrp.province.mdrc.service.impl;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.MdrcBatchConfigStatusRecordMapper;
import com.cmcc.vrp.province.mdrc.service.MdrcBatchConfigStatusRecordService;
import com.cmcc.vrp.province.model.MdrcBatchConfigStatusRecord;

@RunWith(MockitoJUnitRunner.class)
public class MdrcBatchConfigStatusRecordServiceImplTest {

    @InjectMocks
    MdrcBatchConfigStatusRecordService mdrcBatchConfigStatusRecordService = new MdrcBatchConfigStatusRecordServiceImpl();

    @Mock
    MdrcBatchConfigStatusRecordMapper mdrcBatchConfigStatusRecordMapper;

    @Test
    public void testInsertSelective() {
        Mockito.when(mdrcBatchConfigStatusRecordMapper.insertSelective(Mockito.any(MdrcBatchConfigStatusRecord.class)))
                .thenReturn(1);

        assertTrue(mdrcBatchConfigStatusRecordService.insertSelective(Mockito.any(MdrcBatchConfigStatusRecord.class)));
    }

    @Test
    public void testSelectByConfigId() {
        List<MdrcBatchConfigStatusRecord> list = new ArrayList<MdrcBatchConfigStatusRecord>();

        Mockito.when(mdrcBatchConfigStatusRecordMapper.selectByConfigId(Mockito.any(Long.class), Mockito.any(List.class))).thenReturn(list);

        assertTrue(mdrcBatchConfigStatusRecordService.selectByConfigId(Mockito.any(Long.class), Mockito.any(List.class)).equals(list));
    }

    @Test
    public void testSelectByPrimaryKey() {
        MdrcBatchConfigStatusRecord record = new MdrcBatchConfigStatusRecord();

        Mockito.when(mdrcBatchConfigStatusRecordMapper.selectByPrimaryKey(Mockito.any(Long.class))).thenReturn(record);

        assertTrue(mdrcBatchConfigStatusRecordService.selectByPrimaryKey(Mockito.any(Long.class)).equals(record));
    }
}