/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.EntCallbackAddrMapper;
import com.cmcc.vrp.province.model.EntCallbackAddr;
import com.cmcc.vrp.province.service.EntCallbackAddrService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * <p>Title:EntCallbackAddrServiceImplTest </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年10月31日
 */
@RunWith(MockitoJUnitRunner.class)
public class EntCallbackAddrServiceImplTest {

    @InjectMocks
    EntCallbackAddrService eService = new EntCallbackAddrServiceImpl();

    @Mock
    EntCallbackAddrMapper entCallbackAddrMapper;

    @Test
    public void testGet() {

        Long entId = 1L;
        EntCallbackAddr entCallbackAddr = new EntCallbackAddr();

        when(entCallbackAddrMapper.getByEntId(entId)).thenReturn(entCallbackAddr);
        assertNull(eService.get(null));
        assertSame(entCallbackAddr, eService.get(entId));

        Mockito.verify(entCallbackAddrMapper, Mockito.times(1)).getByEntId(entId);
    }

    @Test
    public void testDelete() {

        Long entId = 1L;

        when(entCallbackAddrMapper.deleteByEntId(entId)).thenReturn(-1).thenReturn(1);
        assertFalse(eService.delete(null));
        assertFalse(eService.delete(entId));
        assertTrue(eService.delete(entId));

        Mockito.verify(entCallbackAddrMapper, Mockito.times(2)).deleteByEntId(entId);
    }

    @Test
    public void testUpdate() {
        Long entId = 1L;
        String newCallbackAddr = "aaa";

        when(entCallbackAddrMapper.update(entId, newCallbackAddr)).thenReturn(0).thenReturn(1);
        assertFalse(eService.update(null, null));
        assertFalse(eService.update(entId, null));
        assertFalse(eService.update(entId, newCallbackAddr));
        assertTrue(eService.update(entId, newCallbackAddr));

        Mockito.verify(entCallbackAddrMapper, Mockito.times(2)).update(entId, newCallbackAddr);
    }

    @Test
    public void testInsert() {
        EntCallbackAddr entCallbackAddr = new EntCallbackAddr();

        when(entCallbackAddrMapper.insert(entCallbackAddr)).thenReturn(0).thenReturn(1);
        assertFalse(eService.insert(null));
        assertFalse(eService.insert(entCallbackAddr));
        assertTrue(eService.insert(entCallbackAddr));

        Mockito.verify(entCallbackAddrMapper, Mockito.times(2)).insert(entCallbackAddr);
    }


}
