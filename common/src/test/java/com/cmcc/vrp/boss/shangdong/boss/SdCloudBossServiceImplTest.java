
package com.cmcc.vrp.boss.shangdong.boss;

import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.shangdong.boss.model.ProductMemberFlowSystemPay;
import com.cmcc.vrp.boss.shangdong.boss.service.SdCloudBossServiceImpl;
import com.cmcc.vrp.boss.shangdong.boss.service.SdCloudOperationResultImpl;
import com.cmcc.vrp.boss.shangdong.boss.service.SdCloudWebserviceImpl;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.when;

/**
 * @author qihang
 */
@RunWith(MockitoJUnitRunner.class)
public class SdCloudBossServiceImplTest {

    @Autowired
    private SupplierProductService productService;

    @Mock
    SupplierProductService sProductService;

    @Mock
    SdCloudWebserviceImpl sdCloudWebservice;

    @InjectMocks
    SdCloudBossServiceImpl bossService = new SdCloudBossServiceImpl();

    @Mock
    GlobalConfigService globalConfigService;


    /**
     * init
     */
    @Before
    public void init() {
        //mock数据库中的值
        String defaultValue = "1";

        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.SD_BOSS_FEETYPE.getKey())).
                thenReturn(defaultValue);

        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.SD_BOSS_FACTFEE.getKey())).
                thenReturn(defaultValue);

        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.SD_BOSS_LIMITFLOW.getKey())).
                thenReturn(defaultValue);

        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.SD_BOSS_ITEMID.getKey())).
                thenReturn(defaultValue);

        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.SD_BOSS_STATUS.getKey())).
                thenReturn(defaultValue);
    }


    /**
     * testCharge
     */
    @Test
    @Ignore
    public void testCharge() {

        //正确的json数据
        String jsonCorrect = "{\"opr\":\"order\",\"type\":\"NEW\",\"seq\":\"10000000000\","
                + "\"productID\":\"1092\",\"customerType\":\"01\",\"customerID\":\"BOSSID888891199222\","
                + "\"oprCode\":\"01\",\"customerName\":\"接口测试专用公司\",\"enterpriseId\":\"2614\",\"discount\":\"7\",\"userID\":\"100099990\",\"oldBizId\":\"\","
                + "\"oldSpId\":\"\",\"bizId\":\"109202\",\"spId\":\"472528\",\"chargingType\":\"01\"}";

        //无法解析的json数据
        String jsonNotCompile = "{";

        //中间缺少内容的json
        String jsonError = "{\"opr\":\"order\",\"type\":\"NEW\",\"seq\":\"10000000000\","
                + "\"customerType\":\"01\",\"customerID\":\"BOSSID888891199222\","
                + "\"oprCode\":\"01\",\"customerName\":\"接口测试专用公司\",\"enterpriseId\":\"2614\",\"discount\":\"7\",\"userID\":\"100099990\",\"oldBizId\":\"\","
                + "\"oldSpId\":\"\",\"bizId\":\"109202\",\"spId\":\"472528\",\"chargingType\":\"01\"}";

        //0.测试参数错误
        Assert.assertFalse(bossService.charge(null, null, "18867102087", "11111", null).isSuccess());


        //1. 测试正确充值
        SupplierProduct sp = initSupplierProduct();
        sp.setFeature(jsonCorrect);

        BossOperationResult result = SdCloudOperationResultImpl.
                initHttpErrResult(false);

        when(sProductService.selectByPrimaryKey(sp.getId())).thenReturn(sp);
        when(sdCloudWebservice.updateProductMemberFlowSystemPay(Matchers.
                any(ProductMemberFlowSystemPay.class),Matchers.anyBoolean())).thenReturn(result);

        Assert.assertFalse(bossService.charge(1L, sp.getId(), "18867102087", "11111", null).isSuccess());


        //2. 测试json错误情况
        sp = initSupplierProduct();
        sp.setFeature(jsonNotCompile);

        when(sProductService.selectByPrimaryKey(sp.getId())).thenReturn(sp);
        Assert.assertFalse(bossService.charge(1L, sp.getId(), "18867102087", "11111", null).isSuccess());

        //3.测试json缺少字段的情况
        sp = initSupplierProduct();
        sp.setFeature(jsonError);

        when(sProductService.selectByPrimaryKey(sp.getId())).thenReturn(sp);
        Assert.assertFalse(bossService.charge(1L, sp.getId(), "18867102087", "11111", null).isSuccess());

    }

    /**
     * testGetFingerPrint
     */
    @Test
    public void testGetFingerPrint() {
        Assert.assertEquals(bossService.getFingerPrint(), "shandongcloud");
    }

    /**
     * testMdrcCharge
     */
    @Test
    public void testMdrcCharge() {
        Assert.assertNull(bossService.mdrcCharge("123", "18867102087"));
    }

    /**
     * testSyncFromBoss
     */
    @Test
    public void testSyncFromBoss() {
        Assert.assertTrue(bossService.syncFromBoss(1L, 1L));
    }


    /**
     * initSupplierProduct
     *
     * @return
     */
    private SupplierProduct initSupplierProduct() {
        SupplierProduct supplierProduct = new SupplierProduct();
        supplierProduct.setId(1L);
        supplierProduct.setCode("111");
        supplierProduct.setSize(1024L);
        return supplierProduct;

    }

}
