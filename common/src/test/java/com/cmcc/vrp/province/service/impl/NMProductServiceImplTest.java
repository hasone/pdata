package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.ProductMapper;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.EnterprisesService;

/**
 * Created by sunyiwei on 2016/4/22.
 */
@RunWith(MockitoJUnitRunner.class)
public class NMProductServiceImplTest {

    @InjectMocks
    ProductServiceImpl pService = new NMProductServiceImpl();
    @Mock
    EnterprisesService enterprisesService;
    @Mock
    ProductMapper productMapper;

    @Test
    public void testSelectAllProductsByEnterCode(){
	String enterpriseCode = "12345";
	Enterprise enterprise = new Enterprise();
	enterprise.setId(1L);
	List<Product> list = new ArrayList(); 
	
	when(enterprisesService.selectByCode(enterpriseCode)).thenReturn(null).thenReturn(enterprise);
	assertNull(pService.selectAllProductsByEnterCode(null));
	assertNull(pService.selectAllProductsByEnterCode(enterpriseCode));
	
	when(productMapper.selectAllProductsByEnterId(Mockito.anyLong())).thenReturn(list);
	assertSame(list, pService.selectAllProductsByEnterCode(enterpriseCode));
	
	
    }

}
