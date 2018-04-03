package com.cmcc.vrp.queue.task.channel;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.henan.AbstractHaBossChargeService;
import com.cmcc.vrp.boss.henan.HaPlatformBossServiceImpl;
import com.cmcc.vrp.boss.henan.model.HaErrorCode;
import com.cmcc.vrp.boss.henan.service.Impl.HaBossOperationResutlImpl;
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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;

/**
 * Created by leelyn on 2016/11/17.
 */
@PrepareForTest(AbstractHaBossChargeService.class)
@Ignore
public class HaPlatformWorkerTest extends BaseChannelWorkerTest {

    @InjectMocks
    HaPlatformWorker haPlatformWorker = new HaPlatformWorker();

    @Mock
    HaPlatformBossServiceImpl haPlatformBossService;

    @Mock
    ProductService productService;


    @Before
    public void initMocks() {
        haPlatformWorker.setTaskString(getTaskStr());
        Gson gson = new Gson();
        ReflectionTestUtils.setField(haPlatformWorker, "gson", gson);
        ChargeRecord record = new ChargeRecord();
        record.setStatus(4);
        PowerMockito.when(chargeRecordService.getRecordBySN(anyString())).thenReturn(record);
        PowerMockito.when(chargeRecordService.updateStatus(anyLong(), any(ChargeResult.class))).thenReturn(true);
        PowerMockito.when(productService.get(anyLong())).thenReturn(new Product());
    }

    @Test
    @Ignore
    public void testCqChannel() {
        PowerMockito.when(haPlatformBossService.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult(HaErrorCode.send));
        haPlatformWorker.exec();

        PowerMockito.when(haPlatformBossService.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult(HaErrorCode.faild));
        haPlatformWorker.exec();

        Mockito.verify(haPlatformBossService, times(2)).charge(anyLong(), anyLong(), anyString(), anyString(), anyLong());
    }

    public BossOperationResult buildResult(HaErrorCode errorCode) {
        BossOperationResult result = new HaBossOperationResutlImpl(errorCode);
        return result;
    }
}
