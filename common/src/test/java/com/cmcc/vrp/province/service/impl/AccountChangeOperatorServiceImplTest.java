/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.AccountChangeOperatorMapper;
import com.cmcc.vrp.province.model.AccountChangeOperator;
import com.cmcc.vrp.province.service.AccountChangeOperatorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * <p>Title:AccountChangeOperatorServiceImplTest </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月4日
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountChangeOperatorServiceImplTest {

    @InjectMocks
    AccountChangeOperatorService acoService = new AccountChangeOperatorServiceImpl();

    @Mock
    AccountChangeOperatorMapper mapper;

    @Test
    public void testInsert() {
        //AccountChangeOperator record = new AccountChangeOperator();
        AccountChangeOperator record = build();
        when(mapper.insert(record)).thenReturn(0).thenReturn(1);
        assertFalse(acoService.insert(null));
        assertFalse(acoService.insert(record));
        assertTrue(acoService.insert(record));

        Mockito.verify(mapper, Mockito.times(2)).insert(record);
    }

    private AccountChangeOperator build(){
        AccountChangeOperator record = new AccountChangeOperator();
        record.setId(1L);
        return record;
    }

    @Test
    public void testSelectByPrimaryKey() {
        Long id = 1L;
        AccountChangeOperator aco = new AccountChangeOperator();
        when(mapper.selectByPrimaryKey(id)).thenReturn(aco);
        assertNull(acoService.selectByPrimaryKey(null));
        assertSame(aco, acoService.selectByPrimaryKey(id));
        Mockito.verify(mapper, Mockito.times(1)).selectByPrimaryKey(id);
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        AccountChangeOperator record = new AccountChangeOperator();
        when(mapper.updateByPrimaryKeySelective(record)).thenReturn(0).thenReturn(1);
        assertFalse(acoService.updateByPrimaryKeySelective(null));
        assertFalse(acoService.updateByPrimaryKeySelective(record));
        assertTrue(acoService.updateByPrimaryKeySelective(record));
        Mockito.verify(mapper, Mockito.times(2)).updateByPrimaryKeySelective(record);
    }

    @Test
    public void testSelectByMap() {
        Map map = new HashMap();
        List<AccountChangeOperator> list = new ArrayList();

        when(mapper.selectByMap(map)).thenReturn(list);
        assertSame(list, acoService.selectByMap(map));
        map.put("endTime", "2016-11-4");
        assertSame(list, acoService.selectByMap(map));

        Mockito.verify(mapper, Mockito.times(2)).selectByMap(map);
    }

    @Test
    public void testCountByMap() {
        Map map = new HashMap();
        Long result = 1L;

        when(mapper.countByMap(map)).thenReturn(result);
        assertSame(result, acoService.countByMap(map));
        map.put("endTime", "2016-11-4");
        assertSame(result, acoService.countByMap(map));

        Mockito.verify(mapper, Mockito.times(2)).countByMap(map);
    }

    @Test
    public void testDeleteBySerialNum() {
        String serialNum = "1234567";
        when(mapper.deleteBySerialNum(serialNum)).thenReturn(0).thenReturn(1);
        assertFalse(acoService.deleteBySerialNum(null));
        assertFalse(acoService.deleteBySerialNum(serialNum));
        assertTrue(acoService.deleteBySerialNum(serialNum));

        Mockito.verify(mapper, Mockito.times(2)).deleteBySerialNum(serialNum);
    }

}
