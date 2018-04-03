package com.cmcc.vrp.queue.task.channel;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.hunan.HNBossServcieImpl;
import com.cmcc.vrp.boss.hunan.model.HNChargeResponse;
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
public class HnChannelWorkerTest extends BaseChannelWorkerTest {

    @InjectMocks
    HnChannelWorker hnChannelWorker = new HnChannelWorker();

    @Mock
    HNBossServcieImpl hnBossServcie;
    @Mock
    ProductService productService;

    @Before
    public void initMocks() {
        hnChannelWorker.setTaskString(getTaskStr());
        Gson gson = new Gson();
        ReflectionTestUtils.setField(hnChannelWorker, "gson", gson);
        ChargeRecord record = new ChargeRecord();
        record.setStatus(4);
        PowerMockito.when(chargeRecordService.getRecordBySN(anyString())).thenReturn(record);
        PowerMockito.when(chargeRecordService.updateStatus(anyLong(), any(ChargeResult.class))).thenReturn(true);
        PowerMockito.when(productService.get(anyLong())).thenReturn(new Product());
    }

    @Test
    @Ignore
    public void testCqChannel() {
        PowerMockito.when(hnBossServcie.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult("00000"));
        hnChannelWorker.exec();

        PowerMockito.when(hnBossServcie.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult("00001"));
        hnChannelWorker.exec();

        Mockito.verify(hnBossServcie, times(2)).charge(anyLong(), anyLong(), anyString(), anyString(), anyLong());
    }

    public BossOperationResult buildResult(String code) {
        HNChargeResponse result = new HNChargeResponse();
        result.setX_RESULTCODE(code);
        return result;
    }
}
