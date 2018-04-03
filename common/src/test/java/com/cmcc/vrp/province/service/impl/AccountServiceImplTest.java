package com.cmcc.vrp.province.service.impl;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.cmcc.vrp.boss.SyncAccountResult;
import com.cmcc.vrp.ec.bean.Constants;
import com.cmcc.vrp.ec.bean.Constants.ProductType;
import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.enums.MinusCountReturnType;
import com.cmcc.vrp.province.dao.AccountMapper;
import com.cmcc.vrp.province.model.Account;
import com.cmcc.vrp.province.model.AccountPrizeMap;
import com.cmcc.vrp.province.model.AccountRecord;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.EntFlowControl;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.PresentSerialNum;
import com.cmcc.vrp.province.model.PrizeInfo;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.AccountRecordService;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.AccountTransactionService;
import com.cmcc.vrp.province.service.AdminManagerService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EntFlowControlService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.PresentSerialNumService;
import com.cmcc.vrp.province.service.ProductConverterService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.util.Constants.FLOW_ACCOUNT_FLAG;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * 账户实现的UT <p> Created by sunyiwei on 2016/6/12.
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {
    private final String HASH_KEY = "amountUpper";
    private final String HASH_KEY2 = "amountNow";
    private final String HASH_KEY3 = "amountAddition";
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
    @Mock
    ChargeRecordService chargeRecordService;
    @Mock
    PresentSerialNumService presentSerialNumSerivice;
    @Mock
    AdminManagerService adminManagerService;
    @Mock
    GlobalConfigService globalConfigService;
    @Mock
    EntFlowControlService entFlowControlService;
    @Mock
    Jedis jedis;
    @Mock
    JedisPool jedisPool;
    @Mock
    TaskProducer taskProducer;
    @Mock
    ProductConverterService converterService;
    @InjectMocks
    private AccountServiceImpl accountService = new AccountServiceImpl();

    /**
     * initMocks
     */
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.ACCOUNT_ALERT_MSG.getKey())).thenReturn("OK");
    }

    /**
     * 创建企业账户UT1： enterpriseService的分支测试
     */
    @Test
    public void testCreateEnterAccount() throws Exception {
        //校验参数
        assertFalse(accountService.createEnterAccount(null, new LinkedHashMap<Long, Double>(), "1234"));
        assertFalse(accountService.createEnterAccount(342L, null, "1234"));
        assertFalse(accountService.createEnterAccount(342L, new LinkedHashMap<Long, Double>(), "1234"));
        assertFalse(accountService.createEnterAccount(343L, new LinkedHashMap<Long, Double>(), null));

        final Map<Long, Double> infos = buildInfos();

        when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(null).thenReturn(new Enterprise());
        when(accountMapper.getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt())).thenAnswer(
                new MultiAnswer<Account>(buildValidAnswers(infos.size())));

        when(accountMapper.batchInsert(anyListOf(Account.class))).thenReturn(infos.size());
        when(accountRecordService.batchInsert(anyListOf(AccountRecord.class))).thenReturn(true);

        assertFalse(accountService.createEnterAccount(343L, infos, "dsf22"));
        assertTrue(accountService.createEnterAccount(343L, infos, "433443"));

        verify(enterprisesService, times(2)).selectByPrimaryKey(anyLong());
        verify(accountMapper, times(2 * infos.size())).getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt());
        verify(accountMapper).batchInsert(anyListOf(Account.class));
        verify(accountRecordService).batchInsert(anyListOf(AccountRecord.class));
    }

    /**
     * 创建企业账户UT2： accountMapper分支测试 case 1: accountMapper校验账户存在时不通过
     */
    @Test
    public void testCreateEnterAccount2() throws Exception {
        final Map<Long, Double> infos = buildInfos();

        when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(new Enterprise());

        int randomPos = randomPos(infos.size());
        when(accountMapper.getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt())).thenAnswer(
                new MultiAnswer<Account>(buildInvalidAnswers(infos.size(), randomPos)));

        when(accountMapper.batchInsert(anyListOf(Account.class))).thenReturn(infos.size());
        when(accountRecordService.batchInsert(anyListOf(AccountRecord.class))).thenReturn(true);

        assertFalse(accountService.createEnterAccount(343L, infos, "dsf22"));

        verify(enterprisesService, times(1)).selectByPrimaryKey(anyLong());
        verify(accountMapper, times(randomPos + 1)).getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt());
        verify(accountMapper, never()).batchInsert(anyListOf(Account.class));
        verify(accountRecordService, never()).batchInsert(anyListOf(AccountRecord.class));
    }

    /**
     * 创建企业账户UT3： accountMapper分支测试 case 2: 正常情况
     */
    @Test
    public void testCreateEnterAccount3() throws Exception {
        final Map<Long, Double> infos = buildInfos();

        when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(new Enterprise());

        when(accountMapper.getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt())).thenAnswer(
                new MultiAnswer<Account>(buildValidAnswers(infos.size())));

        when(accountMapper.batchInsert(anyListOf(Account.class))).thenReturn(infos.size());
        when(accountRecordService.batchInsert(anyListOf(AccountRecord.class))).thenReturn(true);

        assertTrue(accountService.createEnterAccount(343L, infos, "dsf22"));

        verify(enterprisesService, times(1)).selectByPrimaryKey(anyLong());
        verify(accountMapper, times(infos.size() * 2)).getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt());
        verify(accountMapper, times(1)).batchInsert(anyListOf(Account.class));
        verify(accountRecordService, times(1)).batchInsert(anyListOf(AccountRecord.class));
    }

    /**
     * 创建企业账户UT4： accountMapper批量插入分支
     */
    @Test(expected = RuntimeException.class)
    public void testCreateEnterAccount4() throws Exception {
        final Map<Long, Double> infos = buildInfos();

        when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(new Enterprise());

        when(accountMapper.getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt())).thenAnswer(
                new MultiAnswer<Account>(buildValidAnswersTwice(infos.size())));

        when(accountMapper.batchInsert(anyListOf(Account.class))).thenReturn(0).thenReturn(infos.size());
        when(accountRecordService.batchInsert(anyListOf(AccountRecord.class))).thenReturn(true);

        accountService.createEnterAccount(343L, infos, "dsf22");
        assertTrue(accountService.createEnterAccount(343L, infos, "dsf22"));

        verify(enterprisesService, times(2)).selectByPrimaryKey(anyLong());
        verify(accountMapper, times(infos.size() * 4)).getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt());
        verify(accountMapper, times(2)).batchInsert(anyListOf(Account.class));
        verify(accountRecordService, times(1)).batchInsert(anyListOf(AccountRecord.class));
    }

    /**
     * 创建企业账户UT5： accountRecordService批量插入分支
     */
    @Test(expected = RuntimeException.class)
    public void testCreateEnterAccount5() throws Exception {
        final Map<Long, Double> infos = buildInfos();

        when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(new Enterprise());

        when(accountMapper.getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt())).thenAnswer(
                new MultiAnswer<Account>(buildValidAnswersTwice(infos.size())));

        when(accountMapper.batchInsert(anyListOf(Account.class))).thenReturn(infos.size());
        when(accountRecordService.batchInsert(anyListOf(AccountRecord.class))).thenReturn(false).thenReturn(true);

        assertFalse(accountService.createEnterAccount(343L, infos, "dsf22"));
        assertTrue(accountService.createEnterAccount(343L, infos, "dsf22"));

        verify(enterprisesService, times(2)).selectByPrimaryKey(anyLong());
        verify(accountMapper, times(infos.size() * 4)).getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt());
        verify(accountMapper, times(2)).batchInsert(anyListOf(Account.class));
        verify(accountRecordService, times(2)).batchInsert(anyListOf(AccountRecord.class));
    }

    /**
     * 创建活动账户
     */
    @Test
    public void testCreateActivityAccount() throws Exception {
        assertFalse(accountService.createActivityAccount(null, AccountType.ENTERPRISE, 343L,
                new LinkedHashMap<Long, Double>(), "fdsa"));
        assertFalse(accountService.createActivityAccount(342L, null, 343L, new LinkedHashMap<Long, Double>(), "fdsa"));
        assertFalse(accountService.createActivityAccount(342L, AccountType.ENTERPRISE, null,
                new LinkedHashMap<Long, Double>(), "fdsa"));
        assertFalse(accountService.createActivityAccount(342L, AccountType.ENTERPRISE, 343L, null, "fdsa"));

        AccountService accountServiceSpy = spy(accountService);
        Map<Long, Double> map = buildInfos();
        int size = map.size();

        when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(null).thenReturn(new Enterprise());
        when(accountMapper.getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt())).thenReturn(null);
        when(accountMapper.batchInsert(anyListOf(Account.class))).thenReturn(map.size());
        doReturn(true).when(accountServiceSpy).addCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(),
                anyString(), anyString());
        doReturn(true).when(accountServiceSpy).minusCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(),
                anyString(), anyString());

        assertFalse(accountServiceSpy.createActivityAccount(342L, AccountType.ENTERPRISE, 343L, map, "fdsa"));
        assertTrue(accountServiceSpy.createActivityAccount(342L, AccountType.ENTERPRISE, 343L, map, "fdsa"));

        verify(enterprisesService, times(2)).selectByPrimaryKey(anyLong());
        verify(accountMapper, times(size)).getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt());
        verify(accountMapper, times(1)).batchInsert(anyListOf(Account.class));
        verify(accountServiceSpy, times(size - 1)).addCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(),
                anyString(), anyString());
        verify(accountServiceSpy, times(size - 1)).minusCount(anyLong(), anyLong(), any(AccountType.class),
                anyDouble(), anyString(), anyString());
    }

    /**
     * 创建活动账户
     */
    @Test
    public void testCreateActivityAccount2() throws Exception {
        AccountService accountServiceSpy = spy(accountService);
        Map<Long, Double> map = buildInfos();

        when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(new Enterprise());
        when(accountMapper.getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt())).thenReturn(null).thenReturn(
                new Account());
        when(accountMapper.batchInsert(anyListOf(Account.class))).thenReturn(map.size());
        doReturn(true).when(accountServiceSpy).addCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(),
                anyString(), anyString());
        doReturn(true).when(accountServiceSpy).minusCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(),
                anyString(), anyString());

        assertFalse(accountServiceSpy.createActivityAccount(342L, AccountType.ENTERPRISE, 343L, map, "fdsa"));

        verify(enterprisesService, times(1)).selectByPrimaryKey(anyLong());
        verify(accountMapper, times(2)).getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt());
        verify(accountMapper, never()).batchInsert(anyListOf(Account.class));
        verify(accountServiceSpy, never()).addCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(),
                anyString(), anyString());
        verify(accountServiceSpy, never()).minusCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(),
                anyString(), anyString());
    }

    /**
     * 创建活动账户
     */
    @Test(expected = RuntimeException.class)
    public void testCreateActivityAccount3() throws Exception {
        AccountService accountServiceSpy = spy(accountService);
        Map<Long, Double> map = buildInfos();
        int size = map.size();

        when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(new Enterprise());
        when(accountMapper.getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt())).thenReturn(null);
        when(accountMapper.batchInsert(anyListOf(Account.class))).thenReturn(0).thenReturn(map.size());
        doReturn(true).when(accountServiceSpy).addCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(),
                anyString(), anyString());
        doReturn(true).when(accountServiceSpy).minusCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(),
                anyString(), anyString());

        assertFalse(accountServiceSpy.createActivityAccount(342L, AccountType.ENTERPRISE, 343L, map, "fdsa"));
        assertTrue(accountServiceSpy.createActivityAccount(342L, AccountType.ENTERPRISE, 343L, map, "fdsa"));

        verify(enterprisesService, times(2)).selectByPrimaryKey(anyLong());
        verify(accountMapper, times(2 * size)).getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt());
        verify(accountMapper, times(2)).batchInsert(anyListOf(Account.class));
        verify(accountServiceSpy, times(size)).addCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(),
                anyString(), anyString());
        verify(accountServiceSpy, times(size)).minusCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(),
                anyString(), anyString());
    }

    /**
     * 创建活动账户
     */
    @Test(expected = RuntimeException.class)
    public void testCreateActivityAccount4() throws Exception {
        AccountService accountServiceSpy = spy(accountService);
        Map<Long, Double> map = buildInfos();
        int size = map.size();

        when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(new Enterprise());
        when(accountMapper.getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt())).thenReturn(null);
        when(accountMapper.batchInsert(anyListOf(Account.class))).thenReturn(map.size());
        doReturn(false).doReturn(true).when(accountServiceSpy)
                .addCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(), anyString(), anyString());
        doReturn(false).doReturn(true).when(accountServiceSpy)
                .minusCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(), anyString(), anyString());

        accountServiceSpy.createActivityAccount(342L, AccountType.ENTERPRISE, 343L, map, "fdsa");
        accountServiceSpy.createActivityAccount(342L, AccountType.ENTERPRISE, 343L, map, "fdsa");
        assertTrue(accountServiceSpy.createActivityAccount(342L, AccountType.ENTERPRISE, 343L, map, "fdsa"));

        verify(enterprisesService, times(3)).selectByPrimaryKey(anyLong());
        verify(accountMapper, times(3 * size)).getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt());
        verify(accountMapper, times(3)).batchInsert(anyListOf(Account.class));
        verify(accountServiceSpy, times(size + 2)).addCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(),
                anyString(), anyString());
        verify(accountServiceSpy, times(size + 1)).minusCount(anyLong(), anyLong(), any(AccountType.class),
                anyDouble(), anyString(), anyString());
    }

    /**
     * 测试增加账户余额
     */
    @Test
    public void testAddCount() throws Exception {
        assertTrue(accountService.addCount(null, 343L, AccountType.ENTERPRISE, null, "DFAS", "fdf"));
        assertFalse(accountService.addCount(null, 343L, AccountType.ENTERPRISE, 343d, "DFAS", "fdf"));
        assertFalse(accountService.addCount(343L, null, AccountType.ENTERPRISE, 343d, "DFAS", "fdf"));
        //assertFalse(accountService.addCount(343L, 343L, AccountType.ENTERPRISE, null, "DFAS", "fdf"));
        assertFalse(accountService.addCount(343L, 343L, AccountType.ENTERPRISE, -343D, "DFAS", "fdf"));
        assertFalse(accountService.addCount(343L, null, AccountType.ENTERPRISE, 343d, null, "fdf"));

        when(accountMapper.getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt())).thenReturn(null).thenReturn(null);
        assertFalse(accountService.addCount(343L, 343L, AccountType.ENTERPRISE, 433D, "DFAS", "fdf"));

        when(accountMapper.getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt())).thenReturn(null).thenReturn(
                buildAccount());
        when(accountMapper.updateCount(anyLong(), anyDouble())).thenReturn(1);
        when(accountRecordService.create(any(AccountRecord.class))).thenReturn(true);

        assertFalse(accountService.addCount(343L, 343L, AccountType.ENTERPRISE, 433D, "DFAS", "fdf"));
        //assertFalse(accountService.addCount(343L, 343L, AccountType.ENTERPRISE, 433D, "DFAS", "fdf"));

        //        verify(accountMapper, times(2)).getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt());
        //        verify(accountMapper, times(1)).updateCount(anyLong(), anyDouble());
        //        verify(accountRecordService, times(1)).create(any(AccountRecord.class));
    }

    /**
     * 测试增加账户余额
     */
    @Test(expected = RuntimeException.class)
    public void testAddCount2() throws Exception {
        when(accountMapper.getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt())).thenReturn(buildAccount());
        when(accountMapper.updateCount(anyLong(), anyDouble())).thenReturn(0).thenReturn(1);
        when(accountRecordService.create(any(AccountRecord.class))).thenReturn(true);

        accountService.addCount(343L, 343L, AccountType.ENTERPRISE, 433D, "DFAS", "fdf");
        assertTrue(accountService.addCount(343L, 343L, AccountType.ENTERPRISE, 433D, "DFAS", "fdf"));

        verify(accountMapper, times(2)).getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt());
        verify(accountMapper, times(2)).updateCount(anyLong(), anyDouble());
        verify(accountRecordService, times(1)).create(any(AccountRecord.class));
    }

    /**
     * 测试增加账户余额
     */
    @Test(expected = RuntimeException.class)
    public void testAddCount3() throws Exception {
        when(accountMapper.getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt())).thenReturn(buildAccount());
        when(accountMapper.updateCount(anyLong(), anyDouble())).thenReturn(1);
        when(accountRecordService.create(any(AccountRecord.class))).thenReturn(false).thenReturn(true);

        accountService.addCount(343L, 343L, AccountType.ENTERPRISE, 433D, "DFAS", "fdf");
        assertTrue(accountService.addCount(343L, 343L, AccountType.ENTERPRISE, 433D, "DFAS", "fdf"));

        verify(accountMapper, times(2)).getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt());
        verify(accountMapper, times(2)).updateCount(anyLong(), anyDouble());
        verify(accountRecordService, times(2)).create(any(AccountRecord.class));
    }

    /**
     * testNewMinusCount1 测试产品账户足够  && 参数错误
     */
    @Test
    public void testNewMinusCount1() throws Exception {
        Long ownerId = 343L;

        Product dstPrd = new Product();//流量包产品
        dstPrd.setId(3L);
        dstPrd.setType(2);
        dstPrd.setFlowAccountFlag(FLOW_ACCOUNT_FLAG.REAL_PRODUCT.getCode());
        dstPrd.setFlowAccountProductId(3L);

        Account account = new Account();
        account.setId(3L);
        account.setCount(1.0d);

        Double delta = 1.0d;

        when(productService.get(dstPrd.getId())).thenReturn(dstPrd);
        when(accountMapper.getByOwnerIdAndPrdId(ownerId, dstPrd.getId(), AccountType.ENTERPRISE.getValue()))
                .thenReturn(account);
        when(accountMapper.updateCount(Mockito.anyLong(), Mockito.anyDouble())).thenReturn(1);
        when(accountRecordService.create(Mockito.any(AccountRecord.class))).thenReturn(true);

        assertTrue(accountService.minusCount(ownerId, dstPrd.getId(), AccountType.ENTERPRISE, delta, "111", "111"));

        //测试参数错误
        delta = -1.0d;
        assertFalse(accountService.minusCount(ownerId, dstPrd.getId(), AccountType.ENTERPRISE, delta, "111", "111"));
        assertFalse(accountService.minusCount(ownerId, dstPrd.getId(), AccountType.ENTERPRISE, delta, "", ""));
        assertFalse(accountService.minusCount(ownerId, dstPrd.getId(), null, delta, "111", ""));
        assertFalse(accountService.minusCount(ownerId, dstPrd.getId(), null, null, "111", ""));
        when(productService.get(dstPrd.getId())).thenReturn(null);
        assertFalse(accountService.minusCount(ownerId, dstPrd.getId(), null, null, "111", ""));
        assertFalse(accountService.minusCount(ownerId, null, null, null, "", ""));
        assertFalse(accountService.minusCount(null, null, null, null, "", ""));
    }

    /**
     * testNewMinusCount2 测试现金产品账户不够，禁止流量池，扣现金账户
     */
    @Test
    public void testNewMinusCount2() throws Exception {
        AccountServiceImpl accountServiceImpl = Mockito.spy(accountService);
        Long ownerId = 343L;

        Product dstPrd = new Product();//流量包产品
        dstPrd.setId(3L);
        dstPrd.setType(3);
        dstPrd.setFlowAccountFlag(2);//非转化

        Account account = new Account();
        account.setId(3L);
        account.setCount(1.0d);

        Double delta = 2.0d; //1<2

        List<Product> availPrdList = new LinkedList<Product>();
        Product currencyPrd = new Product();
        currencyPrd.setId(111L);
        currencyPrd.setType(0);

        Product flowPrd = new Product();
        flowPrd.setId(222L);
        flowPrd.setType(1);

        availPrdList.add(flowPrd);
        availPrdList.add(currencyPrd);

        when(productService.get(dstPrd.getId())).thenReturn(dstPrd);
        when(accountMapper.getByOwnerIdAndPrdId(ownerId, dstPrd.getId(), AccountType.ENTERPRISE.getValue()))
                .thenReturn(account);
        when(converterService.isInterdictConvert(flowPrd.getId(), dstPrd.getId())).thenReturn(true);
        when(converterService.isInterdictConvert(currencyPrd.getId(), dstPrd.getId())).thenReturn(false);
        when(productService.getPrdsByType(Mockito.anyLong(), Mockito.anyListOf(Integer.class), Mockito.anyLong()))
                .thenReturn(availPrdList);

        doReturn(MinusCountReturnType.OK).when(accountServiceImpl).tryMinusCurrencyAccount(Mockito.any(Product.class),
                Mockito.anyLong(), Mockito.anyLong(), Mockito.any(AccountType.class), Mockito.anyDouble(),
                Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean());
        assertTrue(accountServiceImpl.minusCount(ownerId, dstPrd.getId(), AccountType.ENTERPRISE, delta, "111", "111"));

        //扣减失败
        doReturn(MinusCountReturnType.ACCOUNT_NOTEXIST).when(accountServiceImpl).tryMinusCurrencyAccount(
                Mockito.any(Product.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(AccountType.class),
                Mockito.anyDouble(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean());
        assertFalse(accountServiceImpl.minusCount(ownerId, dstPrd.getId(), AccountType.ENTERPRISE, delta, "111", "111"));
    }

    /**
     * testNewMinusCount3 测试现金产品账户不够，允许流量池，现金，池先成功后失败
     */
    @Test
    public void testNewMinusCount3() throws Exception {
        AccountServiceImpl accountServiceImpl = Mockito.spy(accountService);
        Long ownerId = 343L;

        Product dstPrd = new Product();//流量包产品
        dstPrd.setId(3L);
        dstPrd.setType(3);
        dstPrd.setFlowAccountFlag(2);//非转化

        Account account = new Account();
        account.setId(3L);
        account.setCount(1.0d);

        Double delta = 2.0d; //1<2

        List<Product> availPrdList = new LinkedList<Product>();
        Product currencyPrd = new Product();
        currencyPrd.setId(111L);
        currencyPrd.setType(0);

        Product flowPrd = new Product();
        flowPrd.setId(222L);
        flowPrd.setType(1);

        availPrdList.add(flowPrd);
        availPrdList.add(currencyPrd);

        when(productService.get(dstPrd.getId())).thenReturn(dstPrd);
        when(accountMapper.getByOwnerIdAndPrdId(ownerId, dstPrd.getId(), AccountType.ENTERPRISE.getValue()))
                .thenReturn(account);
        when(converterService.isInterdictConvert(flowPrd.getId(), dstPrd.getId())).thenReturn(false);
        when(converterService.isInterdictConvert(currencyPrd.getId(), dstPrd.getId())).thenReturn(false);
        when(productService.getPrdsByType(Mockito.anyLong(), Mockito.anyListOf(Integer.class), Mockito.anyLong()))
                .thenReturn(availPrdList);

        doReturn(MinusCountReturnType.OK).when(accountServiceImpl).tryMinusFlowAccount(Mockito.any(Product.class),
                Mockito.anyLong(), Mockito.anyLong(), Mockito.any(AccountType.class), Mockito.anyDouble(),
                Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean());
        doReturn(MinusCountReturnType.OK).when(accountServiceImpl).tryMinusCurrencyAccount(Mockito.any(Product.class),
                Mockito.anyLong(), Mockito.anyLong(), Mockito.any(AccountType.class), Mockito.anyDouble(),
                Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean());
        assertTrue(accountServiceImpl.minusCount(ownerId, dstPrd.getId(), AccountType.ENTERPRISE, delta, "111", "111"));

        //池失败,扣钱成功
        doReturn(MinusCountReturnType.ACCOUNT_NOTEXIST).when(accountServiceImpl).tryMinusFlowAccount(
                Mockito.any(Product.class), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(AccountType.class),
                Mockito.anyDouble(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean());
        assertTrue(accountServiceImpl.minusCount(ownerId, dstPrd.getId(), AccountType.ENTERPRISE, delta, "111", "111"));
    }

    /**
     * 获取账户对象
     */
    @Test
    public void testGet() throws Exception {
        assertNull(accountService.get(null, 343L, AccountType.ENTERPRISE.getValue()));
        assertNull(accountService.get(343L, null, AccountType.ENTERPRISE.getValue()));
        assertNull(accountService.get(343L, 343L, null));

        when(accountMapper.getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt())).thenReturn(null).thenReturn(
                new Account());

        assertNull(accountService.get(43L, 343L, AccountType.ENTERPRISE.getValue()));
        assertNotNull(accountService.get(43L, 343L, AccountType.ENTERPRISE.getValue()));

        verify(accountMapper, times(2)).getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt());
    }

    /**
     * 根据企业ID获取账户列表
     */
    @Test
    public void testGetByEntId() throws Exception {
        assertNull(accountService.getByEntId(null));

        when(accountMapper.getByOwner(anyLong(), anyInt())).thenReturn(null).thenReturn(new LinkedList<Account>());

        assertNull(accountService.getByEntId(43L));
        assertNotNull(accountService.getByEntId(43L));

        verify(accountMapper, times(2)).getByOwner(anyLong(), anyInt());
    }

    /**
     * 根据企业ID和产品ID获取账户信息UT
     */
    @Test
    public void testGetByEntIdAndProId() throws Exception {
        assertNull(accountService.getByEntIdAndProId(null, new LinkedList<PrizeInfo>()));
        assertNull(accountService.getByEntIdAndProId(4334L, new LinkedList<PrizeInfo>()));
        assertNull(accountService.getByEntIdAndProId(3443L, null));

        List<PrizeInfo> prizeInfos = new LinkedList<PrizeInfo>();
        prizeInfos.add(new PrizeInfo());

        when(accountMapper.selectByEntIdAndProIds(anyLong(), anyListOf(PrizeInfo.class))).thenReturn(null).thenReturn(
                new LinkedList<Account>());

        assertNull(accountService.getByEntIdAndProId(43L, prizeInfos));
        assertNotNull(accountService.getByEntIdAndProId(43L, prizeInfos));

        verify(accountMapper, times(2)).selectByEntIdAndProIds(anyLong(), anyListOf(PrizeInfo.class));
    }

    /**
     * 统计活动冻结的账户总额
     */
    @Test
    public void testSumEntActivitiesFrozenCount() throws Exception {
        assertTrue(accountService.sumEntActivitiesFrozenCount(null) == 0d);

        double returnValue = 433d;
        when(accountMapper.sumActivitiesFrozenAccount(anyLong())).thenReturn(null).thenReturn(returnValue);

        assertTrue(accountService.sumEntActivitiesFrozenCount(243324L) == 0d);
        assertTrue(accountService.sumEntActivitiesFrozenCount(243324L) == returnValue);

        verify(accountMapper, times(2)).sumActivitiesFrozenAccount(anyLong());
    }

    /**
     * 退钱ut
     */
    @Test
    public void testReturnFunds() throws Exception {
        assertFalse(accountService.returnFunds(null, ActivityType.INTERFACE, 343L, 1));
        assertFalse(accountService.returnFunds("dfasdf", null, 343L, 1));
        assertFalse(accountService.returnFunds("dfsdfa", ActivityType.INTERFACE, null, 1));
    }

    /**
     * 测试退款流程2
     */
    @Test
    public void testReturnFunds2() throws Exception {
        when(accountRecordService.getOutgoingRecordByPltSn(anyString())).thenReturn(nullAccountId())
                .thenReturn(nullAccountId()).thenReturn(validAccountRecord());
        when(accountMapper.getById(anyLong())).thenReturn(validAccount());
        when(productService.get(anyLong())).thenReturn(validProduct(Constants.ProductType.CURRENCY)).thenReturn(
                validProduct(Constants.ProductType.FLOW_PACKAGE));
        when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(validEntProduct());
        AccountService spy = spy(accountService);
        doReturn(true).when(spy).addCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(), anyString(),
                anyString());

        assertFalse(spy.returnFunds("fdsfa", ActivityType.INTERFACE, 343L, 1));
        assertFalse(spy.returnFunds("fdsfa", ActivityType.INTERFACE, 3434L, 1));
        assertTrue(spy.returnFunds("fdsfa", ActivityType.INTERFACE, 343L, 1));

        verify(accountRecordService, times(3)).getOutgoingRecordByPltSn(anyString());
        verify(accountMapper, times(1)).getById(anyLong());
        verify(productService, times(2)).get(anyLong());
        verify(entProductService, times(1)).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
        verify(spy, times(1)).addCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(), anyString(),
                anyString());
    }

    /**
     * 测试退款流程3
     */
    @Test
    public void testReturnFunds3() throws Exception {
        when(accountRecordService.getOutgoingRecordByPltSn(anyString())).thenReturn(validAccountRecord());
        when(accountMapper.getById(anyLong())).thenReturn(null).thenReturn(validAccount());
        when(productService.get(anyLong())).thenReturn(validProduct(Constants.ProductType.CURRENCY)).thenReturn(
                validProduct(Constants.ProductType.FLOW_PACKAGE));
        when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(validEntProduct());
        AccountService spy = spy(accountService);
        doReturn(true).when(spy).addCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(), anyString(),
                anyString());

        assertFalse(spy.returnFunds("fdsfa", ActivityType.INTERFACE, 343L, 1));
        assertTrue(spy.returnFunds("fdsfa", ActivityType.INTERFACE, 343L, 1));

        verify(accountRecordService, times(2)).getOutgoingRecordByPltSn(anyString());
        verify(accountMapper, times(2)).getById(anyLong());
        verify(productService, times(2)).get(anyLong());
        verify(entProductService, times(1)).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
        verify(spy, times(1)).addCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(), anyString(),
                anyString());
    }

    /**
     * 测试退款流程4
     */
    @Test
    public void testReturnFunds4() throws Exception {
        when(accountRecordService.getOutgoingRecordByPltSn(anyString())).thenReturn(validAccountRecord());
        when(accountMapper.getById(anyLong())).thenReturn(validAccount());
        when(productService.get(anyLong())).thenReturn(validProduct(Constants.ProductType.CURRENCY))
                .thenReturn(validProduct(Constants.ProductType.FLOW_PACKAGE))
                .thenReturn(validProduct(Constants.ProductType.FLOW_PACKAGE))
                .thenReturn(validProduct(Constants.ProductType.FLOW_PACKAGE))
                .thenReturn(validProduct(Constants.ProductType.FLOW_ACCOUNT))
                .thenReturn(validProduct(Constants.ProductType.FLOW_PACKAGE));
        when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(validEntProduct());
        AccountService spy = spy(accountService);
        doReturn(true).when(spy).addCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(), anyString(),
                anyString());

        assertTrue(spy.returnFunds("fdsfa", ActivityType.INTERFACE, 343L, 1));
        assertTrue(spy.returnFunds("fdsfa", ActivityType.INTERFACE, 343L, 1));
        assertTrue(spy.returnFunds("fdsfa", ActivityType.INTERFACE, 343L, 1));

        verify(accountRecordService, times(3)).getOutgoingRecordByPltSn(anyString());
        verify(accountMapper, times(3)).getById(anyLong());
        verify(productService, times(3 * 2)).get(anyLong());
        verify(entProductService, times(1)).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
        verify(spy, times(3)).addCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(), anyString(),
                anyString());
    }

    /**
     * 测试退款流程5：entProduct返回空时不退钱
     */
    @Test
    public void testReturnFunds5() throws Exception {
        when(accountRecordService.getOutgoingRecordByPltSn(anyString())).thenReturn(validAccountRecord());
        when(accountMapper.getById(anyLong())).thenReturn(validAccount());
        when(productService.get(anyLong())).thenReturn(validProduct(Constants.ProductType.CURRENCY))
                .thenReturn(validProduct(Constants.ProductType.FLOW_PACKAGE))
                .thenReturn(validProduct(Constants.ProductType.CURRENCY))
                .thenReturn(validProduct(Constants.ProductType.FLOW_PACKAGE));
        when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(null).thenReturn(
                validEntProduct());
        AccountService spy = spy(accountService);
        doReturn(true).when(spy).addCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(), anyString(),
                anyString());

        assertTrue(spy.returnFunds("fdsfa", ActivityType.INTERFACE, 343L, 1));
        assertTrue(spy.returnFunds("fdsfa", ActivityType.INTERFACE, 343L, 1));

        verify(accountRecordService, times(2)).getOutgoingRecordByPltSn(anyString());
        verify(accountMapper, times(2)).getById(anyLong());
        verify(productService, times(2 * 2)).get(anyLong());
        verify(entProductService, times(2)).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
        verify(spy, times(2)).addCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(), anyString(),
                anyString());
    }

    /**
     * 测试退款流程6：增加余额时返回失败
     */
    @Test
    public void testReturnFunds6() throws Exception {
        when(accountRecordService.getOutgoingRecordByPltSn(anyString())).thenReturn(validAccountRecord());
        when(accountMapper.getById(anyLong())).thenReturn(validAccount());
        when(productService.get(anyLong())).thenReturn(validProduct(Constants.ProductType.CURRENCY))
                .thenReturn(validProduct(Constants.ProductType.FLOW_PACKAGE))
                .thenReturn(validProduct(Constants.ProductType.CURRENCY))
                .thenReturn(validProduct(Constants.ProductType.FLOW_PACKAGE));
        when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(validEntProduct());
        AccountService spy = spy(accountService);
        doReturn(false).doReturn(true).when(spy)
                .addCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(), anyString(), anyString());

        assertFalse(spy.returnFunds("fdsfa", ActivityType.INTERFACE, 343L, 1));
        assertTrue(spy.returnFunds("fdsfa", ActivityType.INTERFACE, 343L, 1));

        verify(accountRecordService, times(2)).getOutgoingRecordByPltSn(anyString());
        verify(accountMapper, times(2)).getById(anyLong());
        verify(productService, times(2 * 2)).get(anyLong());
        verify(entProductService, times(2)).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
        verify(spy, times(2)).addCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(), anyString(),
                anyString());
    }

    /**
     * 测试退款流程7：批量赠送时要更换流水号
     */
    @Test
    public void testReturnFunds7() throws Exception {
        when(presentSerialNumSerivice.selectByPltSn(anyString())).thenReturn(null).thenReturn(validPresentSerialNum());
        when(accountRecordService.getOutgoingRecordByPltSn(anyString())).thenReturn(validAccountRecord());
        when(accountMapper.getById(anyLong())).thenReturn(validAccount());
        when(productService.get(anyLong())).thenReturn(validProduct(Constants.ProductType.CURRENCY)).thenReturn(
                validProduct(Constants.ProductType.FLOW_PACKAGE));
        when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(validEntProduct());
        AccountService spy = spy(accountService);
        doReturn(true).when(spy).addCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(), anyString(),
                anyString());

        assertFalse(spy.returnFunds("fdsfa", ActivityType.GIVE, 343L, 1));
        assertTrue(spy.returnFunds("fdsfa", ActivityType.GIVE, 343L, 1));

        verify(presentSerialNumSerivice, times(2)).selectByPltSn(anyString());
        verify(accountRecordService, times(1)).getOutgoingRecordByPltSn(anyString());
        verify(accountMapper, times(1)).getById(anyLong());
        verify(productService, times(2 * 1)).get(anyLong());
        verify(entProductService, times(1)).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
        verify(spy, times(1)).addCount(anyLong(), anyLong(), any(AccountType.class), anyDouble(), anyString(),
                anyString());
    }

    /**
     * 测试根据充值流水号自动获取充值记录，并进行退款
     */
    @Test
    public void testReturnFunds8() throws Exception {
        assertFalse(accountService.returnFunds(null));
        assertFalse(accountService.returnFunds(""));

        AccountService spy = spy(accountService);

        when(chargeRecordService.getRecordBySN(anyString())).thenReturn(null).thenReturn(
                validChargeRecord(ChargeRecordStatus.COMPLETE));
        doReturn(true).when(spy).returnFunds(anyString(), any(ActivityType.class), anyLong(), anyInt());

        assertFalse(spy.returnFunds("fdaf"));
        assertTrue(spy.returnFunds("fdaf"));

        verify(chargeRecordService, times(2)).getRecordBySN(anyString());
        verify(spy, times(1)).returnFunds(anyString(), any(ActivityType.class), anyLong(), anyInt());
    }

    /**
     * 测试根据充值流水号自动获取充值记录，并进行退款
     */
    @Test
    public void testReturnFunds9() throws Exception {
        AccountService spy = spy(accountService);

        when(chargeRecordService.getRecordBySN(anyString())).thenReturn(validChargeRecord(ChargeRecordStatus.FAILED))
                .thenReturn(validChargeRecord(ChargeRecordStatus.COMPLETE));
        doReturn(false).doReturn(true).when(spy).returnFunds(anyString(), any(ActivityType.class), anyLong(), anyInt());

        assertFalse(spy.returnFunds("fdaf"));
        assertFalse(spy.returnFunds("fdaf"));
        assertTrue(spy.returnFunds("fdaf"));

        verify(chargeRecordService, times(3)).getRecordBySN(anyString());
        verify(spy, times(2)).returnFunds(anyString(), any(ActivityType.class), anyLong(), anyInt());
    }

    private PresentSerialNum validPresentSerialNum() {
        PresentSerialNum psn = new PresentSerialNum();
        psn.setBlockSerialNum("fdskfj");

        return psn;
    }

    private ChargeRecord validChargeRecord(ActivityType activityType) {
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setTypeCode(activityType.getCode());

        return chargeRecord;
    }

    private ChargeRecord validChargeRecord(ChargeRecordStatus chargeRecordStatus) {
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setStatus(chargeRecordStatus.getCode());

        return chargeRecord;
    }

    private EntProduct validEntProduct() {
        EntProduct entProduct = new EntProduct();
        entProduct.setDiscount(34);

        return entProduct;
    }

    private Account validAccount() {
        Account account = new Account();
        account.setType(AccountType.ENTERPRISE.getValue());

        return account;
    }

    private Product validProduct(Constants.ProductType pt) {
        Product product = new Product();
        product.setType((int) pt.getValue());
        product.setPrice(10);
        product.setProductSize(343L);

        return product;
    }

    private AccountRecord nullAccountId() {
        AccountRecord ar = new AccountRecord();
        ar.setAccountId(null);

        return ar;
    }

    private AccountRecord validAccountRecord() {
        AccountRecord ar = new AccountRecord();
        ar.setAccountId(343L);

        return ar;
    }

    /**
     * 设置额度UT
     */
    @Test
    public void testSetMinCount() throws Exception {
        AccountService accountServiceSpy = spy(accountService);

        when(productService.getCurrencyProduct()).thenReturn(buildProduct());
        doReturn(null).doReturn(buildAccount()).when(accountServiceSpy).get(anyLong(), anyLong(), anyInt());
        when(accountMapper.updateMinCount(anyLong(), anyDouble(), anyInt())).thenReturn(0).thenReturn(1);

        assertFalse(accountServiceSpy.setMinCount(34L, 343D));
        assertFalse(accountServiceSpy.setMinCount(34L, 343D));
        assertTrue(accountServiceSpy.setMinCount(34L, 343D));

        verify(productService, times(3)).getCurrencyProduct();
        verify(accountServiceSpy, times(3)).get(anyLong(), anyLong(), anyInt());
        verify(accountMapper, times(2)).updateMinCount(anyLong(), anyDouble(), anyInt());
    }

    /**
     * testIsDebt2Account
     *
     * @throws Exception 异常
     */
    @Test
    public void testIsDebt2Account() throws Exception {
        assertTrue(accountService.isDebt2Account(null, 343L));
        assertTrue(accountService.isDebt2Account(new LinkedList<AccountPrizeMap>(), 343L));
        assertTrue(accountService.isDebt2Account(new LinkedList<AccountPrizeMap>(), null));

        when(productService.selectProductById(anyLong())).thenReturn(null).thenReturn(buildProduct());
        when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(null).thenReturn(
                buildEntProduct(0));

        assertTrue(accountService.isDebt2Account(buildMap(), 343L));
        assertTrue(accountService.isDebt2Account(buildMap(), 343L));
        assertFalse(accountService.isDebt2Account(buildMap(), 343L));

        verify(productService, times(3)).selectProductById(anyLong());
        verify(entProductService, times(2)).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
    }

    /**
     * testIsDebt2Account2
     *
     * @throws Exception 异常
     */
    @Test
    @Ignore
    public void testIsDebt2Account2() throws Exception {
        AccountService spy = spy(accountService);

        when(productService.selectProductById(anyLong())).thenReturn(buildProduct());
        when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(buildEntProduct(100));
        doReturn(buildAccount(0d)).doReturn(buildAccount(1000000d)).when(spy).getCurrencyAccount(anyLong());

        assertTrue(spy.isDebt2Account(buildMap(), 343L));
        assertFalse(spy.isDebt2Account(buildMap(), 343L));

        verify(productService, times(2)).selectProductById(anyLong());
        verify(entProductService, times(2)).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
        verify(spy, times(2)).getCurrencyAccount(anyLong());
    }

    /**
     * 修改账户预警值
     */
    @Test
    public void testUpdateAlertSelective() throws Exception {
        when(accountMapper.updateAlertSelective(any(Account.class))).thenReturn(0).thenReturn(1);

        assertFalse(accountService.updateAlertSelective(new Account()));
        assertTrue(accountService.updateAlertSelective(new Account()));

        verify(accountMapper, times(2)).updateAlertSelective(any(Account.class));
    }

    /**
     * 获取缓存
     */
    @Test
    public void testRecoverAlert() throws Exception {
        Product product = new Product();
        product.setType(1);
        when(productService.get(Mockito.anyLong())).thenReturn(product);
        accountService.recoverAlert(1L, 1L);

        product.setType(2);
        when(productService.get(Mockito.anyLong())).thenReturn(product);
        accountService.recoverAlert(1L, 1L);
    }

    private Account buildAccount(double count) {
        Random random = new Random();

        Account account = new Account();
        account.setCount(count);
        account.setMinCount(0d);
        account.setEnterId(random.nextLong());
        account.setId(random.nextLong());
        account.setType(AccountType.ENTERPRISE.getValue());
        account.setAlertCount(0d);
        account.setStopCount(0d);
        return account;
    }

    private List<AccountPrizeMap> buildMap() {
        List<AccountPrizeMap> map = new LinkedList<AccountPrizeMap>();

        map.add(buildEnough());
        map.add(buildDebt());

        return map;
    }

    private EntProduct buildEntProduct(int discount) {
        EntProduct entProduct = new EntProduct();
        entProduct.setDiscount(discount);

        return entProduct;
    }

    private AccountPrizeMap buildEnough() {
        AccountPrizeMap apm = new AccountPrizeMap();
        apm.setAccountCount(44D);
        apm.setPrizeCount(43L);

        return apm;
    }

    private AccountPrizeMap buildDebt() {
        AccountPrizeMap apm = new AccountPrizeMap();
        apm.setAccountCount(43D);
        apm.setPrizeCount(44L);

        return apm;
    }

    /**
     * 获取现金账户
     */
    @Test
    public void testGetCurrencyAccount() throws Exception {
        assertNull(accountService.getCurrencyAccount(null));

        when(accountMapper.selectCurrencyAccount(anyLong())).thenReturn(null).thenReturn(new Account());

        assertNull(accountService.getCurrencyAccount(343L));
        assertNotNull(accountService.getCurrencyAccount(343L));

        verify(accountMapper, times(2)).selectCurrencyAccount(anyLong());
    }

    /**
     * 根据企业ID和产品ID校验账户是否存在， UT
     */
    @Test
    public void testCheckAccountByEntIdAndProductId() throws Exception {
        assertFalse(accountService.checkAccountByEntIdAndProductId(null, 343L));
        assertFalse(accountService.checkAccountByEntIdAndProductId(343L, null));

        when(accountMapper.checkAccountByEntIdAndProductId(anyLong(), anyLong())).thenReturn(null)
                .thenReturn(new LinkedList<Account>()).thenReturn(buildValidAnswersInternal(1, false));

        assertFalse(accountService.checkAccountByEntIdAndProductId(343L, 433L));
        assertFalse(accountService.checkAccountByEntIdAndProductId(343L, 433L));
        assertTrue(accountService.checkAccountByEntIdAndProductId(343L, 433L));

        verify(accountMapper, times(3)).checkAccountByEntIdAndProductId(anyLong(), anyLong());
    }

    /**
     * 创建产品账户UT
     */
    @Test
    public void testCreateProductAccount() throws Exception {
        assertFalse(accountService.createProductAccount(null));
        assertFalse(accountService.createProductAccount(new LinkedList<Account>()));

        List<Account> accounts = buildValidAnswersInternal(new Random().nextInt(100) + 10, false);
        when(accountMapper.batchInsert(anyListOf(Account.class))).thenReturn(0).thenReturn(accounts.size());

        assertFalse(accountService.createProductAccount(accounts));
        assertTrue(accountService.createProductAccount(accounts));

        verify(accountMapper, times(2)).batchInsert(anyListOf(Account.class));
    }

    /**
     * 从BOSS侧同步账户UT
     */
    @Test
    public void testSyncFromBoss() throws Exception {
        assertTrue(SyncAccountResult.SUCCESS == accountService.syncFromBoss(null, 43L));
        assertTrue(SyncAccountResult.SUCCESS == accountService.syncFromBoss(43L, null));
        assertTrue(SyncAccountResult.SUCCESS == accountService.syncFromBoss(43L, 43L));
    }

    private int randomPos(int size) {
        return new Random().nextInt(size);
    }

    private List<Account> buildInvalidAnswers(int size, int randomPos) {
        List<Account> accounts = buildValidAnswers(size);

        accounts.set(randomPos, new Account());

        return accounts;
    }

    private List<Account> buildValidAnswersTwice(int size) {
        List<Account> accounts = buildValidAnswers(size);
        accounts.addAll(buildValidAnswers(size));

        return accounts;
    }

    private List<Account> buildValidAnswers(int size) {
        List<Account> accounts = buildValidAnswersInternal(size, true);
        accounts.addAll(buildValidAnswersInternal(size, false));

        return accounts;
    }

    private List<Account> buildValidAnswersInternal(int size, boolean isNull) {
        List<Account> accounts = new LinkedList<Account>();
        for (int i = 0; i < size; i++) {
            if (isNull) {
                accounts.add(null);
            } else {
                accounts.add(buildAccount(new Random().nextDouble()));
            }
        }

        accounts.add(buildAccount(0));
        return accounts;
    }

    private Map<Long, Double> buildInfos() {
        Map<Long, Double> infos = new LinkedHashMap<Long, Double>();

        Random random = new Random();
        int count = random.nextInt(10) + 10;
        for (int i = 0; i < count; i++) {
            if (i == 0) {
                infos.put((long) i, 0d);
            } else {
                infos.put((long) i, random.nextDouble());
            }
        }

        return infos;
    }

    /**
     * 构建List<Administer>
     *
     * @return List<Administer>
     */
    private List<Administer> buildAdminList() {
        List<Administer> adminList = new ArrayList<Administer>();
        Administer admin = new Administer();
        admin.setMobilePhone("18867105827");
        adminList.add(admin);
        return adminList;
    }

    /**
     * 构建企业
     *
     * @return 企业对对象
     */
    private Enterprise buildEnterprise() {
        Enterprise enterprise = new Enterprise();
        enterprise.setName("移动杭研");
        enterprise.setCmPhone("18867105827");
        enterprise.setId(343L);
        return enterprise;
    }

    private Product buildProduct() {
        Random random = new Random();

        Product product = new Product();
        product.setId(random.nextLong());
        product.setPrice(random.nextInt(10));
        product.setProductSize(random.nextLong());
        product.setType(0);
        product.setFlowAccountFlag(new Integer(FLOW_ACCOUNT_FLAG.REAL_PRODUCT.getCode()));
        return product;
    }

    //    private List<AccountRecord> buildAccountRecords() {
    //        Random random = new Random();
    //
    //        List<AccountRecord> ars = new LinkedList<AccountRecord>();
    //        int count = random.nextInt(100) + 10;
    //        for (int i = 0; i < count; i++) {
    //            AccountRecord ar = new AccountRecord();
    //            ar.setAccountId(random.nextLong());
    //
    //            //设置两个特殊的值
    //            if (i == 0) {
    //                ar.setType(AccountRecordType.INCOME.getValue());
    //            } else {
    //                ar.setType(AccountRecordType.OUTGO.getValue());
    //            }
    //
    //            if (i == 1) {
    //                ar.setCount(random.nextDouble() - 1);
    //            } else {
    //                ar.setCount(random.nextDouble());
    //            }
    //
    //            ar.setSerialNum("fdsa");
    //
    //            ars.add(ar);
    //        }
    //
    //        return ars;
    //    }

    Account buildAccount() {
        Account account = new Account();
        Random random = new Random();

        account.setId(random.nextLong());
        account.setEnterId(random.nextLong());
        account.setType(AccountType.ENTERPRISE.getValue());
        account.setVersion(random.nextInt(10));

        return account;
    }

    private Account buildSrcAccount() {
        Account account = new Account();
        account.setCount(1000.0);
        account.setMinCount(1.0);
        account.setAlertCount(0.0);
        account.setStopCount(0.0);
        return account;
    }

    private EntFlowControl buildEntFlowControl() {
        EntFlowControl efc = new EntFlowControl();
        efc.setCountUpper(1000L);
        efc.setCountAddition(100L);
        efc.setFcSmsFlag(1);
        return efc;
    }

    /**
     * 测试批量获取企业现金账户
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetCurrencyAccounts() throws Exception {
        assertNull(accountService.getCurrencyAccounts(null));
        assertNull(accountService.getCurrencyAccounts(new LinkedList<Long>()));

        when(accountMapper.selectCurrencyAccounts(any(List.class))).thenReturn(new LinkedList<Account>());

        assertNotNull(accountService.getCurrencyAccounts(validList()));

        verify(accountMapper, times(1)).selectCurrencyAccounts(any(List.class));
    }

    private List<Long> validList() {
        List<Long> list = new LinkedList<Long>();
        Random random = new Random();

        int count = random.nextInt(100) + 1;
        for (int i = 0; i < count; i++) {
            list.add(random.nextLong());
        }

        return list;
    }

    /**
     * 检测企业的暂停值和预警值 如果通过，返回null 如果不通过，返回错误信息
     *
     * totalPrice: 扣除总金额
     */
    @Test
    public void testCheckAlertStopValue() {
        Long entId = 1l;

        Product currencyPrd = new Product();
        currencyPrd.setId(1L);
        Mockito.when(productService.getCurrencyProduct()).thenReturn(currencyPrd);

        Account srcAccount = new Account();
        srcAccount.setCount(100d);
        srcAccount.setMinCount(0d);
        srcAccount.setStopCount(80d);
        srcAccount.setAlertCount(90d);

        Mockito.when(accountMapper.getByOwnerIdAndPrdId(entId, currencyPrd.getId(), AccountType.ENTERPRISE.getValue()))
                .thenReturn(srcAccount);

        Assert.assertTrue(accountService.checkAlertStopValue(entId, 1d) == null);
        Assert.assertTrue(accountService.checkAlertStopValue(entId, 11d) == null);
        Assert.assertFalse(accountService.checkAlertStopValue(entId, 21d) == null);

        srcAccount.setAlertCount(0d);
        Assert.assertTrue(accountService.checkAlertStopValue(entId, 11d) == null);

        srcAccount.setStopCount(0d);
        Assert.assertTrue(accountService.checkAlertStopValue(entId, 21d) == null);

        Mockito.when(accountMapper.getByOwnerIdAndPrdId(entId, currencyPrd.getId(), AccountType.ENTERPRISE.getValue()))
                .thenReturn(null);
        Assert.assertFalse(accountService.checkAlertStopValue(entId, 1d) == null);
    }

    //流量包充足
    @Test
    public void testIsEnoughInAccount() {
        Long entId = 1L;

        Product product = new Product();
        product.setId(1L);
        product.setType(2);

        Account account = createAccount();

        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);
        Mockito.when(accountMapper.getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt())).thenReturn(account);

        assertTrue(accountService.isEnoughInAccount(product.getId(), entId));
        Mockito.verify(productService).selectProductById(anyLong());
        Mockito.verify(accountMapper).getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt());
    }

    //现金不足
    @Test
    public void testIsEnoughInAccount1() {
        Long entId = 1L;

        Product product = new Product();
        product.setId(1L);
        product.setType(2);

        Account account = createAccount();
        account.setCount(0d);

        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);
        Mockito.when(accountMapper.getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt())).thenReturn(account);
        Mockito.when(accountMapper.selectCurrencyAccount(anyLong())).thenReturn(null);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(null);

        assertFalse(accountService.isEnoughInAccount(product.getId(), entId));

        Mockito.verify(productService, atLeastOnce()).selectProductById(anyLong());
        Mockito.verify(accountMapper).getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt());
        Mockito.verify(entProductService).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
        Mockito.verify(accountMapper).selectCurrencyAccount(anyLong());
    }

    //现金充足
    @Test
    public void testIsEnoughInAccount2() {
        Long entId = 1L;

        Product product = new Product();
        product.setId(1L);
        product.setType(2);
        product.setPrice(10);

        Account account = createAccount();
        account.setCount(0d);

        Account cash = createAccount();
        cash.setCount(10000000d);

        EntProduct entProduct = createEntProduct();

        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);
        Mockito.when(accountMapper.getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt())).thenReturn(account);
        Mockito.when(accountMapper.selectCurrencyAccount(anyLong())).thenReturn(cash);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(entProduct);

        assertTrue(accountService.isEnoughInAccount(product.getId(), entId));

        Mockito.verify(productService, atLeastOnce()).selectProductById(anyLong());
        Mockito.verify(accountMapper).getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt());
        Mockito.verify(entProductService).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
        Mockito.verify(accountMapper).selectCurrencyAccount(anyLong());
    }

    //现金充足
    @Test
    public void testIsEnoughInAccount3() {
        Long entId = 1L;

        Product product = new Product();
        product.setId(1L);
        product.setType(0);
        product.setPrice(10);

        Account account = createAccount();
        account.setCount(0d);

        Account cash = createAccount();
        cash.setCount(10000000d);

        EntProduct entProduct = createEntProduct();

        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);
        Mockito.when(accountMapper.getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt())).thenReturn(account);
        Mockito.when(accountMapper.selectCurrencyAccount(anyLong())).thenReturn(cash);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(entProduct);

        assertTrue(accountService.isEnoughInAccount(product.getId(), entId));

        Mockito.verify(productService, atLeastOnce()).selectProductById(anyLong());
        Mockito.verify(accountMapper).getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt());
        Mockito.verify(entProductService).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
        Mockito.verify(accountMapper).selectCurrencyAccount(anyLong());
    }

    //现金不足
    @Test
    public void testIsEnoughInAccount4() {
        Long entId = 1L;

        Product product = new Product();
        product.setId(1L);
        product.setType(0);

        Account account = createAccount();
        account.setCount(0d);

        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);
        Mockito.when(accountMapper.getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt())).thenReturn(account);
        Mockito.when(accountMapper.selectCurrencyAccount(anyLong())).thenReturn(null);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(null);

        assertFalse(accountService.isEnoughInAccount(product.getId(), entId));

        Mockito.verify(productService, atLeastOnce()).selectProductById(anyLong());
        Mockito.verify(accountMapper).getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt());
        Mockito.verify(entProductService).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
        Mockito.verify(accountMapper).selectCurrencyAccount(anyLong());
    }

    //现金充足
    @Test
    public void testIsEnoughInAccount5() {
        Long entId = 1L;

        Product product = new Product();
        product.setId(1L);
        product.setType(1);
        product.setPrice(10);
        product.setProductSize(100L);

        Account account = createAccount();
        account.setCount(0d);

        Account cash = createAccount();
        cash.setCount(100000d);

        EntProduct entProduct = createEntProduct();

        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);
        Mockito.when(accountMapper.getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt())).thenReturn(account);
        Mockito.when(accountMapper.selectCurrencyAccount(anyLong())).thenReturn(cash);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(entProduct);

        assertTrue(accountService.isEnoughInAccount(product.getId(), entId));

        Mockito.verify(productService, atLeastOnce()).selectProductById(anyLong());
        Mockito.verify(accountMapper).getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt());
        Mockito.verify(entProductService).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
        Mockito.verify(accountMapper).selectCurrencyAccount(anyLong());
    }

    //现金不足
    @Test
    public void testIsEnoughInAccount6() {
        Long entId = 1L;

        Product product = new Product();
        product.setId(1L);
        product.setType(1);
        product.setProductSize(100L);

        Account account = createAccount();
        account.setCount(0d);

        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);
        Mockito.when(accountMapper.getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt())).thenReturn(account);
        Mockito.when(accountMapper.selectCurrencyAccount(anyLong())).thenReturn(null);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(null);

        assertFalse(accountService.isEnoughInAccount(product.getId(), entId));

        Mockito.verify(productService, atLeastOnce()).selectProductById(anyLong());
        Mockito.verify(accountMapper).getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt());
        Mockito.verify(entProductService).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
        Mockito.verify(accountMapper).selectCurrencyAccount(anyLong());
    }

    //流量包充足
    @Test
    public void testIsEnoughInAccount7() {
        Long entId = 1L;

        Product product = new Product();
        product.setId(1L);
        product.setType(1);
        product.setProductSize(100L);

        Account account = createAccount();
        account.setCount(100000d);

        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);
        Mockito.when(accountMapper.getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt())).thenReturn(account);

        assertTrue(accountService.isEnoughInAccount(product.getId(), entId));
        Mockito.verify(productService).selectProductById(anyLong());
        Mockito.verify(accountMapper).getByOwnerIdAndPrdId(anyLong(), anyLong(), anyInt());
    }

    @Test
    public void testverifyCashAccount() {
        Long entId = 1L;

        Product product = new Product();
        product.setId(1L);
        product.setType(1);
        product.setPrice(10);
        product.setProductSize(100L);

        Account account = createAccount();
        account.setCount(0d);

        Account cash = createAccount();
        cash.setCount(0d);
        cash.setMinCount(-50000d);

        EntProduct entProduct = createEntProduct();

        Mockito.when(productService.selectProductById(anyLong())).thenReturn(product);
        Mockito.when(accountMapper.selectCurrencyAccount(anyLong())).thenReturn(cash);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(anyLong(), anyLong())).thenReturn(entProduct);

        assertTrue(accountService.verifyCashAccount(product.getId(), entId));

        Mockito.verify(entProductService).selectByProductIDAndEnterprizeID(anyLong(), anyLong());
        Mockito.verify(accountMapper).selectCurrencyAccount(anyLong());
        Mockito.verify(productService).selectProductById(anyLong());
    }

    private EntProduct createEntProduct() {
        EntProduct entProduct = new EntProduct();
        entProduct.setDiscount(100);
        return entProduct;
    }

    private Account createAccount() {
        Account account = new Account();
        account.setId(1L);
        account.setCount(1d);
        return account;
    }

    @Test
    public void testTryMinusCurrencyAccount() {

        Product srcProduct = new Product();
        srcProduct.setFlowAccountFlag(1);
        srcProduct.setFlowAccountProductId(1L);

        Mockito.when(productService.get(Mockito.anyLong())).thenReturn(srcProduct);
        Mockito.when(entProductService.selectByProductIDAndEnterprizeID(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(null);

        //assertFalse(accountService.tryMinusCurrencyAccount(srcProduct, 1L, 1L, AccountType.ENTERPRISE, 0.00, "123456", "充值",true));
        Assert.assertNotEquals(accountService.tryMinusCurrencyAccount(srcProduct, 1L, 1L, AccountType.ENTERPRISE, 0.00,
                "123456", "充值", true), MinusCountReturnType.OK);
    }

    @Test
    public void testCleanAccountByTpye() {
        assertFalse(accountService.cleanAccountByTpye(null));

        Mockito.when(accountMapper.cleanAccountByTpye(Mockito.anyInt())).thenReturn(1);
        assertTrue(accountService.cleanAccountByTpye(ProductType.FLOW_ACCOUNT));

    }

    @Test
    public void testUpdateCount() {
        assertFalse(accountService.updateCount(null, null));

        Mockito.when(accountMapper.updateCount(Mockito.anyLong(), Mockito.anyDouble())).thenReturn(1);
        assertTrue(accountService.updateCount(1L, 1.0));

    }

    @Test
    public void testIsEnough2Debt() {
        assertTrue(accountService.isEnough2Debt(null, 1.0));

        Account account = new Account();
        account.setCount(20.00);
        account.setMinCount(0.0);
        assertTrue(accountService.isEnough2Debt(account, 1024.00));
        assertFalse(accountService.isEnough2Debt(account, 2048.00));
    }

    //多个响应内容
    private class MultiAnswer<T> implements Answer<T> {
        private List<T> answers;

        public MultiAnswer(List<T> answers) {
            this.answers = answers;
        }

        @Override
        public T answer(InvocationOnMock invocation) throws Throwable {
            return answers.remove(0);
        }
    }

    @Test
    public void testIsEnoughInAccount11() {
        List<Product> list = new ArrayList<Product>();

        assertTrue(accountService.isEnoughInAccount(list, null));

        List<Product> prds = new ArrayList<Product>();

        //现金产品
        Product product0 = new Product();
        product0.setProductSize(10240L);
        product0.setPrice(500);
        product0.setType(0);
        product0.setFlowAccountFlag(FLOW_ACCOUNT_FLAG.REAL_PRODUCT.getCode());
        prds.add(product0);

        //流量池产品
        Product product1 = new Product();
        product1.setProductSize(10240L);
        product1.setPrice(500);
        product1.setType(1);
        product1.setFlowAccountFlag(FLOW_ACCOUNT_FLAG.REAL_PRODUCT.getCode());
        prds.add(product1);

        //流量包产品
        Product product2 = new Product();
        product2.setProductSize(10240L);
        product2.setPrice(500);
        product2.setType(2);
        product2.setFlowAccountFlag(FLOW_ACCOUNT_FLAG.REAL_PRODUCT.getCode());
        prds.add(product2);

        //话费产品      
        Product product3 = new Product();
        product3.setProductSize(10240L);
        product3.setPrice(500);
        product3.setType(3);
        product3.setFlowAccountFlag(FLOW_ACCOUNT_FLAG.REAL_PRODUCT.getCode());
        prds.add(product3);

        //话费产品      
        Product product4 = new Product();
        product4.setProductSize(10240L);
        product4.setPrice(500);
        product4.setType(4);
        product4.setFlowAccountFlag(FLOW_ACCOUNT_FLAG.REAL_PRODUCT.getCode());
        prds.add(product4);

        Mockito.when(accountMapper.getByOwnerIdAndPrdId(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt()))
                .thenReturn(null);
        assertTrue(accountService.isEnoughInAccount(prds, 1L));

        Account account = new Account();
        account.setId(1L);
        account.setCount(1000000.00);
        Mockito.when(accountMapper.getByOwnerIdAndPrdId(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt()))
                .thenReturn(account);

        Mockito.when(productService.getPrdsByType(Mockito.anyLong(), Mockito.anyList(), Mockito.anyLong())).thenReturn(
                prds);

        Mockito.when(converterService.isInterdictConvert(Mockito.anyLong(), Mockito.anyLong())).thenReturn(false);

        List<Product> prds0 = new ArrayList<Product>();
        prds0.add(product0);
        assertTrue(accountService.isEnoughInAccount(prds0, 1L));

        List<Product> prds1 = new ArrayList<Product>();
        prds1.add(product1);

        assertTrue(accountService.isEnoughInAccount(prds1, 1L));

        List<Product> prds2 = new ArrayList<Product>();
        prds2.add(product2);
        assertTrue(accountService.isEnoughInAccount(prds2, 1L));

        List<Product> prds3 = new ArrayList<Product>();
        prds3.add(product3);
        assertTrue(accountService.isEnoughInAccount(prds3, 1L));

        List<Product> prds4 = new ArrayList<Product>();
        prds4.add(product4);
        assertTrue(accountService.isEnoughInAccount(prds4, 1L));

    }

    /**
     * getPaypreAccByType
     */
    @Test
    public void testgetPaypreAccByType() {
        List<Account> list = new ArrayList<Account>();
        Account account = new Account();
        list.add(account);

        Mockito.when(accountMapper.getPaypreAccByType(Mockito.anyLong(), Mockito.anyInt())).thenReturn(list);
        assertNotNull(accountService.getPaypreAccByType(1L, ProductType.PRE_PAY_CURRENCY));

        list.clear();
        Mockito.when(accountMapper.getPaypreAccByType(Mockito.anyLong(), Mockito.anyInt())).thenReturn(list);
        assertNull(accountService.getPaypreAccByType(1L, ProductType.PRE_PAY_CURRENCY));
        assertNull(accountService.getPaypreAccByType(1L, null));
        assertNull(accountService.getPaypreAccByType(null, null));
    }

    /**
     * 
     * @Title: testUpdateMinCount 
     * @Description: TODO
     * @return: void
     */
    @Test
    public void testUpdateMinCount() {
        Mockito.when(accountMapper.updateMinCount2(Mockito.anyLong(), Mockito.any(Double.class))).thenReturn(1);
        assertTrue(accountService.updateMinCount(1L, 2.0));

    }

    /**
     * 
     * @Title: testUpdateAlertCount 
     * @Description: TODO
     * @return: void
     */
    @Test
    public void testUpdateAlertCount() {
        Mockito.when(accountMapper.updateAlertCount(Mockito.anyLong(), Mockito.any(Double.class))).thenReturn(1);
        assertTrue(accountService.updateAlertCount(1L, 2.0));
    }

    /**
     * 
     * @Title: testUpdateStopCount 
     * @Description: TODO
     * @return: void
     */
    @Test
    public void testUpdateStopCount() {
        Mockito.when(accountMapper.updateStopCount(Mockito.anyLong(), Mockito.any(Double.class))).thenReturn(1);
        assertTrue(accountService.updateStopCount(1L, 2.0));
    }

}