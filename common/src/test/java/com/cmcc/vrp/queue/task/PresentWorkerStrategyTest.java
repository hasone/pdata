package com.cmcc.vrp.queue.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.charge.ChargeResult.ChargeResultCode;
import com.cmcc.vrp.charge.ChargeService;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.enums.ChargeType;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.PresentRecordService;
import com.cmcc.vrp.province.service.PresentRuleService;
import com.cmcc.vrp.province.service.PresentSerialNumService;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.pojo.BlockPresentPojo;
import com.cmcc.vrp.queue.pojo.PresentPojo;
import com.cmcc.vrp.queue.queue.busi.DeliverByBossQueue;
import com.cmcc.vrp.queue.task.strategy.Info;

@RunWith(MockitoJUnitRunner.class)
public class PresentWorkerStrategyTest {
    @InjectMocks
    PresentWorkerStrategy presentWorkerStrategy= new PresentWorkerStrategy();
    
    @Mock
    PresentRecordService presentRecordService;

    @Mock
    PresentRuleService presentRuleService;

    @Mock
    ChargeService chargeService;

    @Mock
    ChargeRecordService chargeRecordService;

    @Mock
    SerialNumService serialNumService;

    @Mock
    AccountService accountService;

    @Mock
    EntProductService entProductService;

    @Mock
    ProductService productService;

    @Mock
    PresentSerialNumService presentSerialNumService;

    @Mock
    ApplicationContext applicationContext;
    
    @Mock
    DeliverByBossQueue deliverByBossQueue;
    
    @Test
    public void testGetPresentPojos(){
        String taskString = JSON.toJSONString(initBlockPresentPojo());
        presentWorkerStrategy.getPresentPojos(taskString);
        presentWorkerStrategy.getPresentPojos("{");
    }
    
    @Test
    public void testBuildChargeRecords(){
        presentWorkerStrategy.buildChargeRecords(initListPojo(), "aaa");
    }
    
    @Test
    public void testBuildSerialNums(){
        presentWorkerStrategy.buildSerialNums(initChargeRecords());
        presentWorkerStrategy.buildInfos(initChargeRecords());
    }
    
    @Test
    public void testUpdateStatusCode(){
        List<Long> ids = new ArrayList<Long>();
        ids.add(1L);
        
        Mockito.when(presentRecordService.batchUpdateStatusCode(ids, ChargeResult.ChargeMsgCode.deliverQueue.getCode()))
            .thenReturn(true);
        presentWorkerStrategy.updateStatusCode(ids);
        
        presentWorkerStrategy.setChargeType(ChargeType.REDPACKET_TASK);
        presentWorkerStrategy.setActivityType(ActivityType.COMMON_REDPACKET);
        presentWorkerStrategy.updateStatusCode(ids);
    }
    
    @Test
    public void testRefund(){
        List<Info> failInfos = getInfos();
        Mockito.when(accountService.returnFunds(Mockito.anyString(), Mockito.any(ActivityType.class), Mockito.anyLong(), 
                        Mockito.anyInt())).thenReturn(true);
        Mockito.when(chargeRecordService.updateBySystemNum(Mockito.anyString(), Mockito.anyInt(), 
                Mockito.anyString(), Mockito.anyInt(), Mockito.any(Date.class))).thenReturn(true);
        presentWorkerStrategy.refund(failInfos);
        
        Mockito.when(accountService.returnFunds(Mockito.anyString(), Mockito.any(ActivityType.class), Mockito.anyLong(), 
                Mockito.anyInt())).thenReturn(false);
        Mockito.when(chargeRecordService.updateBySystemNum(Mockito.anyString(), Mockito.anyInt(), 
                Mockito.anyString(), Mockito.anyInt(), Mockito.any(Date.class))).thenReturn(false);
        presentWorkerStrategy.refund(failInfos);
        
        presentWorkerStrategy.refund(new ArrayList<Info>());
        
    }
    
    @Test
    public void testGetFailInfo(){
        List<Info> failInfos = getInfos();
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setPrdId(1L);
        
        
        Info info1=new Info(chargeRecord, new ChargeResult(ChargeResultCode.FAILURE));
        failInfos.add(info1);
        presentWorkerStrategy.getFailInfo(failInfos);
        
    }
    
    @Test
    public void testBuildChargeDeliverPojo(){
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setPrdId(1L);
        presentWorkerStrategy.buildChargeDeliverPojo(chargeRecord, "aaa");
    }
    
    /**
     * initBlockPresentPojo
     */
    private BlockPresentPojo initBlockPresentPojo(){
        BlockPresentPojo pojo = new BlockPresentPojo();
        List<PresentPojo> pojos = new ArrayList<PresentPojo>();
        PresentPojo ppojo = new PresentPojo();
        ppojo.setCount(1);
        ppojo.setEnterpriseId(1L);
        ppojo.setMobile("18867102100");
        ppojo.setProductId(1L);
        ppojo.setRecordId(1L);
        ppojo.setRequestSerialNum("1");
        ppojo.setRuleId(1L);
        pojos.add(ppojo);
        pojo.setPojos(pojos);
        pojo.setSerialNum("1");
        
        return pojo;
    }
    
    /**
     * initListPojo
     */
    private List<PresentPojo> initListPojo(){
        List<PresentPojo> pojos = new ArrayList<PresentPojo>();
        PresentPojo ppojo = new PresentPojo();
        ppojo.setCount(1);
        ppojo.setEnterpriseId(1L);
        ppojo.setMobile("18867102100");
        ppojo.setProductId(1L);
        ppojo.setRecordId(1L);
        ppojo.setRequestSerialNum("1");
        ppojo.setRuleId(1L);
        pojos.add(ppojo);
        return pojos;
    }
    
    /**
     * initChargeRecords()
     */
    private List<ChargeRecord> initChargeRecords(){
        List<ChargeRecord> list= new ArrayList<ChargeRecord>();
        ChargeRecord chargeRecord =new ChargeRecord();
        chargeRecord.setSystemNum("111");
        list.add(chargeRecord);
        
        return list;
    }
    
    /**
     * initChargeRecordSerialNum()
     */
    /*private List<ChargeRecordSerialNum> initChargeRecordSerialNum(){
        List<ChargeRecordSerialNum> list = new ArrayList<ChargeRecordSerialNum>();
        ChargeRecord chargeRecord =new ChargeRecord();
        chargeRecord.setSystemNum("111");
        SerialNum serialNum =new SerialNum();
        serialNum.setPlatformSerialNum("1");
        ChargeRecordSerialNum chargeRecordSerialNum = new ChargeRecordSerialNum(chargeRecord,serialNum);
        
        list.add(chargeRecordSerialNum);
        return list;
    }*/
    
    /**
     * getInfos()
     */
    private List<Info> getInfos(){
        List<Info> infos = new ArrayList<Info>();
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setPrdId(1L);
        
        
        Info info1=new Info(chargeRecord, new ChargeResult(ChargeResultCode.SUCCESS));
        infos.add(info1);
        return infos;
    }
}
