package com.cmcc.vrp.queue.task.channel;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.henan.AbstractHaBossChargeService;
import com.cmcc.vrp.boss.henan.HaChannelBossServiceImpl;
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
public class HaChannelWorkerTest extends BaseChannelWorkerTest {

    @InjectMocks
    HaChannelWorker haChannelWorker = new HaChannelWorker();

    @Mock
    HaChannelBossServiceImpl haChannelBossService;

    @Mock
    ProductService productService;


    @Before
    public void initMocks() {
        haChannelWorker.setTaskString(getTaskStr());
        Gson gson = new Gson();
        ReflectionTestUtils.setField(haChannelWorker, "gson", gson);
        ChargeRecord record = new ChargeRecord();
        record.setStatus(4);
        PowerMockito.when(chargeRecordService.getRecordBySN(anyString())).thenReturn(record);
        PowerMockito.when(chargeRecordService.updateStatus(anyLong(), any(ChargeResult.class))).thenReturn(true);
        PowerMockito.when(productService.get(anyLong())).thenReturn(new Product());
    }

    @Test
    @Ignore
    public void testCqChannel() {
        PowerMockito.when(haChannelBossService.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult(HaErrorCode.send));
        haChannelWorker.exec();

        PowerMockito.when(haChannelBossService.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult(HaErrorCode.faild));
        haChannelWorker.exec();

        Mockito.verify(haChannelBossService, times(2)).charge(anyLong(), anyLong(), anyString(), anyString(), anyLong());
    }

    public BossOperationResult buildResult(HaErrorCode errorCode) {
        BossOperationResult result = new HaBossOperationResutlImpl(errorCode);
        return result;
    }
}
