package com.cmcc.vrp.queue.task.channel;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.hainan.HaiNanBOSSServiceImpl;
import com.cmcc.vrp.boss.hainan.model.HNBOSSChargeResponse;
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
public class HiChannelWorkerTest extends BaseChannelWorkerTest {

    @InjectMocks
    HiChannelWorker hiChannelWorker = new HiChannelWorker();

    @Mock
    HaiNanBOSSServiceImpl haiNanBOSSService;
    @Mock
    ProductService productService;

    @Before
    public void initMocks() {
        hiChannelWorker.setTaskString(getTaskStr());
        Gson gson = new Gson();
        ReflectionTestUtils.setField(hiChannelWorker, "gson", gson);
        ChargeRecord record = new ChargeRecord();
        record.setStatus(4);
        PowerMockito.when(chargeRecordService.getRecordBySN(anyString())).thenReturn(record);
        PowerMockito.when(chargeRecordService.updateStatus(anyLong(), any(ChargeResult.class))).thenReturn(true);
        PowerMockito.when(productService.get(anyLong())).thenReturn(new Product());
    }

    @Test
    @Ignore
    public void testCqChannel() {
        PowerMockito.when(haiNanBOSSService.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult("0"));
        hiChannelWorker.exec();

        PowerMockito.when(haiNanBOSSService.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult("1"));
        hiChannelWorker.exec();

        Mockito.verify(haiNanBOSSService, times(2)).charge(anyLong(), anyLong(), anyString(), anyString(), anyLong());
    }

    public BossOperationResult buildResult(String code) {
        HNBOSSChargeResponse result = new HNBOSSChargeResponse();
        result.setCode(code);
        return result;
    }
}
