package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.enums.OrderOperationStatus;
import com.cmcc.vrp.enums.SupplierLimitStatus;
import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.dao.ShBossProductMapper;
import com.cmcc.vrp.province.dao.SupplierProductMapper;
import com.cmcc.vrp.province.model.DiscountRecord;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.ProductConverter;
import com.cmcc.vrp.province.model.SdBossProduct;
import com.cmcc.vrp.province.model.ShBossProduct;
import com.cmcc.vrp.province.model.Supplier;
import com.cmcc.vrp.province.model.SupplierProdModifyLimitRecord;
import com.cmcc.vrp.province.model.SupplierProdReqUsePerDay;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierProductMap;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.DiscountRecordService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ProductConverterService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SdBossProductService;
import com.cmcc.vrp.province.service.SupplierProdModifyLimitRecordService;
import com.cmcc.vrp.province.service.SupplierProdReqUsePerDayService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.province.service.SupplierService;
import com.cmcc.vrp.util.Constants.DELETE_FLAG;
import com.cmcc.vrp.util.QueryObject;

/**
 * Created by qinqinyan on 2016/11/2.
 */
@RunWith(MockitoJUnitRunner.class)
public class SupplierProductServiceImplTest {
    @InjectMocks
    SupplierProductService supplierProductService = new SupplierProductServiceImpl();
    @Mock
    SupplierProductMapper supplierProductMapper;
    @Mock
    SupplierProductMapService supplierProductMapService;

    @Mock
    EnterprisesService enterprisesService;

    @Mock
    EntProductService entProductService;

    @Mock
    ProductService productService;

    @Mock
    DiscountRecordService discountRecordService;

    @Mock
    AccountService accountService;
    
    @Mock
    ProductConverterService productConverterService;
    @Mock
    ShBossProductMapper shBossProductMapper;
    @Mock
    SdBossProductService sdBossProductService;
    @Mock
    SupplierProdModifyLimitRecordService supplierProdModifyLimitRecordService;
    @Mock
    SupplierProdReqUsePerDayService supplierProdReqUsePerDayService;
    @Mock
    SupplierService supplierService;
    /**
     *
     */
    @Test
    public void testInsert() {
        Mockito.when(supplierProductMapper.insert(any(SupplierProduct.class))).thenReturn(1);
        assertTrue(supplierProductService.insert(new SupplierProduct()));
        Mockito.verify(supplierProductMapper).insert(any(SupplierProduct.class));
    }

    /**
     * deleteSupplierProduct
     * 正常流程
     */
    @Test
    public void testDeleteSupplierProduct() {
        Long productId = 1L;
        Mockito.when(supplierProductMapper.updateByPrimaryKeySelective(any(SupplierProduct.class))).thenReturn(1);
        Mockito.when(supplierProductMapService.deleteBySplPid(anyLong())).thenReturn(true);

        assertTrue(supplierProductService.deleteSupplierProduct(productId));
        Mockito.verify(supplierProductMapper).updateByPrimaryKeySelective(any(SupplierProduct.class));
        Mockito.verify(supplierProductMapService).deleteBySplPid(anyLong());
    }

    /**
     * deleteSupplierProduct
     * 异常流程
     */
    @Test(expected = RuntimeException.class)
    public void testDeleteSupplierProduct1() {
        Long productId = 1L;
        Mockito.when(supplierProductMapper.updateByPrimaryKeySelective(any(SupplierProduct.class))).thenReturn(1);
        Mockito.when(supplierProductMapService.deleteBySplPid(anyLong())).thenReturn(false);

        assertFalse(supplierProductService.deleteSupplierProduct(productId));
    }

    /**
     *
     */
    @Test
    public void testUpdateByPrimaryKey() {
        Mockito.when(supplierProductMapper.updateByPrimaryKeySelective(any(SupplierProduct.class))).thenReturn(1);
        Mockito.when(supplierProductMapService.clearPriorSupplierBySplId(Mockito.anyLong())).thenReturn(1);
        SupplierProduct supplierProduct = new SupplierProduct();
        supplierProduct.setStatus(0);
        assertTrue(supplierProductService.updateByPrimaryKey(supplierProduct));
        Mockito.verify(supplierProductMapper).updateByPrimaryKeySelective(any(SupplierProduct.class));
    }

    /**
     *
     */
    @Test
    public void testSelectByPrimaryKey() {
        SupplierProduct supplierProduct = new SupplierProduct();
        supplierProduct.setId(1L);

        Long id = 1L;
        Mockito.when(supplierProductMapper.selectByPrimaryKey(anyLong())).thenReturn(supplierProduct);
        assertNotNull(supplierProductService.selectByPrimaryKey(id));
        Mockito.verify(supplierProductMapper).selectByPrimaryKey(anyLong());
    }

    /**
     *
     */
    @Test
    public void testSelectBySupplierId() {
        assertNull(supplierProductService.selectBySupplierId(null));

        Long supplierId = 1L;
        Mockito.when(supplierProductMapper.selectBySupplierId(anyLong())).thenReturn(new ArrayList<SupplierProduct>());
        assertNotNull(supplierProductService.selectBySupplierId(supplierId));
        Mockito.verify(supplierProductMapper).selectBySupplierId(anyLong());
    }

    /**
     *
     */
    @Test
    public void testSelectByName() {
        assertNotNull(supplierProductService.selectByName(null));
        String name = "test";
        Mockito.when(supplierProductMapper.selectByName(anyString())).thenReturn(new ArrayList<SupplierProduct>());
        assertNotNull(supplierProductService.selectByName(name));
        Mockito.verify(supplierProductMapper).selectByName(anyString());
    }

    /**
     *
     */
    @Test
    public void testSelectByCode() {
        assertNull(supplierProductService.selectByCode(null));

        String code = "test";
        Mockito.when(supplierProductMapper.selectByCode(anyString())).thenReturn(new SupplierProduct());
        assertNotNull(supplierProductService.selectByCode(code));
        Mockito.verify(supplierProductMapper).selectByCode(anyString());
    }

    /**
     *
     */
    @Test
    public void testQueryPaginationSupplierProduct() {
        QueryObject queryObject = new QueryObject();
        queryObject.getQueryCriterias().put("size", "10");

        Mockito.when(supplierProductMapper.queryPaginationSupplierProduct(anyMap())).thenReturn(
            new ArrayList<SupplierProduct>());
        assertNotNull(supplierProductService.queryPaginationSupplierProduct(queryObject));
        Mockito.verify(supplierProductMapper).queryPaginationSupplierProduct(anyMap());
    }

    /**
     *
     */
    @Test
    public void testQueryPaginationSupplierProductCount() {
        QueryObject queryObject = new QueryObject();
        queryObject.getQueryCriterias().put("size", "10");

        Mockito.when(supplierProductMapper.queryPaginationSupplierProductCount(anyMap())).thenReturn(1);
        assertSame(1, supplierProductService.queryPaginationSupplierProductCount(queryObject));
        Mockito.verify(supplierProductMapper).queryPaginationSupplierProductCount(anyMap());
    }

    /**
     *
     */
    @Test
    public void testQueryPaginationSupplierProduct2PltProduct() {
        QueryObject queryObject = new QueryObject();
        Mockito.when(supplierProductMapper.queryPaginationSupplierProduct2PltProduct(anyMap())).thenReturn(
            new ArrayList<SupplierProduct>());
        assertNotNull(supplierProductService.queryPaginationSupplierProduct2PltProduct(queryObject));
        Mockito.verify(supplierProductMapper).queryPaginationSupplierProduct2PltProduct(anyMap());
    }

    /**
     *
     */
    @Test
    public void testQueryPaginationSupplierProduct2PltProductCount() {
        QueryObject queryObject = new QueryObject();
        Mockito.when(supplierProductMapper.queryPaginationSupplierProduct2PltProductCount(anyMap())).thenReturn(1);
        assertSame(1, supplierProductService.queryPaginationSupplierProduct2PltProductCount(queryObject));
        Mockito.verify(supplierProductMapper).queryPaginationSupplierProduct2PltProductCount(anyMap());
    }

    /**
     *
     */
    @Test
    public void testQuerySupplierProductAvailableCount() {
        QueryObject queryObject = new QueryObject();
        queryObject.getQueryCriterias().put("size", "10");
        Mockito.when(supplierProductMapper
            .querySupplierProductAvailableCount(anyMap())).thenReturn(1);
        assertSame(1, supplierProductService.querySupplierProductAvailableCount(queryObject));
        Mockito.verify(supplierProductMapper).querySupplierProductAvailableCount(anyMap());
    }

    /**
     *
     */
    @Test
    public void testQuerySupplierProductAvailable() {
        QueryObject queryObject = new QueryObject();
        queryObject.getQueryCriterias().put("size", "10");
        Mockito.when(supplierProductMapper
            .querySupplierProductAvailable(anyMap())).thenReturn(new ArrayList<SupplierProduct>());
        assertNotNull(supplierProductService.querySupplierProductAvailable(queryObject));
        Mockito.verify(supplierProductMapper).querySupplierProductAvailable(anyMap());
    }

    /**
     *
     */
    @Test
    public void testSelectByFeature() {
        Mockito.when(supplierProductMapper.selectByFeature(anyString())).thenReturn(new SupplierProduct());
        assertNotNull(supplierProductService.selectByFeature(anyString()));
        Mockito.verify(supplierProductMapper).selectByFeature(anyString());
    }

    /**
     *
     */
    @Test
    public void testBatchInsertSupplierProduct() {
        assertFalse(supplierProductService.batchInsertSupplierProduct(null));

        List<SupplierProduct> list = new ArrayList<SupplierProduct>();
        SupplierProduct record = new SupplierProduct();
        record.setId(1L);
        list.add(record);
        Mockito.when(supplierProductMapper.batchInsertSupplierProduct(anyList())).thenReturn(1);
        assertTrue(supplierProductService.batchInsertSupplierProduct(list));
        Mockito.verify(supplierProductMapper).batchInsertSupplierProduct(anyList());
    }

    /**
     *
     */
    @Test
    public void testSelectSupplierByCreateTime() {
        assertNull(supplierProductService.selectSupplierByCreateTime(null));

        Mockito.when(supplierProductMapper.selectSupplierByCreateTime(any(Date.class))).thenReturn(
            new ArrayList<SupplierProduct>());
        assertNotNull(supplierProductService.selectSupplierByCreateTime(new Date()));
        Mockito.verify(supplierProductMapper).selectSupplierByCreateTime(any(Date.class));
    }

    /**
     *
     */
    @Test
    public void testSelectByCodeAndSupplierId() {
        String code = "test";
        Long supplierId = 1L;
        assertNull(supplierProductService.selectByCodeAndSupplierId(code, null));
        Mockito.when(supplierProductMapper.selectByCodeAndSupplierId(anyString(), anyLong())).thenReturn(
            new SupplierProduct());
        assertNotNull(supplierProductService.selectByCodeAndSupplierId(code, supplierId));
        Mockito.verify(supplierProductMapper).selectByCodeAndSupplierId(anyString(), anyLong());
    }

    /**
     *
     */
    @Test
    public void testCreateNewSupplierProduct() {
        assertFalse(supplierProductService.createNewSupplierProduct(null, null, null));

        SupplierProduct supplierProduct = new SupplierProduct();
        Product product = new Product();
        product.setType(2);
        product.setProductSize(1000L);

        try {
            SupplierProductService sps = Mockito.spy(supplierProductService);
            Mockito.doReturn(false).when(sps).insert(Mockito.any(SupplierProduct.class));
            sps.createNewSupplierProduct(1L, supplierProduct, product);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            SupplierProductService sps = Mockito.spy(supplierProductService);
            Mockito.doReturn(true).when(sps).insert(Mockito.any(SupplierProduct.class));
            Mockito.when(productService.insertProduct(Mockito.any(Product.class))).thenReturn(false);
            sps.createNewSupplierProduct(1L, supplierProduct, product);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        try {
            SupplierProductService sps = Mockito.spy(supplierProductService);
            Mockito.doReturn(true).when(sps).insert(Mockito.any(SupplierProduct.class));
            Mockito.when(productService.insertProduct(Mockito.any(Product.class))).thenReturn(true);
            Mockito.when(productService.getCurrencyProduct()).thenReturn(product);
            Mockito.when(discountRecordService.insert(Mockito.any(DiscountRecord.class))).thenReturn(false);
            sps.createNewSupplierProduct(1L, supplierProduct, product);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            SupplierProductService sps = Mockito.spy(supplierProductService);
            Mockito.doReturn(true).when(sps).insert(Mockito.any(SupplierProduct.class));
            Mockito.when(productService.insertProduct(Mockito.any(Product.class))).thenReturn(true);
            Mockito.when(supplierProductMapService.create(Mockito.any(SupplierProductMap.class))).thenReturn(false);
            sps.createNewSupplierProduct(1L, supplierProduct, product);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            SupplierProductService sps = Mockito.spy(supplierProductService);
            Mockito.doReturn(true).when(sps).insert(Mockito.any(SupplierProduct.class));
            Mockito.when(productService.insertProduct(Mockito.any(Product.class))).thenReturn(true);
            Mockito.when(supplierProductMapService.create(Mockito.any(SupplierProductMap.class))).thenReturn(true);
            Mockito.when(entProductService.insert(Mockito.any(EntProduct.class))).thenReturn(false);
            sps.createNewSupplierProduct(1L, supplierProduct, product);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            SupplierProductService sps = Mockito.spy(supplierProductService);
            Mockito.doReturn(true).when(sps).insert(Mockito.any(SupplierProduct.class));
            Mockito.when(productService.insertProduct(Mockito.any(Product.class))).thenReturn(true);
            Mockito.when(supplierProductMapService.create(Mockito.any(SupplierProductMap.class))).thenReturn(true);
            Mockito.when(discountRecordService.insert(Mockito.any(DiscountRecord.class))).thenReturn(true);
            Mockito.when(entProductService.insert(Mockito.any(EntProduct.class))).thenReturn(true);
            sps.createNewSupplierProduct(1L, supplierProduct, product);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        List<Product> list = new ArrayList<Product>();
        Product p1 = new Product();
        p1.setType(10);
        list.add(p1);
        SupplierProductService sps = Mockito.spy(supplierProductService);
        Mockito.doReturn(true).when(sps).insert(Mockito.any(SupplierProduct.class));
        Mockito.when(productService.insertProduct(Mockito.any(Product.class))).thenReturn(true);
        Mockito.when(supplierProductMapService.create(Mockito.any(SupplierProductMap.class))).thenReturn(true);
        Mockito.when(entProductService.insert(Mockito.any(EntProduct.class))).thenReturn(true);
        Mockito.when(discountRecordService.insert(Mockito.any(DiscountRecord.class))).thenReturn(true);
        Mockito.when(productConverterService.insert(Mockito.any(ProductConverter.class))).thenReturn(true);
        Mockito.when(discountRecordService.insert(Mockito.any(DiscountRecord.class))).thenReturn(true);
        Mockito.when(accountService.createProductAccount(Mockito.anyList())).thenReturn(true);
        Mockito.when(sdBossProductService.selectByCode(Mockito.anyString())).thenReturn(null);
        Mockito.when(productService.selectByType(Mockito.anyInt())).thenReturn(list);

        assertTrue(sps.createNewSupplierProduct(1L, supplierProduct, product));


        Mockito.when(accountService.createProductAccount(Mockito.anyList())).thenReturn(false);
        try {
            sps.createNewSupplierProduct(1L, supplierProduct, product);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     *
     */
    @Test
    public void testSelectById() {
        Mockito.when(supplierProductMapper.selectById(Mockito.anyLong())).thenReturn(new SupplierProduct());
        assertNotNull(supplierProductService.selectById(1L));
    }

    /**
     *
     */
    @Test
    public void testCreateOrUpdateSupplierProduct() {
        assertFalse(supplierProductService.createOrUpdateSupplierProduct(null, null, null));

        Mockito.when(enterprisesService.selectByCode(Mockito.anyString())).thenReturn(null);
        assertFalse(supplierProductService.createOrUpdateSupplierProduct("123456", null, null));

        SupplierProductService sps = Mockito.spy(supplierProductService);
        Mockito.when(enterprisesService.selectByCode(Mockito.anyString())).thenReturn(new Enterprise());
        Mockito.doReturn(true).when(sps)
            .createNewSupplierProduct(Mockito.anyLong(), Mockito.anyListOf(SupplierProduct.class));
        Mockito.doReturn(true).when(sps)
            .updateSulliperProduct(Mockito.anyLong(), Mockito.anyListOf(SupplierProduct.class));

        List<SupplierProduct> newProducts = new ArrayList<SupplierProduct>();
        newProducts.add(new SupplierProduct());

        List<SupplierProduct> updateProducts = new ArrayList<SupplierProduct>();
        updateProducts.add(new SupplierProduct());

        assertTrue(sps.createOrUpdateSupplierProduct("123456", newProducts, updateProducts));

    }

    /**
     *
     */
    @Test
    public void testBatchUpdate() {

        assertTrue(supplierProductService.batchUpdate(null));

        List<SupplierProduct> list = new ArrayList<SupplierProduct>();
        SupplierProduct supplierProduct = new SupplierProduct();
        list.add(supplierProduct);
        Mockito.when(supplierProductMapper.batchUpdate(list)).thenReturn(1);
        assertTrue(supplierProductService.batchUpdate(list));

    }

    /**
     *
     */
    @Test
    public void testSelectByNameAndCodeOrStatusOrDeleteFlag() {
        assertNull(supplierProductService.selectByNameAndCodeOrStatusOrDeleteFlag(null, null, null, null));

        List<SupplierProduct> list = new ArrayList<SupplierProduct>();
        Mockito.when(
            supplierProductMapper.selectByNameAndCodeOrStatusOrDeleteFlag(Mockito.anyString(), Mockito.anyString(),
                Mockito.any(Integer.class), Mockito.any(Integer.class))).thenReturn(list);
        assertNotNull(supplierProductService.selectByNameAndCodeOrStatusOrDeleteFlag("123456", "123", 1, 1));

    }

    /**
     *
     */
    @Test
    public void testSelectByNameWithOutDeleteFlag() {
        assertNull(supplierProductService.selectByNameWithOutDeleteFlag(null));

        List<SupplierProduct> list = new ArrayList<SupplierProduct>();
        Mockito.when(supplierProductMapper.selectByNameWithoutDeleteFlag(Mockito.anyString())).thenReturn(list);
        assertNotNull(supplierProductService.selectByNameWithOutDeleteFlag("123456"));

    }

    /**
     *
     */
    @Test
    public void testUpdateSulliperProduct() {
        assertFalse(supplierProductService.updateSulliperProduct(null, null));
        assertTrue(supplierProductService.updateSulliperProduct(1L, null));

        SupplierProduct supplierProduct = new SupplierProduct();
        supplierProduct.setCode("109201");
        supplierProduct.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        supplierProduct.setStatus(DELETE_FLAG.UNDELETED.getValue());

        List<SupplierProduct> updateProducts = new ArrayList<SupplierProduct>();
        updateProducts.add(supplierProduct);

        List<Product> products = new ArrayList<Product>();
        products.add(new Product());
        Mockito.when(supplierProductMapService.getBySplPid(Mockito.anyLong())).thenReturn(products);

        List<EntProduct> entProductList = new ArrayList<EntProduct>();
        entProductList.add(new EntProduct());
        Mockito.when(entProductService.selectByProductIdWithoutDeleleFlag(Mockito.anyLong())).thenReturn(entProductList);

        Mockito.when(entProductService.batchUpdate(entProductList)).thenReturn(false);
       
        try {
            supplierProductService.updateSulliperProduct(1L, updateProducts);
        } catch (Exception e) {
            // TODO: handle exception
        }

        Mockito.when(entProductService.batchUpdate(entProductList)).thenReturn(true);
        SupplierProductService sps = Mockito.spy(supplierProductService);
        Mockito.doReturn(false).when(sps).batchUpdate(updateProducts);

        try {
            sps.updateSulliperProduct(1L, updateProducts);
        } catch (Exception e) {
            // TODO: handle exception
        }
        List<Product> pList = new ArrayList<Product>();
        Mockito.doReturn(true).when(sps).batchUpdate(updateProducts);
        Mockito.when(productService.batchUpdate(pList)).thenReturn(false);
        try {
            sps.updateSulliperProduct(1L, updateProducts);
        } catch (Exception e) {
            // TODO: handle exception
        }

        Mockito.when(productService.batchUpdate(Mockito.anyList())).thenReturn(true);
        
        List<DiscountRecord> discountRecordList = new ArrayList<DiscountRecord>();
        Mockito.doReturn(true).when(sps).batchUpdate(updateProducts);
        Mockito.when(discountRecordService.batchInsert(discountRecordList)).thenReturn(false);
        try {
            sps.updateSulliperProduct(1L, updateProducts);
        } catch (Exception e) {
            // TODO: handle exception
        }

        Mockito.when(discountRecordService.batchInsert(Mockito.anyList())).thenReturn(true);
        try {
            sps.updateSulliperProduct(1L, updateProducts);
        } catch (Exception e) {
            // TODO: handle exception
        }
        
        List<Product> tempList = new ArrayList<Product>();
        Product p = new Product();
        p.setSupplierOpStatus(OrderOperationStatus.DELETE.getOpStatus());
        tempList.add(p);
        Mockito.when(productService.getBySplIdWithoutDel(Mockito.anyLong())).thenReturn(tempList);
        
        Mockito.when(entProductService.selectByProductIdWithoutDeleleFlag(Mockito.anyLong())).thenReturn(entProductList);
        Mockito.when(productService.batchUpdate(Mockito.anyList())).thenReturn(true);
        Mockito.when(entProductService.batchUpdate(Mockito.anyList())).thenReturn(true);
        try {
            sps.updateSulliperProduct(1L, updateProducts);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     *
     */
    @Test
    public void testCreateNewSupplierProduct3() {

        assertFalse(supplierProductService.createNewSupplierProduct(null, null));

        SupplierProduct supplierProduct = new SupplierProduct();
        supplierProduct.setCode("109201");
        supplierProduct.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        supplierProduct.setStatus(DELETE_FLAG.UNDELETED.getValue());

        SupplierProduct supplierProduct2 = new SupplierProduct();
        supplierProduct2.setCode("109901");
        supplierProduct2.setDeleteFlag(DELETE_FLAG.UNDELETED.getValue());
        supplierProduct2.setStatus(DELETE_FLAG.UNDELETED.getValue());

        List<SupplierProduct> supplierProducts = new ArrayList<SupplierProduct>();
        supplierProducts.add(supplierProduct);
        supplierProducts.add(supplierProduct2);
        
        Mockito.when(sdBossProductService.selectByCode(Mockito.anyString())).thenReturn(null);


        
        SupplierProductService sps = Mockito.spy(supplierProductService);

        Mockito.doReturn(true)
            .when(sps)
            .createNewSupplierProduct(Mockito.anyLong(), Mockito.any(SupplierProduct.class),
                Mockito.any(Product.class));

        SdBossProduct sdBossProduct = new SdBossProduct();
        sdBossProduct.setPrice(1L);
        Mockito.when(sdBossProductService.selectByCode(Mockito.anyString())).thenReturn(sdBossProduct);

        assertTrue(sps.createNewSupplierProduct(1L, supplierProducts));
    }

    /**
     *
     */
    @Test
    public void testUpdateSupplierProduct() {
        List<EntProduct> epList = new ArrayList<EntProduct>();
        List<SupplierProductMap> spmList = new ArrayList<SupplierProductMap>();
        List<Product> pList = new ArrayList<Product>();
        List<SupplierProduct> spList = new ArrayList<SupplierProduct>();
        SupplierProduct sp = new SupplierProduct();
        spList.add(sp);
        List<DiscountRecord> records = new ArrayList<DiscountRecord>();

        assertFalse(supplierProductService.updateSupplierProduct(epList, spmList, pList, spList, null));

        Mockito.when(supplierProductMapper.batchUpdate(Mockito.anyListOf(SupplierProduct.class))).thenReturn(0);
        Throwable re = null;
        try {
            supplierProductService.updateSupplierProduct(epList, spmList, pList, spList, records);
            Assert.fail();
        } catch (TransactionException e) {
            re = e;
        }
        assertEquals(TransactionException.class, re.getClass());


        Mockito.when(supplierProductMapper.batchUpdate(Mockito.anyListOf(SupplierProduct.class))).thenReturn(1);
        Mockito.when(supplierProductMapService.batchUpdate(Mockito.anyListOf(SupplierProductMap.class))).thenReturn(false);
        re = null;
        try {
            supplierProductService.updateSupplierProduct(epList, spmList, pList, spList, records);
            Assert.fail();
        } catch (TransactionException e) {
            re = e;
        }
        assertEquals(TransactionException.class, re.getClass());


        Mockito.when(supplierProductMapService.batchUpdate(Mockito.anyListOf(SupplierProductMap.class))).thenReturn(true);
        Mockito.when(productService.batchUpdate(Mockito.anyListOf(Product.class))).thenReturn(false);
        re = null;
        try {
            supplierProductService.updateSupplierProduct(epList, spmList, pList, spList, records);
            Assert.fail();
        } catch (TransactionException e) {
            re = e;
        }
        assertEquals(TransactionException.class, re.getClass());


        Mockito.when(productService.batchUpdate(Mockito.anyListOf(Product.class))).thenReturn(true);
        Mockito.when(entProductService.batchUpdate(Mockito.anyListOf(EntProduct.class))).thenReturn(false);
        re = null;
        try {
            supplierProductService.updateSupplierProduct(epList, spmList, pList, spList, records);
            Assert.fail();
        } catch (TransactionException e) {
            re = e;
        }
        assertEquals(TransactionException.class, re.getClass());

        Mockito.when(entProductService.batchUpdate(Mockito.anyListOf(EntProduct.class))).thenReturn(true);
        Mockito.when(discountRecordService.batchInsert(Mockito.anyListOf(DiscountRecord.class))).thenReturn(false);
        re = null;
        try {
            supplierProductService.updateSupplierProduct(epList, spmList, pList, spList, records);
            Assert.fail();
        } catch (TransactionException e) {
            re = e;
        }
        assertEquals(TransactionException.class, re.getClass());

        Mockito.when(discountRecordService.batchInsert(Mockito.anyListOf(DiscountRecord.class))).thenReturn(true);
        assertTrue(supplierProductService.updateSupplierProduct(epList, spmList, pList, spList, records));
    }
    
    @Test
    public void testDeleteBysupplierId(){
        assertFalse(supplierProductService.deleteBysupplierId(null));
        Mockito.when(supplierProductMapper.deleteBySupplierId(Mockito.anyLong())).thenReturn(1);
        assertTrue(supplierProductService.deleteBysupplierId(1L));
    }

    @Test
    public void testSelectOnshelfByPrimaryKey(){
        Mockito.when(supplierProductMapper.selectOnshelfByPrimaryKey(Mockito.anyLong())).thenReturn(new SupplierProduct());
        assertNotNull(supplierProductService.selectOnshelfByPrimaryKey(1L));
    }
    @Test
    public void testGetShBossProducts(){
        Mockito.when(shBossProductMapper.getShBossProducts()).thenReturn(new ArrayList<ShBossProduct>());
        assertNotNull(supplierProductService.getShBossProducts());
    }
    
    @Test
    public void testSetProductSupplierLimit(){
        SupplierProduct sp1 = new SupplierProduct();
        
        sp1.setLimitMoneyFlag(SupplierLimitStatus.OFF.getCode());
        Mockito.when(supplierProductMapper.selectById(Mockito.anyLong())).thenReturn(sp1);
        assertTrue(supplierProductService.setProductSupplierLimit(sp1, Mockito.anyLong()));
    
        SupplierProduct sp2 = new SupplierProduct();
        sp2.setLimitMoneyFlag(SupplierLimitStatus.ON.getCode());
        Mockito.when(supplierProductMapper.selectById(Mockito.anyLong())).thenReturn(sp2);
        Mockito.when(supplierProdModifyLimitRecordService.insertSelective(Mockito.any(SupplierProdModifyLimitRecord.class))).thenReturn(false);
        assertFalse(supplierProductService.setProductSupplierLimit(sp1, Mockito.anyLong()));
                
        Mockito.when(supplierProdModifyLimitRecordService.insertSelective(Mockito.any(SupplierProdModifyLimitRecord.class))).thenReturn(true);
        Mockito.when(supplierProdReqUsePerDayService.getTodayRecord(Mockito.anyLong())).thenReturn(new SupplierProdReqUsePerDay());
        Mockito.when(supplierProdReqUsePerDayService.deleteByPrimaryKey(Mockito.anyLong())).thenReturn(false);

        try{
            supplierProductService.setProductSupplierLimit(sp1, Mockito.anyLong());
        }catch(Exception e){
            
        }
        
        Mockito.when(supplierProdReqUsePerDayService.deleteByPrimaryKey(Mockito.anyLong())).thenReturn(true);


        Mockito.when(supplierProductMapper.updateByPrimaryKeySelective(Mockito.any(SupplierProduct.class))).thenReturn(0);
        try{
            supplierProductService.setProductSupplierLimit(sp1, Mockito.anyLong());
        }catch(Exception e){
            
        }

        Mockito.when(supplierProductMapper.updateByPrimaryKeySelective(Mockito.any(SupplierProduct.class))).thenReturn(1);
        Mockito.when(supplierService.updateByPrimaryKeySelective(Mockito.any(Supplier.class))).thenReturn(false);
        try{
            supplierProductService.setProductSupplierLimit(sp1, Mockito.anyLong());
        }catch(Exception e){
            
        }
        
        Mockito.when(supplierService.updateByPrimaryKeySelective(Mockito.any(Supplier.class))).thenReturn(true);
        assertTrue(supplierProductService.setProductSupplierLimit(sp1, Mockito.anyLong()));
        
        SupplierProduct sp3 = new SupplierProduct();
        sp3.setLimitMoneyFlag(SupplierLimitStatus.OFF.getCode());
        SupplierProduct sp4 = new SupplierProduct();
        sp4.setLimitMoneyFlag(SupplierLimitStatus.ON.getCode());        
        Mockito.when(supplierProductMapper.selectById(Mockito.anyLong())).thenReturn(sp3);
        Mockito.when(supplierProdReqUsePerDayService.insertSelective(Mockito.any(SupplierProdReqUsePerDay.class))).thenReturn(false);
        try{
            supplierProductService.setProductSupplierLimit(sp4, Mockito.anyLong());
        }catch(Exception e){
            
        }
        Mockito.when(supplierProdReqUsePerDayService.insertSelective(Mockito.any(SupplierProdReqUsePerDay.class))).thenReturn(true);        
        assertTrue(supplierProductService.setProductSupplierLimit(sp4, Mockito.anyLong()));
        
    }
    
    @Test
    public void testGetByNameOrCodeOrOpStatus(){
        Mockito.when(supplierProductMapper.getByNameOrCodeOrOpStatus(Mockito.anyMap())).thenReturn(new ArrayList<SupplierProduct>());        
        assertNotNull(supplierProductService.getByNameOrCodeOrOpStatus("test", "test", 1));        
    }
    
    @Test
    public void testGetShBossProductsByOrderType(){
        Mockito.when(shBossProductMapper.getShBossProductsByOrderType(Mockito.anyString())).thenReturn(new ArrayList<ShBossProduct>());        
        assertNotNull(supplierProductService.getShBossProductsByOrderType("test"));           
    }
    
    @Test
    public void testSelectAllSupplierProducts(){
        Mockito.when(supplierProductMapper.selectAllSupplierProducts()).thenReturn(new ArrayList<SupplierProduct>());        
        assertNotNull(supplierProductService.selectAllSupplierProducts());           
    }
    
    @Test
    public void testSelectByMap(){
        Mockito.when(supplierProductMapper.selectByMap(Mockito.anyMap())).thenReturn(new ArrayList<SupplierProduct>());        
        assertNotNull(supplierProductService.selectByMap(new HashMap()));           
    }
}
