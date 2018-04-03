package com.cmcc.vrp.province.service.impl;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.MobileBlackListMapper;
import com.cmcc.vrp.province.model.MobileBlackList;

/**
 * @author lgk8023
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class MobileBlackListServiceImplTest {
    @InjectMocks
    MobileBlackListServiceImpl mobileBlackListServiceImpl = new MobileBlackListServiceImpl();

    @Mock
    MobileBlackListMapper mobileBlackListMapper;
    
    /**
     * 
     */
    @Test
    public void testInsert() {
        Assert.assertFalse(mobileBlackListServiceImpl.insert(null));
        Mockito.when(mobileBlackListMapper.insert(Mockito.any(MobileBlackList.class)))
            .thenReturn(1).thenReturn(0);
        Assert.assertTrue(mobileBlackListServiceImpl.insert(new MobileBlackList()));
        Assert.assertFalse(mobileBlackListServiceImpl.insert(new MobileBlackList()));
    }
    /**
     * 
     */
    @Test
    public void testDeleteById() {
        Assert.assertFalse(mobileBlackListServiceImpl.deleteById(null));
        Mockito.when(mobileBlackListMapper.deleteById(Mockito.any(Long.class)))
            .thenReturn(1).thenReturn(0);
        Assert.assertTrue(mobileBlackListServiceImpl.deleteById(1l));
        Assert.assertFalse(mobileBlackListServiceImpl.deleteById(1l));
    }
    /**
     * 
     */
    @Test
    public void testGetByMobile() {
        Mockito.when(mobileBlackListMapper.getByMobile(Mockito.any(String.class)))
            .thenReturn(null).thenReturn(new ArrayList<MobileBlackList>());
        Assert.assertNull(mobileBlackListServiceImpl.getByMobile("18867100000"));
        Assert.assertNotNull(mobileBlackListServiceImpl.getByMobile("18867100000"));
    }
    /**
     * 
     */
    @Test
    public void testGetByMobileAndType() {
        Mockito.when(mobileBlackListMapper.getByMobileAndType(Mockito.any(String.class), Mockito.any(String.class)))
        .thenReturn(null).thenReturn(new MobileBlackList());
        Assert.assertNull(mobileBlackListServiceImpl.getByMobileAndType("18867100000", "1"));
        Assert.assertNotNull(mobileBlackListServiceImpl.getByMobileAndType("18867100000", "1"));
    }
}
