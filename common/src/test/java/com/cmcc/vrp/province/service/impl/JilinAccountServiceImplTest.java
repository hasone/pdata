package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import com.cmcc.vrp.boss.jilin.JlBossServiceImpl;
import com.cmcc.vrp.province.dao.AccountMapper;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.AccountRecord;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.AccountRecordService;
import com.cmcc.vrp.province.service.EnterprisesService;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年4月20日 下午1:46:16
*/
@RunWith(MockitoJUnitRunner.class)
public class JilinAccountServiceImplTest {
    @InjectMocks
    AccountServiceImpl aService = new JiLinAccountServiceImpl();
    
    @Mock
    EnterprisesService enterprisesService;
    
    @Mock
    ApplicationContext applicationContext;
    
    @Mock
    AccountMapper accountMapper;
    
    @Mock
    JlBossServiceImpl bossService;
    
    @Mock
    AccountRecordService accountRecordService;
    
    /**
     * 
     */
    @Test
    public void testSyncFromBoss() {
        Long entId = 1L;
        Long prdId = 1L;
        
        Enterprise enterprise = new Enterprise();
        
        when(enterprisesService.selectByPrimaryKey(entId)).thenReturn(null).thenReturn(enterprise);
        assertSame("企业不存在", aService.syncFromBoss(entId, prdId).getResult());
        when(applicationContext.getBean("jilinBossService", JlBossServiceImpl.class)).thenReturn(null).thenReturn(bossService);
        assertSame("无效的BOSS渠道", aService.syncFromBoss(entId, prdId).getResult());
        
        Account platAccount = new Account();
        platAccount.setCount(100.0);
        platAccount.setOwnerId(1L);
        platAccount.setEnterId(1L);
        platAccount.setProductId(1L);
        when(accountMapper.selectCurrencyAccount(entId)).thenReturn(null).thenReturn(platAccount);
        assertSame("平台现金账户不存在", aService.syncFromBoss(entId, prdId).getResult());
        Account bossAccount = new Account();
        when(bossService.owePayQry(Mockito.anyLong())).thenReturn(null).thenReturn("100");
        assertSame("查询boss资金账户失败", aService.syncFromBoss(entId, prdId).getResult());
        bossAccount.setCount(100.0);
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        assertEquals("同步boss资金账户成功，平台侧资金余额为："+ nf.format((bossAccount.getCount() / 100.0)) + " 元", aService.syncFromBoss(entId, prdId).getResult());
        
        when(bossService.owePayQry(Mockito.anyLong())).thenReturn("50");
        bossAccount.setCount(50.0);
        when(accountMapper.forceUpdateCount(Mockito.anyLong(), Mockito.anyDouble())).thenReturn(0).thenReturn(1);
        when(accountRecordService.create(Mockito.any(AccountRecord.class))).thenReturn(false).thenReturn(true);
        assertEquals("同步boss资金账户失败,BOSS侧资金余额为："+ nf.format((bossAccount.getCount() / 100.0)) + " 元", aService.syncFromBoss(entId, prdId).getResult());
        assertEquals("同步boss资金账户失败,BOSS侧资金余额为："+ nf.format((bossAccount.getCount() / 100.0)) + " 元", aService.syncFromBoss(entId, prdId).getResult());
        assertEquals("同步boss资金账户成功，平台侧资金余额为："+ nf.format((bossAccount.getCount() / 100.0)) + " 元", aService.syncFromBoss(entId, prdId).getResult());
        when(bossService.owePayQry(Mockito.anyLong())).thenReturn("150");
        bossAccount.setCount(150.0);
        assertEquals("同步boss资金账户成功，平台侧资金余额为："+ nf.format((bossAccount.getCount() / 100.0)) + " 元", aService.syncFromBoss(entId, prdId).getResult());
    }
}
