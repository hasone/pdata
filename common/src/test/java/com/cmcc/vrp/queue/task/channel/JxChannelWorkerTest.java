package com.cmcc.vrp.queue.task.channel;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.test.util.ReflectionTestUtils;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.jiangxi.JxBossOperationResultImpl;
import com.cmcc.vrp.boss.jiangxi.JxBossServiceImpl;
import com.cmcc.vrp.boss.jiangxi.model.JXReturnCode;
import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.ProductService;
import com.google.gson.Gson;

/**
 * Created by leelyn on 2016/11/17.
 */
@Ignore
public class JxChannelWorkerTest extends BaseChannelWorkerTest {

    @InjectMocks
    JxChannelWorker jxChannelWorker = new JxChannelWorker();

    @Mock
    JxBossServiceImpl jxBossService;
    @Mock
    ProductService productService;

    @Before
    public void initMocks() {
        jxChannelWorker.setTaskString(getTaskStr());
        Gson gson = new Gson();
        ReflectionTestUtils.setField(jxChannelWorker, "gson", gson);
        ChargeRecord record = new ChargeRecord();
        record.setStatus(4);
        PowerMockito.when(chargeRecordService.getRecordBySN(anyString())).thenReturn(record);
        PowerMockito.when(chargeRecordService.updateStatus(anyLong(), any(ChargeResult.class))).thenReturn(true);
        PowerMockito.when(productService.get(anyLong())).thenReturn(new Product());
    }

    @Test
    public void testCqChannel() {
        PowerMockito.when(jxBossService.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult(JXReturnCode.SUCCESS));
        jxChannelWorker.exec();

        PowerMockito.when(jxBossService.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult(JXReturnCode.FAILD));
        jxChannelWorker.exec();

        Mockito.verify(jxBossService, times(2)).charge(anyLong(), anyLong(), anyString(), anyString(), anyLong());
    }

    public BossOperationResult buildResult(JXReturnCode code) {
        BossOperationResult result = new JxBossOperationResultImpl(code);
        return result;
    }
}
