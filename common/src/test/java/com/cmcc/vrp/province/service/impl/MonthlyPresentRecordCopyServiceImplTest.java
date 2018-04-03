package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.MonthlyPresentRecordCopyMapper;
import com.cmcc.vrp.province.model.MonthlyPresentRecordCopy;

/**   
* @Title: MonthlyPresentRecordCopyServiceImplTest.java 
* @Package com.cmcc.vrp.province.service.impl 
* @Description: 
* @author lgk8023   
* @date 2017年11月15日 下午4:51:16 
* @version V1.0   
*/
@RunWith(MockitoJUnitRunner.class)
public class MonthlyPresentRecordCopyServiceImplTest {
    @InjectMocks
    MonthlyPresentRecordCopyServiceImpl monthlyPresentRecordCopyService = new MonthlyPresentRecordCopyServiceImpl();

    @Mock
    MonthlyPresentRecordCopyMapper monthlyPresentRecordCopyMapper;
    /** 
    * @Title: testBatchInsert 
    * @Description: 
    * @throws 
    */
    @Test
    public void testBatchInsert() {
        Assert.assertFalse(monthlyPresentRecordCopyService.batchInsert(null));
        
        List<MonthlyPresentRecordCopy> list = new ArrayList<MonthlyPresentRecordCopy>();
        Assert.assertFalse(monthlyPresentRecordCopyService.batchInsert(list));
        list.add(new MonthlyPresentRecordCopy());
        when(monthlyPresentRecordCopyMapper.batchInsert(list)).thenReturn(list.size());
        assertTrue(monthlyPresentRecordCopyService.batchInsert(list));
        
        when(monthlyPresentRecordCopyMapper.batchInsert(list)).thenReturn(0);
        assertFalse(monthlyPresentRecordCopyService.batchInsert(list));
    }
    /** 
    * @Title: testGetByRuleId 
    * @Description: 
    * @throws 
    */
    @Test
    public void testGetByRuleId() {
        Mockito.when(monthlyPresentRecordCopyMapper.getByRuleId(Mockito.anyLong())).thenReturn(null);
        Assert.assertNull(monthlyPresentRecordCopyService.getByRuleId(1l));
    }

}
