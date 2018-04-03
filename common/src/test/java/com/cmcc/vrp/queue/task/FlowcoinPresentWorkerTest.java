package com.cmcc.vrp.queue.task;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.enums.AccountRecordType;
import com.cmcc.vrp.enums.ActivityType;
import com.cmcc.vrp.province.model.ActivityWinRecord;
import com.cmcc.vrp.province.model.Administer;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.service.ActivityWinRecordService;
import com.cmcc.vrp.province.service.AdministerService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.FlowcoinPresentPojo;

/**
 * Created by qinqinyan on 2016/11/8.
 */
@RunWith(MockitoJUnitRunner.class)
public class FlowcoinPresentWorkerTest {
    @InjectMocks
    FlowcoinPresentWorker flowcoinPresentWorker = new FlowcoinPresentWorker();
    @Mock
    IndividualAccountService accountService;
    @Mock
    ActivityWinRecordService activityWinRecordService;
    @Mock
    TaskProducer taskProducer;
    @Mock
    GlobalConfigService globalConfigService;
    @Mock
    AdministerService administerService;

    @Test
    public void testExec1() {
        flowcoinPresentWorker.exec();
    }

    @Test
    public void testExec2() {
        FlowcoinPresentPojo pojo = createFlowcoinPresentPojo();
        String jsonStr = JSON.toJSONString(pojo);
        flowcoinPresentWorker.setTaskString(jsonStr);

        Mockito.when(administerService.selectByMobilePhone(anyString())).thenReturn(null);
        Mockito.when(administerService.insertForScJizhong(anyString())).thenReturn(false);
        flowcoinPresentWorker.exec();
        Mockito.verify(administerService).selectByMobilePhone(anyString());
        Mockito.verify(administerService).insertForScJizhong(anyString());
    }

    @Test
    public void testExec3() {
        FlowcoinPresentPojo pojo = createFlowcoinPresentPojo();
        IndividualAccount account = pojo.getIndividualAccount();
        ActivityWinRecord record = pojo.getActivityWinRecord();

        String jsonStr = JSON.toJSONString(pojo);
        flowcoinPresentWorker.setTaskString(jsonStr);

        Administer chargeAdminister = new Administer();

        Mockito.when(administerService.selectByMobilePhone(anyString())).thenReturn(chargeAdminister);
        flowcoinPresentWorker.exec();
        
        Mockito.when(administerService.insertForScJizhong(Mockito.anyString())).thenReturn(true);
        Mockito.when(accountService.changeBossAccount(Mockito.anyLong(), Mockito.any(BigDecimal.class), 
                Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(), 
                Mockito.anyInt())).thenReturn(true);
                
        Mockito.when(accountService.changeFrozenAccount(account.getAdminId(),
                account.getOwnerId(), account.getId(), new BigDecimal(record.getProductSize()),
                account.getIndividualProductId(), record.getRecordId(),
                (int) AccountRecordType.OUTGO.getValue(), "流量币赠送,扣除冻结账户",
                ActivityType.FLOWCOIN_PRESENT.getCode(), 0)).thenReturn(false);
        Mockito.when(activityWinRecordService.batchUpdateStatus(anyList(), anyString(),
                anyInt(), anyInt(), any(Date.class))).thenReturn(false);

        flowcoinPresentWorker.exec();

        /*Mockito.verify(administerService).selectByMobilePhone(anyString());
        Mockito.verify(accountService, Mockito.atLeastOnce()).changeBossAccount(chargeAdminister.getId(),
            new BigDecimal(record.getProductSize()), account.getIndividualProductId(),
            record.getRecordId(),
            (int) AccountRecordType.INCOME.getValue(), "流量币赠送,boss流量币增加",
            ActivityType.FLOWCOIN_PRESENT.getCode(), 0);
        Mockito.verify(accountService).changeFrozenAccount(account.getAdminId(),
            account.getOwnerId(), account.getId(), new BigDecimal(record.getProductSize()),
            account.getIndividualProductId(), record.getRecordId(),
            (int) AccountRecordType.OUTGO.getValue(), "流量币赠送,扣除冻结账户",
            ActivityType.FLOWCOIN_PRESENT.getCode(), 0);
        Mockito.verify(activityWinRecordService).batchUpdateStatus(anyList(), anyString(),
            anyInt(), anyInt(), any(Date.class));*/
    }

    @Test
    public void testExec4() {
        FlowcoinPresentPojo pojo = createFlowcoinPresentPojo();
        IndividualAccount account = pojo.getIndividualAccount();
        ActivityWinRecord record = pojo.getActivityWinRecord();

        String jsonStr = JSON.toJSONString(pojo);
        flowcoinPresentWorker.setTaskString(jsonStr);

        Administer chargeAdminister = new Administer();

        Mockito.when(administerService.selectByMobilePhone(anyString())).thenReturn(chargeAdminister);
        Mockito.when(administerService.insertForScJizhong(Mockito.anyString())).thenReturn(true);
        Mockito.when(accountService.changeBossAccount(Mockito.anyLong(), Mockito.any(BigDecimal.class), 
                Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(), 
                Mockito.anyInt())).thenReturn(true);
                
        Mockito.when(accountService.changeFrozenAccount(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), 
                Mockito.any(BigDecimal.class), Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(), 
                Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
       
        
        Mockito.when(activityWinRecordService.batchUpdateStatus(anyList(), anyString(),
                anyInt(), anyInt(), any(Date.class))).thenReturn(true);
 
        flowcoinPresentWorker.exec();
        
        /*
        Mockito.verify(administerService).selectByMobilePhone(anyString());
        Mockito.verify(accountService).changeBossAccount(chargeAdminister.getId(),
            new BigDecimal(record.getProductSize()), account.getIndividualProductId(),
            record.getRecordId(),
            (int) AccountRecordType.INCOME.getValue(), "流量币赠送,boss流量币增加",
            ActivityType.FLOWCOIN_PRESENT.getCode(), 0);
        Mockito.verify(accountService).changeFrozenAccount(account.getAdminId(),
            account.getOwnerId(), account.getId(), new BigDecimal(record.getProductSize()),
            account.getIndividualProductId(), record.getRecordId(),
            (int) AccountRecordType.OUTGO.getValue(), "流量币赠送,扣除冻结账户",
            ActivityType.FLOWCOIN_PRESENT.getCode(), 0);
        Mockito.verify(globalConfigService).get(anyString());
        Mockito.verify(taskProducer).produceDeliverNoticeSmsMsg(any(SmsPojo.class));
        Mockito.verify(activityWinRecordService).batchUpdateStatus(anyList(), anyString(),
            anyInt(), anyInt(), any(Date.class));*/
    }

    @Test
    public void testExec5() {
        FlowcoinPresentPojo pojo = createFlowcoinPresentPojo();
        IndividualAccount account = pojo.getIndividualAccount();
        ActivityWinRecord record = pojo.getActivityWinRecord();

        String jsonStr = JSON.toJSONString(pojo);
        flowcoinPresentWorker.setTaskString(jsonStr);

        Administer chargeAdminister = new Administer();
        chargeAdminister.setId(1L);

        Mockito.when(administerService.selectByMobilePhone(anyString())).thenReturn(chargeAdminister);
        Mockito.when(accountService.insertAccountForScJizhong(Mockito.anyLong())).thenReturn(false);
        flowcoinPresentWorker.exec();
        
        Mockito.when(accountService.insertAccountForScJizhong(Mockito.anyLong())).thenReturn(true);
        Mockito.when(accountService.changeBossAccount(chargeAdminister.getId(),
                new BigDecimal(record.getProductSize()), account.getIndividualProductId(),
                record.getRecordId(),
                (int) AccountRecordType.INCOME.getValue(), "流量币赠送,boss流量币增加",
                ActivityType.FLOWCOIN_PRESENT.getCode(), 0)).thenReturn(false);

        Mockito.when(activityWinRecordService.batchUpdateStatus(anyList(), anyString(),
                anyInt(), anyInt(), any(Date.class))).thenReturn(false);

        Mockito.when(accountService.changeBossAccount(account.getAdminId(),
                new BigDecimal(record.getProductSize()), account.getIndividualProductId(),
                record.getRecordId(),
                (int) AccountRecordType.INCOME.getValue(),
                "流量币赠送失败，boss流量币增加",
                ActivityType.FLOWCOIN_PRESENT.getCode(), 1)).thenReturn(true);
        Mockito.when(accountService.changeFrozenAccount(account.getAdminId(),
                account.getOwnerId(), account.getId(),
                new BigDecimal(record.getProductSize()), account.getIndividualProductId(),
                record.getRecordId(), (int) AccountRecordType.INCOME.getValue(),
                "流量币赠送失败，扣除冻结账户",
                ActivityType.FLOWCOIN_PRESENT.getCode(), 1)).thenReturn(true);
        Mockito.when(activityWinRecordService.batchUpdateStatus(anyList(), anyString(),
                anyInt(), anyInt(), any(Date.class))).thenReturn(false);
        
        flowcoinPresentWorker.exec();

        Mockito.verify(administerService,Mockito.times(2)).selectByMobilePhone(anyString());
        Mockito.verify(accountService).changeBossAccount(chargeAdminister.getId(),
                new BigDecimal(record.getProductSize()), account.getIndividualProductId(),
                record.getRecordId(),
                (int) AccountRecordType.INCOME.getValue(), "流量币赠送,boss流量币增加",
                ActivityType.FLOWCOIN_PRESENT.getCode(), 0);
        Mockito.verify(activityWinRecordService, Mockito.atLeastOnce()).batchUpdateStatus(anyList(), anyString(),
                anyInt(), anyInt(), any(Date.class));
        Mockito.verify(accountService).changeBossAccount(account.getAdminId(),
                new BigDecimal(record.getProductSize()), account.getIndividualProductId(),
                record.getRecordId(),
                (int) AccountRecordType.INCOME.getValue(),
                "流量币赠送失败，boss流量币增加",
                ActivityType.FLOWCOIN_PRESENT.getCode(), 1);
        Mockito.verify(accountService).changeFrozenAccount(account.getAdminId(),
                account.getOwnerId(), account.getId(),
                new BigDecimal(record.getProductSize()), account.getIndividualProductId(),
                record.getRecordId(), (int) AccountRecordType.INCOME.getValue(),
                "流量币赠送失败，扣除冻结账户",
                ActivityType.FLOWCOIN_PRESENT.getCode(), 1);
        Mockito.verify(activityWinRecordService, Mockito.atLeastOnce()).batchUpdateStatus(anyList(), anyString(),
                anyInt(), anyInt(), any(Date.class));

    }

    @Test
    public void testExec6() {
        FlowcoinPresentPojo pojo = createFlowcoinPresentPojo();
        IndividualAccount account = pojo.getIndividualAccount();
        ActivityWinRecord record = pojo.getActivityWinRecord();

        String jsonStr = JSON.toJSONString(pojo);
        flowcoinPresentWorker.setTaskString(jsonStr);

        Administer chargeAdminister = new Administer();
        chargeAdminister.setId(1L);

        Mockito.when(administerService.selectByMobilePhone(anyString())).thenReturn(chargeAdminister);
        
        Mockito.when(accountService.insertAccountForScJizhong(Mockito.anyLong())).thenReturn(true);
        
        Mockito.when(accountService.changeBossAccount(chargeAdminister.getId(),
                new BigDecimal(record.getProductSize()), account.getIndividualProductId(),
                record.getRecordId(),
                (int) AccountRecordType.INCOME.getValue(), "流量币赠送,boss流量币增加",
                ActivityType.FLOWCOIN_PRESENT.getCode(), 0)).thenReturn(false);

        Mockito.when(activityWinRecordService.batchUpdateStatus(anyList(), anyString(),
                anyInt(), anyInt(), any(Date.class))).thenReturn(false);

        Mockito.when(accountService.changeBossAccount(account.getAdminId(),
                new BigDecimal(record.getProductSize()), account.getIndividualProductId(),
                record.getRecordId(),
                (int) AccountRecordType.INCOME.getValue(),
                "流量币赠送失败，boss流量币增加",
                ActivityType.FLOWCOIN_PRESENT.getCode(), 1)).thenReturn(true);
        Mockito.when(accountService.changeFrozenAccount(account.getAdminId(),
                account.getOwnerId(), account.getId(),
                new BigDecimal(record.getProductSize()), account.getIndividualProductId(),
                record.getRecordId(), (int) AccountRecordType.INCOME.getValue(),
                "流量币赠送失败，扣除冻结账户",
                ActivityType.FLOWCOIN_PRESENT.getCode(), 1)).thenReturn(false);
        Mockito.when(activityWinRecordService.batchUpdateStatus(anyList(), anyString(),
                anyInt(), anyInt(), any(Date.class))).thenReturn(false);       
        flowcoinPresentWorker.exec();

        Mockito.verify(administerService).selectByMobilePhone(anyString());
        Mockito.verify(accountService).changeBossAccount(chargeAdminister.getId(),
                new BigDecimal(record.getProductSize()), account.getIndividualProductId(),
                record.getRecordId(),
                (int) AccountRecordType.INCOME.getValue(), "流量币赠送,boss流量币增加",
                ActivityType.FLOWCOIN_PRESENT.getCode(), 0);
        Mockito.verify(activityWinRecordService, Mockito.atLeastOnce()).batchUpdateStatus(anyList(), anyString(),
                anyInt(), anyInt(), any(Date.class));
        Mockito.verify(accountService).changeBossAccount(account.getAdminId(),
                new BigDecimal(record.getProductSize()), account.getIndividualProductId(),
                record.getRecordId(),
                (int) AccountRecordType.INCOME.getValue(),
                "流量币赠送失败，boss流量币增加",
                ActivityType.FLOWCOIN_PRESENT.getCode(), 1);
        Mockito.verify(accountService).changeFrozenAccount(account.getAdminId(),
                account.getOwnerId(), account.getId(),
                new BigDecimal(record.getProductSize()), account.getIndividualProductId(),
                record.getRecordId(), (int) AccountRecordType.INCOME.getValue(),
                "流量币赠送失败，扣除冻结账户",
                ActivityType.FLOWCOIN_PRESENT.getCode(), 1);
        Mockito.verify(activityWinRecordService, Mockito.atLeastOnce()).batchUpdateStatus(anyList(), anyString(),
                anyInt(), anyInt(), any(Date.class));

    }

    @Test
    public void testExec7() {
        FlowcoinPresentPojo pojo = createFlowcoinPresentPojo();
        IndividualAccount account = pojo.getIndividualAccount();
        ActivityWinRecord record = pojo.getActivityWinRecord();

        String jsonStr = JSON.toJSONString(pojo);
        flowcoinPresentWorker.setTaskString(jsonStr);

        Administer chargeAdminister = new Administer();
        chargeAdminister.setId(1L);

        Mockito.when(administerService.selectByMobilePhone(anyString())).thenReturn(chargeAdminister);
        Mockito.when(accountService.insertAccountForScJizhong(Mockito.anyLong())).thenReturn(true);
        Mockito.when(accountService.changeBossAccount(chargeAdminister.getId(),
                new BigDecimal(record.getProductSize()), account.getIndividualProductId(),
                record.getRecordId(),
                (int) AccountRecordType.INCOME.getValue(), "流量币赠送,boss流量币增加",
                ActivityType.FLOWCOIN_PRESENT.getCode(), 0)).thenReturn(false);

        Mockito.when(activityWinRecordService.batchUpdateStatus(anyList(), anyString(),
                anyInt(), anyInt(), any(Date.class))).thenReturn(false);

        Mockito.when(accountService.changeBossAccount(account.getAdminId(),
                new BigDecimal(record.getProductSize()), account.getIndividualProductId(),
                record.getRecordId(),
                (int) AccountRecordType.INCOME.getValue(),
                "流量币赠送失败，boss流量币增加",
                ActivityType.FLOWCOIN_PRESENT.getCode(), 1)).thenReturn(false);


        Mockito.when(activityWinRecordService.batchUpdateStatus(anyList(), anyString(),
                anyInt(), anyInt(), any(Date.class))).thenReturn(false);

        flowcoinPresentWorker.exec();

        Mockito.verify(administerService).selectByMobilePhone(anyString());
        Mockito.verify(accountService).changeBossAccount(chargeAdminister.getId(),
                new BigDecimal(record.getProductSize()), account.getIndividualProductId(),
                record.getRecordId(),
                (int) AccountRecordType.INCOME.getValue(), "流量币赠送,boss流量币增加",
                ActivityType.FLOWCOIN_PRESENT.getCode(), 0);
        Mockito.verify(activityWinRecordService, Mockito.atLeastOnce()).batchUpdateStatus(anyList(), anyString(),
                anyInt(), anyInt(), any(Date.class));
        Mockito.verify(accountService).changeBossAccount(account.getAdminId(),
                new BigDecimal(record.getProductSize()), account.getIndividualProductId(),
                record.getRecordId(),
                (int) AccountRecordType.INCOME.getValue(),
                "流量币赠送失败，boss流量币增加",
                ActivityType.FLOWCOIN_PRESENT.getCode(), 1);

        Mockito.verify(activityWinRecordService, Mockito.atLeastOnce()).batchUpdateStatus(anyList(), anyString(),
                anyInt(), anyInt(), any(Date.class));

    }

    private FlowcoinPresentPojo createFlowcoinPresentPojo() {
        FlowcoinPresentPojo pojo = new FlowcoinPresentPojo();
        IndividualAccount individualAccount = new IndividualAccount();
        individualAccount.setId(1L);
        individualAccount.setIndividualProductId(1L);
        individualAccount.setAdminId(1L);
        individualAccount.setOwnerId(1L);
        pojo.setIndividualAccount(individualAccount);

        ActivityWinRecord activityWinRecord = new ActivityWinRecord();
        activityWinRecord.setId(1L);
        activityWinRecord.setChargeMobile("18867103333");
        activityWinRecord.setProductSize(1L);
        activityWinRecord.setRecordId("1");

        pojo.setActivityWinRecord(activityWinRecord);
        return pojo;
    }


}
