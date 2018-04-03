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
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.enums.AccountType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.MinusCountReturnType;
import com.cmcc.vrp.exception.ProductInitException;
import com.cmcc.vrp.province.dao.MonthlyPresentRuleMapper;
import com.cmcc.vrp.province.model.EntProduct;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.MonthlyPresentRule;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.quartz.service.ScheduleService;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ActivityCreatorService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.MonthlyPresentRecordCopyService;
import com.cmcc.vrp.province.service.MonthlyPresentRecordService;
import com.cmcc.vrp.province.service.PresentSerialNumService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.VirtualProductService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.util.DateUtil;
import com.cmcc.vrp.util.QueryObject;

/**
 * 
 * @ClassName: MonthlyPresentRuleServiceImpTest 
 * @Description: TODO
 * @author: Rowe
 * @date: 2017年7月18日 上午9:41:38
 */
@RunWith(MockitoJUnitRunner.class)
public class MonthlyPresentRuleServiceImpTest {

    @InjectMocks
    MonthlyPresentRuleServiceImpl monthlyPresentRuleService = new MonthlyPresentRuleServiceImpl();

    @Mock
    MonthlyPresentRuleMapper monthlyPresentRuleMapper;

    @Mock
    MonthlyPresentRecordService monthlyPresentRecordService;

    @Mock
    ChargeRecordService chargeRecordService;

    @Mock
    EnterprisesService enterprisesService;

    @Mock
    EntProductService entProductService;

    @Mock
    ProductService productService;

    @Mock
    AccountService accountService;

    @Mock
    TaskProducer taskProducer;

    @Mock
    GlobalConfigService globalConfigService;

    @Mock
    VirtualProductService virtualProductService;
    
    @Mock
    SerialNumService serialNumService;
    
    @Mock
    ActivityCreatorService activityCreatorService;
    @Mock
    MonthlyPresentRecordCopyService monthlyPresentRecordCopyService;
    @Mock
    ScheduleService scheduleService;
    @Mock
    PresentSerialNumService presentSerialNumService;

    @Test
    public void testSelectByPrimaryKey() {
        MonthlyPresentRule rule = new MonthlyPresentRule();
        when(monthlyPresentRuleMapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(rule);
        assertSame(rule, monthlyPresentRuleService.selectByPrimaryKey(Mockito.anyLong()));
    }

    @Test
    public void testInsert() {
        MonthlyPresentRule rule = new MonthlyPresentRule();
        when(monthlyPresentRuleMapper.insert(Mockito.any(MonthlyPresentRule.class))).thenReturn(1);
        assertFalse(monthlyPresentRuleService.insert(rule));
    }

    @Test
    public void testGetRules() {
        List<MonthlyPresentRule> rules = new ArrayList<MonthlyPresentRule>();
        when(monthlyPresentRuleMapper.getRules(Mockito.anyMap())).thenReturn(rules);
        assertSame(rules, monthlyPresentRuleService.getRules(new QueryObject()));
    }

    @Test
    public void testCountRules() {
        Long count = 1L;
        when(monthlyPresentRuleMapper.countRules(Mockito.anyMap())).thenReturn(count);
        assertSame(count, monthlyPresentRuleService.countRules(new QueryObject()));
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        MonthlyPresentRule rule = new MonthlyPresentRule();
        when(monthlyPresentRuleMapper.updateByPrimaryKeySelective(Mockito.any(MonthlyPresentRule.class))).thenReturn(1);
        assertTrue(monthlyPresentRuleService.updateByPrimaryKeySelective(rule));
    }

    @Test
    public void testCreate() {
        
        when(chargeRecordService.batchInsert(Mockito.anyList())).thenReturn(true);
        when(chargeRecordService.batchUpdateStatusCode(Mockito.anyString(), Mockito.anyList())).thenReturn(true);
        when(monthlyPresentRecordService.batchUpdateStatusCode(Mockito.anyList(), Mockito.anyString())).thenReturn(true);

        String msg = "参数错误";
        assertSame(msg, monthlyPresentRuleService.create(null));

        MonthlyPresentRule rule = new MonthlyPresentRule();
        msg = "参数错误：企业不存在";
        when(enterprisesService.selectById(Mockito.anyLong())).thenReturn(null);
        assertSame(msg, monthlyPresentRuleService.create(rule));
        
        Enterprise enterprise = new Enterprise();
        msg = "参数错误：企业状态异常";
        enterprise.setDeleteFlag(1);
        when(enterprisesService.selectById(Mockito.anyLong())).thenReturn(enterprise);
        assertSame(msg, monthlyPresentRuleService.create(rule));

        msg = "参数错误:企业未订购该产品";
        enterprise.setDeleteFlag(0);
//        enterprise.setStartTime(DateUtil.getDateBefore(new Date(), 1));
//        enterprise.setEndTime(DateUtil.getDateBefore(new Date(), 1));
        when(enterprisesService.selectById(Mockito.anyLong())).thenReturn(enterprise);
        when(entProductService.selectByProductIDAndEnterprizeID(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);
        assertSame(msg, monthlyPresentRuleService.create(rule));

        EntProduct entProduct = new EntProduct();
        msg = "参数错误：产品不存在";
        when(enterprisesService.selectById(Mockito.anyLong())).thenReturn(enterprise);
        when(entProductService.selectByProductIDAndEnterprizeID(Mockito.anyLong(), Mockito.anyLong())).thenReturn(
                entProduct);
        when(productService.selectProductById(Mockito.anyLong())).thenReturn(null);
        assertSame(msg, monthlyPresentRuleService.create(rule));

        Product product = new Product();
        product.setType(1);
        
        String size = "1024";
        rule.setSize(size);
        rule.setPrdId(1L);
        when(enterprisesService.selectById(Mockito.anyLong())).thenReturn(enterprise);
        when(entProductService.selectByProductIDAndEnterprizeID(Mockito.anyLong(), Mockito.anyLong())).thenReturn(
                entProduct);
        when(productService.selectProductById(Mockito.anyLong())).thenReturn(product);
        try {
            when(virtualProductService.initProcess(Mockito.anyLong(), Mockito.anyString())).thenReturn(product);
            assertSame(msg, monthlyPresentRuleService.create(rule));
        } catch (Exception e) {
            // TODO: handle exception
        }

        
        size = "2048";
        rule.setSize(size);
        rule.setEntId(2L);
        rule.setPrdId(2L);
        rule.setMonthCount(2);
        rule.setTotal(2);
        product.setId(2L);
        product.setName("2");
        try {
            Mockito.when(virtualProductService.initProcess(rule.getPrdId(), rule.getSize())).thenReturn(product);
        } catch (ProductInitException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
                
        MinusCountReturnType result = MinusCountReturnType.ACCOUNT_NOTEXIST;        
        Mockito.when(accountService.minusCount(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(AccountType.class), Mockito.anyDouble(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean())).thenReturn(result);
        assertSame(result.getMsg(), monthlyPresentRuleService.create(rule));

        result = MinusCountReturnType.OK;
        msg = "生成包月赠送活动失败";
        Mockito.when(accountService.minusCount(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(AccountType.class), Mockito.anyDouble(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean())).thenReturn(result);
        when(monthlyPresentRuleMapper.insertSelective(rule)).thenReturn(0);
        assertSame(msg, monthlyPresentRuleService.create(rule));

        List phonesList = new ArrayList<String>();
        phonesList.add("18867101970");
        rule.setPhonesList(phonesList);
        msg = "生成包月赠送充值记录失败";
        when(monthlyPresentRuleMapper.insertSelective(rule)).thenReturn(1);
        when(monthlyPresentRecordService.batchInsert(Mockito.anyList())).thenReturn(false);
        assertSame(msg, monthlyPresentRuleService.create(rule));

        msg = "包月赠送生成serialNum记录失败";
        when(monthlyPresentRuleMapper.insertSelective(rule)).thenReturn(1);
        when(monthlyPresentRecordService.batchInsert(Mockito.anyList())).thenReturn(true);
        when(serialNumService.batchInsert(Mockito.anyList())).thenReturn(false);
        assertSame(msg, monthlyPresentRuleService.create(rule));
        
        msg = "插入充值记录时出错";
        when(serialNumService.batchInsert(Mockito.anyList())).thenReturn(true);
        when(chargeRecordService.batchInsert(Mockito.anyList())).thenReturn(false);
        assertSame(msg, monthlyPresentRuleService.create(rule));

        msg = "包月赠送活动失败，进队列失败";
        when(serialNumService.batchInsert(Mockito.anyList())).thenReturn(true);
        when(chargeRecordService.batchInsert(Mockito.anyList())).thenReturn(true);      
        when(taskProducer.produceMonthPresentMsg(Mockito.anyList())).thenReturn(false);
        assertSame(msg, monthlyPresentRuleService.create(rule));
        
        msg = "包月赠送活动失败，更新赠送记录状态码失败";
        when(taskProducer.produceMonthPresentMsg(Mockito.anyList())).thenReturn(true);
        when(monthlyPresentRecordService.batchUpdateStatusCode(Mockito.anyList(), Mockito.anyString())).thenReturn(false);
        when(presentSerialNumService.batchInsert(Mockito.anyString(), Mockito.anyList())).thenReturn(true);
        assertSame(msg, monthlyPresentRuleService.create(rule));
               
        msg = "包月赠送活动失败，更新充值记录失败";
        when(activityCreatorService.insert(Mockito.any(ActivityType.class), Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);
        when(monthlyPresentRecordService.batchUpdateStatusCode(Mockito.anyList(), Mockito.anyString())).thenReturn(true);
        when(chargeRecordService.batchUpdateStatusCode(Mockito.anyString(),Mockito.anyList())).thenReturn(false);
        assertSame(msg, monthlyPresentRuleService.create(rule));
        
        when(chargeRecordService.batchUpdateStatusCode(Mockito.anyString(),Mockito.anyList())).thenReturn(true);
        assertNull(monthlyPresentRuleService.create(rule));
    }

    @Test
    public void testGetDetailByRuleId() {
        MonthlyPresentRule rule = new MonthlyPresentRule();
        when(monthlyPresentRuleMapper.getDetailByRuleId(Mockito.any(Long.class))).thenReturn(rule);
        assertSame(rule, monthlyPresentRuleService.getDetailByRuleId(1L));
    }
    
    @Test
    public void testUpdateRuleStatusFini(){
        when(monthlyPresentRuleMapper.updateRuleStatusFini()).thenReturn(1);
        monthlyPresentRuleService.updateRuleStatusFini();
    }
    
    /** 
    * @Title: testUnSdCreate 
    * @Description: 
    * @throws 
    */
    @Test
    public void testUnSdCreate() {
        when(chargeRecordService.batchInsert(Mockito.anyList())).thenReturn(true);
        when(chargeRecordService.batchUpdateStatusCode(Mockito.anyString(), Mockito.anyList())).thenReturn(true);
        when(monthlyPresentRecordService.batchUpdateStatusCode(Mockito.anyList(), Mockito.anyString())).thenReturn(true);

        String msg = "参数错误";
        assertSame(msg, monthlyPresentRuleService.unSdcreate(null));

        MonthlyPresentRule rule = new MonthlyPresentRule();
        msg = "参数错误：企业不存在";
        when(enterprisesService.selectById(Mockito.anyLong())).thenReturn(null);
        assertSame(msg, monthlyPresentRuleService.unSdcreate(rule));
        
        Enterprise enterprise = new Enterprise();
        msg = "参数错误：企业状态异常";
        enterprise.setDeleteFlag(1);
        when(enterprisesService.selectById(Mockito.anyLong())).thenReturn(enterprise);
        assertSame(msg, monthlyPresentRuleService.unSdcreate(rule));

        msg = "参数错误:企业未订购该产品";
        enterprise.setDeleteFlag(0);
//        enterprise.setStartTime(DateUtil.getDateBefore(new Date(), 1));
//        enterprise.setEndTime(DateUtil.getDateBefore(new Date(), 1));
        when(enterprisesService.selectById(Mockito.anyLong())).thenReturn(enterprise);
        when(entProductService.selectByProductIDAndEnterprizeID(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);
        assertSame(msg, monthlyPresentRuleService.unSdcreate(rule));

        EntProduct entProduct = new EntProduct();
        msg = "参数错误：产品不存在";
        when(enterprisesService.selectById(Mockito.anyLong())).thenReturn(enterprise);
        when(entProductService.selectByProductIDAndEnterprizeID(Mockito.anyLong(), Mockito.anyLong())).thenReturn(
                entProduct);
        when(productService.selectProductById(Mockito.anyLong())).thenReturn(null);
        assertSame(msg, monthlyPresentRuleService.unSdcreate(rule));

        Product product = new Product();
        product.setType(1);
        
        String size = "1024";
        rule.setSize(size);
        rule.setPrdId(1L);
        List<String> list = new ArrayList<String>();
        list.add("18867105766");
        rule.setPhonesList(list);
        rule.setStartTime(new Date());
        rule.setId(1l);
        when(enterprisesService.selectById(Mockito.anyLong())).thenReturn(enterprise);
        when(entProductService.selectByProductIDAndEnterprizeID(Mockito.anyLong(), Mockito.anyLong())).thenReturn(
                entProduct);
        when(productService.selectProductById(Mockito.anyLong())).thenReturn(product);
        try {
            when(virtualProductService.initProcess(Mockito.anyLong(), Mockito.anyString())).thenReturn(product);
            when(monthlyPresentRuleMapper.insertSelective(Mockito.any(MonthlyPresentRule.class))).thenReturn(0).thenReturn(1);
            msg="生成包月赠送活动失败";
            assertSame(msg, monthlyPresentRuleService.unSdcreate(rule));
            msg = "包月赠送活动失败，插入活动创建者表失败";
            when(activityCreatorService.insert(Mockito.any(ActivityType.class), Mockito.anyLong(), Mockito.anyLong())).thenReturn(false).thenReturn(true);
            assertSame(msg, monthlyPresentRuleService.unSdcreate(rule));
        } catch (Exception e) {
            // TODO: handle exception
        }

        
        size = "2048";
        rule.setSize(size);
        rule.setEntId(2L);
        rule.setPrdId(2L);
        rule.setMonthCount(2);
        rule.setTotal(2);
        product.setId(2L);
        product.setName("2");
        try {
            Mockito.when(virtualProductService.initProcess(rule.getPrdId(), rule.getSize())).thenReturn(product);
            Mockito.when(monthlyPresentRecordCopyService.batchInsert(Mockito.anyList())).thenReturn(false).thenReturn(true);
        } catch (ProductInitException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        msg="生成包月赠送充值记录失败";
        assertSame(msg, monthlyPresentRuleService.unSdcreate(rule));
        Mockito.when(scheduleService.createScheduleJob(Mockito.any(Class.class),Mockito.any(String.class),Mockito.any(String.class),
                Mockito.any(String.class),Mockito.any(Date.class))).thenReturn("fail").thenReturn("success");
        msg = "生成定时任务失败";
        assertSame(msg, monthlyPresentRuleService.unSdcreate(rule));
        Assert.assertNull(monthlyPresentRuleService.unSdcreate(rule));
        rule.setStartTime(DateUtil.addMins(new Date(), 100));
        Mockito.when(scheduleService.createScheduleJob(Mockito.any(Class.class),Mockito.any(String.class),Mockito.any(String.class),
                Mockito.any(String.class),Mockito.any(Date.class))).thenReturn("fail").thenReturn("success");
        msg = "生成定时任务失败";
        assertSame(msg, monthlyPresentRuleService.unSdcreate(rule));
        Assert.assertNull(monthlyPresentRuleService.unSdcreate(rule));
    }
}
