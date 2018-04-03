package com.cmcc.vrp.queue.task.channel;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.heilongjiang.HLJBossServiceImpl;
import com.cmcc.vrp.boss.heilongjiang.model.HlJBossOperationResultImpl;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;

/**
 * Created by leelyn on 2016/11/17.
 */
public class HlChannelWorkerTest extends BaseChannelWorkerTest {

    @InjectMocks
    HlChannelWorker hlChannelWorker = new HlChannelWorker();

    @Mock
    HLJBossServiceImpl hljBossService;
    @Mock
    ProductService productService;

    @Before
    public void initMocks() {
        hlChannelWorker.setTaskString(getTaskStr());
        Gson gson = new Gson();
        ReflectionTestUtils.setField(hlChannelWorker, "gson", gson);
        ChargeRecord record = new ChargeRecord();
        record.setStatus(4);
        PowerMockito.when(chargeRecordService.getRecordBySN(anyString())).thenReturn(record);
        PowerMockito.when(chargeRecordService.updateStatus(anyLong(), any(ChargeResult.class))).thenReturn(true);
        PowerMockito.when(productService.get(anyLong())).thenReturn(new Product());
    }

    @Test
    @Ignore
    public void testCqChannel() {
        PowerMockito.when(hljBossService.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult("000000"));
        hlChannelWorker.exec();

        PowerMockito.when(hljBossService.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult("000001"));
        hlChannelWorker.exec();

        Mockito.verify(hljBossService, times(2)).charge(anyLong(), anyLong(), anyString(), anyString(), anyLong());
    }

    public BossOperationResult buildResult(String code) {
        List<String> list = new ArrayList<String>();
        list.add(code);
        BossOperationResult result = new HlJBossOperationResultImpl(list);
        return result;
    }
}
