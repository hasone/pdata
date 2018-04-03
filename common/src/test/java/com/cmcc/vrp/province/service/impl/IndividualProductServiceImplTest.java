package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.boss.chongqing.service.ProductQueryService;
import com.cmcc.vrp.enums.IndividualProductType;
import com.cmcc.vrp.province.dao.IndividualProductMapper;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * @author wujiamin
 * @date 2016年10月27日
 */
@RunWith(MockitoJUnitRunner.class)
public class IndividualProductServiceImplTest {
    @InjectMocks
    IndividualProductService service = new IndividualProductServiceImpl();
    
    @Mock
    IndividualProductMapper mapper;
    
    @Mock
    GlobalConfigService globalConfigService;
    
    @Mock
    ProductQueryService productQueryService;
    
    @Test
    public void testSelectByDefaultValue() {
        when(mapper.selectByDefaultValue(1)).thenReturn(new ArrayList());
        assertNotNull(service.selectByDefaultValue(1));
        verify(mapper,times(1)).selectByDefaultValue(1);        
    }
    
    @Test
    public void testGetFlowcoinProduct(){
        List<IndividualProduct> products = new ArrayList();
        IndividualProduct product = new IndividualProduct();
        products.add(product);
        when(mapper.selectByType(IndividualProductType.FLOW_COIN.getValue())).thenReturn(null, new ArrayList(), products);
    
        assertNull(service.getFlowcoinProduct());
        assertNull(service.getFlowcoinProduct());
        assertNotNull(service.getFlowcoinProduct());
        
        verify(mapper,times(3)).selectByType(IndividualProductType.FLOW_COIN.getValue());       
    }
    
    @Test
    public void testGetPhoneFareProduct() {
        List<IndividualProduct> products = new ArrayList();
        IndividualProduct product = new IndividualProduct();
        products.add(product);
        when(mapper.selectByType(IndividualProductType.PHONE_FARE.getValue())).thenReturn(null, new ArrayList(), products);
    
        assertNull(service.getPhoneFareProduct());
        assertNull(service.getPhoneFareProduct());
        assertNotNull(service.getPhoneFareProduct());
        
        verify(mapper,times(3)).selectByType(IndividualProductType.PHONE_FARE.getValue());       
    }
    
    @Test
    public void testGetIndivialPointProduct() {
        List<IndividualProduct> products = new ArrayList();
        IndividualProduct product = new IndividualProduct();
        products.add(product);
        when(mapper.selectByType(IndividualProductType.INDIVIDUAL_POINT.getValue())).thenReturn(null, new ArrayList(), products);
    
        assertNull(service.getIndivialPointProduct());
        assertNull(service.getIndivialPointProduct());
        assertNotNull(service.getIndivialPointProduct());
        
        verify(mapper,times(3)).selectByType(IndividualProductType.INDIVIDUAL_POINT.getValue());       
    }
    
    @Test
    public void testSelectByPrimaryId() {
        assertNull(service.selectByPrimaryId(null));
        when(mapper.selectByPrimaryKey(1L)).thenReturn(new IndividualProduct());
        assertNotNull(service.selectByPrimaryId(1L));
        verify(mapper,times(1)).selectByPrimaryKey(1L);       
    }

    @Test
    public void testGetProductsByAdminIdAndType() {
        when(mapper.getProductsByAdminIdAndType(1L,1)).thenReturn(new ArrayList());
        assertNotNull(service.getProductsByAdminIdAndType(1L,1));
        verify(mapper,times(1)).getProductsByAdminIdAndType(1L,1);        
    }
    
    @Test
    public void testSelectByType() {
        when(mapper.selectByType(1)).thenReturn(new ArrayList());
        assertNotNull(service.selectByType(1));
        verify(mapper,times(1)).selectByType(1);
        
    }
    
    @Test
    public void testSelectByProductCode(){
        when(mapper.selectByProductCode(Mockito.anyString())).thenReturn(new IndividualProduct());
        assertNotNull(service.selectByProductCode("test"));
        verify(mapper,times(1)).selectByProductCode("test");
    }
    
    @Test
    public void testGetDefaultFlowProduct() {
        List<IndividualProduct> products = new ArrayList();
        IndividualProduct product = new IndividualProduct();
        products.add(product);
        when(mapper.selectByType(IndividualProductType.DEFAULT_FLOW_PACKAGE.getValue())).thenReturn(null, new ArrayList(), products);
    
        assertNull(service.getDefaultFlowProduct());
        assertNull(service.getDefaultFlowProduct());
        assertNotNull(service.getDefaultFlowProduct());
        
        verify(mapper,times(3)).selectByType(IndividualProductType.DEFAULT_FLOW_PACKAGE.getValue());       
    }
    
    @Test
    public void testGetYqxProduct() {
        List<IndividualProduct> products = new ArrayList<IndividualProduct>();
        IndividualProduct p = new IndividualProduct();
        p.setProductCode("test");
        products.add(p);
        when(mapper.selectByType(Mockito.anyInt())).thenReturn(products);
        when(globalConfigService.get(GlobalConfigKeyEnum.YQX_CQ_ORIGIN_ID.getKey())).thenReturn("ydcqyqx");
        when(globalConfigService.get(GlobalConfigKeyEnum.YQX_CQ_PRODUCT_NUM_LIMIT.getKey())).thenReturn("5");
        when(productQueryService.getProductNum(Mockito.anyString())).thenReturn(1.0);
        assertNotNull(service.getYqxProduct("ydcqyqx", true));
        
        assertNotNull(service.getYqxProduct("ydcqyqx", false));
    }
}
