package com.cmcc.vrp.province.service.impl;

import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.model.AsyncCallbackReq;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.service.AccountService;
import com.cmcc.vrp.province.service.AsyncCallbackService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.queue.TaskProducer;
import com.cmcc.vrp.queue.pojo.CallbackPojo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 异步回调服务的UT
 * <p>
 * Created by sunyiwei on 2016/10/11.
 */
@RunWith(MockitoJUnitRunner.class)
public class AsyncCallbackServiceImplTest extends BaseTest {
    @InjectMocks
    private AsyncCallbackService asyncCallbackService = new AsyncCallbackServiceImpl();

    @Mock
    private ChargeRecordService chargeRecordService;

    @Mock
    private AccountService accountService;

    @Mock
    private TaskProducer taskProducer;
    
    @Mock
    GlobalConfigService globalConfigService;

    /**
     * 测试异常情况
     *
     * @throws Exception
     */
    @Test
    public void testProcess() throws Exception {
        assertFalse(asyncCallbackService.process(null));
        assertFalse(asyncCallbackService.process(invalidMobile()));
        assertFalse(asyncCallbackService.process(invalidSystemSerialNum()));
        assertFalse(asyncCallbackService.process(invalidChargeRecordStatus()));
    }

    /**
     * 测试充值记录为空的情况
     *
     * @throws Exception
     */
    @Test
    public void testProcess1() throws Exception {
        AsyncCallbackReq acr = buildValid();
        AsyncCallbackReq acrf = buildFailed();

        when(chargeRecordService.getRecordBySN(anyString()))
            .thenReturn(null).thenReturn(buildChargeRecord(ChargeRecordStatus.COMPLETE));
        when(chargeRecordService.updateStatus(anyLong(), any(ChargeRecordStatus.class), anyString()))
            .thenReturn(true);
        when(accountService.returnFunds(anyString()))
            .thenReturn(true);
        when(taskProducer.productPlatformCallbackMsg(any(CallbackPojo.class)))
            .thenReturn(true);

        assertFalse(asyncCallbackService.process(acr));
        assertTrue(asyncCallbackService.process(acrf));

        verify(chargeRecordService, times(2)).getRecordBySN(anyString());
        verify(chargeRecordService, times(1)).updateStatus(anyLong(), any(ChargeRecordStatus.class), anyString());
        verify(accountService, times(1)).returnFunds(anyString());
        verify(taskProducer, times(1)).productPlatformCallbackMsg(any(CallbackPojo.class));
    }

    /**
     * 测试退款逻辑
     *
     * @throws Exception
     */
    @Test
    public void testProcess2() throws Exception {
        AsyncCallbackReq acr = buildValid();
        AsyncCallbackReq acrf = buildFailed();

        when(chargeRecordService.getRecordBySN(anyString()))
            .thenReturn(buildChargeRecord(ChargeRecordStatus.FAILED))
            .thenReturn(buildChargeRecord(ChargeRecordStatus.COMPLETE));
        when(chargeRecordService.updateStatus(anyLong(), any(ChargeRecordStatus.class), anyString()))
            .thenReturn(true);
        when(accountService.returnFunds(anyString()))
            .thenReturn(true);
        when(taskProducer.productPlatformCallbackMsg(any(CallbackPojo.class)))
            .thenReturn(true);

        assertTrue(asyncCallbackService.process(acr));
        assertTrue(asyncCallbackService.process(acrf));

        verify(chargeRecordService, times(2)).getRecordBySN(anyString());
        verify(chargeRecordService, times(2)).updateStatus(anyLong(), any(ChargeRecordStatus.class), anyString());
        verify(accountService, times(1)).returnFunds(anyString());
        verify(taskProducer, times(2)).productPlatformCallbackMsg(any(CallbackPojo.class));
    }

    /**
     * 测试账户和充值记录出异常的情况
     * ·
     *
     * @throws Exception
     */
    @Test
    public void testProcess3() throws Exception {
        AsyncCallbackReq acr = buildValid();
        AsyncCallbackReq acrf = buildFailed();

        when(chargeRecordService.getRecordBySN(anyString()))
            .thenReturn(buildChargeRecord(ChargeRecordStatus.COMPLETE));
        when(chargeRecordService.updateStatus(anyLong(), any(ChargeRecordStatus.class), anyString()))
            .thenReturn(false).thenReturn(true);
        when(accountService.returnFunds(anyString()))
            .thenReturn(false).thenReturn(true);
        when(taskProducer.productPlatformCallbackMsg(any(CallbackPojo.class)))
            .thenReturn(false).thenReturn(true);

        assertFalse(asyncCallbackService.process(acr));
        assertFalse(asyncCallbackService.process(acrf));
        assertFalse(asyncCallbackService.process(acrf));
        assertTrue(asyncCallbackService.process(acrf));

        verify(chargeRecordService, times(4)).getRecordBySN(anyString());
        verify(chargeRecordService, times(3)).updateStatus(anyLong(), any(ChargeRecordStatus.class), anyString());
        verify(accountService, times(3)).returnFunds(anyString());
        verify(taskProducer, times(2)).productPlatformCallbackMsg(any(CallbackPojo.class));
    }

    private ChargeRecord buildChargeRecord(ChargeRecordStatus chargeRecordStatus) {
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setId(343L);
        chargeRecord.setEnterId(23434L);
        chargeRecord.setTypeCode(0);

        chargeRecord.setStatus(chargeRecordStatus.getCode());
        chargeRecord.setErrorMessage(chargeRecordStatus.getMessage());

        return chargeRecord;
    }

    private AsyncCallbackReq buildValid() {
        return buildValid(ChargeRecordStatus.COMPLETE);
    }

    private AsyncCallbackReq buildFailed() {
        return buildValid(ChargeRecordStatus.FAILED);
    }

    private AsyncCallbackReq buildValid(ChargeRecordStatus chargeRecordStatus) {
        AsyncCallbackReq acr = new AsyncCallbackReq();
        acr.setMobile("18867102100");
        acr.setSystemSerialNum(randStr());

        acr.setChargeRecordStatus(chargeRecordStatus.getCode());
        acr.setErrorMsg(chargeRecordStatus.getMessage());

        return acr;
    }

    private AsyncCallbackReq invalidMobile() {
        AsyncCallbackReq acr = buildValid();
        acr.setMobile(null);

        return acr;
    }

    private AsyncCallbackReq invalidSystemSerialNum() {
        AsyncCallbackReq acr = buildValid();
        acr.setSystemSerialNum(null);

        return acr;
    }

    private AsyncCallbackReq invalidChargeRecordStatus() {
        AsyncCallbackReq acr = buildValid();
        acr.setChargeRecordStatus(-1);

        return acr;
    }
}