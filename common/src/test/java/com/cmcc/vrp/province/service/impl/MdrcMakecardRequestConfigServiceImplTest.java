package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.MdrcMakecardRequestConfigMapper;
import com.cmcc.vrp.province.model.MdrcMakecardRequestConfig;
import com.cmcc.vrp.province.service.MdrcMakecardRequestConfigService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;

/**
 * Created by qinqinyan on 2016/12/1.
 */
@RunWith(PowerMockRunner.class)
public class MdrcMakecardRequestConfigServiceImplTest {

    @InjectMocks
    MdrcMakecardRequestConfigService mdrcMakecardRequestConfigService = new MdrcMakecardRequestConfigServiceImpl();

    @Mock
    MdrcMakecardRequestConfigMapper mdrcMakecardRequestConfigMapper;

    @Test
    public void testDeleteByPrimaryKey(){
        assertFalse(mdrcMakecardRequestConfigService.deleteByPrimaryKey(null));
        Mockito.when(mdrcMakecardRequestConfigMapper.deleteByPrimaryKey(anyLong())).thenReturn(1);
        assertTrue(mdrcMakecardRequestConfigService.deleteByPrimaryKey(1L));
        Mockito.verify(mdrcMakecardRequestConfigMapper).deleteByPrimaryKey(anyLong());
    }

    @Test
    public void testSelectByPrimaryKey(){
        assertNull(mdrcMakecardRequestConfigService.selectByPrimaryKey(null));
        Mockito.when(mdrcMakecardRequestConfigMapper.selectByPrimaryKey(anyLong()))
            .thenReturn(createMdrcMakecardRequestConfig());
        assertNotNull(mdrcMakecardRequestConfigService.selectByPrimaryKey(1L));
        Mockito.verify(mdrcMakecardRequestConfigMapper).selectByPrimaryKey(anyLong());
    }


    @Test
    public void testInsert(){
        assertFalse(mdrcMakecardRequestConfigService.insert(null));
        Mockito.when(mdrcMakecardRequestConfigMapper.insert(any(MdrcMakecardRequestConfig.class))).thenReturn(1);
        assertTrue(mdrcMakecardRequestConfigService.insert(createMdrcMakecardRequestConfig()));
        Mockito.verify(mdrcMakecardRequestConfigMapper).insert(any(MdrcMakecardRequestConfig.class));
    }

    @Test
    public void testInsertSelective(){
        assertFalse(mdrcMakecardRequestConfigService.insertSelective(null));
        Mockito.when(mdrcMakecardRequestConfigMapper.insertSelective(any(MdrcMakecardRequestConfig.class))).thenReturn(1);
        assertTrue(mdrcMakecardRequestConfigService.insertSelective(createMdrcMakecardRequestConfig()));
        Mockito.verify(mdrcMakecardRequestConfigMapper).insertSelective(any(MdrcMakecardRequestConfig.class));
    }

    @Test
    public void testUpdateByPrimaryKeySelective(){
        assertFalse(mdrcMakecardRequestConfigService.updateByPrimaryKeySelective(null));
        Mockito.when(mdrcMakecardRequestConfigMapper.updateByPrimaryKeySelective(any(MdrcMakecardRequestConfig.class))).thenReturn(1);
        assertTrue(mdrcMakecardRequestConfigService.updateByPrimaryKeySelective(createMdrcMakecardRequestConfig()));
        Mockito.verify(mdrcMakecardRequestConfigMapper).updateByPrimaryKeySelective(any(MdrcMakecardRequestConfig.class));
    }

    @Test
    public void testUpdateByPrimaryKey(){
        assertFalse(mdrcMakecardRequestConfigService.updateByPrimaryKey(null));
        Mockito.when(mdrcMakecardRequestConfigMapper.updateByPrimaryKey(any(MdrcMakecardRequestConfig.class))).thenReturn(1);
        assertTrue(mdrcMakecardRequestConfigService.updateByPrimaryKey(createMdrcMakecardRequestConfig()));
        Mockito.verify(mdrcMakecardRequestConfigMapper).updateByPrimaryKey(any(MdrcMakecardRequestConfig.class));
    }

    private MdrcMakecardRequestConfig createMdrcMakecardRequestConfig(){
        MdrcMakecardRequestConfig mdrcMakecardRequestConfig = new MdrcMakecardRequestConfig();
        mdrcMakecardRequestConfig.setId(1L);
        return mdrcMakecardRequestConfig;
    }
    
    @Test
    public void testSelectByRequestId(){
        assertNull(mdrcMakecardRequestConfigService.selectByRequestId(null));
        Mockito.when(mdrcMakecardRequestConfigMapper.selectByRequestId(Mockito.anyLong())).thenReturn(new MdrcMakecardRequestConfig());
        assertNotNull(mdrcMakecardRequestConfigService.selectByRequestId(1L));
    }

}
