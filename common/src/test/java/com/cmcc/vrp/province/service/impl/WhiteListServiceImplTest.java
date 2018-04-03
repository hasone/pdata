package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.WhiteListMapper;
import com.cmcc.vrp.province.model.WhiteList;
import com.cmcc.vrp.province.service.WhiteListService;
import com.cmcc.vrp.util.QueryObject;
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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;

/**
 * @author qinqinyan
 * @Description 白名单服务类
 * @date 2016年5月26日
 */

@RunWith(MockitoJUnitRunner.class)
public class WhiteListServiceImplTest {

    @InjectMocks
    WhiteListService whiteListService = new WhiteListServiceImpl();

    @Mock
    WhiteListMapper whiteListMapper;


    /**
     * 获取白名单对象
     */
    @Test
    public void testGet() throws Exception {

        //invalid
        assertNull(whiteListService.get(null));

        //valid
        WhiteList record = valid();
        Mockito.when(whiteListMapper.get(anyString())).thenReturn(new WhiteList());
        assertNotNull(whiteListService.get(record.getMobile()));
        Mockito.verify(whiteListMapper).get(anyString());
    }

    @Test
    public void testDelete(){
        assertFalse(whiteListService.delete("23"));

        Mockito.when(whiteListMapper.delete(anyString())).thenReturn(0).thenReturn(1);
        assertFalse(whiteListService.delete("18867101234"));
        assertTrue(whiteListService.delete("18867101234"));
        Mockito.verify(whiteListMapper, Mockito.times(2)).delete(anyString());
    }

    @Test
    public void testInsert(){
        assertFalse(whiteListService.insert(null));

        String mobile = "18867101234";

        Mockito.when(whiteListMapper.get(mobile)).thenReturn(new WhiteList());
        assertFalse(whiteListService.insert(mobile));

        String mobile1 = "18867104321";
        Mockito.when(whiteListMapper.get(mobile1)).thenReturn(null);
        Mockito.when(whiteListMapper.insert(Mockito.any(WhiteList.class))).thenReturn(0).thenReturn(1);
        assertFalse(whiteListService.insert(mobile1));
        assertTrue(whiteListService.insert(mobile1));

        Mockito.verify(whiteListMapper, Mockito.times(3)).get(anyString());
        Mockito.verify(whiteListMapper, Mockito.times(2)).insert(any(WhiteList.class));
    }

    /**
     * 查询符合条件的记录数
     */
    @Test
    public void testCount() {
        assertSame(0L, whiteListService.count(null));

        Mockito.when(whiteListMapper.count(anyMap())).thenReturn(1L);
        assertSame(1L, whiteListService.count(new QueryObject()));
        Mockito.verify(whiteListMapper).count(anyMap());
    }

    /**
     * 查询符合条件的记录列表
     */
    @Test
    public void testQuery() {
        assertNull(whiteListService.query(null));

        Mockito.when(whiteListMapper.query(anyMap())).thenReturn(new ArrayList<WhiteList>());
        assertNotNull(whiteListService.query(new QueryObject()));
        Mockito.verify(whiteListMapper).query(anyMap());
    }

    @Test
    public void testDelete1(){
        Long id = 1L;
        Mockito.when(whiteListMapper.deleteById(anyLong())).thenReturn(0).thenReturn(1);
        assertFalse(whiteListService.delete((Long) null));
        assertFalse(whiteListService.delete(id));
        assertTrue(whiteListService.delete(id));
        Mockito.verify(whiteListMapper, Mockito.times(2)).deleteById(anyLong());
    }

    /**
     * 批量插入
     */
    @Test
    public void testInsertBatch() {
        assertFalse(whiteListService.insertBatch(null));

        List<String> list = new ArrayList<String>();
        String mobile1 = "18867100000";
        String mobile2 = "18867100001";
        String mobile3 = "18867100001";
        String mobile4 = "18867100003";
        String mobile5 = "188671003";
        list.add(mobile1);
        list.add(mobile2);
        list.add(mobile3);
        list.add(mobile4);
        list.add(mobile5);

        WhiteList whiteList = new WhiteList();
        whiteList.setId(1L);

        Mockito.when(whiteListMapper.get(mobile1)).thenReturn(null);
        Mockito.when(whiteListMapper.get(mobile2)).thenReturn(whiteList);
        Mockito.when(whiteListMapper.get(mobile4)).thenReturn(null);

        Mockito.when(whiteListMapper.insertBatch(anyList())).thenReturn(-1).thenReturn(2);

        assertFalse(whiteListService.insertBatch(list));
        assertTrue(whiteListService.insertBatch(list));

        Mockito.verify(whiteListMapper, Mockito.times(8)).get(anyString());
        Mockito.verify(whiteListMapper, Mockito.times(2)).insertBatch(anyList());
    }

    /**
     * 批量插入
     */
    @Test
    public void testInsertBatch1() {
        List<String> list = new ArrayList<String>();
        String mobile1 = "18867100000";
        list.add(mobile1);

        WhiteList whiteList = new WhiteList();
        whiteList.setId(1L);

        Mockito.when(whiteListMapper.get(mobile1)).thenReturn(whiteList);
        assertTrue(whiteListService.insertBatch(list));
        Mockito.verify(whiteListMapper).get(anyString());
    }

    private WhiteList valid() {
        WhiteList record = new WhiteList();
        record.setId(1L);
        record.setMobile("18867102222");
        return record;
    }

}
