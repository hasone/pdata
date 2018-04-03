package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.DistrictMapper;
import com.cmcc.vrp.province.model.District;
import com.cmcc.vrp.province.service.DistrictService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;

/**
 * Created by qinqinyan on 2016/11/4.
 */
@RunWith(MockitoJUnitRunner.class)
public class DistrictServiceImplTest {
    @InjectMocks
    DistrictService districtService = new DistrictServiceImpl();

    @Mock
    DistrictMapper districtMapper;

    @Test
    public void testQueryByParentId(){
        assertNull(districtService.queryByParentId(null));

        Long parentId = 1L;
        Mockito.when(districtMapper.selectDisByParentId(anyLong())).thenReturn(new ArrayList<District>());
        assertNotNull(districtService.queryByParentId(parentId));
        Mockito.verify(districtMapper).selectDisByParentId(anyLong());
    }

    @Test
    public void testQueryBySonId(){
        assertNull(districtService.queryBySonId(null));

        Long sonId = 1L;
        Mockito.when(districtMapper.selectDisBySonId(anyLong())).thenReturn(new District());
        assertNotNull(districtService.queryBySonId(sonId));
        Mockito.verify(districtMapper).selectDisBySonId(anyLong());
    }

    @Test
    public void testQueryById(){
        assertNull(districtService.queryById(null));

        Long id = 1L;
        Mockito.when(districtMapper.selectById(anyLong())).thenReturn(new District());
        assertNotNull(districtService.queryById(id));
        Mockito.verify(districtMapper).selectById(anyLong());
    }

    @Test
    public void testSelectFullDistrictNameById(){
        assertNull(districtService.selectFullDistrictNameById(null));

        Long id = 1L;
        Mockito.when(districtMapper.selectFullDistrictNameById(anyLong())).thenReturn("test");
        assertEquals("test", districtService.selectFullDistrictNameById(id));
        Mockito.verify(districtMapper).selectFullDistrictNameById(anyLong());
    }

    @Test
    public void testSelectNodeById(){
        assertNull(districtService.selectNodeById(null));

        Long id = 1L;
        Mockito.when(districtMapper.selectNodeById(anyLong())).thenReturn(new ArrayList<Long>());
        assertNotNull(districtService.selectNodeById(id));
        Mockito.verify(districtMapper).selectNodeById(anyLong());
    }

    @Test
    public void testSelectChildByName(){
        assertNotNull(districtService.selectChildByName(null));

        String name = "test";
        Mockito.when(districtMapper.selectChildByName(anyString())).thenReturn(new ArrayList<District>());
        assertNotNull(districtService.selectChildByName(name));
        Mockito.verify(districtMapper).selectChildByName(anyString());
    }

    @Test
    public void testSelectByName(){
        Mockito.when(districtMapper.selectByName(anyString())).thenReturn(new ArrayList<District>());
        assertNotNull(districtService.selectByName(anyString()));
        Mockito.verify(districtMapper).selectByName(anyString());
    }

    @Test
    public void testSelectByParentId(){
        Mockito.when(districtMapper.selectByParentId(anyLong())).thenReturn(new ArrayList<District>());
        assertNotNull(districtService.selectByParentId(anyLong()));
        Mockito.verify(districtMapper).selectByParentId(anyLong());
    }
}
