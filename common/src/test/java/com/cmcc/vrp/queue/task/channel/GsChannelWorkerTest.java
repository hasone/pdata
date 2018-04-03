package com.cmcc.vrp.queue.task.channel;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.gansu.GSBossOperationResultImpl;
import com.cmcc.vrp.boss.gansu.GSBossServiceImpl;
import com.cmcc.vrp.boss.gansu.model.GSChargeResponse;
import com.cmcc.vrp.boss.gansu.model.GSInterBossResponse;
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
public class GsChannelWorkerTest extends BaseChannelWorkerTest {

    @InjectMocks
    GsChannelWorker gsChannelWorker = new GsChannelWorker();

    @Mock
    GSBossServiceImpl gsBossService;

    @Mock
    ProductService productService;


    @Before
    public void initMocks() {
        gsChannelWorker.setTaskString(getTaskStr());
        Gson gson = new Gson();
        ReflectionTestUtils.setField(gsChannelWorker, "gson", gson);
        ChargeRecord record = new ChargeRecord();
        record.setStatus(4);
        PowerMockito.when(chargeRecordService.getRecordBySN(anyString())).thenReturn(record);
        PowerMockito.when(chargeRecordService.updateStatus(anyLong(), any(ChargeResult.class))).thenReturn(true);
        PowerMockito.when(productService.get(anyLong())).thenReturn(new Product());
    }

    @Test
    @Ignore
    public void testCqChannel() {
        PowerMockito.when(gsBossService.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult("A0000"));
        gsChannelWorker.exec();

        PowerMockito.when(gsBossService.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult("A0001"));
        gsChannelWorker.exec();

        Mockito.verify(gsBossService, times(2)).charge(anyLong(), anyLong(), anyString(), anyString(), anyLong());
    }

    public BossOperationResult buildResult(String returnCode) {
        GSInterBossResponse response = new GSInterBossResponse();
        GSChargeResponse interBOSS = new GSChargeResponse();
        interBOSS.setRspCode(returnCode);
        response.setInterBOSS(interBOSS);
        BossOperationResult result = new GSBossOperationResultImpl(response);
        return result;
    }
}
