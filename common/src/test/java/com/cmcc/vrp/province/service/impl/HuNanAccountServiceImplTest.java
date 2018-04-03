/**
 * 
 */
package com.cmcc.vrp.province.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import com.cmcc.vrp.boss.hunan.HNBossServcieImpl;
import com.cmcc.vrp.province.dao.AccountMapper;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.AccountRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.AccountRecordService;
import com.cmcc.vrp.province.service.EnterprisesService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;
/**
 * <p>Title:HuNanAccountServiceImplTest </p>
 * <p>Description: </p>
 * @author xujue
 * @date 2016年11月8日
 */
@RunWith(MockitoJUnitRunner.class)
public class HuNanAccountServiceImplTest {

    @InjectMocks
    AccountServiceImpl aService = new HuNanAccountServiceImpl();
    
    @Mock
    EnterprisesService enterprisesService;
    
    @Mock
    ApplicationContext applicationContext;
    
    @Mock
    AccountMapper accountMapper;
    
    @Mock
    HNBossServcieImpl bossService;
    
    @Mock
    AccountRecordService accountRecordService;
    
    @Test
    public void testSyncFromBoss() {
	Long entId = 1L;
	Long prdId = 1L;
	
	Enterprise enterprise = new Enterprise();
	
	when(enterprisesService.selectByPrimaryKey(entId)).thenReturn(null).thenReturn(enterprise);
	assertSame("企业不存在", aService.syncFromBoss(entId, prdId).getResult());
	
//	HNBossServcieImpl bossService = new HNBossServcieImpl();
	when(applicationContext.getBean("huNanBossService", HNBossServcieImpl.class)).thenReturn(null).thenReturn(bossService);
	assertSame("无效的BOSS渠道", aService.syncFromBoss(entId, prdId).getResult());
	
	Account platAccount = new Account();
	platAccount.setCount(100.0);
	platAccount.setOwnerId(1L);
	platAccount.setEnterId(1L);
	platAccount.setProductId(1L);
	when(accountMapper.selectCurrencyAccount(entId)).thenReturn(null).thenReturn(platAccount);
	assertSame("平台现金账户不存在", aService.syncFromBoss(entId, prdId).getResult());
	
	Account bossAccount = new Account();
	when(bossService.queryAccountByEntId(Mockito.anyLong(), Mockito.anyLong(), 
		Mockito.anyString())).thenReturn(null).thenReturn(bossAccount);
	assertSame("查询boss资金账户失败", aService.syncFromBoss(entId, prdId).getResult());
	
	bossAccount.setCount(100.0);
	java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setGroupingUsed(false);
	assertEquals("同步boss资金账户成功，平台侧资金余额为："+ nf.format((bossAccount.getCount() / 100.0)) + " 元", aService.syncFromBoss(entId, prdId).getResult());
	
	bossAccount.setCount(50.0);
	when(accountMapper.forceUpdateCount(Mockito.anyLong(), Mockito.anyDouble())).thenReturn(0).thenReturn(1);
	when(accountRecordService.create(Mockito.any(AccountRecord.class))).thenReturn(false).thenReturn(true);
	assertEquals("同步boss资金账户失败,BOSS侧资金余额为："+ nf.format((bossAccount.getCount() / 100.0)) + " 元", aService.syncFromBoss(entId, prdId).getResult());
	assertEquals("同步boss资金账户失败,BOSS侧资金余额为："+ nf.format((bossAccount.getCount() / 100.0)) + " 元", aService.syncFromBoss(entId, prdId).getResult());
	assertEquals("同步boss资金账户成功，平台侧资金余额为："+ nf.format((bossAccount.getCount() / 100.0)) + " 元", aService.syncFromBoss(entId, prdId).getResult());
	
	bossAccount.setCount(150.0);
	assertEquals("同步boss资金账户成功，平台侧资金余额为："+ nf.format((bossAccount.getCount() / 100.0)) + " 元", aService.syncFromBoss(entId, prdId).getResult());
    }
    
    @Test
    public void testSyncFromBoss1() {
    Long entId = 1L;
    Long prdId = 1L;
    
    Enterprise enterprise = new Enterprise();
    
    when(enterprisesService.selectByPrimaryKey(entId)).thenReturn(null).thenReturn(enterprise);
    assertSame("企业不存在", aService.syncFromBoss(entId, prdId).getResult());
    
//  HNBossServcieImpl bossService = new HNBossServcieImpl();
    when(applicationContext.getBean("huNanBossService", HNBossServcieImpl.class)).thenReturn(null).thenReturn(bossService);
    assertSame("无效的BOSS渠道", aService.syncFromBoss(entId, prdId).getResult());
    
    Account platAccount = new Account();
    platAccount.setCount(100.0);
    platAccount.setOwnerId(1L);
    platAccount.setEnterId(1L);
    platAccount.setProductId(1L);
    when(accountMapper.selectCurrencyAccount(entId)).thenReturn(platAccount);

    
    Account bossAccount = new Account();
    when(bossService.queryAccountByEntId(Mockito.anyLong(), Mockito.anyLong(), 
        Mockito.anyString())).thenReturn(bossAccount);

    java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
    nf.setGroupingUsed(false);
    bossAccount.setCount(150.0);
    when(accountMapper.forceUpdateCount(Mockito.anyLong(), Mockito.anyDouble())).thenReturn(0);
    when(accountRecordService.create(Mockito.any(AccountRecord.class))).thenReturn(false);
    assertEquals("同步boss资金账户失败,BOSS侧资金余额为："+ nf.format((bossAccount.getCount() / 100.0)) + " 元", aService.syncFromBoss(entId, prdId).getResult());
    }
    
}
