package com.cmcc.vrp.queue.task;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.cmcc.vrp.ec.bean.CallbackResp;
import com.cmcc.vrp.ec.utils.CallbackResult;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.EntCallbackAddr;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.EntCallbackAddrService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.queue.pojo.CallbackPojo;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.cmcc.vrp.util.HttpUtils;
import com.google.gson.Gson;

/**
 * Created by leelyn on 2016/11/20.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpUtils.class)
@Ignore
public class CallbackEcPlatformWorkerTest {

    @InjectMocks
    CallbackEcPlatformWorker ecPlatformWorker = new CallbackEcPlatformWorker();

    @Mock
    ChargeRecordService recordService;

    @Mock
    EntCallbackAddrService entCallbackAddrService;

    @Mock
    GlobalConfigService globalConfigService;
    
    @Mock
    EnterprisesService enterprisesService;
    @Before
    public void initMocks() {
        ecPlatformWorker.setTaskString(buildTaskString());
        Gson gson = new Gson();
        ReflectionTestUtils.setField(ecPlatformWorker, "gson", gson);
        PowerMockito.when(entCallbackAddrService.get(anyLong())).thenReturn(buildAddr());
        Enterprise enterprise = new Enterprise();
        enterprise.setCode("280");
        PowerMockito.when(enterprisesService.selectById(anyLong())).thenReturn(enterprise);

        PowerMockito.mockStatic(HttpUtils.class);
        PowerMockito.when(HttpUtils.post(anyString(), anyString(), anyString())).thenReturn(buildCbResp());
        when(globalConfigService.get(GlobalConfigKeyEnum.CHARGE_SUCCESS_NOTICE.getKey())).thenReturn("false");
        when(globalConfigService.get(GlobalConfigKeyEnum.CALLBACK_RETRY_TIMES.getKey())).thenReturn("3");
        when(globalConfigService.get(GlobalConfigKeyEnum.CALLBACK_RETRY_SECONDS.getKey())).thenReturn("5");
        when(globalConfigService.get(GlobalConfigKeyEnum.PROVINCE_FLAG.getKey())).thenReturn("chongqing");
        when(globalConfigService.get(GlobalConfigKeyEnum.YQX_ORDERID_ENTER_CODE.getKey())).thenReturn("280");
        when(globalConfigService.get(GlobalConfigKeyEnum.YQX_CQ_CALLBACK_URL.getKey())).thenReturn("http://test");

    }

    @Test
    public void test() {
        PowerMockito.when(recordService.getRecordBySN(anyString())).thenReturn(buildRecord(4));
        ecPlatformWorker.exec();

        PowerMockito.when(recordService.getRecordBySN(anyString())).thenReturn(buildRecord(3));
        ecPlatformWorker.exec();

        verify(entCallbackAddrService, times(2)).get(anyLong());
    }


    private String buildTaskString() {
        CallbackPojo pojo = new CallbackPojo();
        pojo.setSerialNum("xxxxxxxx");
        pojo.setEntId(1l);
        return new Gson().toJson(pojo);
    }

    private ChargeRecord buildRecord(Integer status) {
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setStatus(3);
        chargeRecord.setChargeTime(new Date());
        chargeRecord.setErrorMessage("充值成功");
        chargeRecord.setSerialNum("cqxxxxyqx");
        chargeRecord.setSystemNum("xxxxxxxxxxx");
        chargeRecord.setPhone("18888888888");
        return chargeRecord;
    }

    private EntCallbackAddr buildAddr() {
        EntCallbackAddr callbackAddr = new EntCallbackAddr();
        callbackAddr.setDeleteFlag(0);
        callbackAddr.setCallbackAddr("http://test");
        return callbackAddr;
    }

    private String buildCbResp() {
        CallbackResp resp = new CallbackResp();
        resp.setCode(CallbackResult.SUCCESS.getCode());
        return new Gson().toJson(resp);
    }


}
