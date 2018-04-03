package com.cmcc.vrp.boss.guangdongzc;

import static org.mockito.Mockito.when;

import java.net.URL;
import java.rmi.RemoteException;

import org.apache.axis.client.Service;
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

import com.cmcc.vrp.boss.guangdongzc.webservice.NGADCServicesForECStub;
import com.cmcc.vrp.boss.guangdongzc.webservice.NGEC;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年5月25日 上午11:19:38
*/
@RunWith(PowerMockRunner.class)
@PrepareForTest({Gson.class, GdWheelBossServiceImpl.class})
public class GdWheelBossServiceImplTest {
    @InjectMocks
    GdWheelBossServiceImpl bossService = new GdWheelBossServiceImpl();
    
    @Mock
    SupplierProductService productService;
    
    @Mock
    GlobalConfigService globalConfigService;
   
    
    @Mock
    SerialNumService serialNumService;
    /**
     * @throws RemoteException
     */
    @Before
    public void initMocks() throws RemoteException {

        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGDONG_FLOWCOIN_ECCODE.getKey())).thenReturn("2000188888");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGDONG_FLOWCOIN_ECUSERNAME.getKey())).thenReturn("admin");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGDONG_FLOWCOIN_ECPASSWORD.getKey())).thenReturn("TneHRkPUuqKvpxEJNUSCguMaIoR413Jf");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGDONG_FLOWCOIN_PRDORDNUM.getKey())).thenReturn("50115100100");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_GUANGDONG_FLOWCOIN_CHARGE_URL.getKey())).thenReturn("http://221.179.7.247:8201/NGADCInterface/NGADCServicesForEC.svc/NGADCServicesForEC");

        Gson gson = new Gson();
        ReflectionTestUtils.setField(bossService, "gson", gson);  
         
    }
    /**
     * @throws Exception
     */
    @Test
    public void testCharge() throws Exception {
        String string = "{\"origDomain\":\"NGEC\",\"BIPCode\":\"EC0001\",\"BIPVer\":\"V1.0\",\"transIDO\":\"hy14956976738760078\"," 
                + "\"areacode\":\"MM\",\"ECCode\":\"2000188888\",\"ECUserName\":\"admin\",\"ECUserPwd\":\"TneHRkPUuqKvpxEJNUSCguMaIoR413Jf\"," 
                + "\"processTime\":\"1495697673881\",\"response\":{\"rspCode\":\"0000\",\"rspDesc\":\"请求IP非法,请确保在ADC存在IP配置\"," 
                + "\"__hashCodeCalc\":false},\"svcCont\":\"\",\"__hashCodeCalc\":false}";
        String svcCont = "<MemberShipResponse><BODY><ECCode>2002526782</ECCode><PrdOrdNum>50115004969</PrdOrdNum><Member>" 
                + "<Mobile>13926276632</Mobile><CRMApplyCode>80004080657378</CRMApplyCode><ResultCode>1</ResultCode><ResultMsg/>" 
                + "</Member></BODY></MemberShipResponse>";
        Gson gson = new Gson();
        NGEC ngec = gson.fromJson(string, NGEC.class);
        ngec.setSvcCont(svcCont);
        String systemNum = SerialNumGenerator.buildSerialNum();
        NGADCServicesForECStub locator=PowerMockito.mock(NGADCServicesForECStub.class);  
 
        PowerMockito.whenNew(NGADCServicesForECStub.class).withArguments(Mockito.any(URL.class),Mockito.any(Service.class)).thenReturn(locator);  
        PowerMockito.when(locator.adcServices(Mockito.any(NGEC.class))).thenReturn(ngec);  
        PowerMockito.when(productService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(buildSupplierProduct());
        PowerMockito.when(serialNumService.getByPltSerialNum(Mockito.anyString())).thenReturn(new SerialNum());
        PowerMockito.when(serialNumService.updateSerial(Mockito.any(SerialNum.class))).thenReturn(true);
        Assert.assertTrue(bossService.charge(1l, 1l, "18867101129", systemNum, null).isSuccess());
        
    }
    
    /**
     * @return
     */
    public SupplierProduct buildSupplierProduct() {
        SupplierProduct supplierProduct = new SupplierProduct();
        supplierProduct.setSize(1024l);
        String feature = "{\"serviceCode\":\"8585.memserv3\"}";
        supplierProduct.setFeature(feature);
        return supplierProduct;
    }
    
}
