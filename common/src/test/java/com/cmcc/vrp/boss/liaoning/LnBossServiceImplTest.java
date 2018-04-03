package com.cmcc.vrp.boss.liaoning;

import static org.mockito.Matchers.anyLong;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.cmcc.vrp.boss.liaoning.util.LnGlobalConfig;
import com.cmcc.vrp.boss.liaoning.util.SignUtil;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.google.gson.Gson;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年5月23日 下午6:44:33
*/
@RunWith(PowerMockRunner.class)
@PrepareForTest({SignUtil.class, Gson.class})
public class LnBossServiceImplTest {
    @InjectMocks
    LnBossServiceImpl bossService = new LnBossServiceImpl();

    @Mock
    SupplierProductService supplierProductService;
    @Mock
    EnterprisesService enterprisesService;
    
    @Mock
    SerialNumService serialNumService;

    
    @Mock
    LnGlobalConfig lnGlobalConfig;
    @Before
    public void initMocks() {
        PowerMockito.when(lnGlobalConfig.getAppId()).thenReturn("501800");
        PowerMockito.when(lnGlobalConfig.getAppKey()).thenReturn("09b149b082d320cb54cd7385b129371f");
        PowerMockito.when(lnGlobalConfig.getCustId()).thenReturn("100016261826");
        PowerMockito.when(lnGlobalConfig.getEffectiveWay()).thenReturn("0");
        PowerMockito.when(lnGlobalConfig.getSendMsg()).thenReturn("0");
        PowerMockito.when(lnGlobalConfig.getGiveMonth()).thenReturn("1");
        PowerMockito.when(lnGlobalConfig.getOpenId()).thenReturn("e2f94078-4c61-4d25-96ec-9a5aa82cb063");
        PowerMockito.when(lnGlobalConfig.getUrl()).thenReturn("http://221.180.247.69:5291/oppf");
        PowerMockito.when(lnGlobalConfig.getAccessToken()).thenReturn("58d238d7-45ff-4f02-bc94-6cd93c5fff2c");
        PowerMockito.when(lnGlobalConfig.getOperId()).thenReturn("100004638522");
        Gson gson = new Gson();
        ReflectionTestUtils.setField(bossService, "gson", gson);    

    }
    
    @Test
    public void testCharge() throws Exception {
        String meString ="{\"Response\":{\"ErrorInfo\":{\"Code\":\"0000\",\"DealTime\":10,\"Message\":\"成功\"},\"RetInfo\":{\"BatchNO\":\"384807374249009\",\"CustName\":\"\",\"Date\":\"\",\"ErrorInfo\":{\"Code\":\"0000\",\"Hint\":\"成功\",\"Message\":\"成功\"},\"FailureFee\":\"\",\"FailureNO\":\"\",\"SuccessFee\":\"\",\"SuccessNO\":\"41631858462917\"}}}";
        PowerMockito.mockStatic(SignUtil.class);
        String systemNum = SerialNumGenerator.buildSerialNum();
        Assert.assertNull(bossService.charge(null, null, null, null, null));
        PowerMockito.when(supplierProductService.selectByPrimaryKey(anyLong())).thenReturn(null).thenReturn(buildSupplierProduct());
        Assert.assertNull(bossService.charge(1l, 1l, "18867101129", systemNum, null));
        PowerMockito.when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(null).thenReturn(buildEnt());
        Assert.assertNull(bossService.charge(1l, 1l, "18867101129", systemNum, null));
        Assert.assertNull(bossService.charge(1l, 1l, "", systemNum, null));
        Assert.assertNull(bossService.charge(1l, 1l, "18867101129", null, null));
        PowerMockito.when(SignUtil.execute(Mockito.anyMap(), Mockito.anyString(),Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
            .thenReturn(meString);
        PowerMockito.when(serialNumService.getByPltSerialNum(Mockito.anyString())).thenReturn(new SerialNum());
        PowerMockito.when(serialNumService.updateSerial(Mockito.any(SerialNum.class))).thenReturn(true);
        Assert.assertTrue(bossService.charge(1l, 1l, "18867101129", systemNum, null).isSuccess());
    }
    
    /**
     * @return
     */
    public SupplierProduct buildSupplierProduct() {
        SupplierProduct supplierProduct = new SupplierProduct();
        supplierProduct.setCode("1234");
        return supplierProduct;
    }
    /**
     * @return
     */
    public Enterprise buildEnt() {
        Enterprise enterprise = new Enterprise();
        enterprise.setCode("1234");
        return enterprise;
    }
}
