package com.cmcc.vrp.boss.jiangxi;

import com.cmcc.vrp.boss.BaseBossServiceTest;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.jiangxi.model.JXChargeBodyReq;
import com.cmcc.vrp.boss.jiangxi.model.JXChargeBodyResp;
import com.cmcc.vrp.boss.jiangxi.model.JXChargeHead;
import com.cmcc.vrp.boss.jiangxi.model.JXReturnCode;
import com.cmcc.vrp.boss.jiangxi.model.PresentPhoneVolumeRsp;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by lilin on 2016/10/12.
 */
@PrepareForTest({JxBossServiceImpl.class, SerialNumGenerator.class})
public class JxBossServiceImplTest extends BaseBossServiceTest {

    @InjectMocks
    JxBossServiceImpl jxBossService = new JxBossServiceImpl();

    @Mock
    SupplierProductService productService;

    @Mock
    GlobalConfigService globalConfigService;

    @Mock
    SerialNumService serialNumService;


    @Before
    public void initMocks() {
        when(productService.selectByPrimaryKey(anyLong())).thenReturn(buildSP());
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_JIANGXI_APPSECRET.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_JIANGXI_SERVICEID.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_JIANGXI_ECCODE.getKey())).thenReturn("xxxxxx");
    }

    @Ignore
    @Test
    public void testCharge() throws Exception {
        String systemNum = SerialNumGenerator.buildSerialNum();

        when(serialNumService.getByPltSerialNum(systemNum)).thenReturn(new SerialNum());
        when(serialNumService.updateSerial(any(SerialNum.class))).thenReturn(true);

        PowerMockito.mockStatic(SerialNumGenerator.class);
        PowerMockito.when(SerialNumGenerator.buildNormalBossReqNum("jiangxi", 25)).thenReturn("xxxxxx");

        jxBossService = PowerMockito.spy(jxBossService);
        JXChargeHead head = PowerMockito.mock(JXChargeHead.class);
        PowerMockito.doReturn(head).when(jxBossService, "buildHead", "xxxxxx");
        JXChargeBodyReq bodyReq = PowerMockito.mock(JXChargeBodyReq.class);
        PowerMockito.doReturn(bodyReq).when(jxBossService, "buildBody", "18867101129", "123456");
        PowerMockito.doReturn("xxxxxx").when(jxBossService, "encryptBody", bodyReq, "xxxxxx");
        PowerMockito.doReturn("xxxxxx").when(jxBossService, "buildReqStr", head, "xxxxxx");

        Service service = PowerMockito.mock(Service.class);
        PowerMockito.whenNew(Service.class).withNoArguments().thenReturn(service);
        Call call = PowerMockito.mock(Call.class);
        PowerMockito.when(service.createCall()).thenReturn(call);

        Object[] objects = new Object[]{"xxxxxx"};
        PowerMockito.whenNew(Object.class).withNoArguments().thenReturn(objects);

        PowerMockito.when(call.invoke(objects)).thenReturn("xxxxxx");

        PresentPhoneVolumeRsp rsp = PowerMockito.mock(PresentPhoneVolumeRsp.class);
        PowerMockito.doReturn(rsp).when(jxBossService, "xml2Obj", PresentPhoneVolumeRsp.class, "PresentPhoneVolumeRsp", "xxxxxx");
        PowerMockito.when(rsp.getChargeBody()).thenReturn("xxxxxx");
        PowerMockito.doReturn("xxxxxx").when(jxBossService, "decryptBody", "xxxxxx", "xxxxxx");

        // 1 成功
        PowerMockito.doReturn(buildJXResp(JXReturnCode.SUCCESS.getCode())).when(jxBossService, "xml2Obj", JXChargeBodyResp.class, "BODY", "xxxxxx");
        BossOperationResult success = jxBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertTrue(success.isSuccess());

        // 2 失败
        PowerMockito.doReturn(buildJXResp(JXReturnCode.FAILD.getCode())).when(jxBossService, "xml2Obj", JXChargeBodyResp.class, "BODY", "xxxxxx");
        BossOperationResult faild = jxBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertFalse(faild.isSuccess());

        // 3 系统异常
        PowerMockito.doReturn(buildJXResp(JXReturnCode.SYSTEM_EXCP.getCode())).when(jxBossService, "xml2Obj", JXChargeBodyResp.class, "BODY", "xxxxxx");
        BossOperationResult systemExcep = jxBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertFalse(systemExcep.isSuccess());

        // 4 授权失败
        PowerMockito.doReturn(buildJXResp(JXReturnCode.AUTH_FAILD.getCode())).when(jxBossService, "xml2Obj", JXChargeBodyResp.class, "BODY", "xxxxxx");
        BossOperationResult authFaild = jxBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertFalse(authFaild.isSuccess());

        // 5 数据非法加密
        PowerMockito.doReturn(buildJXResp(JXReturnCode.DATA_ILLEGALITY.getCode())).when(jxBossService, "xml2Obj", JXChargeBodyResp.class, "BODY", "xxxxxx");
        BossOperationResult dataIlleg = jxBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertFalse(dataIlleg.isSuccess());

        // 6 ECCODE参数非法
        PowerMockito.doReturn(buildJXResp(JXReturnCode.PARA_ILLEGALITY.getCode())).when(jxBossService, "xml2Obj", JXChargeBodyResp.class, "BODY", "xxxxxx");
        BossOperationResult paraIlleg = jxBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertFalse(paraIlleg.isSuccess());

        verify(call, times(6)).invoke(objects);
    }

    private JXChargeBodyResp buildJXResp(String resultCode) {
        JXChargeBodyResp resp = new JXChargeBodyResp();
        resp.setResultCode(resultCode);
        return resp;
    }
}
