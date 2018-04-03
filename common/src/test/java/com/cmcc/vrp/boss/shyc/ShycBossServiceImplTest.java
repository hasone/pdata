package com.cmcc.vrp.boss.shyc;

import com.google.gson.Gson;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.shyc.enums.ErrorCode;
import com.cmcc.vrp.boss.shyc.pojos.SingleChargeResponse;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.impl.BaseTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedHashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;

/**
 * 上海月呈渠道ut
 *
 * Created by sunyiwei on 2017/5/25.
 */
@RunWith(MockitoJUnitRunner.class)
public class ShycBossServiceImplTest extends BaseTest {
    @InjectMocks
    private ShycBossServiceImpl bossService = new ShycBossServiceImpl();

    @Mock
    private GlobalConfigService globalConfigService;

    /**
     * 测试获取渠道名称
     */
    @Test
    public void testGetName() throws Exception {
        assertTrue("上海月呈".equals(bossService.getName()));
    }

    /**
     * 测试无效的参数结果
     */
    @Test
    public void testGetInvalidParamResult() throws Exception {
        assertTrue(isInvalidResult(bossService.getInvalidParamResult()));
    }

    /**
     * 测试解析结果
     */
    @Test
    public void testParseResult() throws Exception {
        assertTrue(isInvalidResult(bossService.parseResult(null)));
        assertFalse(isInvalidResult(bossService.parseResult(new SingleChargeResponse())));
    }

    /**
     * 测试获取boss请求流水号
     */
    @Test
    public void testGetBossReqSerialNum() throws Exception {
        final String sn = randStr();
        assertEquals(sn, bossService.getBossReqSerialNum(sn));
    }


    /**
     * 测试校验响应对象的有效性
     */
    @Test
    public void testValidateResponsePojo() throws Exception {
        assertTrue(bossService.validateResponsePojo(new SingleChargeResponse()));
        assertTrue(bossService.validateResponsePojo(null));
    }

    /**
     * 校验请求对象转换器
     */
    @Test
    public void testGetRequestPojoToChargeUrlConverter() throws Exception {
        assertNotNull(bossService.getRequestPojoToChargeUrlConverter());
    }

    /**
     * 测试获取boss侧响应流水号
     */
    @Test
    public void testGetBossRespSn() throws Exception {
        assertNull(bossService.getBossRespSn(null));

        SingleChargeResponse scr = new SingleChargeResponse();
        scr.setTaskId(randStr());
        assertNotNull(bossService.getBossRespSn(scr));
    }

    /**
     * 测试解析响应对象
     * @throws Exception
     */
    @Test
    public void testParseResponse() throws Exception {
        assertNull(bossService.parseResponse(null));
        assertNotNull(bossService.parseResponse(buildResponseStr()));
    }

    /**
     * 测试构建请求对象
     * @throws Exception
     */
    @Test
    public void testBuildRequestPojo() throws Exception {
        assertNull(bossService.buildRequestPojo(null, randMobile(), randStr()));
        assertNotNull(bossService.buildRequestPojo(validSupplierProduct(), randMobile(), randStr()));
    }

    /**
     * 测试指纹
     * @throws Exception
     */
    @Test
    public void testGetFingerPrint() throws Exception {
        assertTrue("shanghaiyuecheng".equals(bossService.getFingerPrint()));
    }

    /**
     * 测试从boss侧同步
     * @throws Exception
     */
    @Test
    public void testSyncFromBoss() throws Exception {
        assertFalse(bossService.syncFromBoss(randLong(), randLong()));
    }

    private boolean isInvalidResult(BossOperationResult bor) {
        final ErrorCode errorCode = ErrorCode.INVALID_PARAM;
        return errorCode.getCode().equals(bor.getResultCode());
    }

    private String buildResponseStr(){
        SingleChargeResponse scr = new SingleChargeResponse();
        scr.setTaskId(randStr());

        return new Gson().toJson(scr);
    }

    private SupplierProduct validSupplierProduct(){
        SupplierProduct sp = new SupplierProduct();

        Map<String, Integer> feature = new LinkedHashMap<String, Integer>();
        feature.put("range", randInt());
        feature.put("packageSize", randInt());

        sp.setFeature(new Gson().toJson(feature));
        return sp;
    }
}