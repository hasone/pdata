//package com.cmcc.vrp.wx;
//
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Matchers.anyLong;
//import static org.mockito.Mockito.when;
//
//import java.rmi.RemoteException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//import static org.mockito.Matchers.any;
//
//import com.cmcc.vrp.province.model.ActivityPaymentInfo;
//import com.cmcc.vrp.province.model.ActivityWinRecord;
//import com.cmcc.vrp.province.model.CrowdfundingActivityDetail;
//import com.cmcc.vrp.province.model.Enterprise;
//import com.cmcc.vrp.province.model.EnterprisesExtInfo;
//import com.cmcc.vrp.province.model.SupplierProduct;
//import com.cmcc.vrp.province.service.ActivityPaymentInfoService;
//import com.cmcc.vrp.province.service.ActivityWinRecordService;
//import com.cmcc.vrp.province.service.CrowdfundingActivityDetailService;
//import com.cmcc.vrp.province.service.EnterprisesExtInfoService;
//import com.cmcc.vrp.province.service.EnterprisesService;
//import com.cmcc.vrp.province.service.GlobalConfigService;
//import com.cmcc.vrp.province.service.SerialNumService;
//import com.cmcc.vrp.province.service.SupplierProductService;
//import com.cmcc.vrp.util.GlobalConfigKeyEnum;
//import com.cmcc.vrp.wx.impl.GdZcBossServiceImpl;
//
///**
//* <p>Title: </p>
//* <p>Description: </p>
//* @author lgk8023
//* @date 2017年3月17日 下午1:51:40
//*/
//@RunWith(MockitoJUnitRunner.class)
//public class GdZcBossServicesImplTest{
//    @InjectMocks
//    GdZcBossServiceImpl gdZcBossService = new GdZcBossServiceImpl();
//
//    
//    @Mock
//    EnterprisesService enterprisesService;
//    
//    @Mock
//    SupplierProductService supplierProductService;
//    
//    @Mock
//    EnterprisesExtInfoService enterprisesExtInfoService;
//    
//    @Mock
//    GlobalConfigService globalConfigService;
//    
//    @Mock
//    ActivityPaymentInfoService activityPaymentInfoService;
//    
//    @Mock
//    ActivityWinRecordService activityWinRecordService;
//     
//    @Mock
//    CrowdfundingActivityDetailService crowdfundingActivityDetailService;
//    
//    @Mock
//    SerialNumService serialNumService;
//    /**
//     * @throws RemoteException
//     */
//    @Before
//    public void initMocks() throws RemoteException {
//
//        when(globalConfigService.get(GlobalConfigKeyEnum.GUANGDONG_ZHONGCHOU_CHARGE_OPTTYPE.getKey())).thenReturn("0");
//        when(globalConfigService.get(GlobalConfigKeyEnum.GUANGDONG_ZHONGCHOU_CHARGE_USECYCLE.getKey())).thenReturn("3");
//        when(globalConfigService.get(GlobalConfigKeyEnum.GUANGDONG_ZHONGCHOU_CHARGE_URL.getKey())).thenReturn("http://221.179.7.250/GDADC_W/NGADCABInterface/TrafficZC/PmpServicesRec.aspx");
//        when(globalConfigService.get(GlobalConfigKeyEnum.GUANGDONG_ZHONGCHOU_LEC_ORDERRELATION_URL.getKey())).thenReturn("http://221.179.7.250/GDADC_W/NGADCABInterface/TrafficZC/PmpServicesRec.aspx");
//        when(globalConfigService.get(GlobalConfigKeyEnum.DYNAMIC_PROXY_BOSS_FLAG.getKey())).thenReturn("false");           
//    }
//    
//    /**
//     * 
//     */
//    @Test
//    public void chargeTest() {
//        SupplierProduct supplierProduct = new SupplierProduct();
//        supplierProduct.setCode("prod.10086000009045");
//        supplierProduct.setFeature("{serviceCode:DiscountNoFee,months:1,mainPrdCode:AppendAttr.8590,mainServiceCode:service.8590}");
//        
//        Enterprise enterprise = new Enterprise();
//        enterprise.setCode("2000703233");
//        
//        EnterprisesExtInfo enterprisesExtInfo = new EnterprisesExtInfo();
//        enterprisesExtInfo.setEcCode("2000703233");
//        enterprisesExtInfo.setEcPrdCode("20170825006");
//               
//        List<ActivityPaymentInfo> activityPaymentInfos = new ArrayList<ActivityPaymentInfo>();
//        ActivityPaymentInfo activityPaymentInfo = new ActivityPaymentInfo();
//        activityPaymentInfo.setReturnPayAmount(12l);
//        activityPaymentInfo.setReturnPayNum("20160425102823");
//        activityPaymentInfos.add(activityPaymentInfo);
//        
//        ActivityWinRecord activityWinRecord = new ActivityWinRecord();
//        activityWinRecord.setActivityId("123456789");
//        
//        CrowdfundingActivityDetail crowdfundingActivityDetail = new CrowdfundingActivityDetail();
//        crowdfundingActivityDetail.setChargeType(1);
//        crowdfundingActivityDetail.setJoinType(1);
//        
//        when(supplierProductService.selectByPrimaryKey(anyLong())).thenReturn(supplierProduct);
//        when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(enterprise);
//        when(enterprisesExtInfoService.get(anyLong())).thenReturn(enterprisesExtInfo);
//        
//        when(activityPaymentInfoService.selectByWinRecordIdAndStatus(any(String.class), any(Integer.class))).thenReturn(activityPaymentInfos);
//        when(activityWinRecordService.selectByRecordId(any(String.class))).thenReturn(activityWinRecord);
//        when(crowdfundingActivityDetailService.selectByActivityId(any(String.class))).thenReturn(crowdfundingActivityDetail);
//        when(supplierProductService.selectById(anyLong())).thenReturn(supplierProduct);
//        //assertNotNull(gdZcBossService.charge(1l, 1l, "13590579551", "1234", 1l));    
//        System.out.println(gdZcBossService.charge(1l, 1l, "13809753861", "1234", 1l).getResultDesc());
//    }
//    /**
//     * 
//     */
//    @Test
//    public void chargeTest1() {
//        SupplierProduct supplierProduct = new SupplierProduct();
//        supplierProduct.setCode("prod.10086000001992");
//        supplierProduct.setFeature("{serviceCode:8585.memserv3}");
//        
//        Enterprise enterprise = new Enterprise();
//        enterprise.setCode("2000188888");
//        
//        EnterprisesExtInfo enterprisesExtInfo = new EnterprisesExtInfo();
//        enterprisesExtInfo.setEcCode("2000188888");
//        enterprisesExtInfo.setEcPrdCode("50115100100");
//               
//        List<ActivityPaymentInfo> activityPaymentInfos = new ArrayList<ActivityPaymentInfo>();
//        ActivityPaymentInfo activityPaymentInfo = new ActivityPaymentInfo();
//        activityPaymentInfo.setReturnPayAmount(12l);
//        activityPaymentInfo.setReturnPayNum("20160425102823");
//        activityPaymentInfos.add(activityPaymentInfo);
//        
//        ActivityWinRecord activityWinRecord = new ActivityWinRecord();
//        activityWinRecord.setActivityId("123456789");
//        
//        CrowdfundingActivityDetail crowdfundingActivityDetail = new CrowdfundingActivityDetail();
//        crowdfundingActivityDetail.setChargeType(1);
//        crowdfundingActivityDetail.setJoinType(2);
//        
//        when(supplierProductService.selectByPrimaryKey(anyLong())).thenReturn(supplierProduct);
//        when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(enterprise);
//        when(enterprisesExtInfoService.get(anyLong())).thenReturn(enterprisesExtInfo);
//        
//        when(activityPaymentInfoService.selectByWinRecordIdAndStatus(any(String.class), any(Integer.class))).thenReturn(activityPaymentInfos);
//        when(activityWinRecordService.selectByRecordId(any(String.class))).thenReturn(activityWinRecord);
//        when(crowdfundingActivityDetailService.selectByActivityId(any(String.class))).thenReturn(crowdfundingActivityDetail);
//        when(supplierProductService.selectById(anyLong())).thenReturn(supplierProduct);
//        assertNotNull(gdZcBossService.charge(1l, 1l, "13590579551", "1234", 1l));     
//    }
//
//    /**
//     * @throws Exception
//     */
//    @Test
//    public void testOrderRelation() throws Exception {
//        EnterprisesExtInfo enterprisesExtInfo = new EnterprisesExtInfo();
//        enterprisesExtInfo.setEcCode("2000703233");
//        enterprisesExtInfo.setEcPrdCode("20170825006");
//        
//        when(enterprisesExtInfoService.get(anyLong())).thenReturn(enterprisesExtInfo);
//        assertTrue(gdZcBossService.orderRelation(1l, "0"));        
//    }
//}
