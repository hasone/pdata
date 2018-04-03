package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.enums.IspType;
import com.cmcc.vrp.province.dao.SupplierMapper;
import com.cmcc.vrp.province.dao.SupplierProductMapMapper;
import com.cmcc.vrp.province.dao.SupplierProductMapper;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.Supplier;
import com.cmcc.vrp.province.model.SupplierFinanceRecord;
import com.cmcc.vrp.province.model.SupplierModifyLimitRecord;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierReqUsePerDay;
import com.cmcc.vrp.province.service.SupplierFinanceRecordService;
import com.cmcc.vrp.province.service.SupplierModifyLimitRecordService;
import com.cmcc.vrp.province.service.SupplierPayRecordService;
import com.cmcc.vrp.province.service.SupplierProductMapService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.province.service.SupplierReqUsePerDayService;
import com.cmcc.vrp.province.service.SupplierService;
import com.cmcc.vrp.util.QueryObject;
import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 供应商实现UT
 * <p>
 * Created by sunyiwei on 2016/10/31.
 */
@RunWith(MockitoJUnitRunner.class)
public class SupplierServiceImplTest extends BaseTest {
    @InjectMocks
    SupplierService supplierService = new SupplierServiceImpl();

    @Mock
    SupplierMapper supplierMapper;

    @Mock
    SupplierProductMapper supplierProductMapper;

    @Mock
    SupplierProductMapMapper supplierProductMapMapper;

    @Mock
    SupplierProductMapService supplierProductMapService;
    @Mock
    SupplierProductService supplierProductService;
    @Mock
    SupplierFinanceRecordService supplierFinanceRecordService;
    @Mock
    SupplierPayRecordService supplierPayRecordService;
    @Mock
    SupplierModifyLimitRecordService supplierModifyLimitRecordService;
    @Mock
    SupplierReqUsePerDayService supplierReqUsePerDayService;

    /**
     * 测试创建供应商
     *
     * @throws Exception
     */
    @Test
    public void testCreate() throws Exception {
        assertFalse(supplierService.create(nullSupplier()));
        //assertFalse(supplierService.create(nullIsp()));
        assertFalse(supplierService.create(nullName()));
        assertFalse(supplierService.create(nullFingerPrint()));

        when(supplierMapper.create(any(Supplier.class)))
            .thenReturn(0).thenReturn(1);
        
        Mockito.when(supplierFinanceRecordService.insertSelective(Mockito.any(SupplierFinanceRecord.class)))
        .thenReturn(true);

        assertFalse(supplierService.create(validSupplier()));
        assertTrue(supplierService.create(validSupplier()));

    }

    /**
     * 删除供应商
     *
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {
        try {
            supplierService.delete(null);
        } catch (Exception e) {
            TestCase.assertTrue(e.getMessage().equals("删除供应商失败."));
        }

        when(supplierMapper.delete(anyLong())).thenReturn(1);
        when(supplierProductMapService.deleteBySupplierId(anyLong()))
            .thenReturn(true);
        when(supplierProductService.deleteBysupplierId(anyLong())).thenReturn(true);
        when(supplierFinanceRecordService.deleteBysupplierId(anyLong())).thenReturn(true);
        when(supplierPayRecordService.deleteBysupplierId(anyLong())).thenReturn(true);

        assertTrue(supplierService.delete(343L));
    }
    
    @Test(expected=RuntimeException.class)
    public void testDelete1(){
        assertTrue(supplierService.delete(null));
    }

    /**
     * 更新供应商信息
     *
     * @throws Exception
     */
    @Test
    public void testUpdate() throws Exception {
        assertFalse(supplierService.update(null, randStr(), IspType.CMCC));
        assertFalse(supplierService.update(343L, null, null));
        assertFalse(supplierService.update(343L, "", null));

        when(supplierMapper.update(anyLong(), anyString(), anyString()))
            .thenReturn(0).thenReturn(1);

        assertFalse(supplierService.update(43L, "fdksa", IspType.CMCC));
        assertTrue(supplierService.update(43L, "fdksa", IspType.CMCC));
        assertTrue(supplierService.update(43L, "", IspType.CMCC));
        assertTrue(supplierService.update(43L, null, IspType.CMCC));
        assertTrue(supplierService.update(43L, "fdasf", null));

        verify(supplierMapper, times(5)).update(anyLong(), anyString(), anyString());
    }

    /**
     * 测试获取供应商
     *
     * @throws Exception
     */
    @Test
    public void testGet() throws Exception {
        assertNull(supplierService.get(null));

        when(supplierMapper.get(anyLong())).thenReturn(null).thenReturn(new Supplier());
        assertNull(supplierService.get(343L));
        assertNotNull(supplierService.get(343L));
        verify(supplierMapper, times(2)).get(anyLong());
    }

    /**
     * 根据同步标识获取供应商列表
     *
     * @throws Exception
     */
    @Test
    public void testGetSupplierBySync() throws Exception {
        when(supplierMapper.getSupplierBySync(anyInt()))
            .thenReturn(null).thenReturn(new LinkedList<Supplier>());

        assertNull(supplierService.getSupplierBySync(43));
        assertNotNull(supplierService.getSupplierBySync(43));

        verify(supplierMapper, times(2)).getSupplierBySync(anyInt());
    }

    /**
     * 获取供应商的分表显示列表
     *
     * @throws Exception
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testQueryPaginationSupplier() throws Exception {
        when(supplierMapper.queryPaginationSupplier(anyMap()))
            .thenReturn(null).thenReturn(new LinkedList<Supplier>());

        assertNull(supplierService.queryPaginationSupplier(new QueryObject()));
        assertNotNull(supplierService.queryPaginationSupplier(new QueryObject()));

        verify(supplierMapper, times(2)).queryPaginationSupplier(anyMap());
    }

    /**
     * 获取符合条件的供应商个数
     *
     * @throws Exception
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testQueryPaginationSupplierCount() throws Exception {
        int result = 343;
        when(supplierMapper.queryPaginationSupplierCount(anyMap()))
            .thenReturn(0).thenReturn(result);

        assertEquals(supplierService.queryPaginationSupplierCount(new QueryObject()), 0);
        assertEquals(supplierService.queryPaginationSupplierCount(new QueryObject()), result);

        verify(supplierMapper, times(2)).queryPaginationSupplierCount(anyMap());
    }

    /**
     * 暂停供应商
     *
     * @throws Exception
     */
    @Test
    public void testSuspendSupplier() throws Exception {
        List<SupplierProduct> sps = buildSp();
        List<Product> products = buildProduct();

        when(supplierProductMapper.selectBySupplierId(anyLong()))
            .thenReturn(sps);
        when(supplierProductMapMapper.getBySpplierId(anyLong()))
            .thenReturn(products);

        when(supplierMapper.delete(anyLong())).thenReturn(0).thenReturn(1);
        when(supplierProductMapper.deleteBySupplierId(anyLong()))
            .thenReturn(sps.size() - 1).thenReturn(sps.size());
        when(supplierProductMapMapper.deleteBySupplierId(anyLong()))
            .thenReturn(products.size() - 1).thenReturn(products.size());

        try {
            supplierService.suspendSupplier(43L);
        } catch (Exception ignored) {
        }

        try {
            supplierService.suspendSupplier(43L);
        } catch (Exception ignored) {
        }

        try {
            supplierService.suspendSupplier(43L);
        } catch (Exception ignored) {
        }

        assertTrue(supplierService.suspendSupplier(43L));

        verify(supplierMapper, times(4)).delete(anyLong());
        verify(supplierProductMapper, times(3)).deleteBySupplierId(anyLong());
        verify(supplierProductMapMapper, times(2)).deleteBySupplierId(anyLong());
    }

    /**
     * 恢复供应商
     *
     * @throws Exception
     */
    @Test
    public void testRecoverSupplier() throws Exception {
        when(supplierMapper.recoverSupplier(anyLong()))
            .thenReturn(0).thenReturn(1);

        try {
            supplierService.recoverSupplier(343L);
        } catch (Exception ignore) {
        }

        assertTrue(supplierService.recoverSupplier(343L));

        verify(supplierMapper, times(2)).recoverSupplier(anyLong());
    }

    /**
     * 根据指纹信息获取供应商信息
     *
     * @throws Exception
     */
    @Test
    public void testGetByFingerPrint() throws Exception {
        when(supplierMapper.getByFingerPrint(anyString()))
            .thenReturn(null).thenReturn(new Supplier());

        assertNull(supplierService.getByFingerPrint("fd"));
        assertNotNull(supplierService.getByFingerPrint("fdasf"));

        verify(supplierMapper, times(2)).getByFingerPrint(anyString());
    }

    /**
     * 获取所有的供应商信息列表
     *
     * @throws Exception
     */
    @Test
    public void testGetAllSuppliers() throws Exception {
        when(supplierMapper.getAllSuppliers())
            .thenReturn(null).thenReturn(new LinkedList<Supplier>());

        assertNull(supplierService.getAllSuppliers());
        assertNotNull(supplierService.getAllSuppliers());

        verify(supplierMapper, times(2)).getAllSuppliers();
    }

    private Supplier validSupplier() {
        Supplier supplier = new Supplier();
        supplier.setIsp(IspType.CMCC.getValue());
        supplier.setName(randStr());
        supplier.setFingerprint(randStr());

        return supplier;
    }

    private Supplier nullSupplier() {
        return null;
    }

    private Supplier nullIsp() {
        Supplier supplier = validSupplier();
        supplier.setIsp(null);

        return supplier;
    }

    private Supplier nullName() {
        Supplier supplier = validSupplier();
        supplier.setName(null);

        return supplier;
    }

    private Supplier nullFingerPrint() {
        Supplier supplier = validSupplier();
        supplier.setFingerprint(null);

        return supplier;
    }

    private List<SupplierProduct> buildSp() {
        int size = new Random().nextInt(100) + 10;
        List<SupplierProduct> sps = new LinkedList<SupplierProduct>();
        for (int i = 0; i < size; i++) {
            sps.add(new SupplierProduct());
        }

        return sps;
    }

    private List<Product> buildProduct() {
        int size = new Random().nextInt(100) + 10;
        List<Product> sps = new LinkedList<Product>();
        for (int i = 0; i < size; i++) {
            sps.add(new Product());
        }

        return sps;
    }
    
    @Test
    public void testSelectByMap(){
        QueryObject queryObject = new QueryObject();
        Mockito.when(supplierMapper.selectByMap(Mockito.anyMap())).thenReturn(new ArrayList<Supplier>());
        assertNotNull(supplierService.selectByMap(queryObject.toMap()));
    }
    
    @Test
    public void testUpdateByPrimaryKeySelective(){
        assertFalse(supplierService.updateByPrimaryKeySelective(null));
        Mockito.when(supplierProductMapService.clearPriorSupplierBySupplierId(Mockito.anyLong())).thenReturn(1);
        
        Mockito.when(supplierMapper.updateByPrimaryKeySelective(Mockito.any(Supplier.class))).thenReturn(1);
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        supplier.setStatus(0);
        assertTrue(supplierService.updateByPrimaryKeySelective(supplier));
    }
    
    private Supplier createSupplier(){
        Supplier record = new Supplier();
        record.setId(1L);
        
        return record;
    }
    
    @Test
    public void testSetSupplierLimit(){
        Supplier history = createSupplier();
        history.setLimitMoneyFlag(1);
        Supplier supplier = createSupplier();
        supplier.setLimitMoneyFlag(0);
        Mockito.when(supplierMapper.get(Mockito.anyLong())).thenReturn(history);
        Mockito.when(supplierModifyLimitRecordService.insertSelective(Mockito.any(SupplierModifyLimitRecord.class))).thenReturn(false);
        assertFalse(supplierService.setSupplierLimit(supplier, 1L));
    }
    
    @Test(expected = RuntimeException.class)
    public void testSetSupplierLimit1(){
        Supplier history = createSupplier();
        history.setLimitMoneyFlag(0);
        Supplier supplier = createSupplier();
        supplier.setLimitMoneyFlag(1);
        Mockito.when(supplierMapper.get(Mockito.anyLong())).thenReturn(history);
        Mockito.when(supplierModifyLimitRecordService.insertSelective(Mockito.any(SupplierModifyLimitRecord.class))).thenReturn(true);
        Mockito.when(supplierReqUsePerDayService.insertSelective(Mockito.any(SupplierReqUsePerDay.class))).thenReturn(false);
        assertFalse(supplierService.setSupplierLimit(supplier, 1L));
    }
    
    @Test(expected = RuntimeException.class)
    public void testSetSupplierLimit2(){
        Supplier history = createSupplier();
        history.setLimitMoneyFlag(1);
        Supplier supplier = createSupplier();
        supplier.setLimitMoneyFlag(0);
        Mockito.when(supplierMapper.get(Mockito.anyLong())).thenReturn(history);
        Mockito.when(supplierModifyLimitRecordService.insertSelective(Mockito.any(SupplierModifyLimitRecord.class))).thenReturn(true);
        
        Mockito.when(supplierReqUsePerDayService.getTodayRecord(Mockito.anyLong())).thenReturn(createSupplierReqUsePerDay());
        Mockito.when(supplierReqUsePerDayService.deleteByPrimaryKey(Mockito.anyLong())).thenReturn(false);
        assertFalse(supplierService.setSupplierLimit(supplier, 1L));
    }
    
    @Test(expected = RuntimeException.class)
    public void testSetSupplierLimit3(){
        Supplier history = createSupplier();
        history.setLimitMoneyFlag(1);
        history.setLimitMoney(5D);
        Supplier supplier = createSupplier();
        supplier.setLimitMoneyFlag(1);
        supplier.setLimitMoney(10D);
        Mockito.when(supplierMapper.get(Mockito.anyLong())).thenReturn(history);
        Mockito.when(supplierModifyLimitRecordService.insertSelective(Mockito.any(SupplierModifyLimitRecord.class))).thenReturn(true);
        Mockito.when(supplierMapper.updateByPrimaryKeySelective(Mockito.any(Supplier.class))).thenReturn(0);
        assertFalse(supplierService.setSupplierLimit(supplier, 1L));
    }
    
    @Test
    public void testSetSupplierLimit4(){
        Supplier history = createSupplier();
        history.setLimitMoneyFlag(1);
        history.setLimitMoney(10D);
        Supplier supplier = createSupplier();
        supplier.setLimitMoneyFlag(1);
        supplier.setLimitMoney(10D);
        Mockito.when(supplierMapper.get(Mockito.anyLong())).thenReturn(history);
        assertTrue(supplierService.setSupplierLimit(supplier, 1L));
    }
    
    /**
     * 
     */
    @Test
    public void testSelectByName(){
        Mockito.when(supplierMapper.selectByName(Mockito.anyString())).thenReturn(new ArrayList<Supplier>());
        assertNotNull(supplierService.selectByName("test"));
    }
    
    private SupplierReqUsePerDay createSupplierReqUsePerDay(){
        SupplierReqUsePerDay supplierReqUsePerDay = new SupplierReqUsePerDay();
        supplierReqUsePerDay.setId(1L);
        return supplierReqUsePerDay;
    }
    
    /**
     * 
     */
    @Test
    public void testGetSupplierByPrdId() {
        Assert.assertNull(supplierService.getSupplierByPrdId(null));
        Mockito.when(supplierMapper.getSupplierByPrdId(Mockito.anyLong())).thenReturn(null);
        Assert.assertNull(supplierService.getSupplierByPrdId(1l));
    }
    /**
     * 
     */
    @Test
    public void testGetPriorSupplierByPrdid() {
        Assert.assertNull(supplierService.getPriorSupplierByPrdid(null));
        List<Supplier> suppliers = new ArrayList<Supplier>();
        Supplier supplier = new Supplier();
        suppliers.add(supplier);
        Mockito.when(supplierMapper.getPriorSupplierByPrdid(Mockito.anyLong())).thenReturn(null).thenReturn(new ArrayList<Supplier>()).thenReturn(suppliers);
        Assert.assertNull(supplierService.getPriorSupplierByPrdid(1l));
        Assert.assertNull(supplierService.getPriorSupplierByPrdid(1l));
        Assert.assertSame(supplier, supplierService.getPriorSupplierByPrdid(1l));
    }
}