package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.MdrcActiveRequestConfigMapper;
import com.cmcc.vrp.province.mdrc.service.MdrcCardInfoService;
import com.cmcc.vrp.province.model.MdrcActiveDetail;
import com.cmcc.vrp.province.model.MdrcActiveRequestConfig;
import com.cmcc.vrp.province.service.MdrcActiveDetailService;
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;

/**
 * Created by qinqinyan on 2016/12/1.
 */
@RunWith(MockitoJUnitRunner.class)
public class MdrcActiveRequestConfigServiceImplTest {
    @InjectMocks
    MdrcActiveRequestConfigServiceImpl mdrcActiveRequestConfigService =
        new MdrcActiveRequestConfigServiceImpl();
    @Mock
    MdrcActiveRequestConfigMapper mdrcActiveRequestConfigMapper;
    @Mock
    MdrcActiveDetailService mdrcActiveDetailService;
    @Mock
    MdrcCardInfoService mdrcCardInfoService;

    @Test
    public void testDeleteByPrimaryKey(){
        assertFalse(mdrcActiveRequestConfigService.deleteByPrimaryKey(null));
        Mockito.when(mdrcActiveRequestConfigMapper.deleteByPrimaryKey(anyLong())).thenReturn(1);
        assertTrue(mdrcActiveRequestConfigService.deleteByPrimaryKey(1L));
        Mockito.verify(mdrcActiveRequestConfigMapper).deleteByPrimaryKey(anyLong());
    }

    @Test
    public void testInsert(){
        assertFalse(mdrcActiveRequestConfigService.insert(null));
        Mockito.when(mdrcActiveRequestConfigMapper.insert(any(MdrcActiveRequestConfig.class))).thenReturn(1);
        assertTrue(mdrcActiveRequestConfigService.insert(createMdrcActiveRequestConfig()));
        Mockito.verify(mdrcActiveRequestConfigMapper).insert(any(MdrcActiveRequestConfig.class));
    }

    @Test
    public void testInsertSelective(){
        assertFalse(mdrcActiveRequestConfigService.insertSelective(null));
        Mockito.when(mdrcActiveRequestConfigMapper.insertSelective(any(MdrcActiveRequestConfig.class))).thenReturn(1);
        assertTrue(mdrcActiveRequestConfigService.insertSelective(createMdrcActiveRequestConfig()));
        Mockito.verify(mdrcActiveRequestConfigMapper).insertSelective(any(MdrcActiveRequestConfig.class));
    }

    @Test
    public void testUpdateByPrimaryKeySelective(){
        assertFalse(mdrcActiveRequestConfigService.updateByPrimaryKeySelective(null));
        Mockito.when(mdrcActiveRequestConfigMapper.updateByPrimaryKeySelective(any(MdrcActiveRequestConfig.class))).thenReturn(1);
        assertTrue(mdrcActiveRequestConfigService.updateByPrimaryKeySelective(createMdrcActiveRequestConfig()));
        Mockito.verify(mdrcActiveRequestConfigMapper).updateByPrimaryKeySelective(any(MdrcActiveRequestConfig.class));
    }

    @Test
    public void testUpdateByPrimaryKey(){
        assertFalse(mdrcActiveRequestConfigService.updateByPrimaryKey(null));
        Mockito.when(mdrcActiveRequestConfigMapper.updateByPrimaryKey(any(MdrcActiveRequestConfig.class))).thenReturn(1);
        assertTrue(mdrcActiveRequestConfigService.updateByPrimaryKey(createMdrcActiveRequestConfig()));
        Mockito.verify(mdrcActiveRequestConfigMapper).updateByPrimaryKey(any(MdrcActiveRequestConfig.class));
    }

    @Test
    public void testSelectByPrimaryKey(){
        assertNull(mdrcActiveRequestConfigService.selectByPrimaryKey(null));
        Mockito.when(mdrcActiveRequestConfigMapper.selectByPrimaryKey(anyLong()))
            .thenReturn(createMdrcActiveRequestConfig());
        assertNotNull(mdrcActiveRequestConfigService.selectByPrimaryKey(1L));
        Mockito.verify(mdrcActiveRequestConfigMapper).selectByPrimaryKey(anyLong());
    }

    private MdrcActiveRequestConfig createMdrcActiveRequestConfig(){
        MdrcActiveRequestConfig record = new MdrcActiveRequestConfig();
        record.setId(1L);
        record.setRequestId(1L);
        record.setConfigId(1L);
        record.setStartSerial("4545465");
        record.setEndSerial("46454");
        return record;
    }

    @Test
    public void testSelectByRequestId(){
        List<MdrcActiveRequestConfig> list = new ArrayList<MdrcActiveRequestConfig>();
        list.add(createMdrcActiveRequestConfig());
        assertNull(mdrcActiveRequestConfigService.selectByRequestId(null));
        Mockito.when(mdrcActiveRequestConfigMapper.selectByRequestId(anyLong()))
            .thenReturn(list);
        assertNotNull(mdrcActiveRequestConfigService.selectByRequestId(1L));
        Mockito.verify(mdrcActiveRequestConfigMapper).selectByRequestId(anyLong());
    }

    @Test
    public void testActive(){
        assertFalse(mdrcActiveRequestConfigService.active(null));

        MdrcActiveRequestConfig record = createMdrcActiveRequestConfig();

        Mockito.when(mdrcActiveRequestConfigMapper.insertSelective(any(MdrcActiveRequestConfig.class)))
            .thenReturn(0);
        assertFalse(mdrcActiveRequestConfigService.active(record));
        Mockito.verify(mdrcActiveRequestConfigMapper).insertSelective(any(MdrcActiveRequestConfig.class));
    }

    @Test(expected = RuntimeException.class)
    public void testActive1(){

        MdrcActiveRequestConfig record = createMdrcActiveRequestConfig();

        Mockito.when(mdrcActiveRequestConfigMapper.insertSelective(any(MdrcActiveRequestConfig.class)))
            .thenReturn(1);
        Mockito.when(mdrcActiveDetailService.selectByRequestId(anyLong()))
            .thenReturn(createMdrcActiveDetail());
        Mockito.when(mdrcCardInfoService.activeRange(anyLong(), anyString(), anyString(),
            anyLong(), anyLong())).thenReturn(false);

        assertFalse(mdrcActiveRequestConfigService.active(record));
    }

    @Test(expected = RuntimeException.class)
    public void testActive2(){

        MdrcActiveRequestConfig record = createMdrcActiveRequestConfig();

        Mockito.when(mdrcActiveRequestConfigMapper.insertSelective(any(MdrcActiveRequestConfig.class)))
            .thenReturn(1);
        Mockito.when(mdrcActiveDetailService.selectByRequestId(anyLong()))
            .thenReturn(createMdrcActiveDetail());
        Mockito.when(mdrcCardInfoService.activeRange(anyLong(), anyString(), anyString(),
            anyLong(), anyLong())).thenReturn(true);

        Mockito.when(mdrcActiveDetailService.updateByPrimaryKeySelective(any(MdrcActiveDetail.class)))
            .thenReturn(false);

        assertFalse(mdrcActiveRequestConfigService.active(record));
    }

    @Test
    public void testActive3(){
        MdrcActiveRequestConfig record = createMdrcActiveRequestConfig();

        Mockito.when(mdrcActiveRequestConfigMapper.insertSelective(any(MdrcActiveRequestConfig.class)))
            .thenReturn(1);
        Mockito.when(mdrcActiveDetailService.selectByRequestId(anyLong()))
            .thenReturn(createMdrcActiveDetail());
        Mockito.when(mdrcCardInfoService.activeRange(anyLong(), anyString(), anyString(),
            anyLong(), anyLong())).thenReturn(true);

        Mockito.when(mdrcActiveDetailService.updateByPrimaryKeySelective(any(MdrcActiveDetail.class)))
            .thenReturn(true);

        assertTrue(mdrcActiveRequestConfigService.active(record));

        Mockito.verify(mdrcActiveRequestConfigMapper).insertSelective(any(MdrcActiveRequestConfig.class));
        Mockito.verify(mdrcActiveDetailService).selectByRequestId(anyLong());
        Mockito.verify(mdrcCardInfoService).activeRange(anyLong(), anyString(), anyString(),
            anyLong(), anyLong());
        Mockito.verify(mdrcActiveDetailService).updateByPrimaryKeySelective(any(MdrcActiveDetail.class));
    }


    private MdrcActiveDetail createMdrcActiveDetail(){
        MdrcActiveDetail mdrcActiveDetail = new MdrcActiveDetail();
        mdrcActiveDetail.setRequestId(1L);
        mdrcActiveDetail.setEntId(1L);
        mdrcActiveDetail.setProductId(1L);
        return mdrcActiveDetail;
    }



}
