/**
 *
 */
package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.SupplierProductMapMapper;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.model.SupplierProductMap;
import com.cmcc.vrp.province.service.SupplierProductMapService;

/**
 * <p>Title:SupplierProductMapServiceImplTest </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月4日
 */

@RunWith(MockitoJUnitRunner.class)
public class SupplierProductMapServiceImplTest {

    @InjectMocks
    SupplierProductMapService spmService = new SupplierProductMapServiceImpl();

    @Mock
    SupplierProductMapMapper supplierProductMapMapper;


    /**
     * 
     */
    @Test
    public void testCreate() {
        SupplierProductMap supplierProductMap = new SupplierProductMap();
        assertFalse(spmService.create(null));
        assertFalse(spmService.create(supplierProductMap));
        supplierProductMap.setPlatformProductId(1L);
        assertFalse(spmService.create(supplierProductMap));

        supplierProductMap.setSupplierProductId(1L);
        when(supplierProductMapMapper.create(supplierProductMap)).thenReturn(0).thenReturn(1);
        assertFalse(spmService.create(supplierProductMap));
        assertTrue(spmService.create(supplierProductMap));

        Mockito.verify(supplierProductMapMapper, Mockito.times(2)).create(supplierProductMap);
    }
    /**
     * 
     */
    @Test
    public void testCreate2() {
        Long pltfPid = 1L;
        Long splPid = 1L;

        assertFalse(spmService.create(null, null));
        assertFalse(spmService.create(pltfPid, null));
        when(supplierProductMapMapper.create(Mockito.any(SupplierProductMap.class))).thenReturn(0).thenReturn(1);
        assertFalse(spmService.create(pltfPid, splPid));
        assertTrue(spmService.create(pltfPid, splPid));

        Mockito.verify(supplierProductMapMapper, Mockito.times(2)).create(Mockito.any(SupplierProductMap.class));
    }
    /**
     * 
     */
    @Test
    public void testUpdateByPltfPid() {
        Long pltfPid = 1L;
        List<Long> splPids = new ArrayList();
        splPids.add(1L);
        splPids.add(2L);
        when(supplierProductMapMapper.deleteByPlftPid(pltfPid)).thenReturn(-1).thenReturn(1);
        try {
            spmService.updateByPltfPid(pltfPid, splPids);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(supplierProductMapMapper.batchInsert(Mockito.anyList())).thenReturn(1);
        assertTrue(spmService.updateByPltfPid(pltfPid, splPids));
        Mockito.verify(supplierProductMapMapper, Mockito.times(1)).batchInsert(Mockito.anyList());
    }
    /**
     * 
     */
    @Test
    public void testUpdateBySplPid() {

        Long splPid = 1L;
        List<Long> pltPids = new ArrayList();
        pltPids.add(1L);
        pltPids.add(2L);
        when(supplierProductMapMapper.deleteBySplPid(splPid)).thenReturn(-1).thenReturn(1);
        try {
            spmService.updateBySplPid(splPid, pltPids);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(supplierProductMapMapper.batchInsert(Mockito.anyList())).thenReturn(1);
        assertTrue(spmService.updateBySplPid(splPid, pltPids));
        Mockito.verify(supplierProductMapMapper, Mockito.times(1)).batchInsert(Mockito.anyList());

    }
    /**
     * 
     */
    @Test
    public void testDeleteByPlftPid() {
        Long plftPid = 1L;
        when(supplierProductMapMapper.deleteByPlftPid(plftPid)).thenReturn(-1).thenReturn(1);
        assertFalse(spmService.deleteByPlftPid(null));
        assertFalse(spmService.deleteByPlftPid(plftPid));
        assertTrue(spmService.deleteByPlftPid(plftPid));
        Mockito.verify(supplierProductMapMapper, Mockito.times(2)).deleteByPlftPid(plftPid);
    }
    /**
     * 
     */
    @Test
    public void testDelete() {
        Long plftPid = 1L;
        Long splPid = 1L;
        when(supplierProductMapMapper.deleteByPlftPidAndSplPid(plftPid, splPid)).thenReturn(-1).thenReturn(0);
        assertFalse(spmService.delete(null, null));
        assertFalse(spmService.delete(plftPid, null));
        assertFalse(spmService.delete(plftPid, splPid));
        assertTrue(spmService.delete(plftPid, splPid));
        Mockito.verify(supplierProductMapMapper, Mockito.times(2)).deleteByPlftPidAndSplPid(plftPid, splPid);
    }
    /**
     * 
     */
    @Test
    public void testDeleteBySplPid() {
        Long splPid = 1L;
        when(supplierProductMapMapper.deleteBySplPid(splPid)).thenReturn(-1).thenReturn(0);
        assertFalse(spmService.deleteBySplPid(null));
        assertFalse(spmService.deleteBySplPid(splPid));
        assertTrue(spmService.deleteBySplPid(splPid));
        Mockito.verify(supplierProductMapMapper, Mockito.times(2)).deleteBySplPid(splPid);
    }
    /**
     * 
     */
    @Test
    public void testDeleteBySupplierId() {
        Long supplierId = 1L;
        when(supplierProductMapMapper.deleteBySupplierId(supplierId)).thenReturn(-1).thenReturn(0);
        assertFalse(spmService.deleteBySupplierId(null));
        assertFalse(spmService.deleteBySupplierId(supplierId));
        assertTrue(spmService.deleteBySupplierId(supplierId));
        Mockito.verify(supplierProductMapMapper, Mockito.times(2)).deleteBySupplierId(supplierId);
    }
    /**
     * 
     */
    @Test
    public void testGetByPltfPid() {
        Long pltfPid = 1L;
        List<SupplierProduct> list = new ArrayList();
        when(supplierProductMapMapper.getByPltfPid(pltfPid)).thenReturn(list);
        assertNull(spmService.getByPltfPid(null));
        assertSame(list, spmService.getByPltfPid(pltfPid));
        Mockito.verify(supplierProductMapMapper, Mockito.times(1)).getByPltfPid(pltfPid);
    }
    /**
     * 
     */
    @Test
    public void testGetBySplPid() {
        Long splPid = 1L;
        List<Product> list = new ArrayList();
        when(supplierProductMapMapper.getBySplPid(splPid)).thenReturn(list);
        assertNull(spmService.getBySplPid(null));
        assertSame(list, spmService.getBySplPid(splPid));
        Mockito.verify(supplierProductMapMapper, Mockito.times(1)).getBySplPid(splPid);
    }
    /**
     * 
     */
    @Test
    public void testBatchInsertMap() {
        List<SupplierProductMap> records = new ArrayList();
        SupplierProductMap spm = new SupplierProductMap();
        records.add(spm);
        when(supplierProductMapMapper.batchInsert(records)).thenReturn(0).thenReturn(records.size());
        assertFalse(spmService.batchInsertMap(null));
        assertFalse(spmService.batchInsertMap(records));
        assertTrue(spmService.batchInsertMap(records));

        Mockito.verify(supplierProductMapMapper, Mockito.times(2)).batchInsert(records);

    }
    /**
     * 
     */   
    @Test
    public void testBatchUpdate(){
    	assertTrue(spmService.batchUpdate(null));

    	List<SupplierProductMap> list = new ArrayList<SupplierProductMap>();
    	SupplierProductMap map = new SupplierProductMap();
    	list.add(map);
    	when(supplierProductMapMapper.batchUpdate(list)).thenReturn(1);
    	assertTrue(spmService.batchUpdate(list));
    }
    /**
     * 
     */
    @Test
    public void testSelectBySplPidWithOutDeleteFlag(){
    	assertNull(spmService.selectBySplPidWithOutDeleteFlag(null));
    	
    	List<SupplierProductMap> list = new ArrayList<SupplierProductMap>();
    	
    	when(supplierProductMapMapper.selectBySplPidWithOutDeleteFlag(Mockito.anyLong())).thenReturn(list);
    	
    	assertSame(list, spmService.selectBySplPidWithOutDeleteFlag(Mockito.anyLong()));
    }
    /**
     * 
     */
    @Test
    public void testGetOnshelfByPltfPid() {
        Assert.assertNull(spmService.getOnshelfByPltfPid(null));
        Mockito.when(supplierProductMapMapper.getOnshelfByPltfPid(Mockito.anyLong()))
            .thenReturn(null);
        Assert.assertNull(spmService.getOnshelfByPltfPid(1l));
    }
    /**
     * 
     */
    @Test
    public void testGetBySplPidWithoutDel() {
        Assert.assertNull(spmService.getBySplPidWithoutDel(null));
        Assert.assertNull(spmService.getBySplPidWithoutDel(-1l));
        Mockito.when(supplierProductMapMapper.getBySplPidWithoutDel(Mockito.anyLong()))
            .thenReturn(null);
        Assert.assertNull(spmService.getBySplPidWithoutDel(1l));
    }
    /**
     * 
     */
    @Test
    public void testUpdatePriorSupplier() {
        Assert.assertFalse(spmService.updatePriorSupplier(null, null));
        Assert.assertFalse(spmService.updatePriorSupplier(1l, null));
        Mockito.when(supplierProductMapMapper.clearPriorSupplier(Mockito.anyLong())).thenReturn(0).thenReturn(1);
        Assert.assertFalse(spmService.updatePriorSupplier(1l, 1l));
        Assert.assertTrue(spmService.updatePriorSupplier(0l, 0l));
        Mockito.when(supplierProductMapMapper.updatePriorSupplier(Mockito.anyLong(), Mockito.anyLong())).thenReturn(0).thenReturn(1);
        Assert.assertFalse(spmService.updatePriorSupplier(1l, 1l));
        Assert.assertTrue(spmService.updatePriorSupplier(1l, 1l));
    }
    /**
     * 
     */
    @Test
    public void testClearPriorSupplier() {
        Assert.assertFalse(spmService.clearPriorSupplier(null));
    }
    /**
     * 
     */
    @Test
    public void testGetBypltfpidAndSplpid() {
        Assert.assertNull(spmService.getBypltfpidAndSplpid(null, null));
        Assert.assertNull(spmService.getBypltfpidAndSplpid(1l, null));
        Mockito.when(supplierProductMapMapper.getBypltfpidAndSplpid(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);
        Assert.assertNull(spmService.getBypltfpidAndSplpid(1l, 1L));
    }
}
