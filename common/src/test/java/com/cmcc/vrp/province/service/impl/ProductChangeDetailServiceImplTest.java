/**
 * 
 */
package com.cmcc.vrp.province.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.ProductChangeDetailMapper;
import com.cmcc.vrp.province.model.ProductChangeDetail;
import com.cmcc.vrp.province.service.ProductChangeDetailService;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * <p>Title:ProductChangeDetailServiceImplTest </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年10月26日
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductChangeDetailServiceImplTest {

    @InjectMocks
    ProductChangeDetailService productChangeDetailService = new ProductChangeDetailServiceImpl();

    @Mock
    ProductChangeDetailMapper mapper;
    
    @Test
    public void testBatchInsert() {
	ProductChangeDetail productChangeDetail = new ProductChangeDetail();
	List<ProductChangeDetail> productChangeDetails = new ArrayList();
	productChangeDetails.add(productChangeDetail);
	
	when(mapper.batchInsert(productChangeDetails)).thenReturn(productChangeDetails.size());
	assertTrue(productChangeDetailService.batchInsert(productChangeDetails));
	
	when(mapper.batchInsert(productChangeDetails)).thenReturn(0);
	assertFalse(productChangeDetailService.batchInsert(productChangeDetails));
	
	assertFalse(productChangeDetailService.batchInsert(null));
	assertFalse(productChangeDetailService.batchInsert(new ArrayList<ProductChangeDetail>()));
	
	Mockito.verify(mapper, Mockito.times(2)).batchInsert(productChangeDetails);
    }
    
    @Test
    public void testGetProductChangeDetailsByRequestId() {
	
	assertNull(null, productChangeDetailService.getProductChangeDetailsByRequestId(null));
	
	Long requestId = 1L;
	ProductChangeDetail productChangeDetail = new ProductChangeDetail();
	List<ProductChangeDetail> productChangeDetails = new ArrayList();
	productChangeDetails.add(productChangeDetail);
	
	when(mapper.getProductChangeDetailsByRequestId(requestId)).thenReturn(productChangeDetails);
	assertSame(productChangeDetails, productChangeDetailService.getProductChangeDetailsByRequestId(requestId));
	
	Mockito.verify(mapper, Mockito.times(1)).getProductChangeDetailsByRequestId(requestId);
    }
    
}
