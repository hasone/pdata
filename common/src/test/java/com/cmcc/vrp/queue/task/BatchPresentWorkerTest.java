package com.cmcc.vrp.queue.task;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.charge.ChargeResult.ChargeResultCode;
import com.cmcc.vrp.charge.ChargeService;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.PresentRule;
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
import com.cmcc.vrp.queue.task.strategy.ChargeRecordSerialNum;
import com.cmcc.vrp.queue.task.strategy.Info;

/**
 * BatchPresentWorkerTest.java
 *
 * @author wujiamin
 * @date 2016年11月9日
 */

@RunWith(PowerMockRunner.class)
public class BatchPresentWorkerTest {
    @InjectMocks
    BatchPresentWorker worker = new BatchPresentWorker();

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
    PresentWorkerStrategy presentWorkerStrategy;

    

    /**
     * testExec
     */
    @Test
    @PrepareForTest(DeliverByBossQueue.class)
    public void testExec() {

        presentWorkerStrategy.getPresentPojos(JSONObject.toJSONString(createPojo()));
        
        
        PowerMockito.when(applicationContext.getBean(PresentWorkerStrategy.class)).thenReturn(presentWorkerStrategy);
        PowerMockito.when(presentWorkerStrategy.getPresentPojos(Mockito.anyString())).thenReturn(getListpojo());
        when(presentRuleService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(new PresentRule());
        //worker.setTaskString(JSONObject.toJSONString(createPojo()));
        PowerMockito.when(presentWorkerStrategy.buildChargeRecords(Mockito.anyList(),Mockito.anyString())).
            thenReturn(new ArrayList<ChargeRecord>());
        PowerMockito.when(presentWorkerStrategy.buildSerialNums(Mockito.anyList())).
            thenReturn(new ArrayList<ChargeRecordSerialNum>());
        
        List<Info> infos = getInfos();
        PowerMockito.when(presentWorkerStrategy.buildInfos(Mockito.anyList())).
            thenReturn(infos);
        PowerMockito.when(presentWorkerStrategy.batchCharge(Mockito.anyList(),Mockito.anyString())).
            thenReturn(infos);
        PowerMockito.when(presentRecordService.batchUpdateChargeResult(Mockito.anyList())).thenReturn(true);
        PowerMockito.when(presentWorkerStrategy.refund(Mockito.anyList())).thenReturn(true);
        worker.exec();
        
        PowerMockito.when(presentRecordService.batchUpdateChargeResult(Mockito.anyList())).thenReturn(false);     
        worker.exec();
        
        PowerMockito.when(presentWorkerStrategy.batchCharge(Mockito.anyList(),Mockito.anyString())).
            thenReturn(new ArrayList<Info>());
        worker.exec();
        
        PowerMockito.when(presentWorkerStrategy.batchCharge(Mockito.anyList(),Mockito.anyString())).
            thenReturn(null);
        worker.exec();
        
        when(presentRuleService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(null);
        worker.exec();
        
        PowerMockito.when(presentWorkerStrategy.getPresentPojos(Mockito.anyString())).thenReturn(new ArrayList<PresentPojo>());
        worker.exec();
        
        PowerMockito.when(presentWorkerStrategy.getPresentPojos(Mockito.anyString())).thenReturn(null);
        worker.exec();
    }
    
    /**
     * createPojo
     */
    private BlockPresentPojo createPojo() {
        BlockPresentPojo pojo = new BlockPresentPojo();
        pojo.setSerialNum("1223");

        List<PresentPojo> pojos = new ArrayList<PresentPojo>();
        PresentPojo pp = new PresentPojo();
        pp.setRecordId(1L);
        pp.setMobile("18867103685");
        pp.setProductId(1L);
        pp.setEnterpriseId(1L);
        pp.setRuleId(1L);
        pp.setRequestSerialNum("1111");
        pojos.add(pp);

        pojo.setPojos(pojos);

        return pojo;
    }
    
    /**
     * getListpojo
     */
    private List<PresentPojo> getListpojo(){
        List<PresentPojo> pojos = new ArrayList<PresentPojo>();
        PresentPojo pp = new PresentPojo();
        pp.setRecordId(1L);
        pp.setMobile("18867103685");
        pp.setProductId(1L);
        pp.setEnterpriseId(1L);
        pp.setRuleId(1L);
        pp.setRequestSerialNum("1111");
        pojos.add(pp);

        return pojos;
    }
   
    /**
     * getInfos()
     */
    private List<Info> getInfos(){
        List<Info> infos = new ArrayList<Info>();
        Info info1=new Info(new ChargeRecord(), new ChargeResult(ChargeResultCode.SUCCESS));
        infos.add(info1);
        return infos;
    }
    
    

}
