package com.cmcc.vrp.boss.fujian;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Hashtable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.cmcc.api.fj.payflow.bean.LLTFMemberOrder;
import com.cmcc.api.fj.payflow.bean.ResultInfo;
import com.cmcc.api.fj.payflow.service.PayFlowService;
import com.cmcc.vrp.boss.BaseBossServiceTest;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.fujian.model.FjReturnCode;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * Created by lilin on 2016/10/11.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SerialNumGenerator.class, FjBossServiceImpl.class})
public class FjBossServiceImplTest extends BaseBossServiceTest {

    @InjectMocks
    FjBossServiceImpl fjBossService = new FjBossServiceImpl();

    @Mock
    SupplierProductService productService;

    @Mock
    GlobalConfigService globalConfigService;

    @Mock
    SerialNumService serialNumService;

    @Before
    public void initMocks(){
        PowerMockito.when(productService.selectByPrimaryKey(anyLong())).thenReturn(buildSP());
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_FUJIAN_APPID.getKey())).thenReturn("xxxxxx");
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_FUJIAN_APPKEY.getKey())).thenReturn("xxxxxx");
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_FUJIAN_ECCODE.getKey())).thenReturn("xxxxxx");
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_FUJIAN_ORDER_URL.getKey())).thenReturn("xxxxxx");
    }

    @Test
    public void testCharge() throws Exception {
        String systemNum = SerialNumGenerator.buildSerialNum();

        PowerMockito.mockStatic(SerialNumGenerator.class);
        PowerMockito.when(SerialNumGenerator.buildNormalBossReqNum("fujian", 25)).thenReturn("xxxxxx");
        PowerMockito.when(SerialNumGenerator.buildNullBossRespNum("fujian")).thenReturn("xxxxxx");

        when(serialNumService.getByPltSerialNum(systemNum)).thenReturn(new SerialNum());
        when(serialNumService.updateSerial(any(SerialNum.class))).thenReturn(true);

        LLTFMemberOrder lltfMemberOrder = new LLTFMemberOrder();
        PowerMockito.whenNew(LLTFMemberOrder.class).withNoArguments().thenReturn(lltfMemberOrder);

        PayFlowService flowService = PowerMockito.mock(PayFlowService.class);
        PowerMockito.whenNew(PayFlowService.class).withArguments("xxxxxx", "xxxxxx", "xxxxxx").thenReturn(flowService);
        Hashtable hashtable = new Hashtable();
        hashtable.put("ResultInfo", buildInfo(FjReturnCode.SUCCESS.getCode()));
        PowerMockito.when(flowService.orderMemberProduct(lltfMemberOrder)).thenReturn(hashtable);

        BossOperationResult result = fjBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertTrue(result.isSuccess());

       /* when(hashtable.get("ResultInfo")).thenReturn(buildRI(FjReturnCode.FAILD.getCode()));
        BossOperationResult result1 = fjBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertFalse(result1.isSuccess());

        verify(flowService, times(2)).orderMemberProduct(lltfMemberOrder);*/
    }

    private ResultInfo buildRI(String code) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setResultCode(code);
        return resultInfo;
    }

    private ResultInfo buildInfo(String resultCode) {
        ResultInfo info = new ResultInfo();
        info.setResultCode(resultCode);
        return info;
    }
}
