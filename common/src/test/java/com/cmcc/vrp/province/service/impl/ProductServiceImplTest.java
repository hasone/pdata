package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import com.cmcc.vrp.boss.chongqing.CQBossServiceImpl;
import com.cmcc.vrp.exception.TransactionException;
import com.cmcc.vrp.province.dao.ProductMapper;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.EntSyncList;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierProductMap;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EntSyncListService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SupplierProductAccountService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.QueryObject;
import com.cmcc.vrp.util.SizeUnits;

/**
 * @author qinqinyan
 * @Description 产品服务类
 * @date 2016年5月27日
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {

    @InjectMocks
    ProductService productService = new ProductServiceImpl();
    @Mock
    ProductMapper productMapper;
    @Mock
    EntProductService entProductService;
    @Mock
    EnterprisesService enterprisesService;
    @Mock
    AccountService accountService;

    @Mock
    SupplierProductService supplierProductService;

    @Mock
    SupplierProductMapService supplierProductMapService;
    @Mock
    EntSyncListService entSyncListService;

    @Mock
    SupplierProductAccountService supplierProductAccountService;

    @Mock
    ApplicationContext applicationContext;

    @Mock
    CQBossServiceImpl bossService;
    
    @Mock
    GlobalConfigService globalConfigService;

    /**
     * 测试获取符合条件的产品个数
     */
    @Test
    public void testList() {
        //invalid
        assertNull(productService.list(null));

        //valid
        when(productMapper.getProductList(anyMapOf(String.class, Object.class))).thenReturn(new LinkedList<Product>());
        assertNotNull(productService.list(new QueryObject()));

        verify(productMapper).getProductList(anyMapOf(String.class, Object.class));
    }

    /**
     *
     */
    @Test
    public void testList1() {
        //invalid
        assertNull(productService.list(null));

        //valid
        when(productMapper.getProductList(anyMapOf(String.class, Object.class))).thenReturn(new LinkedList<Product>());
        QueryObject q = new QueryObject();
        q.getQueryCriterias().put("productSize", "10240");
        assertNotNull(productService.list(q));

        verify(productMapper).getProductList(anyMapOf(String.class, Object.class));
    }

    /**
     * @Title:queryCount
     * @Description: 得到满足条件的记录条数
     */
    @Test
    public void testGetProductCount() {
        //invalid
        assertSame(0, productService.getProductCount(null));
        //valid
        int productCount = 5;
        when(productMapper.getProductCount(anyMapOf(String.class, Object.class))).thenReturn(productCount);

        assertSame(5, productService.getProductCount(initQueryObject()));

        //System.out.println(count);

        verify(productMapper, times(1)).getProductCount(anyMapOf(String.class, Object.class));
    }

    /**
     *
     */
    @Test
    public void testGetProductCount1() {
        //invalid
        assertSame(0, productService.getProductCount(null));
        //valid
        int productCount = 5;
        when(productMapper.getProductCount(anyMapOf(String.class, Object.class))).thenReturn(productCount);

        QueryObject q = initQueryObject();
        q.getQueryCriterias().put("productSize", "10240");
        assertSame(5, productService.getProductCount(q));

        //System.out.println(count);

        verify(productMapper, times(1)).getProductCount(anyMapOf(String.class, Object.class));
    }

    /**
     * @Title:delete
     * @Description: 删除产品
     */
    @Test
    public void testDelete() {
        //invalid
        assertFalse(productService.delete(null));
        //valid
        Product product = proWithId();
        when(productMapper.selectByPrimaryKey(product.getId())).thenReturn(product);
        when(productMapper.updateByPrimaryKeySelective(any(Product.class))).thenReturn(1);
        assertTrue(productService.delete(product.getId()));

        verify(productMapper).selectByPrimaryKey(Mockito.anyLong());
        verify(productMapper).updateByPrimaryKeySelective(any(Product.class));
    }

    /**
     *
     */
    @Test
    public void testDelete1() {
        //valid
        Product product = proWithId();
        when(productMapper.selectByPrimaryKey(product.getId())).thenReturn(null);
        assertFalse(productService.delete(product.getId()));

        when(productMapper.selectByPrimaryKey(product.getId())).thenReturn(product);
        when(productMapper.updateByPrimaryKeySelective(any(Product.class))).thenReturn(0);
        assertFalse(productService.delete(product.getId()));

        verify(productMapper, times(2)).selectByPrimaryKey(Mockito.anyLong());
        verify(productMapper).updateByPrimaryKeySelective(any(Product.class));
    }

    /**
     * @Title:changeProductStatus
     * @Description: 改变产品的上下架状态
     */
    @Test
    public void testChangeProductStatus() {
        //invalid
        assertFalse(productService.changeProductStatus(null));
        //valid
        when(productMapper.updateByPrimaryKeySelective(any(Product.class))).thenReturn(1);
        assertTrue(productService.changeProductStatus(proWithId()));

        verify(productMapper).updateByPrimaryKeySelective(any(Product.class));
    }

    /**
     * @Title:selectProductById
     * @Description: 根据产品Id查找产品
     */
    @Test
    public void testSelectProductById() {
        //invalid
        assertNull(productService.selectProductById(null));
        //valid
        when(productMapper.selectByPrimaryKey(proWithId().getId())).thenReturn(proWithId());
        assertSame(proWithId().getId(), productService.selectProductById(proWithId().getId()).getId());

        verify(productMapper).selectByPrimaryKey(Mockito.anyLong());
    }

    /**
     * @Title:selectProductById
     * @Description: 根据产品编码查找产品
     */
    @Test
    public void testSelectByCode() {
        //invalid
        assertNull(productService.selectByCode(null));
        //valid
        List<Product> productList = new LinkedList<Product>();
        productList.add(proWithId());
        when(productMapper.selectByCode(proWithId().getProductCode())).thenReturn(productList);
        assertSame(proWithId().getProductCode(), productService.selectByCode(proWithId().getProductCode())
                .getProductCode());
        verify(productMapper).selectByCode(Mockito.anyString());
    }

    /**
     *
     */
    @Test
    public void testSelectByCode1() {
        //invalid
        assertNull(productService.selectByCode(null));
        //valid
        List<Product> productList = new LinkedList<Product>();
        productList.add(proWithId());
        when(productMapper.selectByCode(proWithId().getProductCode())).thenReturn(null);
        assertNull(productService.selectByCode(proWithId().getProductCode()));

        when(productMapper.selectByCode(proWithId().getProductCode())).thenReturn(new ArrayList());
        assertNull(productService.selectByCode(proWithId().getProductCode()));

        verify(productMapper, times(2)).selectByCode(Mockito.anyString());
    }

    /**
     * @Title:insertProduct
     * @Description: 插入产品
     */
    @Test
    public void testInsertProduct() {
        //invalid
        assertFalse(productService.insertProduct(null));
        //valid
        when(productMapper.selectByProductName(proWithId().getName())).thenReturn(proWithId());
        when(productMapper.selectByProductName(proWithoutId().getName())).thenReturn(null);
        when(productMapper.insertSelective(any(Product.class))).thenReturn(1);

        assertTrue(productService.insertProduct(proWithId()));
        assertTrue(productService.insertProduct(proWithoutId()));

    }

    /**
     *
     */
    @Test
    public void testInsertProduct1() {
        Product p = new Product();
        p.setName("11");
        p.setProductCode("1111");
        List<Product> products = new ArrayList<Product>();
        products.add(p);
        when(productMapper.selectByCode(Mockito.anyString())).thenReturn(products);

        assertFalse(productService.insertProduct(proWithId()));

        verify(productMapper).selectByCode(Mockito.anyString());
    }

    /**
     * @Title:updateProduct
     * @Description: 更新产品
     */
    @Test
    public void testUpdateProduct() {
        //invalid
        assertFalse(productService.updateProduct(null));

        //valid
        when(productService.selectByProductName(proWithId().getName())).thenReturn(proWithId());
        when(productService.selectByProductName(proOneName().getName())).thenReturn(proOtherName());
        when(productMapper.updateByPrimaryKeySelective(any(Product.class))).thenReturn(1);

        assertTrue(productService.updateProduct(proWithId()));
        assertTrue(productService.updateProduct(proOneName()));

    }

    /**
     *
     */
    @Test
    public void testUpdateProduct1() {
        Product p = new Product();
        when(productService.selectByProductName(proWithId().getName())).thenReturn(p);
        when(productMapper.updateByPrimaryKeySelective(any(Product.class))).thenReturn(0);

        assertFalse(productService.updateProduct(new Product()));

        verify(productMapper).updateByPrimaryKeySelective(any(Product.class));
    }

    /**
     *
     */
    @Test
    public void testUpdateProduct2() {
        Product p = proOtherName();
        p.setId(1111L);
        List<Product> products = new ArrayList<Product>();
        products.add(p);
        when(productMapper.selectByCode(Mockito.anyString())).thenReturn(products);
        assertFalse(productService.updateProduct(proOneName()));

        when(productMapper.selectByCode(Mockito.anyString())).thenReturn(null);
        assertFalse(productService.updateProduct(proOneName()));

    }

    /**
     * @Title:checkUnique
     * @Description: 检查产品唯一性
     */
    @Test
    public void testCheckUnique() {
        assertTrue(productService.checkUnique(null));

        Product p = new Product();
        p.setId(1L);
        List<Product> ps = new ArrayList();
        ps.add(p);
        when(productMapper.selectAllProducts()).thenReturn(ps);
        assertTrue(productService.checkUnique(p));

        Product p2 = new Product();
        p2.setName("11");
        p2.setId(2L);
        ps.add(p2);
        p.setName("11");
        when(productMapper.selectAllProducts()).thenReturn(ps);
        assertFalse(productService.checkUnique(p));

    }

    /**
     * @Title:checkNameUnique
     * @Description: 检查产品名称唯一性
     */
    @Test
    public void testCheckNameUnique() {
        assertTrue(productService.checkNameUnique(null));
    }

    /**
     * @Title:checkProductCodeUnique
     * @Description: 产品编码唯一性检查
     */
    @Test
    public void checkProductCodeUnique() {
        Product p2 = new Product();
        p2.setName("11");
        p2.setId(2L);
        p2.setProductCode("11111");
        List<Product> ps = new ArrayList();
        ps.add(p2);

        Product p = new Product();

        when(productMapper.selectAllProducts()).thenReturn(ps);
        assertTrue(productService.checkProductCodeUnique(p));

        p.setId(1L);
        when(productMapper.selectAllProducts()).thenReturn(ps);
        assertTrue(productService.checkProductCodeUnique(p));

        p.setProductCode("11111");
        when(productMapper.selectAllProducts()).thenReturn(ps);
        assertFalse(productService.checkProductCodeUnique(p));
    }

    /**
     * @Title:selectAllProducts
     * @Description: TODO
     */
    @Test
    public void testSelectAllProducts() {
        when(productMapper.selectAllProducts()).thenReturn(new LinkedList<Product>());
        assertNotNull(productService.selectAllProducts());
        verify(productMapper).selectAllProducts();
    }

    /**
     *
     */
    @Test
    public void testGetProListByProSizeEnterId() {
        when(productMapper.getProListByProSizeEnterId(anyMapOf(String.class, Object.class))).thenReturn(
                new LinkedList<Product>());
        assertNotNull(productService.getProListByProSizeEnterId("10240", 1L));
        Mockito.verify(productMapper).getProListByProSizeEnterId(Mockito.anyMap());
    }

    /**
     * @Title:selectDistinctFlowSize
     * @Description: TODO
     */
    @Test
    public void testSelectDistinctProSize() {

        List<Product> productList = new LinkedList<Product>();
        productList.add(proWithId());
        productList.add(proOtherName());

        when(productMapper.selectDistinctProSize()).thenReturn(productList);
        assertSame(productList.size(), productService.selectDistinctProSize().size());
        verify(productMapper).selectDistinctProSize();
    }

    /**
     * 根据企业编码查询所有的产品信息
     *
     * @return
     */
    @Test
    public void testSelectAllProductsByEnterCode() {
        String enterpriseCode = "123456";
        List<Product> productList = new LinkedList<Product>();
        productList.add(proWithId());
        productList.add(proOtherName());

        when(productMapper.selectAllProductsByEnterCode(enterpriseCode)).thenReturn(productList);
        assertSame(productList.size(), productService.selectAllProductsByEnterCode(enterpriseCode).size());

        verify(productMapper).selectAllProductsByEnterCode(Mockito.anyString());
    }

    /**
     * 得到平台中定义的现金产品
     */
    @Test
    public void testGetCurrencyProduct() {
        when(productMapper.getCurrencyProduct()).thenReturn(proWithId());
        assertSame(proWithId().getId(), productService.getCurrencyProduct().getId());
        verify(productMapper).getCurrencyProduct();
    }

    @Test
    public void testGetFlowProduct() {
        when(productMapper.getFlowProduct()).thenReturn(proWithId());
        assertSame(proWithId().getId(), productService.getFlowProduct().getId());
        verify(productMapper).getFlowProduct();
    }

    /**
     *
     */
    @Test
    public void testSelectAllProductsByEnterId() {
        assertNull(productService.selectAllProductsByEnterId(null));

        Long enterpriseId = 1L;
        List<Product> productList = new LinkedList<Product>();
        productList.add(proWithId());
        productList.add(proOtherName());

        when(productMapper.selectAllProductsByEnterId(enterpriseId)).thenReturn(productList);
        assertSame(productList.size(), productService.selectAllProductsByEnterId(enterpriseId).size());
        verify(productMapper).selectAllProductsByEnterId(Mockito.anyLong());
    }

    /**
     * @Title:selectByProductName
     * @Description: TODO
     */
    @Test
    public void testSelectByProductName() {
        assertNull(productService.selectByProductName(null));

        when(productMapper.selectByProductName(proWithId().getName())).thenReturn(proWithId());
        assertTrue(productService.selectByProductName(proWithId().getName()).getName().equals(proWithId().getName()));

        verify(productMapper).selectByProductName(Mockito.anyString());
    }

    /**
     *
     */
    @Test
    public void testSelectByProductCode() {

        assertNull(productService.selectByProductCode(null));

        when(productMapper.selectByProductCode(proWithId().getProductCode())).thenReturn(proWithId());
        assertTrue(productService.selectByProductCode(proWithId().getProductCode()).getProductCode()
                .equals(proWithId().getProductCode()));

        verify(productMapper).selectByProductCode(Mockito.anyString());
    }

    /**
     *
     */
    @Test
    public void testSelectProIdsByProductCode() {
        List<String> proCodeList = new LinkedList<String>();
        proCodeList.add(proWithId().getProductCode());
        proCodeList.add(proOtherName().getProductCode());
        when(productService.selectByProductCode(proWithId().getProductCode())).thenReturn(proWithId());
        when(productService.selectByProductCode(proOtherName().getProductCode())).thenReturn(proOtherName());

        assertSame(proCodeList.size(), productService.selectProIdsByProductCode(proCodeList).size());
    }

    /**
     * @Title:get
     * @Description: TODO
     */
    @Test
    public void testGet() {
        assertNull(productService.get(null));

        when(productMapper.selectByPrimaryKey(proWithId().getId())).thenReturn(proWithId());
        assertSame(proWithId().getId(), productService.get(proWithId().getId()).getId());

        verify(productMapper).selectByPrimaryKey(Mockito.anyLong());
    }

    /**
     * 查询所有上架的产品
     *
     * @return
     */
    @Test
    public void testSelectAllProductsON() {

        when(productMapper.selectAllProductsON()).thenReturn(new LinkedList<Product>());
        assertNotNull(productService.selectAllProductsON());

        verify(productMapper).selectAllProductsON();
    }

    /**
     *
     */
    @Test
    public void testBatchSelectByCodes() {
        assertNull(productService.batchSelectByCodes(null));

        Set<String> setParams = new HashSet<String>();
        setParams.add(proWithId().getProductCode());

        List<Product> productList = new LinkedList<Product>();
        productList.add(proWithId());

        when(productMapper.batchSelectProductsByProductCodes(setParams)).thenReturn(productList);
        assertNotNull(productService.batchSelectByCodes(setParams));

        verify(productMapper).batchSelectProductsByProductCodes(Mockito.anySet());
    }

    /**
     *
     */
    @Test
    public void testGetProductListForEnter() {
        assertNull(productService.getProductListForEnter(null));

        when(productMapper.getProductListForEnter(anyMapOf(String.class, Object.class))).thenReturn(
                new LinkedList<Product>());

        assertNotNull(productService.getProductListForEnter(new QueryObject()));
        verify(productMapper).getProductListForEnter(anyMapOf(String.class, Object.class));
    }

    /**
     *
     */
    @Test
    public void testGetProductListForEnter1() {
        when(productMapper.getProductListForEnter(anyMapOf(String.class, Object.class))).thenReturn(
                new LinkedList<Product>());
        QueryObject q = new QueryObject();
        q.getQueryCriterias().put("size", "10240");
        assertNotNull(productService.getProductListForEnter(q));
        verify(productMapper).getProductListForEnter(anyMapOf(String.class, Object.class));
    }

    /**
     *
     */
    @Test
    public void testGetProductCountForEnter() {
        assertSame(0, productService.getProductCountForEnter(null));

        when(productMapper.getProductCountForEnter(anyMapOf(String.class, Object.class))).thenReturn(0);
        assertSame(0, productService.getProductCountForEnter(new QueryObject()));

        verify(productMapper).getProductCountForEnter(anyMapOf(String.class, Object.class));
    }

    /**
     *
     */
    @Test
    public void testGetProductCountForEnter1() {
        when(productMapper.getProductCountForEnter(anyMapOf(String.class, Object.class))).thenReturn(0);
        QueryObject q = new QueryObject();
        q.getQueryCriterias().put("size", "10240");
        assertSame(0, productService.getProductCountForEnter(q));

        verify(productMapper).getProductCountForEnter(anyMapOf(String.class, Object.class));
    }

    /**
     *
     */
    @Test
    public void testSelectDefaultProductByCustomerType() {
        when(productMapper.selectDefaultProductByCustomerType(Mockito.anyLong())).thenReturn(new ArrayList());
        assertNotNull(productService.selectDefaultProductByCustomerType(1L));
        verify(productMapper).selectDefaultProductByCustomerType(Mockito.anyLong());
    }

    /**
     * @Title: createNewProduct
     * @Description: 新增BOSS端产品, 根据各个省公司定义的BOSS端产品信息不同，分别去实现
     */
    @Test
    public void testCreateNewProduct() {
        assertFalse(productService.createNewProduct(new HashMap<String, Object>()));
    }

    /**
     *
     */
    @Test(expected = RuntimeException.class)
    public void testDeletePlatformProduct0() {
        Product product = proWithId();
        when(productMapper.selectByPrimaryKey(product.getId())).thenReturn(product);

        when(productMapper.updateByPrimaryKeySelective(any(Product.class))).thenReturn(0);
        productService.deletePlatformProduct(product.getId());
    }

    /**
     *
     */
    @Test(expected = RuntimeException.class)
    public void testDeletePlatformProduct1() {
        Product product = proWithId();
        when(productMapper.selectByPrimaryKey(product.getId())).thenReturn(product);
        when(productMapper.updateByPrimaryKeySelective(any(Product.class))).thenReturn(1);

        when(entProductService.deleteByProductID(Mockito.anyLong())).thenReturn(false);
        productService.deletePlatformProduct(product.getId());
    }

    /**
     *
     */
    @Test(expected = RuntimeException.class)
    public void testDeletePlatformProduct2() {
        Product product = proWithId();
        when(productMapper.selectByPrimaryKey(product.getId())).thenReturn(product);
        when(productMapper.updateByPrimaryKeySelective(any(Product.class))).thenReturn(1);
        when(entProductService.deleteByProductID(Mockito.anyLong())).thenReturn(true);

        when(supplierProductMapService.deleteByPlftPid(Mockito.anyLong())).thenReturn(false);

        productService.deletePlatformProduct(product.getId());
    }

    /**
     *
     */
    @Test
    public void testDeletePlatformProduct3() {
        Product product = proWithId();
        when(productMapper.selectByPrimaryKey(product.getId())).thenReturn(product);
        when(productMapper.updateByPrimaryKeySelective(any(Product.class))).thenReturn(1);
        when(entProductService.deleteByProductID(Mockito.anyLong())).thenReturn(true);

        when(supplierProductMapService.deleteByPlftPid(Mockito.anyLong())).thenReturn(true);

        assertTrue(productService.deletePlatformProduct(product.getId()));
    }

    /**
     *
     */
    @Test
    public void testGetProductListFromEnterAccount() {
        assertNull(productService.getProductListFromEnterAccount(null));

        when(productMapper.getProductListFromEnterAccount(Mockito.anyMap())).thenReturn(new ArrayList());

        QueryObject q = new QueryObject();
        assertNotNull(productService.getProductListFromEnterAccount(q));
        q.getQueryCriterias().put("size", "10240");
        assertNotNull(productService.getProductListFromEnterAccount(q));

        verify(productMapper, times(2)).getProductListFromEnterAccount(Mockito.anyMap());
    }

    /**
     *
     */
    @Test
    public void testGetProductCountFromEnterAccount() {
        assertEquals(productService.getProductCountFromEnterAccount(null), 0);

        when(productMapper.getProductCountFromEnterAccount(Mockito.anyMap())).thenReturn(1);

        QueryObject q = new QueryObject();
        assertEquals(productService.getProductCountFromEnterAccount(q), 1);
        q.getQueryCriterias().put("size", "10240");
        assertEquals(productService.getProductCountFromEnterAccount(q), 1);

        verify(productMapper, times(2)).getProductCountFromEnterAccount(Mockito.anyMap());
    }

    /**
     *
     */
    @Test
    public void testGetProductsByPrizes() {
        assertNull(productService.getProductsByPrizes(null));

        when(productMapper.selectProductsByPrizes(Mockito.anyList())).thenReturn(new ArrayList());
        assertNotNull(productService.getProductsByPrizes(new ArrayList()));

        verify(productMapper).selectProductsByPrizes(Mockito.anyList());
    }

    /**
     *
     */
    @Test
    @Ignore
    public void testGetProductByEntIdAndIsp() {
        assertNull(productService.getProductByEntIdAndIsp(null));

        QueryObject q = new QueryObject();
        assertNull(productService.getProductByEntIdAndIsp(q));

        q.getQueryCriterias().put("enterId", "1");
        assertNull(productService.getProductByEntIdAndIsp(q));

        q.getQueryCriterias().put("isp", "M");
        when(productMapper.getProductListForEnter(Mockito.anyMap())).thenReturn(null);
        assertNull(productService.getProductByEntIdAndIsp(q));

        when(productMapper.getProductListForEnter(Mockito.anyMap())).thenReturn(new ArrayList());
        assertNull(productService.getProductByEntIdAndIsp(q));

        List<Product> l = new ArrayList();
        l.add(new Product());
        when(productMapper.getProductListForEnter(Mockito.anyMap())).thenReturn(l);
        when(productMapper.getProducts(Mockito.anyMap())).thenReturn(new ArrayList());
        assertNotNull(productService.getProductByEntIdAndIsp(q));

        verify(productMapper, times(3)).getProductListForEnter(Mockito.anyMap());
        verify(productMapper).getProducts(Mockito.anyMap());

    }

    /**
     *
     */
    @Test
    @Ignore
    public void testCountProductByEntIdAndIsp() {
        assertEquals(productService.countProductByEntIdAndIsp(null).intValue(), 0);

        QueryObject q = new QueryObject();

        assertEquals(productService.countProductByEntIdAndIsp(q).intValue(), 0);

        q.getQueryCriterias().put("enterId", "1");

        assertEquals(productService.countProductByEntIdAndIsp(q).intValue(), 0);

        q.getQueryCriterias().put("isp", "M");
        when(productMapper.getProductCountForEnter(Mockito.anyMap())).thenReturn(1);
        assertEquals(productService.countProductByEntIdAndIsp(q).intValue(), 1);

        verify(productMapper, times(1)).getProductCountForEnter(Mockito.anyMap());

    }

    /**
     *
     */
    @Test
    public void batchInsertProduct() {
        List<Product> ps = new ArrayList();
        ps.add(new Product());

        assertFalse(productService.batchInsertProduct(null));

        when(productMapper.batchInsertProduct(Mockito.anyList())).thenReturn(ps.size());
        assertTrue(productService.batchInsertProduct(ps));

        when(productMapper.batchInsertProduct(Mockito.anyList())).thenReturn(0);
        assertFalse(productService.batchInsertProduct(ps));

        verify(productMapper, times(2)).batchInsertProduct(Mockito.anyList());
    }

    /**
     *
     */
    @Test
    public void testSelectProductByCreateTime() {
        assertNull(productService.selectProductByCreateTime(null));

        when(productMapper.selectProductByCreateTime(Mockito.any(Date.class))).thenReturn(new ArrayList());
        assertNotNull(productService.selectProductByCreateTime(new Date()));
        verify(productMapper).selectProductByCreateTime(Mockito.any(Date.class));
    }

    /**
     *
     */
    @Test
    public void testBatchUpdate() {
        assertTrue(productService.batchUpdate(null));
        List<Product> list = new ArrayList<Product>();
        list.add(proWithId());
        when(productMapper.batchUpdate(list)).thenReturn(0);
        assertFalse(productService.batchUpdate(list));

        when(productMapper.batchUpdate(list)).thenReturn(1);
        assertTrue(productService.batchUpdate(list));
    }

    /**
     *
     */
    @Test
    public void testGetProductsByPidWithDeleteFlag() {
        assertNull(productService.getProductsByPidWithDeleteFlag(null));

        List<Product> list = new ArrayList<Product>();
        when(productMapper.getProductsByPidWithDeleteFlag(Mockito.anyLong())).thenReturn(list);

        assertSame(list, productService.getProductsByPidWithDeleteFlag(1L));
    }

    @Test
    public void getProductForFlowAccount() {
        when(productMapper.selectFlowAccountProductByProductSize(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);
        assertNull(productService.getProductForFlowAccount("11", 1L));
    }


    /**
     *
     */
    @Test
    public void testSelectByProSize() {
        assertNull(productService.selectByProSize(null));

        when(productMapper.selectProductByProductSize(Mockito.anyString())).thenReturn(new ArrayList());

        assertNotNull(productService.selectByProSize(10240L));
    }

    /**
     *
     */
    @Test
    public void synPrdsWithSupplierProTest() {
        assertFalse(productService.synPrdsWithSupplierPro(null, "123"));
        assertFalse(productService.synPrdsWithSupplierPro("123", null));
        assertFalse(productService.synPrdsWithSupplierPro("", "123"));
        assertFalse(productService.synPrdsWithSupplierPro("123", ""));

        when(enterprisesService.selectByCode(any(String.class)))
                .thenReturn(null);
        assertFalse(productService.synPrdsWithSupplierPro("123", "123"));

        when(enterprisesService.selectByCode(any(String.class)))
                .thenReturn(new Enterprise());
        when(applicationContext.getBean("cqBossService", CQBossServiceImpl.class))
                .thenReturn(null);
        assertFalse(productService.synPrdsWithSupplierPro("123", "123"));
        when(applicationContext.getBean("cqBossService", CQBossServiceImpl.class))
                .thenReturn(bossService);
        List<String> prdsList = new ArrayList<String>();
        prdsList.add("gl_mwsq_10M");

        when(bossService.getProductsOrderByEnterCode(any(String.class)))
                .thenReturn(null);
        assertFalse(productService.synPrdsWithSupplierPro("123", "123"));
        when(bossService.getProductsOrderByEnterCode(any(String.class)))
                .thenReturn(new ArrayList<String>());
        assertFalse(productService.synPrdsWithSupplierPro("123", "123"));

        when(bossService.getProductsOrderByEnterCode(any(String.class)))
                .thenReturn(prdsList);
        when(bossService.getEnterPrdSolde(any(String.class), any(String.class)))
                .thenReturn("50");
        Product product = new Product();
        product.setId((long) 1);
        product.setName("xxxx");
        when(productMapper.selectByProductCode(any(String.class)))
                .thenReturn(product);
        when(supplierProductService.selectByFeature(any(String.class)))
                .thenReturn(null);
        when(supplierProductService.insert(any(SupplierProduct.class)))
                .thenReturn(true);


        when(supplierProductMapService.create(any(SupplierProductMap.class)))
                .thenReturn(true);
        when(entProductService.selectByProductIDAndEnterprizeID(any(Long.class), any(Long.class)))
                .thenReturn(null);
        when(entProductService.insert(any(EntProduct.class)))
                .thenReturn(true);
        when(accountService.get(any(Long.class), any(Long.class), any(Integer.class)))
                .thenReturn(null);
        when(accountService.createEnterAccount(any(Long.class), anyMapOf(Long.class, Double.class), any(String.class)))
                .thenReturn(true);
        EntSyncList entSyncList = new EntSyncList();
        entSyncList.setId((long) 1);
        SupplierProduct supplierPro = new SupplierProduct();
        supplierPro.setId((long) 1);
        when(entSyncListService.getByEntIdAndEntProCode(any(Long.class), any(String.class)))
                .thenReturn(entSyncList);
        when(supplierProductService.selectByFeature(any(String.class)))
                .thenReturn(supplierPro);
        when(supplierProductAccountService.createSupplierProductAccount(any(Long.class), any(Double.class), any(Long.class)))
                .thenReturn(true);
        assertTrue(productService.synPrdsWithSupplierPro("123", "123"));
        Account account = new Account();
        account.setId((long) 1);
        when(accountService.get(any(Long.class), any(Long.class), any(Integer.class)))
                .thenReturn(account);
        when(accountService.updateCount(any(Long.class), any(Double.class)))
                .thenReturn(true);
        assertTrue(productService.synPrdsWithSupplierPro("123", "123"));
        when(supplierProductAccountService.createSupplierProductAccount(any(Long.class), any(Double.class), any(Long.class)))
                .thenReturn(false);
        try {
            productService.synPrdsWithSupplierPro("123", "123");
        } catch (TransactionException e) {
            assertNotNull(e.getMessage());
        }

        when(supplierProductService.selectByFeature(any(String.class)))
                .thenReturn(null);
        assertTrue(productService.synPrdsWithSupplierPro("123", "123"));

        when(accountService.updateCount(any(Long.class), any(Double.class)))
                .thenReturn(false);
        try {
            productService.synPrdsWithSupplierPro("123", "123");
        } catch (TransactionException e) {
            assertNotNull(e.getMessage());
        }
        when(entProductService.insert(any(EntProduct.class)))
                .thenReturn(false);
        try {
            productService.synPrdsWithSupplierPro("123", "123");
        } catch (TransactionException e) {
            assertNotNull(e.getMessage());
        }
        when(supplierProductMapService.create(any(SupplierProductMap.class)))
                .thenReturn(false);

        try {
            productService.synPrdsWithSupplierPro("123", "123");
        } catch (TransactionException e) {
            assertNotNull(e.getMessage());
        }
        when(productMapper.selectByProductCode(any(String.class)))
            .thenReturn(null);
        try {
            productService.synPrdsWithSupplierPro("123", "123");
        } catch (TransactionException e) {
            assertNotNull(e.getMessage());
        }
        when(supplierProductService.insert(any(SupplierProduct.class)))
                .thenReturn(false);
        try {
            productService.synPrdsWithSupplierPro("123", "123");
        } catch (TransactionException e) {
            assertNotNull(e.getMessage());
        }
    }
    
    /**
     * testGetPrdsByType
     */
    @Test
    public void testGetPrdsByType(){
        Long diffId = null;
        List<Integer> types = new ArrayList<Integer>();
        Long entId = null;
        
        List<Product> prdList = new ArrayList<Product>();
        Mockito.when(productMapper.getPrdsByType(diffId, types,entId)).thenReturn(prdList);
        
        Assert.assertEquals(productService.getPrdsByType(diffId, types, entId), prdList);
    }
    
    /**
     * testGetPrdsByTypePageList
     */
    @Test
    public void testGetPrdsByTypePageList(){
        QueryObject queryObject = new QueryObject();
        List<Integer> types = new ArrayList<Integer>();
        
        List<Product> prdList = new ArrayList<Product>();
        Mockito.when(productMapper.getPrdsByTypePageList(Mockito.anyMapOf(String.class, Object.class))).thenReturn(prdList);
        
        Assert.assertEquals(productService.getPrdsByTypePageList(queryObject, types),prdList);
    }
    
    /**
     * testGetPrdsByTypePageCount
     */
    @Test
    public void testGetPrdsByTypePageCount(){
        QueryObject queryObject = new QueryObject();
        List<Integer> types = new ArrayList<Integer>();

        Mockito.when(productMapper.getPrdsByTypePageCount(Mockito.anyMapOf(String.class, Object.class))).thenReturn(1);
        
        Assert.assertEquals(productService.getPrdsByTypePageCount(queryObject, types),1);
    }
    
    /**
     * 
     */
    @Test
    public void testGetPrdBySizeAndId() {
        assertNull(productService.getPrdBySizeAndId(null, 1l));
        assertNull(productService.getPrdBySizeAndId(1l, null));
        Product product = proWithId();
        when(productMapper.selectPrdBySizeAndId(any(Long.class), any(Long.class)))
            .thenReturn(product);
        assertEquals(product, productService.getPrdBySizeAndId(1l, 1l));
    }
    
    @Test
    public void testGetPlatFormBySupplierId(){
        assertNull(productService.getPlatFormBySupplierId(null));
        when(productMapper.getPlatFormBySupplierId(any(Long.class)))
            .thenReturn(new ArrayList<Product>());
        assertNotNull(productService.getPlatFormBySupplierId(1L));
    }
    
    /**
     * 
     */
    @Test
    public void testListProductsByTemplateId() {
        when(productMapper.listProductsByTemplateId(Mockito.anyMap()))
            .thenReturn(new ArrayList<Product>());
        Assert.assertNotNull(productService.listProductsByTemplateId(new HashMap()));
    }

    /**
     * 
     */
    @Test
    public void testCountProductsByTemplate() {
        when(productMapper.countProductsByTemplate(Mockito.anyMap()))
            .thenReturn(1);
        Assert.assertEquals(1, productService.countProductsByTemplate(new HashMap()));
    }
    
    /**
     * 
     */
    @Test
    public void testShowForPageResultCount() {
        Assert.assertEquals(0, productService.showForPageResultCount(null));
        when(productMapper.showForPageResultCount(Mockito.anyMap()))
            .thenReturn(1);
        Assert.assertEquals(1, productService.showForPageResultCount(new QueryObject()));
    }
    /**
     * 
     */
    @Test
    public void testShowForPageResultList() {
        Assert.assertTrue(productService.showForPageResultList(null).isEmpty());
        when(productMapper.showForPageResultList(Mockito.anyMap()))
            .thenReturn(new ArrayList<Product>());
        Assert.assertTrue(productService.showForPageResultList(new QueryObject()).isEmpty());
    }
    
    /**
     * 
     */
    @Test
    public void testSelectByType() {
        Assert.assertNull(productService.selectByType(null));

        when(productMapper.selectByType(Mockito.anyInt()))
            .thenReturn(new ArrayList<Product>());
        Assert.assertTrue(productService.selectByType(1).isEmpty());
    }
    
    /**
     * 
     * sdPrdsSort
     */
    @Test
    public void testSdPrdsSort(){
        when(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey())).thenReturn("sd");
        List<Product> listPrds = new ArrayList<Product>();
        Product prd1 = new Product();
        prd1.setProductCode("108704");
        Product prd2 = new Product();
        prd2.setProductCode("108701");
        Product prd3 = new Product();
        prd3.setProductCode("109204");
        Product prd4 = new Product();
        prd4.setProductCode("109201");
        Product prd5 = new Product();
        prd5.setProductCode("");
        
        Assert.assertTrue(productService.sdPrdsSort(listPrds));
        
        when(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey())).thenReturn("js");
        Assert.assertFalse(productService.sdPrdsSort(listPrds));
                
    }
    
    private Product proWithId() {
        Product record = new Product();
        record.setId(1L);
        record.setProductCode("1111");
        record.setName("不重名测试");
        record.setProductSize(SizeUnits.GB.toKB(1));
        return record;
    }

    private Product proWithoutId() {
        Product record = new Product();
        record.setId(null);

        record.setName("重名测试");
        return record;
    }

    private Product proOneName() {
        Product record = new Product();
        record.setId(1L);
        record.setName("重名测试");
        return record;
    }

    private Product proOtherName() {
        Product record = new Product();
        record.setId(2L);
        record.setName("重名测试");
        record.setProductCode("2222");
        record.setProductSize(SizeUnits.MB.toKB(10));
        return record;
    }

    private QueryObject initQueryObject() {
        QueryObject queryObject = new QueryObject();
        queryObject.getQueryCriterias().put("productName", "1g流量包");
        queryObject.getQueryCriterias().put("status", 1);
        return queryObject;
    }

}