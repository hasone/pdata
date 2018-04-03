//package com.cmcc.vrp.wx;
//
//import static org.junit.Assert.assertNotNull;
//import static org.mockito.Matchers.any;
//import static org.mockito.Mockito.when;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import com.cmcc.vrp.province.model.ChargeRecord;
//import com.cmcc.vrp.province.model.SerialNum;
//import com.cmcc.vrp.province.service.ChargeRecordService;
//import com.cmcc.vrp.province.service.GlobalConfigService;
//import com.cmcc.vrp.province.service.SerialNumService;
//import com.cmcc.vrp.wx.impl.GdzcBossQuery;
//
///**
//* <p>Title: </p>
//* <p>Description: </p>
//* @author lgk8023
//* @date 2017年3月17日 下午4:28:59
//*/
//@RunWith(MockitoJUnitRunner.class)
//public class GdZcBossQueryTest {
//    
//    @InjectMocks
//    GdzcBossQuery gdzcBossQuery = new GdzcBossQuery();
//    
//    @Mock
//    private ChargeRecordService chargeRecordService;
//
//    @Mock
//    private SerialNumService serialNumService;
//    
//    @Mock
//    GlobalConfigService globalConfigService;
//    
//    /**
//     * 
//     */
//    @Test
//    public void testQueryStatus() {
//        ChargeRecord chargeRecord = new ChargeRecord();
//        chargeRecord.setPhone("");
//        SerialNum serialNum = new SerialNum();
//        serialNum.setBossReqSerialNum("LEC200070280420170320091228290404344");
//        serialNum.setBossRespSerialNum("SAD200070280420170320091229084811817");
//        when(chargeRecordService.getRecordBySN(any(String.class))).thenReturn(chargeRecord);
//        when(serialNumService.getByPltSerialNum(any(String.class))).thenReturn(serialNum);
//        assertNotNull(gdzcBossQuery.queryStatus("LEC200070280420170320091228290404344"));
//    }
//}
