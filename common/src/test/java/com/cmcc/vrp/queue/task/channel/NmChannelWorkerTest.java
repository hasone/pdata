package com.cmcc.vrp.queue.task.channel;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.neimenggu.NMBossOperationResultImpl;
import com.cmcc.vrp.boss.neimenggu.NMBossServiceImpl;
import com.cmcc.vrp.boss.neimenggu.model.send.ResponseSendData;
import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.ProductService;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;

/**
 * Created by leelyn on 2016/11/17.
 */
public class NmChannelWorkerTest extends BaseChannelWorkerTest {

    @InjectMocks
    NmChannelWorker nmChannelWorker = new NmChannelWorker();

    @Mock
    NMBossServiceImpl nmBossService;
    @Mock
    ProductService productService;

    @Before
    public void initMocks() {
        nmChannelWorker.setTaskString(getTaskStr());
        Gson gson = new Gson();
        ReflectionTestUtils.setField(nmChannelWorker, "gson", gson);
        ChargeRecord record = new ChargeRecord();
        record.setStatus(4);
        PowerMockito.when(chargeRecordService.getRecordBySN(anyString())).thenReturn(record);
        PowerMockito.when(chargeRecordService.updateStatus(anyLong(), any(ChargeResult.class))).thenReturn(true);
        PowerMockito.when(productService.get(anyLong())).thenReturn(new Product());
    }

    @Test
    @Ignore
    public void testCqChannel() {
        PowerMockito.when(nmBossService.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult("0"));
        nmChannelWorker.exec();

        PowerMockito.when(nmBossService.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult("1"));
        nmChannelWorker.exec();

        Mockito.verify(nmBossService, times(2)).charge(anyLong(), anyLong(), anyString(), anyString(), anyLong());
    }

    public BossOperationResult buildResult(String code) {
        ResponseSendData sendData = new ResponseSendData();
        sendData.setResultCode(code);
        BossOperationResult result = new NMBossOperationResultImpl(sendData);
        return result;
    }
}
