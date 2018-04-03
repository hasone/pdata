/**
 * 
 */
package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.EcApprovalDetailMapper;
import com.cmcc.vrp.province.model.EcApprovalDetail;
import com.cmcc.vrp.province.service.EcApprovalDetailService;
/**
 * <p>Title:EcApprovalDetailServiceImplTest </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年11月2日
 */
@RunWith(MockitoJUnitRunner.class)
public class EcApprovalDetailServiceImplTest {
    
    @InjectMocks
    EcApprovalDetailService eadService = new EcApprovalDetailServiceImpl();
    
    @Mock
    EcApprovalDetailMapper mapper;
    
    @Test
    public void testInsert() {
	EcApprovalDetail ecApprovalDetail = new EcApprovalDetail();
	when(mapper.insert(ecApprovalDetail)).thenReturn(0).thenReturn(1);
	assertFalse(eadService.insert(ecApprovalDetail));
	assertTrue(eadService.insert(ecApprovalDetail));
	
	Mockito.verify(mapper, Mockito.times(2)).insert(ecApprovalDetail);
    }
    
    @Test
    public void testSelectByRequestId() {
	Long requestId = 1L;
	EcApprovalDetail ecApprovalDetail = new EcApprovalDetail();
	when(mapper.selectByRequestId(requestId)).thenReturn(ecApprovalDetail);
	assertNull(eadService.selectByRequestId(null));
	assertSame(ecApprovalDetail, eadService.selectByRequestId(requestId));
	
	Mockito.verify(mapper, Mockito.times(1)).selectByRequestId(requestId);
    }
}
