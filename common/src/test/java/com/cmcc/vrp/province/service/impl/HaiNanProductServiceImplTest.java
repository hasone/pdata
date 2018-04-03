/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.province.dao.ProductMapper;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierProductMap;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.province.service.SupplierService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * <p>Title:HaiNanProductServiceImplTest </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月4日
 */

@RunWith(MockitoJUnitRunner.class)
public class HaiNanProductServiceImplTest {

    @InjectMocks
    HaiNanProductServiceImpl hnPctService = new HaiNanProductServiceImpl();

    @Mock
    EnterprisesService enterprisesService;
    @Mock
    EntProductService entProductService;
    @Mock
    SupplierService supplierService;
    @Mock
    SupplierProductService supplierProductService;
    @Mock
    SupplierProductMapService supplierProductMapService;
    @Mock
    ProductService productService;
    @Mock
    ProductMapper productMapper;

    @Test
    public void testSelectAllProductsByEnterId() {
        Long enterpriseId = 1L;
        List<Product> list = new ArrayList();

        when(entProductService.selectProductByEnterId(enterpriseId)).thenReturn(list);
        assertNull(hnPctService.selectAllProductsByEnterId(null));
        assertSame(list, hnPctService.selectAllProductsByEnterId(enterpriseId));
        Mockito.verify(entProductService, Mockito.times(1)).selectProductByEnterId(enterpriseId);
    }

    @Test
    public void testCreateNewProduct() {

        Map<String, Object> map = new HashMap();
        assertFalse(hnPctService.createNewProduct(map));

        map.put("GRP_ID", "1");
        assertFalse(hnPctService.createNewProduct(map));

        map.put("PAK_MONEY", "2");
        assertFalse(hnPctService.createNewProduct(map));

        map.put("PAK_GPRS", "3");
        Enterprise enter = new Enterprise();
        enter.setId(1L);
        when(enterprisesService.selectByCode(Mockito.anyString())).thenReturn(null).thenReturn(enter);
        assertFalse(hnPctService.createNewProduct(map));

        when(supplierProductService.insert(Mockito.any(SupplierProduct.class))).thenReturn(false).thenReturn(true);
        try {
            hnPctService.createNewProduct(map);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Product product = new Product();
        when(productService.selectByProductName(Mockito.anyString())).thenReturn(null);
        when(productMapper.insertSelective(Mockito.any(Product.class))).thenReturn(0).thenReturn(1);
        try {
            hnPctService.createNewProduct(map);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(supplierProductMapService.create(Mockito.any(SupplierProductMap.class))).thenReturn(false).thenReturn(true);
        try {
            hnPctService.createNewProduct(map);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(entProductService.insert(Mockito.any(EntProduct.class))).thenReturn(false).thenReturn(true);
        try {
            hnPctService.createNewProduct(map);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertTrue(hnPctService.createNewProduct(map));

        when(productService.selectByProductName(Mockito.anyString())).thenReturn(product);
        assertTrue(hnPctService.createNewProduct(map));

    }
}
