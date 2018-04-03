package com.cmcc.vrp.boss.heilongjiang;

import com.cmcc.vrp.boss.BaseBossServiceTest;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.heilongjiang.model.HLJSupplementModel;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyLong;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by lilin on 2016/10/13.
 */
@PrepareForTest({HLJBossServiceImpl.class, GsonBuilder.class, Gson.class})
public class HljBossServiceImplTest extends BaseBossServiceTest {

    @InjectMocks
    HLJBossServiceImpl hljBossService = new HLJBossServiceImpl();

    @Mock
    SupplierProductService supplierProductService;

    @Mock
    EnterprisesService enterprisesService;

    @Mock
    GlobalConfigService globalConfigService;

    @Before
    public void initMocks() {
        when(enterprisesService.selectByPrimaryKey(anyLong())).thenReturn(this.buildEnt());
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_WSDL_ADDRESS.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_CHARGE_ADDRESS.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_CHANNEL_SOURCE.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_OP_CODE.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_LOGIN_NO.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_LOGIN_PWD.getKey())).thenReturn("xxxxxx");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_PHONE_NO.getKey())).thenReturn("xxxxxx");
    }

    @Ignore
    @Test
    public void testCharge() throws Exception {
        String systemNum = SerialNumGenerator.buildSerialNum();
        String featrue = buildFeatrue();
        when(supplierProductService.selectByPrimaryKey(anyLong())).thenReturn(buildSP(featrue));

        GsonBuilder builder = PowerMockito.mock(GsonBuilder.class);
        PowerMockito.whenNew(GsonBuilder.class).withNoArguments().thenReturn(builder);
        Gson gson = PowerMockito.mock(Gson.class);
        PowerMockito.when(builder.create()).thenReturn(gson);
        HLJSupplementModel model = PowerMockito.mock(HLJSupplementModel.class);
        PowerMockito.when(gson.fromJson(featrue, HLJSupplementModel.class)).thenReturn(model);

        hljBossService = PowerMockito.spy(hljBossService);
        PowerMockito.doReturn(buildRespList()).when(hljBossService, "charge", "xxxxxx", "123456", "18867101129", model);

        BossOperationResult result = hljBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertTrue(result.isSuccess());
    }

    @Override
    public Enterprise buildEnt() {
        Enterprise enterprise = super.buildEnt();
        enterprise.setCode("xxxxxx");
        return enterprise;
    }

    private List buildRespList() {
        List list = new ArrayList();
        list.add("");
        list.add("000000");
        list.add("充值成功");
        return list;
    }

    public SupplierProduct buildSP(String featrue) {
        SupplierProduct supplierProduct = super.buildSP();
        supplierProduct.setFeature(featrue);
        return supplierProduct;
    }

    private String buildFeatrue() {
        HLJSupplementModel model = new HLJSupplementModel();
        model.setiEffLength("xxxxxx");
        model.setiEffType("xxxxxx");
        model.setiOfferId("xxxxxx");
        return new Gson().toJson(model);
    }
}
