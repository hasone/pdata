//package com.cmcc.vrp.wx;
//
//import static org.junit.Assert.assertNotNull;
//import static org.mockito.Matchers.anyLong;
//import static org.mockito.Mockito.when;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import com.cmcc.vrp.province.model.IndividualProduct;
//import com.cmcc.vrp.province.service.GlobalConfigService;
//import com.cmcc.vrp.province.service.IndividualProductService;
//import com.cmcc.vrp.wx.flowcoin.GdFlowCoinCharge;
//
///**
//* <p>Title: </p>
//* <p>Description: </p>
//* @author lgk8023
//* @date 2017年3月21日 下午1:44:31
//*/
//@RunWith(MockitoJUnitRunner.class)
//public class GdFlowCoinChargeTest {
//    @InjectMocks
//    GdFlowCoinCharge gdFlowCoinCharge = new GdFlowCoinCharge();
//
//    @Mock
//    private GlobalConfigService globalConfigService;
//    
//    @Mock
//    private IndividualProductService individualProductService;
//    
//    @Mock
//    WxExchangeRecordService wxExchangeRecordService;
//    /**
//     * 
//     */
//    @Test
//    public void chargeTest() {
//        IndividualProduct individualProduct = new IndividualProduct();
//        individualProduct.setProductCode("prod.10086000001992");
//        individualProduct.setFeature("{serviceCode:8585.memserv3}");
//        
//        
//        when(individualProductService.selectByPrimaryId(anyLong())).thenReturn(individualProduct);
//        assertNotNull(gdFlowCoinCharge.chargeFlow("18218940806", 1l, "12345678"));     
//    }
//}
