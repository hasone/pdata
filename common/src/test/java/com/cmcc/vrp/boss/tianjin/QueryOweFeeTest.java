package com.cmcc.vrp.boss.tianjin;

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
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.cmcc.vrp.boss.tianjin.util.GlobalConfig;
import com.cmcc.vrp.boss.tianjin.util.Sign;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.util.HttpUtils;
import com.google.gson.Gson;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年5月25日 下午5:24:30
*/
@RunWith(PowerMockRunner.class)
@PrepareForTest({Sign.class, Gson.class, HttpUtils.class})
@SuppressStaticInitializationFor("com.cmcc.vrp.util.HttpUtils")
public class QueryOweFeeTest {
    @InjectMocks
    QueryOweFee bossService = new QueryOweFee();
    @Mock
    EnterprisesService enterprisesService;  
    @Mock
    GlobalConfig globalConfig;
    @Mock
    GetProductInfo getProductInfo;
    /**
     * 
     */
    @Before
    public void initMocks() {
        PowerMockito.when(globalConfig.getUrl()).thenReturn("http://211.103.90.123:80/oppf");
        PowerMockito.when(globalConfig.getAppId()).thenReturn("505386");
        PowerMockito.when(globalConfig.getStatus()).thenReturn("1");
        PowerMockito.when(globalConfig.getAppKey()).thenReturn("d82ab7f18cf511977a42551e4f784ac4");
        PowerMockito.when(globalConfig.getPublicKey()).thenReturn("xzxzxzx");
        PowerMockito.when(globalConfig.getTradeDepartId()).thenReturn("30824");
        PowerMockito.when(globalConfig.getTradeStaffId()).thenReturn("KVZY0001");
        PowerMockito.when(globalConfig.getDepartId()).thenReturn("30824");
        PowerMockito.when(globalConfig.getTradeDepartPasswd()).thenReturn("lc1234");

        Gson gson = new Gson();
        ReflectionTestUtils.setField(bossService, "gson", gson);    
    }
    @Test
    public void testQueryOweFee() {
        String mString = "{\"respCode\":\"0\",\"respDesc\":\"ok\",\"result\":{\"SVC_CONTENT\":[{\"ACCT_ID\":\"1116031607212537\",\"ACCT_NEW_BALANCE\":\"100\"," 
                + "\"CITY_CODE\":\"\",\"CUST_ID\":\"1116112429517893\",\"CUST_TYPE\":\"MEB\",\"DB_SOURCE\":\"0022\",\"DEPART_CODE\":" 
                + "\"30824\",\"DEPART_ID\":\"\",\"DEPART_NAME\":\"\",\"EPARCHY_CODE\":\"\",\"IN_MODE_CODE\":\"Z\",\"LOGIN_EPARCHY_CODE\":" 
                + "\"0022\",\"MAIN_ID\":\"\",\"ORDER_ID\":\"1117021474287345\",\"ROUND_AUDIT\":\"\",\"SOURCE_NAME\":\"\"," 
                + "\"STAFF_EPARCHY_CODE\":\"0022\",\"STAFF_ID\":\"KVZY0001\",\"STAFF_NAME\":\"\",\"SUBSYS_CODE\":\"groupserv\"," 
                + "\"TRADE_ID\":\"1117021431645879\",\"TRADE_TYPE_CODE\":\"3166\",\"USER_ID\":\"1116031648729848\",\"X_CLIENT_IP\":\"\"," 
                + "\"X_IBOSSMODE\":\"0\",\"X_MENU_ID\":\"\",\"X_NODENAME\":\"app-node16-srv06:dc1643d3fa12468ebeb291d2c1d2b782:1487039184124\"," 
                +"\"X_PAGINCOUNT\":\"0\",\"X_PAGINCURRENT\":\"0\",\"X_PAGINSIZE\":\"0\",\"X_RECORDNUM\":\"1\",\"X_RESULTCODE\":\"0\"," 
                + "\"X_RESULTCOUNT\":\"0\",\"X_RESULTINFO\":\"ok\",\"X_RESULTSIZE\":\"1\",\"X_RSPCODE\":\"0000\",\"X_RSPDESC\":\"ok\"," 
                + "\"X_RSPTYPE\":\"0\",\"X_TRANSMODE\":\"0\",\"X_TRANS_CODE\":\"SS.TcsGrpIntfSVC.netWorkGiftPurchase\"}]," 
                + "\"X_RESULTCODE\":\"0\",\"X_RESULTINFO\":\"ok\"}}";
        PowerMockito.mockStatic(Sign.class);
        PowerMockito.mockStatic(HttpUtils.class);
        Assert.assertNull(bossService.queryOweFee(null, null));
        PowerMockito.when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(null).thenReturn(buildEnt());
        Assert.assertNull(bossService.queryOweFee(1l, "123"));
        PowerMockito.when(getProductInfo.getUrlID(Mockito.anyString(),Mockito.anyString())).thenReturn(null).thenReturn("123456");
        Assert.assertNull(bossService.queryOweFee(1l, "123"));
        PowerMockito.when(Sign.generateSign(Mockito.anyMap(), Mockito.anyString(), Mockito.anyString()))
            .thenReturn("123445567");
        PowerMockito.when(HttpUtils.post(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
            .thenReturn(mString);
        Assert.assertEquals("100",bossService.queryOweFee(1l, "123"));
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
