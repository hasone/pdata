/**
 * 
 */
package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.ProductChangeOperatorMapper;
import com.cmcc.vrp.province.model.ProductChangeOperator;
import com.cmcc.vrp.province.service.ProductChangeOperatorService;

/**
 * <p>Title:ProductChangeOperatorServiceImplTest </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年10月26日
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductChangeOperatorServiceImplTest {
    
    @InjectMocks
    ProductChangeOperatorService productChangeOperatorService = new ProductChangeOperatorServiceImpl();
    
    @Mock
    ProductChangeOperatorMapper productChangeOperatorMapper;
    
    @Test
    public void testBatchInsert() {
	
	List<ProductChangeOperator> pcoList = new ArrayList();
	ProductChangeOperator productChangeOperator = new ProductChangeOperator();
	pcoList.add(productChangeOperator);
	
	when(productChangeOperatorMapper.batchInsert(Mockito.anyMap())).thenReturn(pcoList.size());
	assertTrue(productChangeOperatorService.batchInsert(pcoList));
	
	assertFalse(productChangeOperatorService.batchInsert(null));
	assertFalse(productChangeOperatorService.batchInsert(new ArrayList<ProductChangeOperator>()));
	
	when(productChangeOperatorMapper.batchInsert(Mockito.anyMap())).thenReturn(0);
	assertFalse(productChangeOperatorService.batchInsert(pcoList));
	
	Mockito.verify(productChangeOperatorMapper, Mockito.times(2)).batchInsert(Mockito.anyMap());
    }
    
    @Test
    public void testGetProductChangeRecordByEntId() {
	
	Long entId = 1L;
	List<ProductChangeOperator> pcoList = new ArrayList();
	ProductChangeOperator productChangeOperator = new ProductChangeOperator();
	pcoList.add(productChangeOperator);
	when(productChangeOperatorMapper.getProductChangeRecordByEntId(entId)).thenReturn(pcoList);
	assertSame(pcoList, productChangeOperatorService.getProductChangeRecordByEntId(entId));
	assertSame(null, productChangeOperatorService.getProductChangeRecordByEntId(null));
	
	Mockito.verify(productChangeOperatorMapper, Mockito.times(1)).getProductChangeRecordByEntId(entId);
    }

    @Test
    public void testDeleteProductChangeRecordByEntId() {
	
	Long entId = 1L;
	when(productChangeOperatorMapper.deleteProductChangeRecordByEntId(entId)).thenReturn(1);
	assertTrue(productChangeOperatorService.deleteProductChangeRecordByEntId(entId));
	
	when(productChangeOperatorMapper.deleteProductChangeRecordByEntId(entId)).thenReturn(0);
	assertFalse(productChangeOperatorService.deleteProductChangeRecordByEntId(entId));
	
	assertFalse(productChangeOperatorService.deleteProductChangeRecordByEntId(null));
	
	Mockito.verify(productChangeOperatorMapper, Mockito.times(2)).deleteProductChangeRecordByEntId(entId);
    }

}
