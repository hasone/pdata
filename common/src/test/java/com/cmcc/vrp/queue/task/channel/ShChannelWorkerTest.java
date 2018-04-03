package com.cmcc.vrp.queue.task.channel;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.shanghai.ShBossOperationResultImpl;
import com.cmcc.vrp.boss.shanghai.ShNationalBossServiceImpl;
import com.cmcc.vrp.boss.shanghai.model.ShReturnCode;
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
public class ShChannelWorkerTest extends BaseChannelWorkerTest {

    @InjectMocks
    ShChannelWorker shChannelWorker = new ShChannelWorker();

    @Mock
    ShNationalBossServiceImpl shNationalBossService;
    @Mock
    ProductService productService;

    @Before
    public void initMocks() {
        shChannelWorker.setTaskString(getTaskStr());
        Gson gson = new Gson();
        ReflectionTestUtils.setField(shChannelWorker, "gson", gson);
        ChargeRecord record = new ChargeRecord();
        record.setStatus(4);
        PowerMockito.when(chargeRecordService.getRecordBySN(anyString())).thenReturn(record);
        PowerMockito.when(chargeRecordService.updateStatus(anyLong(), any(ChargeResult.class))).thenReturn(true);
        PowerMockito.when(productService.get(anyLong())).thenReturn(new Product());
    }

    @Test
    @Ignore
    public void testCqChannel() {
        PowerMockito.when(shNationalBossService.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult(ShReturnCode.SUCCESS));
        shChannelWorker.exec();

        PowerMockito.when(shNationalBossService.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult(ShReturnCode.FAILD));
        shChannelWorker.exec();

        Mockito.verify(shNationalBossService, times(2)).charge(anyLong(), anyLong(), anyString(), anyString(), anyLong());
    }

    public BossOperationResult buildResult(ShReturnCode code) {
        BossOperationResult result = new ShBossOperationResultImpl(code);
        return result;
    }
}
