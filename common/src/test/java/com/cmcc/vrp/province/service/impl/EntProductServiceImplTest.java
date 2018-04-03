/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.EntProductMapper;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.ProductTemplateEnterpriseMap;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.util.Constants;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * <p>Title:EntProductServiceImplTest </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月1日
 */
@RunWith(MockitoJUnitRunner.class)
public class EntProductServiceImplTest {

    @InjectMocks
    EntProductService epService = new EntProductServiceImpl();

    @Mock
    EntProductMapper entProductMapper;

    @Mock
    ProductService productService;

    @Mock
    EnterprisesService enterprisesService;

    @Mock
    AccountService accountService;

    /**
     * 
     */
    @Test
    public void testSelectByEnterprizeID() {
        Long enterprizeId = 1L;
        List<EntProduct> list = new ArrayList();
        when(entProductMapper.selectByEnterpriseID(enterprizeId)).thenReturn(list);

        assertNull(epService.selectByEnterprizeID(null));
        assertSame(list, epService.selectByEnterprizeID(enterprizeId));
        Mockito.verify(entProductMapper, Mockito.times(1)).selectByEnterpriseID(enterprizeId);
    }
    /**
     * 
     */
    @Test
    public void testSelectProIdsByEnterprizeID() {
        Long enterprizeId = 1L;
        List<EntProduct> list = new ArrayList();
        List<Long> listProIds = new ArrayList();

        when(entProductMapper.selectByEnterpriseID(enterprizeId)).thenReturn(list);
        assertEquals(listProIds, epService.selectProIdsByEnterprizeID(null));

        listProIds.add(1L);
        EntProduct ep = new EntProduct();
        ep.setEnterprizeId(1L);
        ep.setProductId(1L);
        list.add(ep);
        assertEquals(listProIds, epService.selectProIdsByEnterprizeID(enterprizeId));
        Mockito.verify(entProductMapper, Mockito.times(1)).selectByEnterpriseID(enterprizeId);
    }

    /**
     * 
     */
    @Test
    public void testSelectByProductIDAndEnterprizeID() {
        Long productId = 1L;
        Long enterpriseId = 1L;
        EntProduct ep = new EntProduct();

        when(entProductMapper.selectByProductIDAndEnterprizeID(Mockito.anyMap())).thenThrow(new RuntimeException())
                .thenReturn(ep);

        assertNull(epService.selectByProductIDAndEnterprizeID(null, null));
        assertNull(epService.selectByProductIDAndEnterprizeID(productId, null));
        assertNull(epService.selectByProductIDAndEnterprizeID(productId, enterpriseId));
        assertSame(ep, epService.selectByProductIDAndEnterprizeID(productId, enterpriseId));
        Mockito.verify(entProductMapper, Mockito.times(2)).selectByProductIDAndEnterprizeID(Mockito.anyMap());
    }
    /**
     * 
     */
    @Test
    public void testDeleteByProductID() {
        Long productID = 1L;
        List<EntProduct> list = new ArrayList();
        EntProduct ep1 = new EntProduct();
        EntProduct ep2 = new EntProduct();
        list.add(ep1);
        list.add(ep2);

        when(entProductMapper.selectByProductID(productID)).thenReturn(list);
        when(entProductMapper.deleteByProductID(productID)).thenReturn(0).thenReturn(list.size());

        assertFalse(epService.deleteByProductID(null));
        assertTrue(epService.deleteByProductID(productID));
        assertTrue(epService.deleteByProductID(productID));

        Mockito.verify(entProductMapper, Mockito.times(2)).deleteByProductID(productID);
    }
    /**
     * 
     */
    @Test
    public void testInsert() {
        EntProduct record = new EntProduct();

        when(entProductMapper.insert(record)).thenReturn(0).thenReturn(1);
        assertFalse(epService.insert(null));
        assertFalse(epService.insert(record));
        assertTrue(epService.insert(record));

        Mockito.verify(entProductMapper, Mockito.times(2)).insert(record);
    }
    /**
     * 
     */
    @Test
    public void testInsertSelective() {
        EntProduct record = new EntProduct();

        when(entProductMapper.insertSelective(record)).thenReturn(0).thenReturn(1);
        assertFalse(epService.insertSelective(null));
        assertFalse(epService.insertSelective(record));
        assertTrue(epService.insertSelective(record));
        Mockito.verify(entProductMapper, Mockito.times(2)).insertSelective(record);
    }
    /**
     * 
     */
    @Test
    public void testUpdateEnterpriseProduct() {
        List<String> productCodes = new ArrayList();
        Long enterpriseId = 1L;
        productCodes.add("001");
        productCodes.add("002");
        List<EntProduct> epList = new ArrayList();
        EntProduct ep1 = new EntProduct();
        ep1.setEnterprizeId(enterpriseId);
        ep1.setProductId(1L);
        EntProduct ep2 = new EntProduct();
        ep2.setEnterprizeId(enterpriseId);
        ep2.setProductId(2L);
        EntProduct ep3 = new EntProduct();
        ep3.setEnterprizeId(enterpriseId);
        ep3.setProductId(3L);
        epList.add(ep1);
        epList.add(ep2);
        epList.add(ep3);
        List<Long> bossProIds = new ArrayList();
        bossProIds.add(3L);
        bossProIds.add(4L);
        bossProIds.add(5L);

        when(entProductMapper.selectByEnterpriseID(enterpriseId)).thenReturn(epList);
        when(productService.selectProIdsByProductCode(productCodes)).thenReturn(bossProIds);
        when(entProductMapper.deleteByProIdEnterId(Mockito.any(EntProduct.class))).thenReturn(1);
        when(entProductMapper.insertSelective(Mockito.any(EntProduct.class))).thenReturn(1);

        assertFalse(epService.updateEnterpriseProduct(null, null));
        assertFalse(epService.updateEnterpriseProduct(null, enterpriseId));
        assertTrue(epService.updateEnterpriseProduct(productCodes, enterpriseId));
    }
    /**
     * 
     */
    @Test
    public void testSelectProductByEnterId() {
        Long enterId = 1L;
        List<Product> list = new ArrayList();
        when(entProductMapper.selectProductByEnterId(enterId)).thenReturn(list);

        assertNull(epService.selectProductByEnterId(null));
        assertSame(list, epService.selectProductByEnterId(enterId));
        Mockito.verify(entProductMapper, Mockito.times(1)).selectProductByEnterId(enterId);
    }
    /**
     * 
     */
    @Test
    public void testUpdateEnterProductByRecords() {
        Long enterId = 1L;
        List<EntProduct> entProductAdds = new ArrayList();
        EntProduct ep = new EntProduct();
        ep.setEnterprizeId(enterId);
        ep.setProductId(1L);
        entProductAdds.add(ep);
        List<EntProduct> entProductDeletes = new ArrayList();
        EntProduct ep1 = new EntProduct();
        ep1.setEnterprizeId(enterId);
        ep1.setProductId(1L);
        entProductDeletes.add(ep1);
        List<EntProduct> entProductUpdates = new ArrayList();
        EntProduct ep2 = new EntProduct();
        ep2.setEnterprizeId(enterId);
        ep2.setProductId(1L);
        entProductUpdates.add(ep2);

        assertFalse(epService.updateEnterProductByRecords(null, null, null, null));
        assertTrue(epService.updateEnterProductByRecords(enterId, null, null, null));

        when(accountService.checkAccountByEntIdAndProductId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(true)
                .thenReturn(false);
        when(entProductMapper.batchInsert(entProductAdds)).thenReturn(0).thenReturn(entProductAdds.size());
        assertFalse(epService.updateEnterProductByRecords(enterId, entProductAdds, null, null));

        when(entProductMapper.updateByProIdEnterId(Mockito.any(EntProduct.class))).thenReturn(0).thenReturn(1);
        try {
            epService.updateEnterProductByRecords(enterId, entProductAdds, entProductDeletes, entProductUpdates);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(entProductMapper.deleteByProIdEnterId(Mockito.any(EntProduct.class))).thenReturn(0).thenReturn(1);
        try {
            epService.updateEnterProductByRecords(enterId, entProductAdds, entProductDeletes, entProductUpdates);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(accountService.createProductAccount(Mockito.anyList())).thenReturn(false).thenReturn(true);
        try {
            epService.updateEnterProductByRecords(enterId, entProductAdds, entProductDeletes, entProductUpdates);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertTrue(epService.updateEnterProductByRecords(enterId, entProductAdds, entProductDeletes, entProductUpdates));
    }
    /**
     * 
     */
    @Test
    public void testUpdateDiscountByEnterId() {
        Long enterId = 1L;
        Integer discount = 1;

        when(entProductMapper.updateDiscountByEnterId(enterId, discount)).thenReturn(-1).thenReturn(1);
        assertFalse(epService.updateDiscountByEnterId(enterId, discount));
        assertTrue(epService.updateDiscountByEnterId(enterId, discount));
        Mockito.verify(entProductMapper, Mockito.times(2)).updateDiscountByEnterId(enterId, discount);
    }
    /**
     * 
     */
    @Test
    public void testProductAvailableByEnterId() {
        Long enterId = 1L;
        List<Product> p = new ArrayList();

        when(entProductMapper.productAvailableByEnterId(enterId)).thenReturn(p);
        assertSame(p, epService.productAvailableByEnterId(enterId));
        Mockito.verify(entProductMapper, Mockito.times(1)).productAvailableByEnterId(enterId);
    }
    /**
     * 
     */
    @Test
    public void testBatchEntProduct() {
        List<EntProduct> records = new ArrayList();
        when(entProductMapper.batchInsert(records)).thenReturn(-1).thenReturn(records.size());

        assertFalse(epService.batchInsertEntProduct(null));
        assertFalse(epService.batchInsertEntProduct(records));
        assertTrue(epService.batchInsertEntProduct(records));
        Mockito.verify(entProductMapper, Mockito.times(2)).batchInsert(records);
    }
    /**
     * 
     */
    @Test
    public void testBatchUpdate() {
        //参数校验
        assertTrue(epService.batchUpdate(null));

        List<EntProduct> list = new ArrayList<EntProduct>();
        assertTrue(epService.batchUpdate(list));

        EntProduct entProduct = new EntProduct();
        list.add(entProduct);
        when(entProductMapper.batchUpdate(list)).thenReturn(0);
        assertFalse(epService.batchUpdate(list));

        when(entProductMapper.batchUpdate(list)).thenReturn(1);
        assertTrue(epService.batchUpdate(list));
    }
    /**
     * 
     */
    @Test
    public void testSelectByProductIdWithoutDeleleFlag() {
        //参数校验
        assertEquals(null, epService.selectByProductIdWithoutDeleleFlag(null));

        //
        List<EntProduct> list = new ArrayList<EntProduct>();
        when(entProductMapper.selectByProductIDWithoutDeleteFlag(Mockito.anyLong())).thenReturn(list);
        assertEquals(list, epService.selectByProductIdWithoutDeleleFlag(Mockito.anyLong()));
    }
    
    /**
     * 
     */
    @Test
    public void testValidateEntAndPrd() {
        
        assertEquals("企业不存在", epService.validateEntAndPrd(null, 1L));
        
        when(enterprisesService.selectById(Mockito.anyLong())).thenReturn(null);
        assertEquals("企业不存在", epService.validateEntAndPrd(1L, 1L));

        Enterprise enterprise = new Enterprise();
        enterprise.setDeleteFlag(1);
        when(enterprisesService.selectById(Mockito.anyLong())).thenReturn(enterprise);
        assertEquals("企业状态异常", epService.validateEntAndPrd(1L, 1L));

        
        Product product = new Product();
        enterprise.setDeleteFlag(0);
        when(enterprisesService.selectById(Mockito.anyLong())).thenReturn(enterprise);

        assertEquals("产品不存在", epService.validateEntAndPrd(1L, null));

        
        when(productService.get(Mockito.anyLong())).thenReturn(product);
        assertEquals("产品不存在", epService.validateEntAndPrd(1L, null));
        
        product.setStatus(Constants.PRODUCT_STATUS.OFF.getStatus());
        when(productService.get(Mockito.anyLong())).thenReturn(product);
        assertEquals("产品状态异常", epService.validateEntAndPrd(1L, 1L));

        product.setStatus(Constants.PRODUCT_STATUS.ON.getStatus());
        product.setDeleteFlag(0);
        when(productService.get(Mockito.anyLong())).thenReturn(product);
        EntProductService spy = spy(epService);
        when(spy.selectByProductIDAndEnterprizeID(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);
        assertEquals("企业未订购该产品", spy.validateEntAndPrd(1L, 1L));

    }
    @Test
    public void testSelectAllProductByEnterId() {
        Assert.assertNull(epService.selectAllProductByEnterId(null));
        Mockito.when(entProductMapper.selectAllProductByEnterId(Mockito.anyLong())).thenReturn(new ArrayList<Product>());
        Assert.assertNotNull(epService.selectAllProductByEnterId(1l));
    }
    @Test
    public void testBatchDeleteByEnterIdAndProductId() {
        Mockito.when(entProductMapper.batchDeleteByEnterIdAndProductId(Mockito.anyList())).thenReturn(1).thenReturn(-1);
        Assert.assertTrue(epService.batchDeleteByEnterIdAndProductId(new ArrayList<EntProduct>()));
        Assert.assertFalse(epService.batchDeleteByEnterIdAndProductId(new ArrayList<EntProduct>()));
    }
    @Test
    public void testBatchDeleteByEnterId() {
        Mockito.when(entProductMapper.batchDeleteByEnterId(Mockito.anyList())).thenReturn(1).thenReturn(-1);
        Assert.assertTrue(epService.batchDeleteByEnterId(new ArrayList<ProductTemplateEnterpriseMap>()));
        Assert.assertFalse(epService.batchDeleteByEnterId(new ArrayList<ProductTemplateEnterpriseMap>()));
    }
}
