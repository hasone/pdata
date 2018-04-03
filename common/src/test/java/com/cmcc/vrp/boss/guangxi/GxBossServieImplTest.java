package com.cmcc.vrp.boss.guangxi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;

import org.apache.http.impl.client.BasicResponseHandler;
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

import com.cmcc.vrp.boss.BaseBossServiceTest;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.guangxi.model.AdditionRsp;
import com.cmcc.vrp.boss.guangxi.model.GxChargeResp;
import com.cmcc.vrp.boss.guangxi.model.GxReturnCode;
import com.cmcc.vrp.boss.guangxi.model.OperSeqList;
import com.cmcc.vrp.boss.guangxi.model.Response;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.HttpUtils;
import com.thoughtworks.xstream.XStream;

/**
 * Created by leelyn on 2016/10/10.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SerialNumGenerator.class, HttpUtils.class})
@SuppressStaticInitializationFor("com.cmcc.vrp.util.HttpUtils")
public class GxBossServieImplTest extends BaseBossServiceTest {

    @InjectMocks
    GxBossServiceImpl gxBossService = new GxBossServiceImpl();

    @Mock
    SupplierProductService productService;

    @Mock
    SerialNumService serialNumService;

    @Mock
    private GlobalConfigService globalConfigService;

    @Before
    public void initMocks() {
        PowerMockito.when(productService.selectByPrimaryKey(anyLong())).thenReturn(buildSP());
        PowerMockito.when(globalConfigService.get(anyString())).thenReturn("test");

    }

    @Test
    public void testCharge() throws Exception {
        Assert.assertFalse((gxBossService.charge(1l, 1l, "18867101129", null, null)).isSuccess());
                
        String systemNum = SerialNumGenerator.buildSerialNum();

        PowerMockito.when(serialNumService.getByPltSerialNum(anyString())).thenReturn(new SerialNum());
        PowerMockito.when(serialNumService.updateSerial(any(SerialNum.class))).thenReturn(false);

        PowerMockito.mockStatic(SerialNumGenerator.class);
        PowerMockito.when(SerialNumGenerator.buildNormalBossReqNum("guangxi", 25)).thenReturn("xxxxxx");

        PowerMockito.mockStatic(HttpUtils.class);
        String gxRespStr = buildResp();
        gxRespStr = gxRespStr.replace("xxxxxx", buildAddtionRsp(GxReturnCode.SUCCESS.getCode()));

        PowerMockito.when(HttpUtils.post(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(),
                Mockito.anyString(), Mockito.any(BasicResponseHandler.class))).thenReturn(gxRespStr);
 
        BossOperationResult result = gxBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertTrue(result.isSuccess());
        
    }
    
    @Test
    public void testCharge1() throws Exception {
        String systemNum = SerialNumGenerator.buildSerialNum();

        PowerMockito.when(serialNumService.getByPltSerialNum(anyString())).thenReturn(new SerialNum());
        PowerMockito.when(serialNumService.updateSerial(any(SerialNum.class))).thenReturn(false);

        PowerMockito.mockStatic(SerialNumGenerator.class);
        PowerMockito.when(SerialNumGenerator.buildNormalBossReqNum("guangxi", 25)).thenReturn("xxxxxx");

        PowerMockito.mockStatic(HttpUtils.class);
        String gxRespStr = buildResp();
        gxRespStr = gxRespStr.replace("xxxxxx", buildAddtionRsp(GxReturnCode.FAILD.getCode()));

        PowerMockito.when(HttpUtils.post(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(),
                Mockito.anyString(), Mockito.any(BasicResponseHandler.class))).thenReturn(gxRespStr);
        
        BossOperationResult result = gxBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertFalse(result.isSuccess());
        
        gxRespStr = buildRespFail();
        PowerMockito.when(HttpUtils.post(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(),
                Mockito.anyString(), Mockito.any(BasicResponseHandler.class))).thenReturn(gxRespStr);
        
        result = gxBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertFalse(result.isSuccess());
        
        gxRespStr = buildResp();
        //gxRespStr = gxRespStr.replace("xxxxxx", buildAddtionRsp(GxReturnCode.FAILD.getCode()));

        PowerMockito.when(HttpUtils.post(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(),
                Mockito.anyString(), Mockito.any(BasicResponseHandler.class))).thenReturn(gxRespStr);
        
        result = gxBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertFalse(result.isSuccess());
        
    }

    private String buildResp() {
        GxChargeResp resp = new GxChargeResp();
        Response response = new Response();
        response.setRspCode("0000");
        resp.setResponse(response);
        resp.setSvcCont("xxxxxx");
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        xStream.alias("InterBOSS", GxChargeResp.class);
        return xStream.toXML(resp);
    }
    
    private String buildRespFail() {
        GxChargeResp resp = new GxChargeResp();
        Response response = new Response();
        response.setRspCode("0001");
        resp.setResponse(response);
        resp.setSvcCont("xxxxxx");
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        xStream.alias("InterBOSS", GxChargeResp.class);
        return xStream.toXML(resp);
    }

    private String buildAddtionRsp(String status) {
        AdditionRsp rsp = new AdditionRsp();
        rsp.setStatus(status);
        OperSeqList OperSeqList = new OperSeqList();
        OperSeqList.setOperSeq("xxxxxx");
        rsp.setOperSeqList(OperSeqList);
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        xStream.alias("AdditionRsp", AdditionRsp.class);
        StringBuffer SB = new StringBuffer();
        SB.append("<![CDATA[");
        SB.append(xStream.toXML(rsp));
        SB.append("]]>");
        return SB.toString();
    }
    
    @Test
    public void testGetFingerPrint(){
        assertEquals(gxBossService.getFingerPrint(), "guangxi123456789");
    }

    @Test
    public void testMdrcCharge(){
        assertNull(gxBossService.mdrcCharge("test", "test"));
    }
    
    @Test
    public void testSyncFromBoss(){
        assertFalse(gxBossService.syncFromBoss(1L,1L));
    }

}
