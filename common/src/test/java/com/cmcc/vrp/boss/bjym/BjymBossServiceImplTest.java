package com.cmcc.vrp.boss.bjym;

import com.google.gson.Gson;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.bjym.enums.BjymChargeResponseCodeEnum;
import com.cmcc.vrp.boss.bjym.pojos.BjymChargeRequest;
import com.cmcc.vrp.boss.bjym.pojos.BjymChargeResponse;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.province.service.impl.BaseTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedHashMap;
import java.util.Map;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 北京云漫渠道的ut测试
 *
 * Created by sunyiwei on 2017/5/25.
 */
@RunWith(MockitoJUnitRunner.class)
public class BjymBossServiceImplTest extends BaseTest {
    @InjectMocks
    private BjymBossServiceImpl bossService = new BjymBossServiceImpl();

    @Mock
    private GlobalConfigService globalConfigService;

    @Mock
    private SupplierProductService supplierProductService;

    @Mock
    private SerialNumService serialNumService;

    /**
     * 测试获取渠道名称
     */
    @Test
    public void testGetName() throws Exception {
        assertEquals("北京云漫", bossService.getName());
    }

    /**
     * 测试获取无效的参数结果
     */
    @Test
    public void testGetInvalidParamResult() throws Exception {
        BossOperationResult bor = bossService.getInvalidParamResult();
        assertTrue(isInvalidParamResult(bor));
    }

    /**
     * 测试解析充值结果
     */
    @Test
    public void testParseResult() throws Exception {
        BossOperationResult bor = bossService.parseResult(null);
        assertTrue(isInvalidParamResult(bor));

        assertNotNull(bossService.parseResult(new BjymChargeResponse()));
    }

    private boolean isInvalidParamResult(BossOperationResult bossOperationResult) {
        final BjymChargeResponseCodeEnum bcrce = BjymChargeResponseCodeEnum.INVALID_PARAM;
        return bcrce.getCode().equals(bossOperationResult.getResultCode());
    }

    /**
     * 获取向上游请求的流水号
     */
    @Test
    public void testGetBossReqSerialNum() throws Exception {
        final String pltSn = randStr();
        assertEquals(bossService.getBossReqSerialNum(pltSn), pltSn);
    }

    /**
     * 测试构建请求对象
     */
    @Test
    public void testBuildRequestPojo() throws Exception {
        assertNull(bossService.buildRequestPojo(null, randMobile(), randStr()));

        when(globalConfigService.get(anyString())).thenReturn(randStr());
        assertNotNull(bossService.buildRequestPojo(validSupplierProduct(), randMobile(), randStr()));
        verify(globalConfigService, times(4)).get(anyString());
    }

    /**
     * 测试获取转换器
     */
    @Test
    public void testGetRequestPojoToChargeUrlConverter() throws Exception {
        assertNotNull(bossService.getRequestPojoToChargeUrlConverter());
    }

    /**
     * 测试校验响应对象的有效性
     */
    @Test
    public void testValidateResponsePojo() throws Exception {
        assertFalse(bossService.validateResponsePojo(null));
        assertFalse(bossService.validateResponsePojo(nullCode()));
        assertFalse(bossService.validateResponsePojo(nullDesc()));
        assertFalse(bossService.validateResponsePojo(nullSendId()));
        assertFalse(bossService.validateResponsePojo(nullRequestId()));

        assertTrue(bossService.validateResponsePojo(valid()));
    }

    /**
     * 测试解析响应内容
     */
    @Test
    public void testParseResponse() throws Exception {
        assertNull(bossService.parseResponse(null));
        assertNull(bossService.parseResponse(""));
        assertNull(bossService.parseResponse("fdsajfldksajl"));

        BjymChargeResponse bcr = valid();
        assertNotNull(bossService.parseResponse(new Gson().toJson(bcr)));
    }

    /**
     * 从响应对象中解析boss侧流水号
     */
    @Test
    public void testGetBossRespSn() throws Exception {
        assertNull(bossService.getBossRespSn(null));
        assertNotNull(bossService.getBossRespSn(valid()));
    }

    /**
     * 测试fingerprint
     */
    @Test
    public void testGetFingerPrint() throws Exception {
        assertTrue("beijingyunman".equals(bossService.getFingerPrint()));
    }

    /**
     * 测试从boss侧同步账户余额
     */
    @Test
    public void testSyncFromBoss() throws Exception {
        assertTrue(bossService.syncFromBoss(1l, 1l));
        assertTrue(bossService.syncFromBoss(null, null));
        assertTrue(bossService.syncFromBoss(1l, null));
        assertTrue(bossService.syncFromBoss(null, 1l));
    }

    /**
     * 测试无效的充值
     */
    @Test
    public void testInvalidCharge() throws Exception {
        assertTrue(isInvalidParamResult(
                bossService.charge(
                        null, randLong(), randMobile(), randStr(), randLong())));

        assertTrue(isInvalidParamResult(
                bossService.charge(
                        randLong(), null, randMobile(), randStr(), randLong())));

        assertTrue(isInvalidParamResult(
                bossService.charge(
                        randLong(), randLong(), null, randStr(), randLong())));

        assertTrue(isInvalidParamResult(
                bossService.charge(
                        randLong(), randLong(), randMobile(), null, randLong())));

        assertTrue(isInvalidParamResult(
                bossService.charge(
                        randLong(), randLong(), randMobile(), "", randLong())));

        when(supplierProductService.selectByPrimaryKey(anyLong()))
                .thenReturn(null)
                .thenReturn(new SupplierProduct());

        assertTrue(isInvalidParamResult(
                bossService.charge(
                        randLong(), randLong(), randMobile(), randStr(), randLong())));

        assertTrue(isInvalidParamResult(
                bossService.charge(
                        randLong(), randLong(), randMobile(), randStr(), randLong())));


        verify(supplierProductService, times(2)).selectByPrimaryKey(anyLong());
    }

    /**
     * 测试更新流水号失败的充值
     */
    @Test
    public void testInvalidCharge2() throws Exception {
        when(supplierProductService.selectByPrimaryKey(anyLong()))
                .thenReturn(validSupplierProduct());

        when(serialNumService.updateSerial(any(SerialNum.class)))
                .thenReturn(false);


        assertTrue(isInvalidParamResult(
                bossService.charge(
                        randLong(), randLong(), randMobile(), randStr(), randLong())));

        verify(supplierProductService).selectByPrimaryKey(anyLong());
        verify(serialNumService).updateSerial(any(SerialNum.class));
    }

    /**
     * 测试构建请求参数失败的情况
     */
    @Test
    public void testInvalidCharge3() throws Exception {
        when(supplierProductService.selectByPrimaryKey(anyLong()))
                .thenReturn(validSupplierProduct());

        when(serialNumService.updateSerial(any(SerialNum.class)))
                .thenReturn(true);

        BjymBossServiceImpl spy = spy(bossService);
        doReturn(null).when(spy).buildRequestPojo(any(SupplierProduct.class), anyString(), anyString());

        assertTrue(isInvalidParamResult(
                spy.charge(
                        randLong(), randLong(), randMobile(), randStr(), randLong())));

        verify(supplierProductService).selectByPrimaryKey(anyLong());
        verify(serialNumService).updateSerial(any(SerialNum.class));
        verify(spy).buildRequestPojo(any(SupplierProduct.class), anyString(), anyString());
    }

    /**
     * 测试请求转换失败的情况
     */
    @Test
    public void testInvalidCharge4() throws Exception {
        when(supplierProductService.selectByPrimaryKey(anyLong()))
                .thenReturn(validSupplierProduct());

        when(serialNumService.updateSerial(any(SerialNum.class)))
                .thenReturn(true);

        BjymBossServiceImpl spy = spy(bossService);
        doReturn(new BjymChargeRequest())
                .when(spy)
                .buildRequestPojo(any(SupplierProduct.class), anyString(), anyString());

        doReturn(null).when(spy).getRequestPojoToChargeUrlConverter();

        assertTrue(isInvalidParamResult(
                spy.charge(
                        randLong(), randLong(), randMobile(), randStr(), randLong())));

        verify(supplierProductService).selectByPrimaryKey(anyLong());
        verify(serialNumService).updateSerial(any(SerialNum.class));
        verify(spy).buildRequestPojo(any(SupplierProduct.class), anyString(), anyString());
        verify(spy).getRequestPojoToChargeUrlConverter();
    }

    /**
     * 测试解析响应结果失败的情况
     */
    @Test
    public void testInvalidCharge5() throws Exception {
        when(supplierProductService.selectByPrimaryKey(anyLong()))
                .thenReturn(validSupplierProduct());

        when(serialNumService.updateSerial(any(SerialNum.class)))
                .thenReturn(true);

        when(globalConfigService.get(anyString()))
                .thenReturn("http://www.baidu.com/");

        BjymBossServiceImpl spy = spy(bossService);
        doReturn(new BjymChargeRequest())
                .when(spy)
                .buildRequestPojo(any(SupplierProduct.class), anyString(), anyString());

        doReturn(null).when(spy).parseResponse(anyString());

        assertTrue(isInvalidParamResult(
                spy.charge(
                        randLong(), randLong(), randMobile(), randStr(), randLong())));

        verify(supplierProductService).selectByPrimaryKey(anyLong());
        verify(serialNumService).updateSerial(any(SerialNum.class));
        verify(spy).buildRequestPojo(any(SupplierProduct.class), anyString(), anyString());
        verify(spy).getRequestPojoToChargeUrlConverter();
        verify(spy).parseResponse(anyString());
    }

    /**
     * 测试校验响应结果失败的情况
     */
    @Test
    public void testInvalidCharge6() throws Exception {
        when(supplierProductService.selectByPrimaryKey(anyLong()))
                .thenReturn(validSupplierProduct());

        when(serialNumService.updateSerial(any(SerialNum.class)))
                .thenReturn(true);

        when(globalConfigService.get(anyString()))
                .thenReturn("http://www.baidu.com/");

        BjymBossServiceImpl spy = spy(bossService);
        doReturn(new BjymChargeRequest())
                .when(spy)
                .buildRequestPojo(any(SupplierProduct.class), anyString(), anyString());

        doReturn(new BjymChargeResponse())
                .when(spy).parseResponse(anyString());

        doReturn(false)
                .when(spy).validateResponsePojo(any(BjymChargeResponse.class));

        assertTrue(isInvalidParamResult(
                spy.charge(
                        randLong(), randLong(), randMobile(), randStr(), randLong())));

        verify(supplierProductService).selectByPrimaryKey(anyLong());
        verify(serialNumService).updateSerial(any(SerialNum.class));
        verify(spy).buildRequestPojo(any(SupplierProduct.class), anyString(), anyString());
        verify(spy).getRequestPojoToChargeUrlConverter();
        verify(spy).parseResponse(anyString());
        verify(spy).validateResponsePojo(any(BjymChargeResponse.class));
    }

    /**
     * 测试获取boss响应流水号失败的场景
     */
    @Test
    public void testInvalidCharge7() throws Exception {
        when(supplierProductService.selectByPrimaryKey(anyLong()))
                .thenReturn(validSupplierProduct());

        when(serialNumService.updateSerial(any(SerialNum.class)))
                .thenReturn(true);

        when(globalConfigService.get(anyString()))
                .thenReturn("http://www.baidu.com/");

        BjymBossServiceImpl spy = spy(bossService);
        doReturn(new BjymChargeRequest())
                .when(spy)
                .buildRequestPojo(any(SupplierProduct.class), anyString(), anyString());

        doReturn(new BjymChargeResponse())
                .when(spy).parseResponse(anyString());

        doReturn(true)
                .when(spy).validateResponsePojo(any(BjymChargeResponse.class));

        doReturn(null)
                .when(spy).getBossRespSn(any(BjymChargeResponse.class));

        assertFalse(isInvalidParamResult(
                spy.charge(
                        randLong(), randLong(), randMobile(), randStr(), randLong())));

        verify(supplierProductService).selectByPrimaryKey(anyLong());
        verify(serialNumService).updateSerial(any(SerialNum.class));
        verify(spy).buildRequestPojo(any(SupplierProduct.class), anyString(), anyString());
        verify(spy).getRequestPojoToChargeUrlConverter();
        verify(spy).parseResponse(anyString());
        verify(spy).validateResponsePojo(any(BjymChargeResponse.class));
        verify(spy).getBossRespSn(any(BjymChargeResponse.class));
    }

    /**
     * 测试有效的充值场景
     */
    @Test
    public void testInvalidCharge8() throws Exception {
        when(supplierProductService.selectByPrimaryKey(anyLong()))
                .thenReturn(validSupplierProduct());

        when(serialNumService.updateSerial(any(SerialNum.class)))
                .thenReturn(true);

        when(globalConfigService.get(anyString()))
                .thenReturn("http://www.baidu.com/");

        BjymBossServiceImpl spy = spy(bossService);
        doReturn(new BjymChargeRequest())
                .when(spy)
                .buildRequestPojo(any(SupplierProduct.class), anyString(), anyString());

        doReturn(new BjymChargeResponse())
                .when(spy).parseResponse(anyString());

        doReturn(true)
                .when(spy).validateResponsePojo(any(BjymChargeResponse.class));

        doReturn(randStr())
                .when(spy).getBossRespSn(any(BjymChargeResponse.class));

        when(serialNumService.updateSerial(any(SerialNum.class))).thenReturn(true);

        assertFalse(isInvalidParamResult(
                spy.charge(
                        randLong(), randLong(), randMobile(), randStr(), randLong())));

        verify(supplierProductService).selectByPrimaryKey(anyLong());
        verify(serialNumService, times(2)).updateSerial(any(SerialNum.class));
        verify(spy).buildRequestPojo(any(SupplierProduct.class), anyString(), anyString());
        verify(spy).getRequestPojoToChargeUrlConverter();
        verify(spy).parseResponse(anyString());
        verify(spy).validateResponsePojo(any(BjymChargeResponse.class));
        verify(spy).getBossRespSn(any(BjymChargeResponse.class));
    }


    /**
     * 测试mdrc营销卡充值
     */
    @Test
    public void testMdrcCharge() throws Exception {
        assertNull(bossService.mdrcCharge(null, null));
        assertNull(bossService.mdrcCharge("", null));
        assertNull(bossService.mdrcCharge(null, ""));
        assertNull(bossService.mdrcCharge(randStr(), randStr()));
    }

    private BjymChargeResponse valid() {
        BjymChargeResponse bcr = new BjymChargeResponse();
        bcr.setCode(randStr());
        bcr.setDescription(randStr());
        bcr.setSendId(randStr());
        bcr.setRequestId(randStr());

        return bcr;
    }

    private BjymChargeResponse nullCode() {
        BjymChargeResponse bcr = valid();
        bcr.setCode(null);

        return bcr;
    }

    private BjymChargeResponse nullDesc() {
        BjymChargeResponse bcr = valid();
        bcr.setDescription(null);

        return bcr;
    }

    private BjymChargeResponse nullSendId() {
        BjymChargeResponse bcr = valid();
        bcr.setSendId(null);

        return bcr;
    }

    private BjymChargeResponse nullRequestId() {
        BjymChargeResponse bcr = valid();
        bcr.setRequestId(null);

        return bcr;
    }

    private SupplierProduct validSupplierProduct() {
        SupplierProduct supplierProduct = new SupplierProduct();

        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("userPackage", randStr());
        supplierProduct.setFeature(new Gson().toJson(map));

        return supplierProduct;
    }
}
