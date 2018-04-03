package com.cmcc.vrp.province.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.ec.bean.individual.IndividualRedpacketActivityParam;
import com.cmcc.vrp.ec.bean.individual.IndividualRedpacketReq;
import com.cmcc.vrp.province.activity.model.AutoResponsePojo;
import com.cmcc.vrp.province.model.Activities;
import com.cmcc.vrp.province.model.ActivityInfo;
import com.cmcc.vrp.province.model.ActivityPrize;
import com.cmcc.vrp.province.model.ActivityTemplate;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualActivitySerialNum;
import com.cmcc.vrp.province.model.IndividualFlowOrder;
import com.cmcc.vrp.province.model.IndividualProduct;
import com.cmcc.vrp.province.model.IndividualProductMap;
import com.cmcc.vrp.province.service.ActivitiesService;
import com.cmcc.vrp.province.service.ActivityInfoService;
import com.cmcc.vrp.province.service.ActivityPrizeService;
import com.cmcc.vrp.province.service.ActivityTemplateService;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualActivitiesService;
import com.cmcc.vrp.province.service.IndividualActivityOrderService;
import com.cmcc.vrp.province.service.IndividualActivitySerialNumService;
import com.cmcc.vrp.province.service.IndividualFlowOrderService;
import com.cmcc.vrp.province.service.IndividualProductMapService;
import com.cmcc.vrp.province.service.IndividualProductService;

/**
 * IndividualActivitiesServiceImplTest.java
 * @author wujiamin
 * @date 2017年1月22日
 */
@RunWith(MockitoJUnitRunner.class)
public class IndividualActivitiesServiceImplTest {
    @InjectMocks
    IndividualActivitiesService service = new IndividualActivitiesServiceImpl();
    
    @Mock
    AdministerService administerService;
    
    @Mock
    IndividualProductMapService individualProductMapService;
    
    @Mock
    GlobalConfigService globalConfigService;
    
    @Mock
    IndividualAccountService individualAccountService;
    
    @Mock
    IndividualProductService individualProductService;
    
    @Mock
    ActivitiesService activitiesService;
    
    @Mock
    ActivityPrizeService activityPrizeService;
    
    @Mock
    ActivityInfoService activityInfoService;
    
    @Mock
    ActivityTemplateService activityTemplateService;
    
    @Mock
    IndividualActivitySerialNumService individualActivitySerialNumService;
    
    @Mock
    IndividualFlowOrderService individualFlowOrderService;
    
    @Mock
    ActivityWinRecordService activityWinRecordService;
    
    @Mock
    IndividualActivityOrderService individualActivityOrderService;
    
    @Test
    public void testGenerateFlowRedpacket(){
        Mockito.when(administerService.selectByMobilePhone(Mockito.anyString())).thenReturn(null);
        Mockito.when(administerService.insertForScJizhong(Mockito.anyString())).thenReturn(false);
        assertNull(service.generateFlowRedpacket(createReq()));
        
        Mockito.when(administerService.selectByMobilePhone(Mockito.anyString())).thenReturn(createAdmin());

        IndividualProductMap individualProductMap = new IndividualProductMap();
        individualProductMap.setPrice(1);
        individualProductMap.setDiscount(100);
        individualProductMap.setIndividualProductId(1L);
        individualProductMap.setProductName("流量");
        Mockito.when(individualProductMapService.getByAdminIdAndProductType(Mockito.anyLong(), Mockito.anyInt())).thenReturn(individualProductMap);
        Mockito.when(globalConfigService.get("JIZHONG_ACTIVITY_CHECK_URL")).thenReturn("url");
        Mockito.when(individualProductMapService.getByAdminIdAndProductId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(individualProductMap);
        
        IndividualProduct product = new IndividualProduct();
        product.setId(1L);
        Mockito.when(individualProductService.getDefaultFlowProduct()).thenReturn(product);
        IndividualAccount account = new IndividualAccount();
        //余额不足
        account.setCount(new BigDecimal(5));
        Mockito.when(individualAccountService.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(), 
                Mockito.anyInt())).thenReturn(account);
        assertNull(service.generateFlowRedpacket(createReq()));
        
        //余额充足
        account.setCount(new BigDecimal(150));
        Mockito.when(individualAccountService.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(), 
                Mockito.anyInt())).thenReturn(account);

        Mockito.when(activitiesService.insert(Mockito.any(Activities.class))).thenReturn(true);
        Mockito.when(activityPrizeService.insertForRedpacket(Mockito.any(ActivityPrize.class))).thenReturn(true);
        Mockito.when(activityInfoService.insertForRedpacket(Mockito.any(ActivityInfo.class))).thenReturn(true);
        Mockito.when(activityTemplateService.insertSelective(Mockito.any(ActivityTemplate.class))).thenReturn(true);
        
        
        Mockito.when(individualActivitySerialNumService.insertSelective(Mockito.any(IndividualActivitySerialNum.class))).thenReturn(true);
        Mockito.when(activitiesService.createActivitySchedule(Mockito.anyString())).thenReturn(true);
        
        Mockito.when(activityPrizeService.selectByActivityIdForIndividual(Mockito.anyString())).thenReturn(null);
        assertNull(service.generateFlowRedpacket(createReq()));

        Mockito.when(activityPrizeService.selectByActivityIdForIndividual(Mockito.anyString())).thenReturn(createActivityPrizeList());
        Mockito.when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(createActivity());
        ActivityInfo info = new ActivityInfo();
        info.setTotalProductSize(1L);
        info.setUrl("test-activity-url");
        Mockito.when(activityInfoService.selectByActivityId(Mockito.anyString())).thenReturn(info);
        Mockito.when(individualAccountService.changeAccount(Mockito.any(IndividualAccount.class), Mockito.any(BigDecimal.class), Mockito.anyString(),
                Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(false);
        try{
            service.generateFlowRedpacket(createReq());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        Mockito.when(individualAccountService.changeAccount(Mockito.any(IndividualAccount.class), Mockito.any(BigDecimal.class), Mockito.anyString(),
                Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
        
        
        Mockito.when(individualAccountService.createAccountForActivity(Mockito.any(Activities.class), Mockito.any(ActivityInfo.class), Mockito.anyList(),
                Mockito.any(IndividualAccount.class))).thenReturn(true);
        assertNull(service.generateFlowRedpacket(createReq()));
        
        AutoResponsePojo pojo = new AutoResponsePojo();
        pojo.setCode(200);
        pojo.setMsg("success");
        pojo.setUrl("test-activity-url");
        Mockito.when(activitiesService.sendToGenerateActivity(Mockito.anyString())).thenReturn(pojo);
        Mockito.when(individualAccountService.insertAccountForScJizhong(Mockito.anyLong())).thenReturn(true);
        assertNotNull(service.generateFlowRedpacket(createReq()));
    }
    
    @Test
    public void testGenerateFlowRedpacketForPage(){
        Mockito.when(administerService.selectByMobilePhone(Mockito.anyString())).thenReturn(null);
        Mockito.when(administerService.insertForScJizhong(Mockito.anyString())).thenReturn(false);
        assertNull(service.generateFlowRedpacketForPage(createParam()));
        
        Mockito.when(administerService.selectByMobilePhone(Mockito.anyString())).thenReturn(createAdmin());
        Mockito.when(individualAccountService.insertAccountForScJizhong(Mockito.anyLong())).thenReturn(false);
        assertNull(service.generateFlowRedpacketForPage(createParam()));
        
        Mockito.when(individualAccountService.insertAccountForScJizhong(Mockito.anyLong())).thenReturn(true);
        List<Activities> actList = new ArrayList<Activities>();
        actList.add(new Activities());
        Mockito.when(activitiesService.selectForOrder(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(actList);
        Mockito.when(activityWinRecordService.countChargeMobileByActivityId(Mockito.anyString())).thenReturn(0);
        Mockito.when(activityInfoService.selectByActivityId(Mockito.anyString())).thenReturn(null);
        assertNull(service.generateFlowRedpacketForPage(createParam()));
        
        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setPrizeCount(1L);
        Mockito.when(activityInfoService.selectByActivityId(Mockito.anyString())).thenReturn(activityInfo);
        
        assertEquals(service.generateFlowRedpacketForPage(createParam()).get("url"), "processing");        
        
        IndividualProductMap individualProductMap = new IndividualProductMap();
        individualProductMap.setPrice(1);
        individualProductMap.setDiscount(100);
        individualProductMap.setIndividualProductId(1L);
        individualProductMap.setProductName("流量");
        Mockito.when(individualProductMapService.getByAdminIdAndProductType(Mockito.anyLong(), Mockito.anyInt())).thenReturn(individualProductMap);
        Mockito.when(globalConfigService.get("JIZHONG_ACTIVITY_CHECK_URL")).thenReturn("url");
        Mockito.when(individualProductMapService.getByAdminIdAndProductId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(individualProductMap);
        
        IndividualProduct product = new IndividualProduct();
        product.setId(1L);
        Mockito.when(individualProductService.getDefaultFlowProduct()).thenReturn(product);
        IndividualAccount account = new IndividualAccount();
        //余额不足
        account.setCount(new BigDecimal(5));
        Mockito.when(individualAccountService.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(), 
                Mockito.anyInt())).thenReturn(account);
        Mockito.when(individualFlowOrderService.selectBySystemNum(Mockito.anyString())).thenReturn(createFlowOrder());
        assertEquals(service.generateFlowRedpacketForPage(createParam()).get("url"), "processing");
        
        //余额充足
        account.setCount(new BigDecimal(150));
        Mockito.when(individualAccountService.getAccountByOwnerIdAndProductId(Mockito.anyLong(), Mockito.anyLong(), 
                Mockito.anyInt())).thenReturn(account);

        Mockito.when(activitiesService.insert(Mockito.any(Activities.class))).thenReturn(true);
        Mockito.when(activityPrizeService.insertForRedpacket(Mockito.any(ActivityPrize.class))).thenReturn(true);
        Mockito.when(activityInfoService.insertForRedpacket(Mockito.any(ActivityInfo.class))).thenReturn(true);
        Mockito.when(activityTemplateService.insertSelective(Mockito.any(ActivityTemplate.class))).thenReturn(true);
        
        
        Mockito.when(individualActivitySerialNumService.insertSelective(Mockito.any(IndividualActivitySerialNum.class))).thenReturn(true);
        Mockito.when(activitiesService.createActivitySchedule(Mockito.anyString())).thenReturn(true);
        
        Mockito.when(activityPrizeService.selectByActivityIdForIndividual(Mockito.anyString())).thenReturn(null);
        assertEquals(service.generateFlowRedpacketForPage(createParam()).get("url"), "processing");

        Mockito.when(activityPrizeService.selectByActivityIdForIndividual(Mockito.anyString())).thenReturn(createActivityPrizeList());
        Mockito.when(activitiesService.selectByActivityId(Mockito.anyString())).thenReturn(createActivity());
        ActivityInfo info = new ActivityInfo();
        info.setTotalProductSize(1L);
        info.setPrizeCount(0L);
        info.setUrl("test-activity-url");
        Mockito.when(activityInfoService.selectByActivityId(Mockito.anyString())).thenReturn(info);
        Mockito.when(individualAccountService.changeAccount(Mockito.any(IndividualAccount.class), Mockito.any(BigDecimal.class), Mockito.anyString(),
                Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(false);
        try{
            service.generateFlowRedpacketForPage(createParam());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        Mockito.when(individualAccountService.changeAccount(Mockito.any(IndividualAccount.class), Mockito.any(BigDecimal.class), Mockito.anyString(),
                Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
        
        
        Mockito.when(individualAccountService.createAccountForActivity(Mockito.any(Activities.class), Mockito.any(ActivityInfo.class), Mockito.anyList(),
                Mockito.any(IndividualAccount.class))).thenReturn(true);
        Mockito.when(individualActivityOrderService.insert(Mockito.anyString(), Mockito.anyLong())).thenReturn(true);
        
        assertNull(service.generateFlowRedpacketForPage(createParam()));
        
        AutoResponsePojo pojo = new AutoResponsePojo();
        pojo.setCode(200);
        pojo.setMsg("success");
        pojo.setUrl("test-activity-url");
        Mockito.when(activitiesService.sendToGenerateActivity(Mockito.anyString())).thenReturn(pojo);
        assertNotNull(service.generateFlowRedpacketForPage(createParam()));
    }
    
    @Test
    public void testInsertFlowRedpacket(){
        //insertFlowRedpacket异常检测
        Mockito.when(activitiesService.insert(Mockito.any(Activities.class))).thenReturn(false);
        assertFalse(service.insertFlowRedpacket(new Activities(), new ActivityPrize(), new ActivityInfo(), new ActivityTemplate()));

        Mockito.when(activitiesService.insert(Mockito.any(Activities.class))).thenReturn(true);
        Mockito.when(activityPrizeService.insertForRedpacket(Mockito.any(ActivityPrize.class))).thenReturn(false);
        try{
            service.insertFlowRedpacket(new Activities(), new ActivityPrize(), new ActivityInfo(), new ActivityTemplate());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        
        
        Mockito.when(activityInfoService.insertForRedpacket(Mockito.any(ActivityInfo.class))).thenReturn(true);
        Mockito.when(activityTemplateService.insertSelective(Mockito.any(ActivityTemplate.class))).thenReturn(false);
        try{
            service.insertFlowRedpacket(new Activities(), new ActivityPrize(), new ActivityInfo(), new ActivityTemplate());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        Mockito.when(activityTemplateService.insertSelective(Mockito.any(ActivityTemplate.class))).thenReturn(true);

    }

    private IndividualRedpacketReq createReq(){
        IndividualRedpacketReq request = new IndividualRedpacketReq();
        request.setEcSerialNumber("ecSerialTest");
        request.setParam(createParam());
        return request;
    }
    
    private IndividualRedpacketActivityParam createParam(){
        IndividualRedpacketActivityParam param = new IndividualRedpacketActivityParam();
        param.setMobile("18867100000");
        param.setActivityName("wjm测试活动11");
        param.setStartTime("20170122080000");
        param.setType(7);
        param.setSize(30L);
        param.setCount(5L);
        param.setObject("对象对象对象对象测试");
        param.setRules("规则规则规则规则测试");
        return param;
    }
    
    private Administer createAdmin(){
        Administer admin = new Administer();
        admin.setId(1L);
        return admin;
    }
    
    private List<ActivityPrize> createActivityPrizeList(){
        List<ActivityPrize> activityPrizes = new ArrayList<ActivityPrize>();
        ActivityPrize prize = new ActivityPrize();
        prize.setProductId(1L);
        activityPrizes.add(prize);
        return activityPrizes;
    }
    
    private Activities createActivity(){
        Activities activity = new Activities();
        activity.setCreatorId(1L);
        return activity;
    }
    
    private IndividualFlowOrder createFlowOrder(){
        IndividualFlowOrder flowOrder = new IndividualFlowOrder();
        flowOrder.setId(1L);
        return flowOrder;
    }
    
}
