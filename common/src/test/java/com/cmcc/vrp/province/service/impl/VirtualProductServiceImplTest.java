package com.cmcc.vrp.province.service.impl;


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.cmcc.vrp.exception.ProductInitException;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierProductMap;
import com.cmcc.vrp.province.model.json.post.PresentRecordJson;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;

/**
 * Created by leelyn on 2016/12/12.
 */
@RunWith(PowerMockRunner.class)
public class VirtualProductServiceImplTest {

    @InjectMocks
    VirtualProductServiceImpl virtualProductService = new VirtualProductServiceImpl();

    @Mock
    ProductService productService;

    @Mock
    GlobalConfigService globalConfigService;

    @Mock
    SupplierProductService supplierProductService;

    @Mock
    SupplierProductMapService supplierProductMapService;

    @Before
    public void initMocks() {
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.HLJ_FEE_SUPPLIER_ID.getKey())).thenReturn("12");
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.HLJ_FEE_GROUPACCNO.getKey())).thenReturn("xxxx");
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.HLJ_FEE_GROUPNO.getKey())).thenReturn("xxxx");
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.HLJ_FEE_APPKEY.getKey())).thenReturn("xxxx");

        PowerMockito.when(productService.insertProduct(any(Product.class))).thenReturn(true);
        PowerMockito.when(supplierProductService.insert(any(SupplierProduct.class))).thenReturn(true);
        PowerMockito.when(supplierProductMapService.create(any(SupplierProductMap.class))).thenReturn(true);

        PowerMockito.when(productService.getProductForFlowAccount(anyString(), anyLong())).thenReturn(new Product());
    }

    @Test
    public void testInitProcess() {
        try {
            virtualProductService.initProcess(1l, "50001");
        } catch (ProductInitException e) {
            Assert.assertEquals(e.getMessage(), "参数缺失");
        }
        
        PowerMockito.when(productService.get(anyLong())).thenReturn(buildPrd(3));
        try {
            virtualProductService.initProcess(1l, "50001");
        } catch (ProductInitException e) {
            Assert.assertEquals(e.getMessage(), "单笔额度超过500.0元");
        }
        PowerMockito.when(productService.get(anyLong())).thenReturn(buildPrd(3));
        PowerMockito.when(productService.getPrdBySizeAndId(anyLong(), anyLong())).thenReturn(new Product());
        try {
            Assert.assertNotNull(virtualProductService.initProcess(1l, "50000"));
        } catch (ProductInitException e) {
            e.printStackTrace();
        }
        PowerMockito.when(productService.get(anyLong())).thenReturn(buildPrd(3));
        PowerMockito.when(productService.getPrdBySizeAndId(anyLong(), anyLong())).thenReturn(null);
        try {
            Assert.assertNotNull(virtualProductService.initProcess(1l, "50000"));
        } catch (ProductInitException e) {
            e.printStackTrace();
        }

        PowerMockito.when(productService.get(anyLong())).thenReturn(buildPrd(2));
        try {
            Assert.assertNotNull(virtualProductService.initProcess(1l, "50000"));
        } catch (ProductInitException e) {
            e.printStackTrace();
        }

        PowerMockito.when(productService.get(anyLong())).thenReturn(buildPrd(1));
        try {
            Assert.assertNotNull(virtualProductService.initProcess(1l, "50000"));
        } catch (ProductInitException e) {
            e.printStackTrace();
        }
        
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.MAX_SIZE_VIRTRUAL_FLOW_PRODUCT.getKey())).thenReturn("1");
        try {
            Assert.assertNotNull(virtualProductService.initProcess(1l, "1025"));
        } catch (ProductInitException e) {
            e.printStackTrace();
        }
        
        PowerMockito.when(productService.get(anyLong())).thenReturn(buildPrd(4));
        try {
            Assert.assertNotNull(virtualProductService.initProcess(1l, "50000"));
        } catch (ProductInitException e) {
            e.printStackTrace();
        }
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.GIVE_VIRTUAL_COIN_MAX.getKey())).thenReturn("100");
        try {
            Assert.assertNotNull(virtualProductService.initProcess(1l, "50000"));
        } catch (ProductInitException e) {
            e.printStackTrace();
        }
        
        //不存在的类型
        PowerMockito.when(productService.get(anyLong())).thenReturn(buildPrd(1111));        
        try {
            Assert.assertNotNull(virtualProductService.initProcess(1l, "50000"));
        } catch (ProductInitException e) {
            e.printStackTrace();
        }
        
        PowerMockito.when(productService.get(anyLong())).thenReturn(new Product());
        try {
            Assert.assertNotNull(virtualProductService.initProcess(1l, "50000"));
        } catch (ProductInitException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBatchInitProcess() throws ProductInitException {
        try{
            virtualProductService.batchInitProcess(null);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        PowerMockito.when(productService.get(anyLong())).thenReturn(buildPrd(1));
        Assert.assertNotNull(virtualProductService.batchInitProcess(getRecords()));
    }

    @Test
    public void testActivityInitProcess() throws ProductInitException {
        PowerMockito.when(productService.get(anyLong())).thenReturn(buildPrd(2));
        Assert.assertNotNull(virtualProductService.activityInitProcess(buildPrizes()));
    }

    private String buildPrizes() {
        List<ActivityPrize> list = new ArrayList<ActivityPrize>();
        for (int i = 0; i < 10; i++) {
            ActivityPrize prize = new ActivityPrize();
            prize.setProbability("80");
            prize.setProductId((long) i);
            prize.setSize((long) i);
            list.add(prize);
        }
        return new Gson().toJson(list);
    }

    private Product buildPrd(Integer type) {
        Product product = new Product();
        product.setType(type);
        product.setPrice(100);
        product.setFlowAccountFlag(1);
        return product;
    }

    private List<PresentRecordJson> getRecords() {
        List<PresentRecordJson> list = new ArrayList<PresentRecordJson>();
        for (int i = 0; i < 10; i++) {
            PresentRecordJson recordJson = new PresentRecordJson();
            recordJson.setPrdId((long) i);
            recordJson.setSize("1");
            list.add(recordJson);
        }
        return list;
    }
    
    @Test
    public void testGetMaxSizeOfVirtualMobileFee(){
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.MAX_SIZE_VIRTRUAL_MOBILE_FEE.getKey()))
        .thenReturn(null).thenReturn("10").thenReturn("xx");
        try {
            Assert.assertNotNull(virtualProductService.getMaxSizeOfVirtualMobileFee());
            Assert.assertNotNull(virtualProductService.getMaxSizeOfVirtualMobileFee());
            Assert.assertNotNull(virtualProductService.getMaxSizeOfVirtualMobileFee());
        } catch (Exception e) {
           
        }
        
    }
    
    @Test
    public void testGetMaxSizeOfVirtualFlowProduct(){
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.MAX_SIZE_VIRTRUAL_FLOW_PRODUCT.getKey()))
            .thenReturn(null).thenReturn("10").thenReturn("xx");

        try {
            Assert.assertNotNull(virtualProductService.getMaxSizeOfVirtualFlowProduct());
            Assert.assertNotNull(virtualProductService.getMaxSizeOfVirtualFlowProduct());
            Assert.assertNotNull(virtualProductService.getMaxSizeOfVirtualFlowProduct());
        } catch (Exception e) {
        
        }
    }
}
