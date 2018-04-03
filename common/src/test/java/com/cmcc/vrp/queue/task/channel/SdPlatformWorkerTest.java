package com.cmcc.vrp.queue.task.channel;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;

import org.apache.commons.lang.StringUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.test.util.ReflectionTestUtils;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.shangdong.boss.service.SdCloudBossServiceImpl;
import com.cmcc.vrp.boss.shangdong.boss.service.SdCloudOperationResultImpl;
import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.PhoneRegion;
import com.cmcc.vrp.province.model.Product;
import com.cmcc.vrp.province.service.ProductService;
import com.cmcc.vrp.queue.pojo.ChargeDeliverPojo;
import com.google.gson.Gson;

/**
 * Created by leelyn on 2016/12/14.
 * 
 * 已统一SdPlatformWorker和其它的Worker，这里本类不再需要，这里去掉@test by qihang 2016/12/27
 */
@PrepareForTest(StringUtils.class)
public class SdPlatformWorkerTest extends BaseChannelWorkerTest {

    @InjectMocks
    SdPlatformWorker sdPlatformWorker = new SdPlatformWorker();

    @Mock
    SdCloudBossServiceImpl sdCloudBossService;
    @Mock
    ProductService productService;

    //@Before
    /**
     * initMocks()
     */
    public void initMocks() {

        Gson gson = new Gson();
        ReflectionTestUtils.setField(sdPlatformWorker, "gson", gson);
        ChargeRecord record = new ChargeRecord();
        record.setStatus(4);
        PowerMockito.when(chargeRecordService.getRecordBySN(anyString())).thenReturn(record);
        PowerMockito.when(chargeRecordService.updateStatus(anyLong(), any(ChargeResult.class))).thenReturn(false);
        PowerMockito.when(productService.get(anyLong())).thenReturn(new Product());
    }

    /**
     * getTaskStr()
     */
    public String getTaskStr() {
        ChargeDeliverPojo pojo = new ChargeDeliverPojo();
        pojo.setActivityName("单元测试");
        pojo.setActivityType(null);
        pojo.setEntId(1l);
        pojo.setMobile("1888888888");
        pojo.setPhoneRegion(new PhoneRegion());
        pojo.setPrdId(1l);
        pojo.setRecordId(1l);
        pojo.setType("ec");
        pojo.setSerialNum("xxxx");
        return new Gson().toJson(pojo);
    }

    //@Test
    /**
     * testExc()
     */
    public void testExc() {
        sdPlatformWorker.setTaskString("");
        PowerMockito.when(sdCloudBossService.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult("200", "成功"));
        sdPlatformWorker.exec();

        PowerMockito.mockStatic(StringUtils.class);
        PowerMockito.when(StringUtils.isBlank(anyString())).thenReturn(false);
        sdPlatformWorker.setTaskString(null);
        PowerMockito.when(sdCloudBossService.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult("200", "成功"));
        sdPlatformWorker.exec();

        sdPlatformWorker.setTaskString(this.getTaskStr());
        PowerMockito.when(sdCloudBossService.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult("200", "成功"));
        sdPlatformWorker.exec();

        sdPlatformWorker.setTaskString(getTaskStr());
        PowerMockito.when(sdCloudBossService.charge(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(buildResult("500", "失败"));
        sdPlatformWorker.exec();

        Mockito.verify(sdCloudBossService, times(2)).charge(anyLong(), anyLong(), anyString(), anyString(), anyLong());
    }

    /**
     * buildResult(String code, String message)
     */
    public BossOperationResult buildResult(String code, String message) {
        BossOperationResult result = new SdCloudOperationResultImpl(code, message,false);
        return result;
    }
}
