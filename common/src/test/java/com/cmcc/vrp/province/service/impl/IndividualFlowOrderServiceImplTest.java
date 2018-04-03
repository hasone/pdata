package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.boss.sichuan.model.flowredpacket.OrderRedPacketReq;
import com.cmcc.vrp.boss.sichuan.model.flowredpacket.OrderRedPacketResp;
import com.cmcc.vrp.boss.sichuan.model.flowredpacket.OrderRedPacketRespOutdata;
import com.cmcc.vrp.boss.sichuan.service.ScOrderRedPacketService;
import com.cmcc.vrp.province.cache.CacheService;
import com.cmcc.vrp.province.dao.IndividualFlowOrderMapper;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualFlowOrder;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualFlowOrderService;
import com.cmcc.vrp.province.service.IndividualProductService;

/**
 * IndividualActivitiesServiceImplTest.java
 * @author wujiamin
 * @date 2017年1月22日
 */
@RunWith(MockitoJUnitRunner.class)
public class IndividualFlowOrderServiceImplTest {
    @InjectMocks
    IndividualFlowOrderService service = new IndividualFlowOrderServiceImpl();
    
    @Mock
    IndividualFlowOrderMapper mapper;
    
    @Mock
    AdministerService administerService;
    
    @Mock
    IndividualProductService individualProductService;
    
    @Mock
    GlobalConfigService globalConfigService;
    
    @Mock
    ScOrderRedPacketService scOrderRedPacketService;
    
    @Mock
    IndividualAccountService individualAccountService;
    
    @Mock
    CacheService cacheService;
    
    @Test
    public void testInsert(){
        Mockito.when(mapper.insert(Mockito.any(IndividualFlowOrder.class))).thenReturn(1);
        assertTrue(service.insert(new IndividualFlowOrder()));
        Mockito.verify(mapper,Mockito.times(1)).insert(Mockito.any(IndividualFlowOrder.class));
    }
    
    @Test
    public void testInsertSelective(){
        Mockito.when(mapper.insertSelective(Mockito.any(IndividualFlowOrder.class))).thenReturn(1);
        assertTrue(service.insertSelective(new IndividualFlowOrder()));
        Mockito.verify(mapper,Mockito.times(1)).insertSelective(Mockito.any(IndividualFlowOrder.class));
    }
    
    @Test
    public void testSelectByPrimaryKey(){
        Mockito.when(mapper.selectByPrimaryKey(1L)).thenReturn(new IndividualFlowOrder());
        assertNotNull(service.selectByPrimaryKey(1L));
        Mockito.verify(mapper,Mockito.times(1)).selectByPrimaryKey(1L);
    }
    
    @Test
    public void testUpdateByPrimaryKeySelective(){
        Mockito.when(mapper.updateByPrimaryKeySelective(Mockito.any(IndividualFlowOrder.class))).thenReturn(1);
        assertTrue(service.updateByPrimaryKeySelective(new IndividualFlowOrder()));
        Mockito.verify(mapper,Mockito.times(1)).updateByPrimaryKeySelective(Mockito.any(IndividualFlowOrder.class));
    }
    
    @Test
    public void testUpdateByPrimaryKey(){
        Mockito.when(mapper.updateByPrimaryKey(Mockito.any(IndividualFlowOrder.class))).thenReturn(1);
        assertTrue(service.updateByPrimaryKey(new IndividualFlowOrder()));
        Mockito.verify(mapper,Mockito.times(1)).updateByPrimaryKey(Mockito.any(IndividualFlowOrder.class));
    }
    
    @Test
    public void testOrderFlow(){
        Mockito.when(administerService.selectByMobilePhone(Mockito.anyString())).thenReturn(null);
        Mockito.when(administerService.insertForScJizhong(Mockito.anyString())).thenReturn(false);
        
        assertNotNull(service.orderFlow("18867103685", "prdCode", "ecSerialNum"));
        
        Administer admin = new Administer();
        admin.setId(1L);
        Mockito.when(administerService.selectByMobilePhone(Mockito.anyString())).thenReturn(admin);
    
        
        Mockito.when(individualProductService.selectByProductCode(Mockito.anyString())).thenReturn(null);
        assertNotNull(service.orderFlow("18867103685", "prdCode", "ecSerialNum"));

        Mockito.when(individualAccountService.insertAccountForScJizhong(Mockito.anyLong())).thenReturn(true);

        Mockito.when(individualProductService.selectByProductCode(Mockito.anyString())).thenReturn(createProduct());
        Mockito.when(mapper.insertSelective(Mockito.any(IndividualFlowOrder.class))).thenReturn(0);
        assertNotNull(service.orderFlow("18867103685", "prdCode", "ecSerialNum"));
        
        Mockito.when(mapper.insertSelective(Mockito.any(IndividualFlowOrder.class))).thenReturn(1);
        Mockito.when(globalConfigService.get(Mockito.anyString())).thenReturn("true");
        Mockito.when(mapper.updateByPrimaryKeySelective(Mockito.any(IndividualFlowOrder.class))).thenReturn(1);
        Mockito.when(scOrderRedPacketService.sendRequest(Mockito.any(OrderRedPacketReq.class))).thenReturn(null);
        Mockito.when(individualProductService.getDefaultFlowProduct()).thenReturn(new IndividualProduct());
        Mockito.when(individualAccountService.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(),
                Mockito.anyInt())).thenReturn(new IndividualAccount());
        assertNotNull(service.orderFlow("18867103685", "prdCode", "ecSerialNum"));
        
        Mockito.when(globalConfigService.get(Mockito.anyString())).thenReturn("false");
        OrderRedPacketResp resp = new OrderRedPacketResp();
        resp.setResCode("0000000");
        OrderRedPacketRespOutdata data = new OrderRedPacketRespOutdata();
        data.setOrderId("orderId");
        data.setProdPrcid("test");
        resp.setOutData(data);
        Mockito.when(scOrderRedPacketService.sendRequest(Mockito.any(OrderRedPacketReq.class))).thenReturn(resp);
        assertNotNull(service.orderFlow("18867103685", "prdCode", "ecSerialNum"));
        
        resp.setResCode("0000001");
        Mockito.when(scOrderRedPacketService.sendRequest(Mockito.any(OrderRedPacketReq.class))).thenReturn(resp);
        assertNotNull(service.orderFlow("18867103685", "prdCode", "ecSerialNum"));
    
        Mockito.when(scOrderRedPacketService.sendRequest(Mockito.any(OrderRedPacketReq.class))).thenReturn(null);
        assertNotNull(service.orderFlow("18867103685", "prdCode", "ecSerialNum"));
    }
    
    @Test
    public void testOrderFlowForPage(){
        Mockito.when(administerService.selectByMobilePhone(Mockito.anyString())).thenReturn(null);
        Mockito.when(administerService.insertForScJizhong(Mockito.anyString())).thenReturn(false);
        
        assertFalse(service.orderFlowForPage("18867103685", "prdCode"));
        
        Administer admin = new Administer();
        admin.setId(1L);
        Mockito.when(administerService.selectByMobilePhone(Mockito.anyString())).thenReturn(admin);
    
        Mockito.when(individualProductService.selectByProductCode(Mockito.anyString())).thenReturn(null);
        assertFalse(service.orderFlowForPage("18867103685", "prdCode"));

        Mockito.when(individualProductService.selectByProductCode(Mockito.anyString())).thenReturn(createProduct());        
        Mockito.when(cacheService.getIncrOrUpdate(Mockito.anyString(), Mockito.anyInt())).thenReturn(1L);
        Mockito.when(cacheService.getDecr(Mockito.anyString())).thenReturn(1L);
        Mockito.when(globalConfigService.get(Mockito.anyString())).thenReturn("0");        
        assertFalse(service.orderFlowForPage("18867103685", "prdCode"));
        
        Mockito.when(globalConfigService.get(Mockito.anyString())).thenReturn("2");
        Mockito.when(mapper.insertSelective(Mockito.any(IndividualFlowOrder.class))).thenReturn(0);
        assertFalse(service.orderFlowForPage("18867103685", "prdCode"));
        
        Mockito.when(mapper.insertSelective(Mockito.any(IndividualFlowOrder.class))).thenReturn(1);
        Mockito.when(mapper.updateByPrimaryKeySelective(Mockito.any(IndividualFlowOrder.class))).thenReturn(0);
        
        
        Mockito.when(scOrderRedPacketService.sendRequest(Mockito.any(OrderRedPacketReq.class))).thenReturn(null);
        Mockito.when(individualProductService.getDefaultFlowProduct()).thenReturn(new IndividualProduct());
        Mockito.when(individualAccountService.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(),
                Mockito.anyInt())).thenReturn(new IndividualAccount());
        assertFalse(service.orderFlowForPage("18867103685", "prdCode"));
        
        OrderRedPacketResp resp = new OrderRedPacketResp();
        resp.setResCode("0000000");
        OrderRedPacketRespOutdata data = new OrderRedPacketRespOutdata();
        data.setOrderId("orderId");
        data.setProdPrcid("test");
        resp.setOutData(data);
        Mockito.when(scOrderRedPacketService.sendRequest(Mockito.any(OrderRedPacketReq.class))).thenReturn(resp);
        Mockito.when(individualAccountService.insertAccountForScJizhong(Mockito.anyLong())).thenReturn(false);
        assertFalse(service.orderFlowForPage("18867103685", "test"));
        
        Mockito.when(individualAccountService.insertAccountForScJizhong(Mockito.anyLong())).thenReturn(true);
        assertTrue(service.orderFlowForPage("18867103685", "test"));
        
        resp.setResCode("0000001");
        Mockito.when(scOrderRedPacketService.sendRequest(Mockito.any(OrderRedPacketReq.class))).thenReturn(resp);
        assertFalse(service.orderFlowForPage("18867103685", "prdCode"));
    
        Mockito.when(scOrderRedPacketService.sendRequest(Mockito.any(OrderRedPacketReq.class))).thenReturn(null);
        assertFalse(service.orderFlowForPage("18867103685", "prdCode"));
    }
    
    @Test
    public void testSelectBySystemNum(){
        Mockito.when(mapper.selectBySystemNum(Mockito.anyString())).thenReturn(new IndividualFlowOrder());
        assertNotNull(service.selectBySystemNum("111"));
        Mockito.verify(mapper,Mockito.times(1)).selectBySystemNum(Mockito.anyString());
    }
    
    @Test
    public void testCountByDate(){
        Mockito.when(mapper.countByDate(Mockito.any(Date.class), Mockito.any(Date.class))).thenReturn(1);
        assertSame(service.countByDate(new Date(), new Date()),1);
        Mockito.verify(mapper,Mockito.times(1)).countByDate(Mockito.any(Date.class), Mockito.any(Date.class));
    }
    
    @Test
    public void testValidateLimit(){
        Mockito.when(cacheService.get(Mockito.anyString())).thenReturn("1");
        Mockito.when(globalConfigService.get(Mockito.anyString())).thenReturn("0");        
        service.validateLimit(1L, new HashMap());
        
        Mockito.verify(globalConfigService,Mockito.times(2)).get(Mockito.anyString());
        Mockito.verify(cacheService,Mockito.times(2)).get(Mockito.anyString());       
    }
    
    @Test
    public void testSelectByMap(){
        Mockito.when(mapper.selectByMap(Mockito.anyMap())).thenReturn(new ArrayList());
        assertNotNull(service.selectByMap(new HashMap()));
    }
    
    @Test
    public void testCountByMap(){
        Mockito.when(mapper.countByMap(Mockito.anyMap())).thenReturn(1);
        assertNotNull(service.countByMap(new HashMap()));
    }
    
    private IndividualProduct createProduct(){
        IndividualProduct product = new IndividualProduct();
        product.setId(1L);
        product.setProductSize(10L);
        return product;
    }
}
