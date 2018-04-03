package com.cmcc.vrp.queue.task.channel;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.hebei.HbBossOperationResultImpl;
import com.cmcc.vrp.boss.hebei.HbBossServiceImpl;
import com.cmcc.vrp.boss.hebei.model.HbReturnCode;
import com.cmcc.vrp.boss.henan.AbstractHaBossChargeService;
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
public class HeChannelWorkerTest extends BaseChannelWorkerTest {
    @InjectMocks
    HeChannelWorker heChannelWorker = new HeChannelWorker();

    @Mock
    HbBossServiceImpl hbBossService;

    @Mock
    ProductService productService;


    @Before
    public void initMocks() {
        heChannelWorker.setTaskString(getTaskStr());
        Gson gson = new Gson();
        ReflectionTestUtils.setField(heChannelWorker, "gson", gson);
        ChargeRecord record = new ChargeRecord();
        record.setStatus(4);
        PowerMockito.when(chargeRecordService.getRecordBySN(anyString())).thenReturn(record);
        PowerMockito.when(chargeRecordService.updateStatus(anyLong(), any(ChargeResult.class))).thenReturn(true);
        PowerMockito.when(productService.get(anyLong())).thenReturn(new Product());
    }

    @Test
    @Ignore
    public void testCqChannel() {
        PowerMockito.when(hbBossService.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult(HbReturnCode.SUCCESS));
        heChannelWorker.exec();

        PowerMockito.when(hbBossService.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult(HbReturnCode.FAILD));
        heChannelWorker.exec();

        Mockito.verify(hbBossService, times(2)).charge(anyLong(), anyLong(), anyString(), anyString(), anyLong());
    }

    public BossOperationResult buildResult(HbReturnCode errorCode) {
        BossOperationResult result = new HbBossOperationResultImpl(errorCode);
        return result;
    }
}
