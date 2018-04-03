/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.AccountChangeDetailMapper;
import com.cmcc.vrp.province.model.AccountChangeDetail;
import com.cmcc.vrp.province.service.AccountChangeDetailService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>Title:AccountChangeDetailServiceImplTest </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月4日
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountChangeDetailServiceImplTest {

    @InjectMocks
    AccountChangeDetailService acdService = new AccountChangeDetailServiceImpl();

    @Mock
    AccountChangeDetailMapper mapper;

    @Test
    public void testInsert() {
        //AccountChangeDetail record = new AccountChangeDetail();
        AccountChangeDetail record = build();
        when(mapper.insert(record)).thenReturn(0).thenReturn(1);
        assertFalse(acdService.insert(null));
        assertFalse(acdService.insert(record));
        assertTrue(acdService.insert(record));

        verify(mapper, Mockito.times(2)).insert(record);
    }

    @Test
    public void testSelectByRequestId() {
        Long requestId = 1L;
        AccountChangeDetail acd = new AccountChangeDetail();
        when(mapper.selectByRequestId(requestId)).thenReturn(acd);

        assertNull(acdService.selectByRequestId(null));
        assertSame(acd, acdService.selectByRequestId(requestId));
        verify(mapper, Mockito.times(1)).selectByRequestId(requestId);
    }

    private AccountChangeDetail build(){
        AccountChangeDetail record = new AccountChangeDetail();
        record.setId(1L);
        return record;
    }

}
