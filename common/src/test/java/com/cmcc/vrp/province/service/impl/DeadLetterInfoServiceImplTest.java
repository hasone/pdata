package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.DeadLetterInfoMapper;
import com.cmcc.vrp.province.model.DeadLetterInfo;
import com.cmcc.vrp.province.service.DeadLetterInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeadLetterInfoServiceImplTest {
    @InjectMocks
    DeadLetterInfoService service = new DeadLetterInfoServiceImpl();

    @Mock
    DeadLetterInfoMapper mapper;

    @Test
    public void testCreate() {
        assertFalse(service.create(null));
        assertFalse(service.create(new DeadLetterInfo()));

        DeadLetterInfo info = new DeadLetterInfo();
        info.setContent("11");
        when(mapper.insert(Mockito.any(DeadLetterInfo.class))).thenReturn(0);
        assertFalse(service.create(info));

        when(mapper.insert(Mockito.any(DeadLetterInfo.class))).thenReturn(1);
        assertTrue(service.create(info));

        verify(mapper, Mockito.times(2)).insert(Mockito.any(DeadLetterInfo.class));
    }

    @Test
    public void testBatchDelete() {
        assertFalse(service.batchDelete(null));
        assertFalse(service.batchDelete(new ArrayList()));
        when(mapper.batchDelete(Mockito.anyList())).thenReturn(1);
        List<Long> ids = new ArrayList();
        ids.add(1L);

        assertTrue(service.batchDelete(ids));
        verify(mapper).batchDelete(Mockito.anyList());
    }

    @Test
    public void testGetAllUndeletedInfos() {
        when(mapper.getAllUndeletedRecords()).thenReturn(new ArrayList());
        assertNotNull(service.getAllUndeletedInfos());
        verify(mapper).getAllUndeletedRecords();
    }

}
