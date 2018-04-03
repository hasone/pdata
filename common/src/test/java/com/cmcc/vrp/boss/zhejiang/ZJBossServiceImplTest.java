package com.cmcc.vrp.boss.zhejiang;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.test.util.ReflectionTestUtils;

import com.cmcc.vrp.boss.BaseBossServiceTest;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.zhejiang.model.Result;
import com.cmcc.vrp.boss.zhejiang.model.ZjChargeReq;
import com.cmcc.vrp.boss.zhejiang.model.ZjChargeResp;
import com.cmcc.vrp.boss.zhejiang.model.ZjErrorCode;
import com.cmcc.vrp.boss.zhejiang.utils.TimestampUtil;
import com.cmcc.vrp.boss.zhejiang.utils.ZjHttpUtil;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;

/**
 * Created by lilin on 2016/10/11.
 */
@Ignore
@PrepareForTest({ZjBossServiceImpl.class, TimestampUtil.class, SerialNumGenerator.class, Gson.class, ZjHttpUtil.class})
public class ZJBossServiceImplTest extends BaseBossServiceTest {

    @InjectMocks
    ZjBossServiceImpl zjBossService = new ZjBossServiceImpl();

    @Mock
    SupplierProductService productService;

    @Mock
    GlobalConfigService configService;

    @Mock
    SerialNumService serialNumService;

    /**
     * 浙江BOSS单元测试初始化配置
     */
    @Before
    public void initMocks() {
        when(productService.selectByPrimaryKey(anyLong())).thenReturn(buildSP());
        when(configService.get(GlobalConfigKeyEnum.BOSS_ZJ_CHARGE_URL.getKey())).thenReturn("xxxxxx");
        when(configService.get(GlobalConfigKeyEnum.BOSS_ZJ_CHARGE_APPKEY.getKey())).thenReturn("xxxxxx");
        when(configService.get(GlobalConfigKeyEnum.BOSS_ZJ_CHARGE_APPSECRET.getKey())).thenReturn("xxxxxx");
        when(configService.get(GlobalConfigKeyEnum.BOSS_ZJ_CHARGE_BIZCODE.getKey())).thenReturn("xxxxxx");
    }

    /**
     * 测试充值
     *
     * @throws Exception
     */
    @Ignore
    @Test
    public void testCharge() throws Exception {
        String systemNum = SerialNumGenerator.buildSerialNum();
        when(serialNumService.getByPltSerialNum(systemNum)).thenReturn(new SerialNum());
        when(serialNumService.updateSerial(any(SerialNum.class))).thenReturn(true);

        PowerMockito.mockStatic(TimestampUtil.class);
        PowerMockito.when(TimestampUtil.getTradeDate()).thenReturn("xxxxxx");

        PowerMockito.mockStatic(SerialNumGenerator.class);
        PowerMockito.when(SerialNumGenerator.genRandomNum(8)).thenReturn("xxxxxx");

        zjBossService = PowerMockito.spy(zjBossService);
        ZjChargeReq req = new ZjChargeReq();
        PowerMockito.doReturn(req).when(zjBossService, "buildZJReq", "18867101129", "LLSLxxxxxxxxxxxxxxxxxx", "123456", "xxxxxx");

        Gson gson = PowerMockito.mock(Gson.class);
        PowerMockito.when(gson.toJson(req)).thenReturn("xxxxxx");
        ReflectionTestUtils.setField(zjBossService, "gson", gson);

        PowerMockito.mockStatic(ZjHttpUtil.class);
        PowerMockito.when(ZjHttpUtil.doPost("xxxxxx", "xxxxxx", "xxxxxx", "xxxxxx")).thenReturn("xxxxxx");

        PowerMockito.when(gson.fromJson("xxxxxx", ZjChargeResp.class)).thenReturn(buildResp(ZjErrorCode.SUCCESS.getCode()));
        BossOperationResult result = zjBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertTrue(result.isSuccess());
    }

    private ZjChargeResp buildResp(String code) {
        ZjChargeResp resp = new ZjChargeResp();
        resp.setCode(code);
        Result result = new Result();
        result.setOsbSerialNo("xxxxxx");
        resp.setResult(result);
        return resp;
    }
}
