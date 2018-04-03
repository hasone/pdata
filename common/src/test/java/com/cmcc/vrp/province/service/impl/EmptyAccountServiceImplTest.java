package com.cmcc.vrp.province.service.impl;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.boss.SyncAccountResult;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.province.dao.AccountMapper;
import com.cmcc.vrp.province.model.AccountPrizeMap;
import com.cmcc.vrp.province.service.AccountRecordService;
import com.cmcc.vrp.province.service.AccountTransactionService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.ProductService;

/**
 * Created by sunyiwei on 2016/10/26.
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class EmptyAccountServiceImplTest {
    @InjectMocks
    EmptyAccountServiceImpl emptyAccountService = new EmptyAccountServiceImpl();

    @Mock
    AccountMapper accountMapper;

    @Mock
    EnterprisesService enterprisesService;

    @Mock
    AccountRecordService accountRecordService;

    @Mock
    ProductService productService;

    @Mock
    EntProductService entProductService;

    @Mock
    AccountTransactionService accountTransactionService;

    /**
     * 扣减账户余额
     *
     * @throws Exception
     */
    @Test
    public void testAddCount() throws Exception {
        assertTrue(emptyAccountService.addCount(null, 34L, AccountType.ENTERPRISE, 433D, "dfas", "fdas"));
    }

    /**
     * 扣减账户余额
     *
     * @throws Exception
     */
    @Test
    public void testMinusCount() throws Exception {
        assertTrue(emptyAccountService.minusCount(null, 34L, AccountType.ENTERPRISE, 433D, "dfas", "fdas"));
        assertTrue(emptyAccountService.minusCount(34L, null, AccountType.ENTERPRISE, 433D, "dfas", "fdas"));
        assertTrue(emptyAccountService.minusCount(34L, 34L, null, 433D, "dfas", "fdas"));
        assertTrue(emptyAccountService.minusCount(34L, 34L, AccountType.ENTERPRISE, null, "dfas", "fdas"));
        assertTrue(emptyAccountService.minusCount(34L, 34L, AccountType.ENTERPRISE, 433D, null, "fdas"));
        assertTrue(emptyAccountService.minusCount(34L, 34L, AccountType.ENTERPRISE, 433D, "dfas", null));
        assertTrue(emptyAccountService.minusCount(34L, 34L, AccountType.ENTERPRISE, 433D, "dfas", "dfsd"));
    }


    /**
     * 退钱！
     *
     * @throws Exception
     */
    @Test
    public void testReturnFunds() throws Exception {
        assertTrue(emptyAccountService.returnFunds(null));
        assertTrue(emptyAccountService.returnFunds("fda"));
        assertTrue(emptyAccountService.returnFunds(""));
    }

    /**
     * 退钱
     *
     * @throws Exception
     */
    @Test
    public void testReturnFunds2() throws Exception {
        assertTrue(emptyAccountService.returnFunds(null, null, null, null));
    }

    /**
     * 判断是否负债
     *
     * @throws Exception
     */
    @Test
    public void testIsDebt2Account() throws Exception {
        assertFalse(emptyAccountService.isDebt2Account(null, 343L));
        assertFalse(emptyAccountService.isDebt2Account(new LinkedList<AccountPrizeMap>(), null));
        assertFalse(emptyAccountService.isDebt2Account(new LinkedList<AccountPrizeMap>(), 343L));
    }

    /**
     * 根据企业ID和产品ID校验账户信息
     *
     * @throws Exception
     */
    @Test
    public void testCheckAccountByEntIdAndProductId() throws Exception {
        assertTrue(emptyAccountService.checkAccountByEntIdAndProductId(null, 343L));
        assertTrue(emptyAccountService.checkAccountByEntIdAndProductId(343L, null));
        assertTrue(emptyAccountService.checkAccountByEntIdAndProductId(343L, 343L));
    }

    /**
     * 从boss侧同步账户信息
     *
     * @throws Exception
     */
    @Test
    public void testSyncFromBoss() throws Exception {
        assertTrue(SyncAccountResult.SUCCESS == emptyAccountService.syncFromBoss(null, 4333L));
        assertTrue(SyncAccountResult.SUCCESS == emptyAccountService.syncFromBoss(434L, null));
        assertTrue(SyncAccountResult.SUCCESS == emptyAccountService.syncFromBoss(434L, 4333L));
    }
    
    @Test
    public void testCheckAlertStopValue(){
        assertNull(emptyAccountService.checkAlertStopValue(1L, 1.0));
    }
    
    @Test
    public void testIsEmptyAccount(){
        assertTrue(emptyAccountService.isEmptyAccount());
    }
    
    @Test
    public void testIsEnough2Debt(){
        assertTrue(emptyAccountService.isEnough2Debt(null, null));
    }
    
    @Test
    public void testIsEnoughInAccount(){
        assertTrue(emptyAccountService.isEnoughInAccount(1L, 1L));
    }
    @Test
    public void testIsEnoughInAccount2(){
        assertTrue(emptyAccountService.isEnoughInAccount(new ArrayList(), 1L));
    }
    
    @Test
    public void testMinusCount1(){
        assertTrue(emptyAccountService.minusCount( null, null, null, null, null, null));
    }
    @Test
    public void testMinusCount2(){
        Assert.assertNotNull(emptyAccountService.minusCount( null, null, null, null, null, null,true));
    }
    
    @Test
    public void testRecoverAlert(){
    }
    
    @Test
    public void testUpdateAlertSelective(){
        assertFalse(emptyAccountService.updateAlertSelective(null));
    }
    
    @Test
    public void testVerifyAccountForRendomPacket(){
        assertTrue(emptyAccountService.verifyAccountForRendomPacket(null, null, null));
    }
    @Test
    public void testrecoverAlert(){
        emptyAccountService.recoverAlert(null, null);
    }
}