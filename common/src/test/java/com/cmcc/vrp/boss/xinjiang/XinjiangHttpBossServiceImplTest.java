package com.cmcc.vrp.boss.xinjiang;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.cmcc.vrp.boss.xinjiang.service.impl.XinjiangHttpBossServiceImpl;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.HttpUtils;

/**
 * 
 * XinjiangHttpBossServiceImplTest
 *
 */
@PrepareForTest({HttpUtils.class})
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.cmcc.vrp.util.HttpUtils")
@Ignore
public class XinjiangHttpBossServiceImplTest {
    @InjectMocks
    XinjiangHttpBossServiceImpl service = new XinjiangHttpBossServiceImpl();
    
    @Mock
    GlobalConfigService globalConfigService;
    
    /**
     * testGetResourcePoolRespNew
     */
    @Test
    public void testGetResourcePoolRespNew(){
        PowerMockito.mockStatic(HttpUtils.class);
        String result = "{\"respCode\":\"0\",\"respDesc\":\"ok\",\"result\":{\"OUTDATA\":"
                + "{\"ALL_VALUE\":\"288039.0\",\"END_DATE\":\"20180228235959\",\"START_DATE\":\"20170216000000\"},"
                + "\"X_NODE_NAME\":\"app-node02-srv01:227496d9bb1a44fa9d2c53edd73ce008:1497333052962\","
                + "\"X_RECORDNUM\":\"1\",\"X_RESULTCODE\":\"0\",\"X_RESULTINFO\":\"ok\"}}";
        PowerMockito.when(HttpUtils.post(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(result);
        Assert.assertEquals(service.getResourcePoolRespNew("111").getResultCode(),"0");
        Assert.assertEquals(service.getResourcePoolRespNew("111").getAddValue(),"288039.0");
    }
    
    /**
     * testGetSendResp
     */
    @Test
    public void testGetSendResp(){
        PowerMockito.mockStatic(HttpUtils.class);
        String result = " {\"respCode\":\"0\",\"respDesc\":\"ok\",\"result\":{\"OUTDATA\":"
                + "{\"ACCESS_NUM\":\"13999867721\",\"ORDER_ID\":\"9117061351908966\",\"ORDER_LINE_ID\":"
                + "\"9117061352157885\",\"ORDER_TYPE_CODE\":\"5092\",\"ROUTE_CODE\":\"0991\",\"printFlag\":\"false\"},"
                + "\"X_NODE_NAME\":\"app-node02-srv01:265fa7ac876949df8f9891f9231faaa0:1497333334744\","
                + "\"X_RECORDNUM\":\"1\",\"X_RESULTCODE\":\"0\",\"X_RESULTINFO\":\"ok\"}}";
        PowerMockito.when(HttpUtils.post(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(result);
        Assert.assertEquals(service.getSendResp("123", "123", "123", "123", "123").getResultCode(),"0");
    }
    
    /**
     * testGetNewSendResp
     */
    @Test
    public void testGetNewSendResp(){
        PowerMockito.mockStatic(HttpUtils.class);
        String result = " {\"respCode\":\"0\",\"respDesc\":\"ok\",\"result\":{\"OUTDATA\":"
                + "{\"ACCESS_NUM\":\"13999867721\",\"ORDER_ID\":\"9117061351908966\",\"ORDER_LINE_ID\":"
                + "\"9117061352157885\",\"ORDER_TYPE_CODE\":\"5092\",\"ROUTE_CODE\":\"0991\",\"printFlag\":\"false\"},"
                + "\"X_NODE_NAME\":\"app-node02-srv01:265fa7ac876949df8f9891f9231faaa0:1497333334744\","
                + "\"X_RECORDNUM\":\"1\",\"X_RESULTCODE\":\"0\",\"X_RESULTINFO\":\"ok\"}}";
        PowerMockito.when(HttpUtils.post(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(result);
        Assert.assertEquals(service.getNewSendResp("123", "123", "123", "123").getResultCode(),"0");
    }
}
