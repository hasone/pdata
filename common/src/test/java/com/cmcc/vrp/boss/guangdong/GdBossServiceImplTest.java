package com.cmcc.vrp.boss.guangdong;

import com.cmcc.vrp.boss.BaseBossServiceTest;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.guangdong.model.GdFeatrue;
import com.cmcc.vrp.boss.guangdong.model.GdReturnCode;
import com.cmcc.vrp.boss.guangdong.webservice.NGADCServicesForECStub;
import com.cmcc.vrp.boss.guangdong.webservice.NGEC;
import com.cmcc.vrp.boss.guangdong.webservice.Response;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;
import org.apache.axis.client.Service;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.URL;
import java.rmi.RemoteException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by lilin on 2016/10/10.
 */
@PrepareForTest({GdBossServiceImpl.class, SerialNumGenerator.class})
public class GdBossServiceImplTest extends BaseBossServiceTest {

    @InjectMocks
    GdBossServiceImpl gdBossService = new GdBossServiceImpl();

    @Mock
    SupplierProductService productService;

    @Mock
    SerialNumService serialNumService;

    @Mock
    GlobalConfigService globalConfigService;

    @Autowired
    Gson gson;

    private SupplierProduct product;


    @Before
    public void initMocks() throws RemoteException {
        product = this.buildSP();
        when(productService.selectByPrimaryKey(1l)).thenReturn(product);

        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGDONG_ECCODE.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGDONG_ECUSERNAME.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGDONG_ECPASSWORD.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGDONG_PRDORDNUM.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGDONG_CHARGE_URL.getKey())).thenReturn("xxxxxx");
        ReflectionTestUtils.setField(gdBossService, "gson", gson);

    }

    @Ignore
    @Test
    public void testCharge() throws Exception {
        String systemNum = SerialNumGenerator.buildSerialNum();

        when(serialNumService.getByPltSerialNum(systemNum)).thenReturn(new SerialNum());
        when(serialNumService.updateSerial(any(SerialNum.class))).thenReturn(true);

        Service service = PowerMockito.mock(Service.class);
        PowerMockito.whenNew(Service.class).withNoArguments().thenReturn(service);
        URL endpointURL = PowerMockito.mock(URL.class);
        PowerMockito.whenNew(URL.class).withArguments("xxxxxx").thenReturn(endpointURL);

        PowerMockito.mockStatic(SerialNumGenerator.class);
        PowerMockito.when(SerialNumGenerator.buildNormalBossReqNum("guangdong", 25)).thenReturn("xxxxxx");

        gdBossService = PowerMockito.spy(gdBossService);
        NGEC ngec = new NGEC();
        PowerMockito.doReturn(ngec).when(gdBossService, "buildNGEC", "18867101129", product, "xxxxxx", true);
        PowerMockito.doReturn(ngec).when(gdBossService, "buildNGEC", "18867101129", product, "xxxxxx", false);

        NGADCServicesForECStub stub = PowerMockito.mock(NGADCServicesForECStub.class);
        PowerMockito.whenNew(NGADCServicesForECStub.class).withArguments(endpointURL, service).thenReturn(stub);

        PowerMockito.when(stub.adcServices(ngec)).thenReturn(buildNGEC(GdReturnCode.SUCCESS.getCode()));
        BossOperationResult result = gdBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertTrue(result.isSuccess());

        PowerMockito.when(stub.adcServices(ngec)).thenReturn(buildNGEC(GdReturnCode.FAILD.getCode()));
        BossOperationResult result1 = gdBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertFalse(result1.isSuccess());

//        PowerMockito.when(stub.adcServices(ngec)).thenReturn(buildNGEC(GdReturnCode.ERR_940037.getCode()));
//        BossOperationResult result2 = gdBossService.charge(1l, 1l, "18867101129", systemNum);
//        Assert.assertTrue(result2.isSuccess());
    }

    @Override
    public SupplierProduct buildSP() {
        SupplierProduct product = super.buildSP();
        product.setFeature(buildFeatureStr());
        return product;
    }

    private String buildFeatureStr() {
        GdFeatrue featrue = new GdFeatrue();
        featrue.setServiceCode("xxxxxxx");
        return new Gson().toJson(featrue);
    }

    private NGEC buildNGEC(String rspCode) {
        NGEC ngec = new NGEC();
        Response response = new Response();
        response.setRspCode(rspCode);
        ngec.setResponse(response);
        return ngec;
    }
}
