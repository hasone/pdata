package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.boss.sichuan.service.IndividualBossService;
import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.IndividualAccountType;
import com.cmcc.vrp.enums.IndividualProductType;
import com.cmcc.vrp.province.dao.IndividualAccountMapper;
import com.cmcc.vrp.province.dao.TmpaccountMapper;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualAccountRecord;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.WxAdminister;
import com.cmcc.vrp.province.module.Membership;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.IndividualAccountRecordService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualFlowcoinRecordService;
import com.cmcc.vrp.province.service.IndividualProductService;
import com.cmcc.vrp.province.service.WxAdministerService;
import com.cmcc.vrp.wx.WxGradeService;
import com.cmcc.vrp.wx.model.Tmpaccount;
import com.cmcc.vrp.wx.model.WxGrade;

/**
 * @author wujiamin
 * @date 2016年11月2日
 */
@RunWith(MockitoJUnitRunner.class)
public class IndividualAccountServiceImplTest {
    @InjectMocks
    IndividualAccountService individualAccountService = new IndividualAccountServiceImpl();

    @Mock
    IndividualAccountMapper individualAccountMapper;

    @Mock
    WxAdministerService administerService;

    @Mock
    IndividualProductService individualProductService;

    @Mock
    IndividualAccountRecordService individualAccountRecordService;

    @Mock
    IndividualBossService individualBossService;

    @Mock
    IndividualFlowcoinRecordService individualFlowcoinRecordService;

    @Mock
    ActivitiesService activitiesService;

    @Mock
    ActivityPrizeService activityPrizeService;
    
    @Mock
    TmpaccountMapper tmpaccountMapper;
    
    @Mock
    WxGradeService wxGradeService;


    @Test
    public void testSelectByPrimaryKey() {
        when(individualAccountMapper.selectByPrimaryKey(1L)).thenReturn(new IndividualAccount());
        assertNotNull(individualAccountService.selectByPrimaryKey(1L));
        verify(individualAccountMapper, times(1)).selectByPrimaryKey(1L);
    }

    @Test
    public void testBatchInsert() {
        when(individualAccountMapper.batchInsert(Mockito.anyList())).thenReturn(0);
        assertTrue(individualAccountService.batchInsert(new ArrayList()));

        when(individualAccountMapper.batchInsert(Mockito.anyList())).thenReturn(1);
        assertFalse(individualAccountService.batchInsert(new ArrayList()));

        verify(individualAccountMapper, times(2)).batchInsert(Mockito.anyList());
    }

    @Test
    public void testGetAccountByOwnerIdAndProductId() {
        when(individualAccountMapper.getAccountByOwnerIdAndProductId(1L, 1L, 1)).thenReturn(new IndividualAccount());
        assertNotNull(individualAccountService.getAccountByOwnerIdAndProductId(1L, 1L, 1));

        verify(individualAccountMapper, times(1)).getAccountByOwnerIdAndProductId(1L, 1L, 1);
    }

    @Test
    public void testAddCount() {
        IndividualAccount account = new IndividualAccount();
        account.setVersion(1);
        account.setId(1L);
        when(individualAccountMapper.addCount(account.getId(), new BigDecimal(0))).thenReturn(1, 0);
        assertTrue(individualAccountService.addCount(account, new BigDecimal(0)));
        assertFalse(individualAccountService.addCount(account, new BigDecimal(0)));
        verify(individualAccountMapper, times(2)).addCount(account.getId(), new BigDecimal(0));
    }

    @Test
    public void testMinusCount() {
        IndividualAccount account = new IndividualAccount();
        account.setVersion(1);
        account.setId(1L);
        when(individualAccountMapper.minusCount(account.getId(), new BigDecimal(0))).thenReturn(1, 0);
        assertTrue(individualAccountService.minusCount(account, new BigDecimal(0)));
        assertFalse(individualAccountService.minusCount(account, new BigDecimal(0)));
        verify(individualAccountMapper, times(2)).minusCount(account.getId(), new BigDecimal(0));
    }

    @Test
    public void testChangeBossAccount() {
        Long adminId = 1L;
        BigDecimal count = new BigDecimal(0);
        Long productId = 1L;
        String systemSerial = "123456";
        Integer accountRecordType = 1;
        String desc = "123";
        Integer activityType = 1;
        Integer back = 1;

        try {
            individualAccountService.changeBossAccount(null, count, productId, systemSerial, accountRecordType, desc, activityType, back);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            individualAccountService.changeBossAccount(adminId, count, null, systemSerial, accountRecordType, desc, activityType, back);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(administerService.selectWxAdministerById(adminId)).thenReturn(null);
        assertFalse(individualAccountService.changeBossAccount(adminId, count, productId, systemSerial, accountRecordType, desc, activityType, back));
        WxAdminister admin = new WxAdminister();
        when(administerService.selectWxAdministerById(adminId)).thenReturn(admin);
        assertFalse(individualAccountService.changeBossAccount(adminId, count, productId, systemSerial, accountRecordType, desc, activityType, back));
        admin.setMobilePhone("");
        when(administerService.selectWxAdministerById(adminId)).thenReturn(admin);
        assertFalse(individualAccountService.changeBossAccount(adminId, count, productId, systemSerial, accountRecordType, desc, activityType, back));


        admin.setMobilePhone("18600000000");
        when(administerService.selectWxAdministerById(adminId)).thenReturn(admin);
        when(individualProductService.selectByPrimaryId(productId)).thenReturn(null);
        assertFalse(individualAccountService.changeBossAccount(adminId, count, productId, systemSerial, accountRecordType, desc, activityType, back));

        IndividualProduct product = new IndividualProduct();
        product.setType(IndividualProductType.PHONE_FARE.getValue());
        product.setId(productId);

        when(individualProductService.selectByPrimaryId(productId)).thenReturn(product);
        when(individualAccountMapper.getAccountByOwnerIdAndProductId(adminId, productId, IndividualAccountType.INDIVIDUAL_BOSS.getValue())).thenReturn(null);
        try {
            individualAccountService.changeBossAccount(adminId, count, productId, systemSerial, accountRecordType, desc, activityType, back);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        IndividualAccount account = new IndividualAccount();
        account.setId(1L);
        when(individualAccountMapper.getAccountByOwnerIdAndProductId(adminId, productId, IndividualAccountType.INDIVIDUAL_BOSS.getValue())).thenReturn(account);
        when(individualAccountRecordService.create(Mockito.any(IndividualAccountRecord.class))).thenReturn(false);
        try {
            individualAccountService.changeBossAccount(adminId, count, productId, systemSerial, accountRecordType, desc, activityType, back);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(individualAccountRecordService.create(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);

        when(individualBossService.changeBossPhoneFare(admin.getMobilePhone(), count, accountRecordType, systemSerial)).thenReturn(true);
        when(individualAccountRecordService.updateByPrimaryKeySelective(Mockito.any(IndividualAccountRecord.class))).thenReturn(false);
        when(individualFlowcoinRecordService.createRecord(Mockito.any(IndividualAccountRecord.class))).thenReturn(false);
        when(individualProductService.getFlowcoinProduct()).thenReturn(product);
        assertTrue(individualAccountService.changeBossAccount(adminId, count, productId, systemSerial, accountRecordType, desc, activityType, back));

        when(individualFlowcoinRecordService.createRecord(Mockito.any(IndividualAccountRecord.class))).thenReturn(false);
        when(individualProductService.getFlowcoinProduct()).thenReturn(new IndividualProduct());
        assertTrue(individualAccountService.changeBossAccount(adminId, count, productId, systemSerial, accountRecordType, desc, activityType, back));

        when(individualFlowcoinRecordService.createRecord(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);
        when(individualProductService.getFlowcoinProduct()).thenReturn(product);
        assertTrue(individualAccountService.changeBossAccount(adminId, count, productId, systemSerial, accountRecordType, desc, activityType, back));

        when(individualAccountRecordService.updateByPrimaryKeySelective(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);
        when(individualFlowcoinRecordService.createRecord(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);
        assertTrue(individualAccountService.changeBossAccount(adminId, count, productId, systemSerial, accountRecordType, desc, activityType, back));

        when(individualBossService.changeBossPhoneFare(admin.getMobilePhone(), count, accountRecordType, systemSerial)).thenReturn(false);
        when(individualAccountRecordService.updateByPrimaryKeySelective(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);
        assertFalse(individualAccountService.changeBossAccount(adminId, count, productId, systemSerial, accountRecordType, desc, activityType, back));

        when(individualBossService.changeBossPhoneFare(admin.getMobilePhone(), count, accountRecordType, systemSerial)).thenReturn(false);
        when(individualAccountRecordService.updateByPrimaryKeySelective(Mockito.any(IndividualAccountRecord.class))).thenReturn(false);
        assertFalse(individualAccountService.changeBossAccount(adminId, count, productId, systemSerial, accountRecordType, desc, activityType, back));


        product.setType(IndividualProductType.FLOW_COIN.getValue());

        when(individualProductService.selectByPrimaryId(productId)).thenReturn(product);
        assertFalse(individualAccountService.changeBossAccount(adminId, count, productId, systemSerial, accountRecordType, desc, activityType, back));

    }

    @Test
    public void testChangeFrozenAccount() {
        Long adminId = 1L;
        Long ownerId = 1L;
        Long accountId = 1L;
        BigDecimal count = new BigDecimal(0);
        Long productId = 1L;
        String systemSerial = "123456";
        Integer accountRecordType = 1;
        String desc = "123";
        Integer activityType = 1;
        Integer back = 1;

        try {
            individualAccountService.changeFrozenAccount(null, ownerId, accountId, count, productId, systemSerial, accountRecordType, desc, activityType, back);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            individualAccountService.changeFrozenAccount(adminId, ownerId, accountId, count, null, systemSerial, accountRecordType, desc, activityType, back);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            individualAccountService.changeFrozenAccount(adminId, ownerId, null, count, productId, systemSerial, accountRecordType, desc, activityType, back);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(individualAccountRecordService.create(Mockito.any(IndividualAccountRecord.class))).thenReturn(false);
        try {
            individualAccountService.changeFrozenAccount(adminId, ownerId, accountId, count, productId, systemSerial, accountRecordType, desc, activityType, back);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(individualAccountRecordService.create(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);
        when(individualAccountMapper.selectByPrimaryKey(accountId)).thenReturn(null);
        try {
            individualAccountService.changeFrozenAccount(adminId, ownerId, accountId, count, productId, systemSerial, accountRecordType, desc, activityType, back);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        IndividualAccount account = new IndividualAccount();
        account.setId(1L);
        account.getVersion();
        when(individualAccountMapper.selectByPrimaryKey(accountId)).thenReturn(account);
        when(individualAccountMapper.addCount(account.getId(), count)).thenReturn(0);
        when(individualAccountRecordService.updateByPrimaryKeySelective(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);
        try {
            individualAccountService.changeFrozenAccount(adminId, ownerId, accountId, count, productId,
                    systemSerial, (int) AccountRecordType.INCOME.getValue(), desc, activityType, back);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        when(individualAccountRecordService.updateByPrimaryKeySelective(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);
        when(individualAccountMapper.addCount(account.getId(), count)).thenReturn(1);
        assertTrue(individualAccountService.changeFrozenAccount(adminId, ownerId, accountId, count, productId, systemSerial, (int) AccountRecordType.INCOME.getValue(), desc, activityType, back));

        when(individualAccountRecordService.updateByPrimaryKeySelective(Mockito.any(IndividualAccountRecord.class))).thenReturn(false);
        try {
            individualAccountService.changeFrozenAccount(adminId, ownerId, accountId, count, productId,
                    systemSerial, (int) AccountRecordType.INCOME.getValue(), desc, activityType, back);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        when(individualAccountMapper.minusCount(account.getId(), count)).thenReturn(0);
        try {
            individualAccountService.changeFrozenAccount(adminId, ownerId, accountId, count, productId,
                    systemSerial, (int) AccountRecordType.OUTGO.getValue(), desc, activityType, back);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        when(individualAccountMapper.minusCount(account.getId(), count)).thenReturn(1);
        when(individualAccountRecordService.updateByPrimaryKeySelective(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);
        assertTrue(individualAccountService.changeFrozenAccount(adminId, ownerId, accountId, count, productId,
                systemSerial, (int) AccountRecordType.OUTGO.getValue(), desc, activityType, back));

        when(individualAccountMapper.minusCount(account.getId(), count)).thenReturn(1);
        when(individualAccountRecordService.updateByPrimaryKeySelective(Mockito.any(IndividualAccountRecord.class))).thenReturn(false);
        try {
            individualAccountService.changeFrozenAccount(adminId, ownerId, accountId, count, productId,
                    systemSerial, (int) AccountRecordType.OUTGO.getValue(), desc, activityType, back);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            individualAccountService.changeFrozenAccount(adminId, ownerId, accountId, count, productId,
                    systemSerial, 3, desc, activityType, back);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testInsert() {
        assertFalse(individualAccountService.insert(null));

        when(individualAccountMapper.insert(Mockito.any(IndividualAccount.class))).thenReturn(1, 0);
        assertTrue(individualAccountService.insert(new IndividualAccount()));
        assertFalse(individualAccountService.insert(new IndividualAccount()));

        verify(individualAccountMapper, times(2)).insert(Mockito.any(IndividualAccount.class));
    }

    @Test
    public void testCreateAccountForActivity() {
        assertFalse(individualAccountService.createAccountForActivity(null, new ActivityInfo(), new ArrayList(), new IndividualAccount()));
        assertFalse(individualAccountService.createAccountForActivity(new Activities(), null, new ArrayList(), new IndividualAccount()));
        assertFalse(individualAccountService.createAccountForActivity(new Activities(), new ActivityInfo(), null, new IndividualAccount()));
        assertFalse(individualAccountService.createAccountForActivity(new Activities(), new ActivityInfo(), new ArrayList(), null));

        when(individualAccountMapper.insert(Mockito.any(IndividualAccount.class))).thenReturn(1);
        when(individualAccountRecordService.create(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);

        assertTrue(individualAccountService.createAccountForActivity(new Activities(), new ActivityInfo(), new ArrayList(), new IndividualAccount()));
    }

    @Test
    public void testCreateAccountForActivity1() {
        when(individualAccountMapper.insert(Mockito.any(IndividualAccount.class))).thenReturn(0);
        when(individualAccountRecordService.create(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);

        assertFalse(individualAccountService.createAccountForActivity(new Activities(), new ActivityInfo(), new ArrayList(), new IndividualAccount()));
    }

    @Test
    public void testChargeFlowcoinForIndividualActivity() {
        assertFalse(individualAccountService.chargeFlowcoinForIndividualActivity(null, 1, "18867103685", "123"));
        assertFalse(individualAccountService.chargeFlowcoinForIndividualActivity("12345", null, "18867103685", "123"));
        assertFalse(individualAccountService.chargeFlowcoinForIndividualActivity("12345", 1, "", "123"));

        when(activitiesService.selectByActivityId("12345")).thenReturn(null);
        when(activityPrizeService.selectByActivityIdForIndividual("12345")).thenReturn(null);
        when(administerService.selectByMobilePhone("18867103685")).thenReturn(null);
        assertFalse(individualAccountService.chargeFlowcoinForIndividualActivity("12345", 1, "18867103685", "123"));

        when(activitiesService.selectByActivityId("12345")).thenReturn(createActivity());
        when(activityPrizeService.selectByActivityIdForIndividual("12345")).thenReturn(null);
        when(administerService.selectByMobilePhone("18867103685")).thenReturn(null);
        assertFalse(individualAccountService.chargeFlowcoinForIndividualActivity("12345", 1, "18867103685", "123"));

        when(activityPrizeService.selectByActivityIdForIndividual("12345")).thenReturn(new ArrayList());
        when(administerService.selectByMobilePhone("18867103685")).thenReturn(null);
        assertFalse(individualAccountService.chargeFlowcoinForIndividualActivity("12345", 1, "18867103685", "123"));


        when(activityPrizeService.selectByActivityIdForIndividual("12345")).thenReturn(createPrizeList());
        when(administerService.selectByMobilePhone("18867103685")).thenReturn(null);
        assertFalse(individualAccountService.chargeFlowcoinForIndividualActivity("12345", 1, "18867103685", "123"));

        when(administerService.selectByMobilePhone("18867103685")).thenReturn(createAdmin());

        Long adminId = 1L;
        Long productId = 1L;
        Integer accountRecordType = 0;

        IndividualProduct product = new IndividualProduct();
        product.setType(IndividualProductType.PHONE_FARE.getValue());
        product.setId(productId);

        when(individualProductService.selectByPrimaryId(productId)).thenReturn(product);
        when(administerService.selectWxAdministerById(1L)).thenReturn(null);

        assertFalse(individualAccountService.chargeFlowcoinForIndividualActivity("12345", 1, "18867103685", "123"));


        when(administerService.selectWxAdministerById(1L)).thenReturn(createAdmin());
        IndividualAccount account = new IndividualAccount();
        account.setId(1L);
        when(individualAccountMapper.getAccountByOwnerIdAndProductId(adminId, productId, IndividualAccountType.INDIVIDUAL_BOSS.getValue())).thenReturn(account);
        when(individualAccountRecordService.create(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);

        when(individualBossService.changeBossPhoneFare("18867103685", new BigDecimal(1), accountRecordType, "123")).thenReturn(true);
        when(individualAccountRecordService.updateByPrimaryKeySelective(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);
        when(individualFlowcoinRecordService.createRecord(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);
        when(individualProductService.getFlowcoinProduct()).thenReturn(product);

        assertTrue(individualAccountService.chargeFlowcoinForIndividualActivity("12345", 1, "18867103685", "123"));

    }

    @Test
    public void testGiveBackForActivity0() {
        String activityId = "12345";
        assertFalse(individualAccountService.giveBackForActivity(null));

        when(activitiesService.selectByActivityId(activityId)).thenReturn(null);
        when(activityPrizeService.selectByActivityIdForIndividual(activityId)).thenReturn(null);
        assertFalse(individualAccountService.giveBackForActivity(activityId));

        when(activitiesService.selectByActivityId(activityId)).thenReturn(new Activities());
        when(activityPrizeService.selectByActivityIdForIndividual(activityId)).thenReturn(null);
        assertFalse(individualAccountService.giveBackForActivity(activityId));

        when(activitiesService.selectByActivityId(activityId)).thenReturn(createActivity());
        when(activityPrizeService.selectByActivityIdForIndividual(activityId)).thenReturn(null);
        assertFalse(individualAccountService.giveBackForActivity(activityId));

        when(activitiesService.selectByActivityId(activityId)).thenReturn(createActivity());
        when(activityPrizeService.selectByActivityIdForIndividual(activityId)).thenReturn(new ArrayList());
        assertFalse(individualAccountService.giveBackForActivity(activityId));

        when(activitiesService.selectByActivityId(activityId)).thenReturn(createActivity());
        when(activityPrizeService.selectByActivityIdForIndividual(activityId)).thenReturn(createPrizeListEmpty());
        assertFalse(individualAccountService.giveBackForActivity(activityId));
    }

    @Test
    public void testGiveBackForActivity1() {
        String activityId = "12345";

        when(activitiesService.selectByActivityId(activityId)).thenReturn(createActivity());
        when(activityPrizeService.selectByActivityIdForIndividual(activityId)).thenReturn(createPrizeList());
        when(individualAccountMapper.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt())).thenReturn(null);
        assertFalse(individualAccountService.giveBackForActivity(activityId));
    }

    @Test
    public void testGiveBackForActivity2() {
        String activityId = "12345";

        when(activitiesService.selectByActivityId(activityId)).thenReturn(createActivity());
        when(activityPrizeService.selectByActivityIdForIndividual(activityId)).thenReturn(createPrizeList());
        when(individualAccountMapper.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt())).thenReturn(createAccount());
        assertFalse(individualAccountService.giveBackForActivity(activityId));

        Long adminId = 1L;
        Long productId = 1L;
        Integer accountRecordType = 0;

        IndividualProduct product = new IndividualProduct();
        product.setType(IndividualProductType.PHONE_FARE.getValue());
        product.setId(productId);

        when(individualProductService.selectByPrimaryId(productId)).thenReturn(product);
        when(administerService.selectWxAdministerById(1L)).thenReturn(null);

        assertFalse(individualAccountService.giveBackForActivity(activityId));

        when(administerService.selectWxAdministerById(1L)).thenReturn(createAdmin());
        IndividualAccount account = new IndividualAccount();
        account.setId(1L);
        when(individualAccountMapper.getAccountByOwnerIdAndProductId(adminId, productId, IndividualAccountType.INDIVIDUAL_BOSS.getValue())).thenReturn(account);
        when(individualAccountRecordService.create(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);

        when(individualBossService.changeBossPhoneFare(Mockito.anyString(), Mockito.any(BigDecimal.class), Mockito.anyInt(), Mockito.anyString())).thenReturn(true);
        when(individualAccountRecordService.updateByPrimaryKeySelective(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);
        when(individualFlowcoinRecordService.createRecord(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);
        when(individualProductService.getFlowcoinProduct()).thenReturn(product);

        when(individualAccountMapper.minusCount(account.getId(), new BigDecimal(0))).thenReturn(0);
        assertFalse(individualAccountService.giveBackForActivity(activityId));

        when(individualAccountMapper.minusCount(account.getId(), new BigDecimal(1))).thenReturn(1);
        assertTrue(individualAccountService.giveBackForActivity(activityId));
    }
    
    @Test
    public void testGiveBackFlow(){
        assertFalse(individualAccountService.giveBackFlow(null)); 
        
        assertFalse(individualAccountService.giveBackFlow("activityId"));
        
        when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(createActivity());
        when(activityPrizeService.selectByActivityIdForIndividual(Mockito.anyString())).thenReturn(createPrizeList());
        
        IndividualAccount account = createAccount();
        when(individualAccountMapper.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt())).thenReturn(account,null);        
        assertFalse(individualAccountService.giveBackFlow("activityId"));

        when(individualAccountMapper.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt())).thenReturn(account);        
        when(individualAccountMapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(account);
        
        //changeAccount
        when(individualAccountRecordService.create(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);
        when(individualAccountMapper.minusCount(Mockito.anyLong(), Mockito.any(BigDecimal.class))).thenReturn(1);
        when(individualAccountMapper.addCount(Mockito.anyLong(), Mockito.any(BigDecimal.class))).thenReturn(1);        
        when(individualAccountRecordService.updateByPrimaryKeySelective(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);
        assertTrue(individualAccountService.giveBackFlow("activityId"));        
    }

    @Test
    public void testCreateFlowcoinExchangeAndPurchaseAccount() {
        when(individualAccountMapper.insert(Mockito.any(IndividualAccount.class))).thenReturn(1);
        when(individualAccountRecordService.create(Mockito.any(IndividualAccountRecord.class))).thenReturn(false);
        assertFalse(individualAccountService.createFlowcoinExchangeAndPurchaseAccount(new IndividualAccount(), "12345", "123", 1));
    }

    @Test
    public void testCreateFlowcoinExchangeAndPurchaseAccount1() {
        when(individualAccountMapper.insert(Mockito.any(IndividualAccount.class))).thenReturn(1);

        when(individualAccountRecordService.create(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);

        Long adminId = 1L;
        Long productId = 1L;
        Integer accountRecordType = 0;

        IndividualProduct product = new IndividualProduct();
        product.setType(IndividualProductType.PHONE_FARE.getValue());
        product.setId(productId);

        when(individualProductService.selectByPrimaryId(productId)).thenReturn(product);

        when(administerService.selectWxAdministerById(1L)).thenReturn(createAdmin());
        IndividualAccount account = new IndividualAccount();
        account.setId(1L);
        when(individualAccountMapper.getAccountByOwnerIdAndProductId(adminId, productId, IndividualAccountType.INDIVIDUAL_BOSS.getValue())).thenReturn(account);
        when(individualAccountRecordService.create(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);

        when(individualBossService.changeBossPhoneFare(Mockito.anyString(), Mockito.any(BigDecimal.class), Mockito.anyInt(), Mockito.anyString())).thenReturn(true);
        when(individualAccountRecordService.updateByPrimaryKeySelective(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);
        when(individualFlowcoinRecordService.createRecord(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);
        when(individualProductService.getFlowcoinProduct()).thenReturn(product);

        assertTrue(individualAccountService.createFlowcoinExchangeAndPurchaseAccount(createAccount(), "12345", "123", 1));

    }

    @Test(expected = RuntimeException.class)
    public void testCreateFlowcoinExchangeAndPurchaseAccount2() {
        when(individualAccountMapper.insert(Mockito.any(IndividualAccount.class))).thenReturn(1);
        when(individualAccountRecordService.create(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);

        IndividualProduct product = new IndividualProduct();
        product.setType(IndividualProductType.PHONE_FARE.getValue());
        product.setId(1L);

        when(individualProductService.selectByPrimaryId(1L)).thenReturn(product);
        when(administerService.selectWxAdministerById(1L)).thenReturn(null);

        assertFalse(individualAccountService.createFlowcoinExchangeAndPurchaseAccount(createAccount(), "12345", "123", 1));
    }
    
    @Test
    public void testAddCountForcely(){
        assertFalse(individualAccountService.addCountForcely("111", 1L, "serialNum", new BigDecimal("1"), ActivityType.SIGN_IN, "签名")); 
        assertFalse(individualAccountService.addCountForcely("18867103685", 1L, null, new BigDecimal("1"), ActivityType.SIGN_IN, "签名")); 
        
        when(administerService.selectByMobilePhone(Mockito.anyString())).thenReturn(null);
        assertFalse(individualAccountService.addCountForcely("18867103685", 1L, "serialNum", new BigDecimal("1"), ActivityType.SIGN_IN, "签名")); 
        
        WxAdminister admin = new WxAdminister();
        admin.setId(1L);
        when(administerService.selectByMobilePhone(Mockito.anyString())).thenReturn(admin);
        when(individualAccountMapper.getAccountByOwnerIdAndProductId(1L, 1L, -1)).thenReturn(null);
        when(individualAccountMapper.insert(Mockito.any(IndividualAccount.class))).thenReturn(0);
        assertFalse(individualAccountService.addCountForcely("18867103685", 1L, "serialNum", new BigDecimal("1"), ActivityType.SIGN_IN, "签名")); 
        
        when(individualAccountMapper.getAccountByOwnerIdAndProductId(1L, 1L, -1)).thenReturn(new IndividualAccount());
        when(individualAccountMapper.addCount(Mockito.anyLong(), Mockito.any(BigDecimal.class))).thenReturn(0);
        assertFalse(individualAccountService.addCountForcely("18867103685", 1L, "serialNum", new BigDecimal("1"), ActivityType.SIGN_IN, "签名")); 
        
        when(individualAccountMapper.addCount(Mockito.anyLong(), Mockito.any(BigDecimal.class))).thenReturn(1);
        when(!individualAccountRecordService.create(Mockito.any(IndividualAccountRecord.class))).thenReturn(false);
        assertFalse(individualAccountService.addCountForcely("18867103685", 1L, "serialNum", new BigDecimal("1"), ActivityType.SIGN_IN, "签名")); 
        
        when(!individualAccountRecordService.create(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);
        assertTrue(individualAccountService.addCountForcely("18867103685", 1L, "serialNum", new BigDecimal("1"), ActivityType.SIGN_IN, "签名"));         
    }
    
    @Test
    public void testGetExpireAccountTest(){
        when(individualAccountMapper.getExpireAccount(Mockito.any(Date.class))).thenReturn(new ArrayList<IndividualAccount>());
        assertNotNull(individualAccountService.getExpireAccount(new Date()));
        verify(individualAccountMapper,times(1)).getExpireAccount(Mockito.any(Date.class));
    }
    
    @Test
    public void testUpdateExpireTimeAndOrderId(){
        when(individualAccountMapper.updateByPrimaryKeySelective(Mockito.any(IndividualAccount.class))).thenReturn(1);
        assertTrue(individualAccountService.updateExpireTimeAndOrderId(1L, new Date(), 1L));
        verify(individualAccountMapper,times(1)).updateByPrimaryKeySelective(Mockito.any(IndividualAccount.class));
    }
    
    @Test
    public void testCheckAndInsertAccountForWx(){
        IndividualProduct score = new IndividualProduct();
        score.setId(1L);
        when(individualProductService.getIndivialPointProduct()).thenReturn(score);
        when(individualAccountMapper.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt())).thenReturn(new IndividualAccount());
        assertTrue(individualAccountService.checkAndInsertAccountForWx(1L, "openid"));
        
        when(individualAccountMapper.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt())).thenReturn(null);
        List<IndividualProduct> products = new ArrayList<IndividualProduct>();
        products.add(new IndividualProduct());
        when(individualProductService.selectByDefaultValue(1)).thenReturn(products);
        when(individualAccountMapper.batchInsert(Mockito.anyList())).thenReturn(2);
        try{
            individualAccountService.checkAndInsertAccountForWx(1L, "openid");           
        }catch(Exception e){
            assertEquals("插入individual_account失败！", e.getMessage());
        }
        
        when(individualAccountMapper.batchInsert(Mockito.anyList())).thenReturn(1);
        List<Tmpaccount> tmpAccounts = new ArrayList<Tmpaccount>();
        Tmpaccount tmpaccount = new Tmpaccount();
        tmpaccount.setCount(new BigDecimal(1));
        tmpAccounts.add(tmpaccount);        
        when(tmpaccountMapper.selectByOpenid("openid")).thenReturn(tmpAccounts);
        when(individualAccountMapper.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt()))
        .thenReturn(null).thenReturn(new IndividualAccount());
        when(individualAccountRecordService.create(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);
        when(individualAccountMapper.minusCount(Mockito.anyLong(), Mockito.any(BigDecimal.class))).thenReturn(1);
        when(individualAccountMapper.addCount(Mockito.anyLong(), Mockito.any(BigDecimal.class))).thenReturn(1);        
        when(individualAccountRecordService.updateByPrimaryKeySelective(Mockito.any(IndividualAccountRecord.class))).thenReturn(true);

        assertTrue(individualAccountService.checkAndInsertAccountForWx(1L, "openid"));
    }
    
    @Test
    public void testGetMembershipList(){
        IndividualProduct score = new IndividualProduct();
        score.setId(1L);
        when(individualProductService.getIndivialPointProduct()).thenReturn(score);
        List<Membership> lists = new ArrayList<Membership>();
        Membership list = new Membership();
        lists.add(list);
        when(individualAccountMapper.getMembershipList(Mockito.anyMap())).thenReturn(lists);
        when(individualAccountMapper.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt())).thenReturn(new IndividualAccount());
        when(individualAccountRecordService.selectAccumulateAccount(Mockito.anyLong())).thenReturn(new BigDecimal(10));
        List<WxGrade> grades = new ArrayList<WxGrade>();
        WxGrade grade = new WxGrade();
        grade.setPoints(1L);
        grades.add(grade);
        when(wxGradeService.selectAllGrade()).thenReturn(grades);
        when(administerService.selectByMobilePhone(Mockito.anyString())).thenReturn(new WxAdminister());
        assertNotNull(individualAccountService.getMembershipList(new HashMap()));
    }
    
    @Test
    public void testCountMembershipList(){
        IndividualProduct score = new IndividualProduct();
        score.setId(1L);
        when(individualProductService.getIndivialPointProduct()).thenReturn(score);
        when(individualAccountMapper.countMembershipList(Mockito.anyMap())).thenReturn(1);       
        assertNotNull(individualAccountService.countMembershipList(new HashMap()));
    }

    private Activities createActivity() {
        Activities activity = new Activities();
        activity.setName("11");
        activity.setType(1);
        activity.setId(1L);
        activity.setCreatorId(1L);
        return activity;
    }

    private List<ActivityPrize> createPrizeList() {
        List<ActivityPrize> list = new ArrayList();
        ActivityPrize prize = new ActivityPrize();
        prize.setActivityId("12345");
        prize.setProductId(1L);

        list.add(prize);

        return list;
    }

    private List<ActivityPrize> createPrizeListEmpty() {
        List<ActivityPrize> list = new ArrayList();
        ActivityPrize prize = new ActivityPrize();
        list.add(new ActivityPrize());

        return list;
    }

    private WxAdminister createAdmin() {
        WxAdminister admin = new WxAdminister();
        admin.setId(1L);
        admin.setMobilePhone("18867103685");
        return admin;
    }

    private IndividualAccount createAccount() {
        IndividualAccount account = new IndividualAccount();
        account.setId(1L);
        account.setIndividualProductId(1L);
        account.setCount(new BigDecimal(1));
        account.setAdminId(1L);
        return account;
    }

    private String randStr(int length) {
        StringBuilder sb = new StringBuilder();

        Random r = new Random();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + r.nextInt(26)));
        }

        return sb.toString();
    }

    private String randSN() {
        return randStr(10);
    }
}
