package com.cmcc.vrp.boss.hebei;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cmcc.vrp.boss.BaseBossServiceTest;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.BossQueryResult;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * Created by leelyn on 2016/10/13.
 */
public class HbBossServiceImplTest extends BaseBossServiceTest {

    @InjectMocks
    HbBossServiceImpl hbBossService = new HbBossServiceImpl();

    @InjectMocks
    HbQueryServiceImpl queryService = new HbQueryServiceImpl();

    @Mock
    SupplierProductService productService;

    @Mock
    SerialNumService serialNumService;

    @Mock
    GlobalConfigService globalConfigService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(productService.selectByPrimaryKey(anyLong())).thenReturn(this.buildSP());
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HEBEI_GROUPNO.getKey())).thenReturn("311A264042");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HEBEI_GRPSUBSID.getKey())).thenReturn("3112139563477");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HEBEI_URL.getKey())).thenReturn("http://hebei-interface/interface/HomeReceiver_portal");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HEBEI_QUERY_URL.getKey())).thenReturn("http://hebei-interface/interface/services/WSInterface?wsdl");
    }

    @Ignore
    @Test
    public void testCharge() {
        String systemNum = SerialNumGenerator.buildSerialNum();

        when(serialNumService.getByPltSerialNum(systemNum)).thenReturn(new SerialNum());
        when(serialNumService.updateSerial(any(SerialNum.class))).thenReturn(true);
//        BossOperationResult result = hbBossService.charge(1l, 1l, "13582323606", systemNum);
//        Assert.assertTrue(result.isSuccess());
        BossOperationResult result = hbBossService.charge(1l, 1l, "", systemNum, null);
        Assert.assertFalse(result.isSuccess());
    }

    @Ignore
    @Test
    public void testQuery() {
        when(serialNumService.getByPltSerialNum(anyString())).thenReturn(buildSN());
        queryService.init();
        BossQueryResult result = queryService.queryStatus("xxxxxx");
        Assert.assertEquals(BossQueryResult.SUCCESS, result);
    }

    @Override
    public SupplierProduct buildSP() {
        SupplierProduct product = super.buildSP();
        product.setCode("10MSN");
        return product;
    }

    private SerialNum buildSN() {
        SerialNum serialNum = new SerialNum();
        serialNum.setPlatformSerialNum("xxxxxx");
        serialNum.setBossRespSerialNum("88378571038841");
        return serialNum;
    }
}
