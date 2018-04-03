package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.AdminChangeDetailMapper;
import com.cmcc.vrp.province.model.AdminChangeDetail;
import com.cmcc.vrp.util.QueryObject;

/**
 * 
 * AdminChangeDetailServiceImplTest
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class AdminChangeDetailServiceImplTest {
    @InjectMocks
    AdminChangeDetailServiceImpl adminChangeDetailService = new AdminChangeDetailServiceImpl();
    @Mock
    AdminChangeDetailMapper adminChangeDetailMapper;
    
    /**
     * testInsert()
     */
    @Test
    public void testInsert() {
        AdminChangeDetail adminChangeDetail = initAdminChangeDetail();
        Mockito.when(adminChangeDetailMapper.insertSelective(adminChangeDetail)).thenReturn(1);
        assertTrue(adminChangeDetailService.insert(adminChangeDetail));
        
        Mockito.when(adminChangeDetailMapper.insertSelective(adminChangeDetail)).thenReturn(0);
        assertFalse(adminChangeDetailService.insert(adminChangeDetail));
        
    }
    
    /**
     * testGetByRequestId
     */
    @Test
    public void testGetByRequestId() {
        List<AdminChangeDetail> list = initList();
        
        Mockito.when(adminChangeDetailMapper.getByRequestId(Mockito.anyLong())).thenReturn(list);
        Assert.assertNotNull(adminChangeDetailService.getByRequestId(1L));
        
        Mockito.when(adminChangeDetailMapper.getByRequestId(Mockito.anyLong())).
            thenReturn(new ArrayList<AdminChangeDetail>());
        Assert.assertNull(adminChangeDetailService.getByRequestId(1L));   
    }
    
    /**
     * testGetDetailByRequestId
     */
    @Test
    public void testGetDetailByRequestId() {
        List<AdminChangeDetail> list = initList();
        
        Mockito.when(adminChangeDetailMapper.getDetailByRequestId(Mockito.anyLong())).thenReturn(list);
        Assert.assertNotNull(adminChangeDetailService.getDetailByRequestId(1L));
        
        Mockito.when(adminChangeDetailMapper.getDetailByRequestId(Mockito.anyLong())).
            thenReturn(new ArrayList<AdminChangeDetail>());
        Assert.assertNull(adminChangeDetailService.getDetailByRequestId(1L));   
    }
    
    /**
     * testGetVerifyingCountByMobile
     */
    @Test
    public void testGetVerifyingCountByMobile() {
        Mockito.when(adminChangeDetailMapper.getVerifyingCountByMobile(Mockito.anyString())).thenReturn(0);
        Assert.assertEquals(adminChangeDetailService.getVerifyingCountByMobile(""), 0);
        
    }
    
    /**
     * testQueryPaginationAdminCount
     */
    @Test
    public void testQueryPaginationAdminCount() {
        Mockito.when(adminChangeDetailMapper.getNoVirifyListPageCount(Mockito.anyMap())).thenReturn(1);
        Mockito.when(adminChangeDetailMapper.getVirifyListPageCount(Mockito.anyMap())).thenReturn(1);
        Assert.assertEquals(1, adminChangeDetailService.queryPaginationAdminCount(new QueryObject(),true));
        Assert.assertEquals(1, adminChangeDetailService.queryPaginationAdminCount(new QueryObject(),false));
    }
    
    /**
     * testQueryPaginationAdminList
     */
    @Test
    public void testQueryPaginationAdminList() {
        Mockito.when(adminChangeDetailMapper.getNoVirifyListPage(Mockito.anyMap())).thenReturn(null);
        Mockito.when(adminChangeDetailMapper.getVirifyListPage(Mockito.anyMap())).thenReturn(null);
        Assert.assertEquals(null, adminChangeDetailService.queryPaginationAdminList(new QueryObject(),true));
        Assert.assertEquals(null, adminChangeDetailService.queryPaginationAdminList(new QueryObject(),false));
    }
    
    /**
     * testGetVerifyingCount
     */
    @Test
    public void testGetVerifyingCount() {
        Mockito.when(adminChangeDetailMapper.getVerifyingCount(Mockito.anyLong())).thenReturn(0);
        Assert.assertEquals(adminChangeDetailService.getVerifyingCount(1L), 0);
        
    }
    
    /**
     * initAdminChangeDetail()
     */
    private AdminChangeDetail initAdminChangeDetail(){
        AdminChangeDetail adminChangeDetail = new AdminChangeDetail();
        adminChangeDetail.setId(1L);
        return adminChangeDetail;
    }
    
    /**
     * initList()
     */
    private List<AdminChangeDetail> initList(){
        AdminChangeDetail adminChangeDetail = new AdminChangeDetail();
        adminChangeDetail.setId(1L);
        List<AdminChangeDetail> list = new ArrayList<AdminChangeDetail>();
        list.add(adminChangeDetail);
        return list;
    }
   
}
