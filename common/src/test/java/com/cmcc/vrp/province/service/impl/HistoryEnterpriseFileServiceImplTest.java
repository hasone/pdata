package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.HistoryEnterpriseFileMapper;
import com.cmcc.vrp.province.model.HistoryEnterpriseFile;
import com.cmcc.vrp.province.service.HistoryEnterpriseFileService;

/**
 * Created by qinqinyan on 2016/10/24.
 * @Description 历史企业文件服务测试类
 */
@RunWith(MockitoJUnitRunner.class)
public class HistoryEnterpriseFileServiceImplTest {
    @InjectMocks
    HistoryEnterpriseFileService historyEnterpriseFileService = new HistoryEnterpriseFileServiceImpl();
    @Mock
    HistoryEnterpriseFileMapper historyEnterpriseFileMapper;

    /**
     * 插入对象
     * */
    @Test
    public void testInsertSelective() {
        //invalid
        assertFalse(historyEnterpriseFileService.insertSelective(null));

        //valid
        HistoryEnterpriseFile record = new HistoryEnterpriseFile();
        record.setId(1L);
        Mockito.when(historyEnterpriseFileMapper.insertSelective(record)).thenReturn(1);
        assertTrue(historyEnterpriseFileService.insertSelective(record));
        Mockito.verify(historyEnterpriseFileMapper).insertSelective(record);
    }

    @Test
    public void testSelectByRequestId() throws Exception {
        //invalid
        assertNull(historyEnterpriseFileService.selectByRequestId(null));

        //valid
        Long requestId = 1L;
        Mockito.when(historyEnterpriseFileMapper.selectByRequestId(requestId)).thenReturn(new HistoryEnterpriseFile());
        assertNotNull(historyEnterpriseFileService.selectByRequestId(requestId));
        Mockito.verify(historyEnterpriseFileMapper).selectByRequestId(requestId);
    }




}
