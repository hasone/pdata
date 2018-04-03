package com.cmcc.vrp.boss.jilin;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import java.rmi.RemoteException;

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

import com.cmcc.vrp.boss.jilin.utils.Sign;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;


/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年4月6日 下午6:33:51
*/
@RunWith(PowerMockRunner.class)
@PrepareForTest({Sign.class})
public class JlBossServicesImplTest{
    @InjectMocks
    JlBossServiceImpl bossService = new JlBossServiceImpl();

    
    @Mock
    EnterprisesService enterprisesService;
    
    @Mock
    SupplierProductService supplierProductService;
    
    @Mock
    GlobalConfigService globalConfigService;
   
    
    @Mock
    SerialNumService serialNumService;
    
    /**
     * @throws RemoteException
     */
    @Before
    public void initMocks() throws RemoteException {

        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_JILIN_CHARGE_URL.getKey())).thenReturn("http://211.141.63.140:18080/rest/1.0/orderPrcSubmit");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_JILIN_APPKEY.getKey())).thenReturn("10001020");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_JILIN_STATUS.getKey())).thenReturn("1");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_JILIN_USERNAME.getKey())).thenReturn("linguangkuo");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_JILIN_PRIVATEKEY_FILE.getKey())).thenReturn("D:\\airreCharge_private_key.pem");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_JILIN_OWEPAY_URL.getKey())).thenReturn("http://211.141.63.140:18080/rest/1.0/owePayQry");

    }
    
    /**
     * 
     */
    @Test
    public void chargeTest() {
        String string = "{   \"RETURN_CODE\":\"0\",   \"RETURN_MSG\":\"OK\",   \"TRANSIDO\":\"jilin69856f9f5c4b609\",   \"BOSSTRANSIDD\":\"O17040500000000\"}";
        PowerMockito.mockStatic(Sign.class);
        String systemNum = SerialNumGenerator.buildSerialNum();
        SupplierProduct supplierProduct = new SupplierProduct();
        supplierProduct.setCode("22CBZ1013188");
        
        Enterprise enterprise = new Enterprise();
        enterprise.setCode("10012702254");
        Assert.assertNull(bossService.charge(null, null, null, null, null));
        PowerMockito.when(supplierProductService.selectByPrimaryKey(anyLong())).thenReturn(null).thenReturn(supplierProduct);
        Assert.assertNull(bossService.charge(1l, 1l, "18867101129", systemNum, null));
        PowerMockito.when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(null).thenReturn(enterprise);
        Assert.assertNull(bossService.charge(1l, 1l, "18867101129", systemNum, null));
        Assert.assertNull(bossService.charge(1l, 1l, "", systemNum, null));
        Assert.assertNull(bossService.charge(1l, 1l, "18867101129", null, null));
        when(supplierProductService.selectByPrimaryKey(anyLong())).thenReturn(supplierProduct);
        when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(enterprise);
        PowerMockito.when(Sign.toSign(Mockito.anyString(), Mockito.anyString()))
            .thenReturn("1234");
        PowerMockito.when(Sign.sendPost(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(string);
        PowerMockito.when(serialNumService.getByPltSerialNum(Mockito.anyString())).thenReturn(new SerialNum());
        PowerMockito.when(serialNumService.updateSerial(Mockito.any(SerialNum.class))).thenReturn(true);
        Assert.assertTrue(bossService.charge(1l, 1l, "18867101129", systemNum, null).isSuccess());
    }
    
    /**
     * 
     */
    @Test
    public void owePayQryTest() {
        String string = "{   \"RETURN_CODE\":\"0\",   \"RETURN_MSG\":\"OK\",   \"TRANSIDO\":\"jilin69856f9f5c4b609\",   " 
                + "\"BOSSTRANSIDD\":\"O17040500000000\", \"BALANCE\":\"100\"}";
        PowerMockito.mockStatic(Sign.class);
        Assert.assertNull(bossService.owePayQry(null)); 
        Enterprise enterprise = new Enterprise();
        enterprise.setCode("10012702254");
        when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(null).thenReturn(enterprise);  
        Assert.assertNull(bossService.owePayQry(1l)); 
        PowerMockito.when(Sign.toSign(Mockito.anyString(), Mockito.anyString()))
            .thenReturn("1234");
        PowerMockito.when(Sign.sendPost(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(string);
        Assert.assertEquals("100", bossService.owePayQry(1l));
    }
}
