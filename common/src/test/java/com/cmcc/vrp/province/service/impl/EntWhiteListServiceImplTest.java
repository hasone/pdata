/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.EntWhiteListMapper;
import com.cmcc.vrp.province.model.EntWhiteList;
import com.cmcc.vrp.province.service.EntWhiteListService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * <p>Title:EntWhiteListServiceImplTest </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月1日
 */
@RunWith(MockitoJUnitRunner.class)
public class EntWhiteListServiceImplTest {

    @InjectMocks
    EntWhiteListService entWhiteListService = new EntWhiteListServiceImpl();

    @Mock
    EntWhiteListMapper entWhiteListMapper;

    @Test
    public void testIsIpInEntWhiteList() {
        String ip = "127.0.0.1";
        Long entId = 1L;
        List<EntWhiteList> list = new ArrayList();
        EntWhiteList e1 = new EntWhiteList();
        e1.setEntId(1L);
        e1.setIpAddress("192.168.10.1");
        EntWhiteList e2 = new EntWhiteList();
        e2.setEntId(1L);
        e2.setIpAddress("127.0.0.1");
        list.add(e1);
        list.add(e2);
        when(entWhiteListMapper.selectByEntId(entId)).thenReturn(new ArrayList<EntWhiteList>()).thenReturn(list);

        assertFalse(entWhiteListService.isIpInEntWhiteList(ip, entId));
        assertTrue(entWhiteListService.isIpInEntWhiteList(ip, entId));
        Mockito.verify(entWhiteListMapper, Mockito.times(2)).selectByEntId(entId);

    }
    
    @Test
    public void testIsIpInEntWhiteList2() {
        String ip = "127.0.0.1";
        Long entId = 1L;
        when(entWhiteListMapper.selectByEntId(entId)).thenReturn(null);

        assertFalse(entWhiteListService.isIpInEntWhiteList(ip, entId));
        Mockito.verify(entWhiteListMapper).selectByEntId(entId);
    }

    @Test
    public void testSelectByEntId() {
        Long entId = 1L;
        List<EntWhiteList> list = new ArrayList();
        when(entWhiteListMapper.selectByEntId(entId)).thenReturn(list);
        assertSame(list, entWhiteListService.selectByEntId(entId));
        Mockito.verify(entWhiteListMapper, Mockito.times(1)).selectByEntId(entId);
    }

    @Test
    public void testDeleteByEntId() {
        Long entId = 1L;
        when(entWhiteListMapper.deleteByEntId(entId)).thenReturn(-1).thenReturn(1);
        assertFalse(entWhiteListService.deleteByEntId(entId));
        assertTrue(entWhiteListService.deleteByEntId(entId));
        Mockito.verify(entWhiteListMapper, Mockito.times(2)).deleteByEntId(entId);
    }

    @Test
    public void testBatchInsert() {
        List<EntWhiteList> entWhiteLists = new ArrayList();
        EntWhiteList e = new EntWhiteList();
        entWhiteLists.add(e);
        when(entWhiteListMapper.batchInsert(entWhiteLists)).thenReturn(0).thenReturn(entWhiteLists.size());
        assertFalse(entWhiteListService.batchInsert(entWhiteLists));
        assertTrue(entWhiteListService.batchInsert(entWhiteLists));
        Mockito.verify(entWhiteListMapper, Mockito.times(2)).batchInsert(entWhiteLists);
    }

    @Test
    public void testInsertIps() {
        List<String> ips = new ArrayList();
        Long entId = 1L;
        String ip = "127.0.0.1";
        ips.add(ip);
        when(entWhiteListMapper.insertIps(ips, entId)).thenReturn(0).thenReturn(ips.size());
        assertFalse(entWhiteListService.insertIps(ips, entId));
        assertTrue(entWhiteListService.insertIps(ips, entId));
        Mockito.verify(entWhiteListMapper, Mockito.times(2)).insertIps(ips, entId);
    }
}
