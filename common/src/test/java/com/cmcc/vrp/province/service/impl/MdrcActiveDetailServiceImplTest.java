package com.cmcc.vrp.province.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.cmcc.vrp.province.dao.MdrcActiveDetailMapper;
import com.cmcc.vrp.province.model.MdrcActiveDetail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;

/**
 * Created by qinqinyan on 2016/11/28.
 */
@RunWith(MockitoJUnitRunner.class)
public class MdrcActiveDetailServiceImplTest {

    @InjectMocks
    MdrcActiveDetailServiceImpl mdrcActiveDetailService = new MdrcActiveDetailServiceImpl();
    @Mock
    MdrcActiveDetailMapper mdrcActiveDetailMapper;

    @Test
    public void testDeleteByPrimaryKey(){
        assertFalse(mdrcActiveDetailService.deleteByPrimaryKey(null));
        Mockito.when(mdrcActiveDetailMapper.deleteByPrimaryKey(anyLong())).thenReturn(1);
        assertTrue(mdrcActiveDetailService.deleteByPrimaryKey(1L));
        Mockito.verify(mdrcActiveDetailMapper).deleteByPrimaryKey(anyLong());
    }

    @Test
    public void testInsert(){
        assertFalse(mdrcActiveDetailService.insert(null));
        Mockito.when(mdrcActiveDetailMapper.insert(any(MdrcActiveDetail.class))).thenReturn(1);
        assertTrue(mdrcActiveDetailService.insert(new MdrcActiveDetail()));
        Mockito.verify(mdrcActiveDetailMapper).insert(any(MdrcActiveDetail.class));
    }

    @Test
    public void testInsertSelective(){
        assertFalse(mdrcActiveDetailService.insertSelective(null));
        Mockito.when(mdrcActiveDetailMapper.insertSelective(any(MdrcActiveDetail.class))).thenReturn(1);
        assertTrue(mdrcActiveDetailService.insertSelective(new MdrcActiveDetail()));
        Mockito.verify(mdrcActiveDetailMapper).insertSelective(any(MdrcActiveDetail.class));
    }

    @Test
    public void testSelectByPrimaryKey(){
        assertNull(mdrcActiveDetailService.selectByPrimaryKey(null));
        MdrcActiveDetail record = new MdrcActiveDetail();
        record.setId(1L);
        Mockito.when(mdrcActiveDetailMapper.selectByPrimaryKey(anyLong())).thenReturn(new MdrcActiveDetail());
        assertNotNull(mdrcActiveDetailService.selectByPrimaryKey(1L));
        Mockito.verify(mdrcActiveDetailMapper).selectByPrimaryKey(anyLong());
    }

    @Test
    public void testUpdateByPrimaryKeySelective(){
        assertFalse(mdrcActiveDetailService.updateByPrimaryKeySelective(null));
        Mockito.when(mdrcActiveDetailMapper.updateByPrimaryKeySelective(any(MdrcActiveDetail.class))).thenReturn(1);
        assertTrue(mdrcActiveDetailService.updateByPrimaryKeySelective(new MdrcActiveDetail()));
        Mockito.verify(mdrcActiveDetailMapper).updateByPrimaryKeySelective(any(MdrcActiveDetail.class));
    }

    @Test
    public void testUpdateByPrimaryKey(){
        assertFalse(mdrcActiveDetailService.updateByPrimaryKey(null));
        Mockito.when(mdrcActiveDetailMapper.updateByPrimaryKey(any(MdrcActiveDetail.class))).thenReturn(1);
        assertTrue(mdrcActiveDetailService.updateByPrimaryKey(new MdrcActiveDetail()));
        Mockito.verify(mdrcActiveDetailMapper).updateByPrimaryKey(any(MdrcActiveDetail.class));
    }

    @Test
    public void testSelectByRequestId(){
        assertNull(mdrcActiveDetailService.selectByRequestId(null));
        MdrcActiveDetail record = new MdrcActiveDetail();
        record.setId(1L);
        Mockito.when(mdrcActiveDetailMapper.selectByRequestId(anyLong())).thenReturn(record);
        assertNotNull(mdrcActiveDetailService.selectByRequestId(1L));
        Mockito.verify(mdrcActiveDetailMapper).selectByRequestId(anyLong());
    }

    @Test
    public void testUpdateByRequestIdSelective(){
        assertFalse(mdrcActiveDetailService.updateByRequestIdSelective(null));
        Mockito.when(mdrcActiveDetailMapper.updateByRequestIdSelective(any(MdrcActiveDetail.class))).thenReturn(1);
        assertTrue(mdrcActiveDetailService.updateByRequestIdSelective(new MdrcActiveDetail()));
        Mockito.verify(mdrcActiveDetailMapper).updateByRequestIdSelective(any(MdrcActiveDetail.class));
    }
    
    @Test
    public void testSelectByconfigIdAndStatus(){
        List<MdrcActiveDetail> list = new ArrayList<MdrcActiveDetail>();
        Mockito.when(mdrcActiveDetailMapper.selectByconfigIdAndStatus(any(Long.class), any(Integer.class))).thenReturn(list);
        assertTrue(mdrcActiveDetailService.selectByconfigIdAndStatus(1L, 1).equals(list));
    }
}
