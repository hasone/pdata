/**
 * 
 */
package com.cmcc.vrp.province.service.impl;
import com.cmcc.vrp.province.dao.AccountRecordMapper;
import com.cmcc.vrp.province.model.AccountRecord;
import com.cmcc.vrp.province.service.AccountRecordService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * <p>Title:EmptyAccountRecordServiceImplTest </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年11月9日
 */
@RunWith(MockitoJUnitRunner.class)
public class EmptyAccountRecordServiceImplTest {
    
    @InjectMocks
    AccountRecordService arService = new EmptyAccountRecordServiceImpl();
    
    @Mock
    AccountRecordMapper accountRecordMapper;
    
    @Test
    public void testBatchInsert() {
	List<AccountRecord> records = new ArrayList();
	assertTrue(arService.batchInsert(records));
    }
    
    @Test
    public void testCreate() {
	AccountRecord accountRecord = new AccountRecord();
	assertTrue(arService.create(accountRecord));
    }
    
    @Test
    public void testSelectBySerialNumAndEnterId() {
	String serialNum = "aaa";
        Long enterId = 1L;
	List<AccountRecord> list = new ArrayList();
	when(accountRecordMapper.selectBySerialNumAndEnterId(Mockito.anyString(), Mockito.anyLong())).thenReturn(list);
	assertSame(list, arService.selectBySerialNumAndEnterId(serialNum, enterId));
	Mockito.verify(accountRecordMapper, Mockito.times(1)).selectBySerialNumAndEnterId(Mockito.anyString(), Mockito.anyLong());
    }
    
    @Test
    public void testGetOutgoingRecordByPltSn() {
        Assert.assertNull(arService.getOutgoingRecordByPltSn(""));
    }
}
