package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.dao.TmpaccountMapper;
import com.cmcc.vrp.province.dao.WxAdministerMapper;
import com.cmcc.vrp.province.model.ChangeMobileRecord;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.WxAdminister;
import com.cmcc.vrp.province.service.ChangeMobileRecordService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.WxAdministerService;
import com.cmcc.vrp.wx.model.Tmpaccount;

@RunWith(MockitoJUnitRunner.class)
public class WxAdministerServiceImplTest {
    @InjectMocks
    WxAdministerService wxAdministerService = new WxAdministerServiceImpl();
    @Mock
    WxAdministerMapper wxAdministerMapper;
    @Mock
    ChangeMobileRecordService changeMobileRecordService;
    @Mock
    IndividualProductService individualProductService;
    @Mock
    IndividualAccountService individualAccountService;
    @Mock
    TmpaccountMapper tmpaccountMapper;
    
    private WxAdminister createWxAdminister(){
        WxAdminister record = new WxAdminister();
        record.setId(1L);
        record.setMobilePhone("18867103717");
        return record;
    }
    
    @Test
    public void testSelectWxAdministerById(){
        assertNull(wxAdministerService.selectWxAdministerById(null));
        
        Mockito.when(wxAdministerMapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(createWxAdminister());
        assertNotNull(wxAdministerMapper.deleteByPrimaryKey(1L));
    }
    
    @Test
    public void testUpdateSelective(){
        WxAdminister record = createWxAdminister();
        record.setId(null);
        assertFalse(wxAdministerService.updateSelective(record));
        Mockito.when(wxAdministerMapper.updateByPrimaryKeySelective(Mockito.any(WxAdminister.class))).thenReturn(1);
        record.setId(1L);
        assertTrue(wxAdministerService.updateSelective(record));
    }
    
    @Test
    public void testSelectByMobilePhone(){
        assertNull(wxAdministerService.selectByMobilePhone("123"));
        Mockito.when(wxAdministerMapper.selectByMobilePhone(Mockito.anyString())).thenReturn(createWxAdminister());
        assertNotNull(wxAdministerService.selectByMobilePhone("18867103717"));
    }
    
    @Test
    public void testUpdateWxAdminster(){
        String oldMobile = "18867103717"; 
        String newMobile = "13597092654";
        
        assertFalse(wxAdministerService.updateWxAdminster(oldMobile, ""));
        
        Mockito.when(wxAdministerMapper.selectByMobilePhone(Mockito.anyString())).thenReturn(createWxAdminister());
        Mockito.when(wxAdministerMapper.updateByPrimaryKeySelective(Mockito.any(WxAdminister.class))).thenReturn(0).thenReturn(1);
        
        assertFalse(wxAdministerService.updateWxAdminster(oldMobile, newMobile));
        Mockito.when(changeMobileRecordService.insertSelective(Mockito.any(ChangeMobileRecord.class))).thenReturn(true);
        assertTrue(wxAdministerService.updateWxAdminster(oldMobile, newMobile));
    }
    
    @Test(expected = RuntimeException.class)
    public void testUpdateWxAdminster1(){
        String oldMobile = "18867103717"; 
        String newMobile = "13597092654";
        Mockito.when(wxAdministerMapper.selectByMobilePhone(Mockito.anyString())).thenReturn(createWxAdminister());
        Mockito.when(wxAdministerMapper.updateByPrimaryKeySelective(Mockito.any(WxAdminister.class))).thenReturn(1);
        Mockito.when(changeMobileRecordService.insertSelective(Mockito.any(ChangeMobileRecord.class))).thenReturn(false);
        assertFalse(wxAdministerService.updateWxAdminster(oldMobile, newMobile));
    }
    
    private List<IndividualProduct> create(){
        List<IndividualProduct> list = new ArrayList<IndividualProduct>();
        IndividualProduct record = new IndividualProduct();
        record.setId(1L);
        list.add(record);
        return list;
    }
    
    private List<Tmpaccount> createTmpaccountList(){
        List<Tmpaccount> list = new ArrayList<Tmpaccount>();
        Tmpaccount item = new Tmpaccount();
        item.setOpenid("test");
        item.setCount(BigDecimal.valueOf(10l));
        list.add(item);
        return list;
    }
    
    @Test(expected = RuntimeException.class)
    public void testInsertForWx(){
        String mobile = "18867103717";
        String openid = "test";
        Mockito.when(wxAdministerMapper.insertSelective(Mockito.any(WxAdminister.class))).thenReturn(0);
        assertFalse(wxAdministerService.insertForWx(mobile, openid));
    }
    
    @Test(expected = RuntimeException.class)
    public void testInsertForWx1(){
        String mobile = "18867103717";
        String openid = "test";
        Mockito.when(wxAdministerMapper.insertSelective(Mockito.any(WxAdminister.class))).thenReturn(1);
        Mockito.when(individualProductService.selectByDefaultValue(Mockito.anyInt())).thenReturn(create());
        
        Mockito.when(individualAccountService.batchInsert(Mockito.anyList())).thenReturn(false);
        assertFalse(wxAdministerService.insertForWx(mobile, openid));
    }
    
    private IndividualProduct createIndividualProduct(){
        IndividualProduct product = new IndividualProduct();
        product.setId(1L);
        return product;
    }
    
    private IndividualAccount createIndividualAccount(){
        IndividualAccount account = new IndividualAccount();
        account.setId(1L);
        return account;
    }
    
    @Test(expected = RuntimeException.class)
    public void testInsertForWx2(){
        String mobile = "18867103717";
        String openid = "test";
        Mockito.when(wxAdministerMapper.insertSelective(Mockito.any(WxAdminister.class))).thenReturn(1);
        Mockito.when(individualProductService.selectByDefaultValue(Mockito.anyInt())).thenReturn(create());
        
        Mockito.when(individualAccountService.batchInsert(Mockito.anyList())).thenReturn(true);
        Mockito.when(tmpaccountMapper.selectByOpenid(Mockito.anyString())).thenReturn(createTmpaccountList());
        
        Mockito.when(individualProductService.getIndivialPointProduct()).thenReturn(createIndividualProduct());
        Mockito.when(individualAccountService.getAccountByOwnerIdAndProductId(Mockito.anyLong(), 
                Mockito.anyLong(), Mockito.anyInt())).thenReturn(createIndividualAccount());
        
        Mockito.when(individualAccountService.changeAccount(Mockito.any(IndividualAccount.class), 
                Mockito.any(BigDecimal.class), Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), 
                Mockito.anyInt(), Mockito.anyInt())).thenReturn(false);
        
        assertFalse(wxAdministerService.insertForWx(mobile, openid));
    }
    
    @Test
    public void testInsertForWx3(){
        String mobile = "18867103717";
        String openid = "test";
        Mockito.when(wxAdministerMapper.insertSelective(Mockito.any(WxAdminister.class))).thenReturn(1);
        Mockito.when(individualProductService.selectByDefaultValue(Mockito.anyInt())).thenReturn(create());
        
        Mockito.when(individualAccountService.batchInsert(Mockito.anyList())).thenReturn(true);
        Mockito.when(tmpaccountMapper.selectByOpenid(Mockito.anyString())).thenReturn(createTmpaccountList());
        
        Mockito.when(individualProductService.getIndivialPointProduct()).thenReturn(createIndividualProduct());
        Mockito.when(individualAccountService.getAccountByOwnerIdAndProductId(Mockito.anyLong(), 
                Mockito.anyLong(), Mockito.anyInt())).thenReturn(createIndividualAccount());
        
        Mockito.when(individualAccountService.changeAccount(Mockito.any(IndividualAccount.class), 
                Mockito.any(BigDecimal.class), Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), 
                Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
        
        assertTrue(wxAdministerService.insertForWx(mobile, openid));
    }
    
    
    
    

}
