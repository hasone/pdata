package com.cmcc.vrp.queue.task;

import com.alibaba.fastjson.JSON;
import com.cmcc.vrp.boss.sichuan.service.IndividualBossService;
import com.cmcc.vrp.charge.ChargeService;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.IndividualAccount;
import com.cmcc.vrp.province.model.IndividualFlowcoinExchange;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.IndividualAccountRecordService;
import com.cmcc.vrp.province.service.IndividualAccountService;
import com.cmcc.vrp.province.service.IndividualFlowcoinExchangeService;
import com.cmcc.vrp.province.service.IndividualFlowcoinRecordService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.queue.pojo.FlowcoinExchangePojo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * <p>Title:FlowcoinExchangeWorkerTest </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月9日
 */
@RunWith(MockitoJUnitRunner.class)
public class FlowcoinExchangeWorkerTest {

    @InjectMocks
    FlowcoinExchangeWorker feWorker = new FlowcoinExchangeWorker();

    @Mock
    IndividualAccountService accountService;
    @Mock
    ChargeService chargeService;
    @Mock
    SerialNumService serialNumService;
    @Mock
    IndividualFlowcoinExchangeService individualFlowcoinExchangeService;
    @Mock
    IndividualBossService individualBossService;
    @Mock
    ChargeRecordService chargeRecordService;
    @Mock
    IndividualAccountRecordService accountRecordService;
    @Mock
    IndividualFlowcoinRecordService flowcoinRecordService;

    @Test
    public void testExec1() {

        feWorker.setTaskString(null);
        feWorker.exec();
    }

    /**
     *
     */
    @Test
    public void testExec2() {
        FlowcoinExchangePojo pojo = new FlowcoinExchangePojo();
        String jsonStr = JSON.toJSONString(pojo);
        feWorker.setTaskString(jsonStr);
        feWorker.exec();
    }

    /**
     *
     */
    @Test
    public void testExec3() {
        FlowcoinExchangePojo pojo = createFlowcoinExchangePojo();
        String jsonStr = JSON.toJSONString(pojo);

        when(serialNumService.insert(Mockito.any(SerialNum.class))).thenReturn(false).thenReturn(true);
        when(chargeRecordService.create(Mockito.any(ChargeRecord.class))).thenReturn(false).thenReturn(true);
        feWorker.setTaskString(jsonStr);
        feWorker.exec();
        feWorker.exec();
    }

    /**
     *
     */
    @Test
    public void testExec4() {
        FlowcoinExchangePojo pojo = createFlowcoinExchangePojo();
        String jsonStr = JSON.toJSONString(pojo);

        when(serialNumService.insert(Mockito.any(SerialNum.class))).thenReturn(true);
        when(chargeRecordService.create(Mockito.any(ChargeRecord.class))).thenReturn(true);
        when(individualBossService.chargeFlow(Mockito.anyString(), Mockito.anyLong(), anyString())).thenReturn(true);
        when(chargeRecordService.updateByPrimaryKeySelective(Mockito.any(ChargeRecord.class))).thenReturn(false).thenReturn(true);
        when(accountService.changeFrozenAccount(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(),
            Mockito.any(BigDecimal.class), Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(),
            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(false).thenReturn(true);
        when(individualFlowcoinExchangeService.updateStatus(Mockito.anyLong(), Mockito.anyInt())).thenReturn(false).thenReturn(true);
        feWorker.setTaskString(jsonStr);
        feWorker.exec();
        feWorker.exec();
        feWorker.exec();
    }

    /**
     *
     */
    @Test
    public void testExec5() {
        FlowcoinExchangePojo pojo = createFlowcoinExchangePojo();
        String jsonStr = JSON.toJSONString(pojo);

        when(serialNumService.insert(Mockito.any(SerialNum.class))).thenReturn(true);
        when(chargeRecordService.create(Mockito.any(ChargeRecord.class))).thenReturn(true);
        when(individualBossService.chargeFlow(Mockito.anyString(), Mockito.anyLong(), anyString())).thenReturn(false);
        when(chargeRecordService.updateByPrimaryKeySelective(Mockito.any(ChargeRecord.class))).thenReturn(false).thenReturn(true);
        when(individualFlowcoinExchangeService.updateStatus(Mockito.anyLong(), Mockito.anyInt())).thenReturn(false).thenReturn(false).thenReturn(true);
        when(accountService.changeBossAccount(Mockito.anyLong(), Mockito.any(BigDecimal.class), Mockito.anyLong(),
            Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(),
            Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
        when(accountService.changeFrozenAccount(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any(BigDecimal.class),
            Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(),
            Mockito.anyInt())).thenReturn(false).thenReturn(true);
        feWorker.setTaskString(jsonStr);
        feWorker.exec();
        feWorker.exec();
        feWorker.exec();
        feWorker.exec();


    }

    /**
     *
     */
    @Test
    public void testExec6() {
        FlowcoinExchangePojo pojo = createFlowcoinExchangePojo();
        String jsonStr = JSON.toJSONString(pojo);

        when(serialNumService.insert(Mockito.any(SerialNum.class))).thenReturn(true);
        when(chargeRecordService.create(Mockito.any(ChargeRecord.class))).thenReturn(true);
        when(individualBossService.chargeFlow(Mockito.anyString(), Mockito.anyLong(), anyString())).thenReturn(false);
        when(chargeRecordService.updateByPrimaryKeySelective(Mockito.any(ChargeRecord.class))).thenReturn(false).thenReturn(true);
        when(individualFlowcoinExchangeService.updateStatus(Mockito.anyLong(), Mockito.anyInt())).thenReturn(false).thenReturn(false).thenReturn(true);
        when(accountService.changeBossAccount(Mockito.anyLong(), Mockito.any(BigDecimal.class), Mockito.anyLong(),
            Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(),
            Mockito.anyInt(), Mockito.anyInt())).thenReturn(false);
        feWorker.setTaskString(jsonStr);
        feWorker.exec();
        feWorker.exec();


    }


    private FlowcoinExchangePojo createFlowcoinExchangePojo() {
        FlowcoinExchangePojo pojo = new FlowcoinExchangePojo();
        IndividualAccount account = new IndividualAccount();
        IndividualFlowcoinExchange record = new IndividualFlowcoinExchange();
        record.setAdminId(1L);
        record.setIndividualProductId(1L);
        record.setMobile("18867101111");
        record.setId(1L);

        //pojo.setIndividualAccount(account);
        pojo.setIndividualFlowcoinExchangeRecord(record);

        return pojo;
    }

}
