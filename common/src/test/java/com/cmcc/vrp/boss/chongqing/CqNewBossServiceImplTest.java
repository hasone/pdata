package com.cmcc.vrp.boss.chongqing;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

/**
 * @author lgk8023
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CqNewBossServiceImplTest {

    @InjectMocks
    CqNewBossServiceImpl cqNewBossServiceImpl = new CqNewBossServiceImpl();

    @Mock
    SupplierProductService supplierProductService;
    @Mock
    EnterprisesService enterprisesService;
    @Mock
    GlobalConfigService globalConfigService;
    
    /**
     * 
     */
    @Before
    public void initMocks() {
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CQ_NEW_BOSS_URL.getKey())).thenReturn("https://183.230.30.244:7102/openapi/httpService/OrderService");
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CQ_NEW_BOSS_CLIENT_ID.getKey())).thenReturn("20170726000098025");
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CQ_NEW_BOSS_CLIENT_SECRET.getKey())).thenReturn("5ce4bb6ba1c8f3c1d3b304656cacbcb8");
        Mockito.when(globalConfigService.get(GlobalConfigKeyEnum.CQ_NEW_BOSS_TOKEN_URL.getKey())).thenReturn("https://183.230.30.244:7102/OAuth/restOauth2Server/access_token");
    }
    
    /**
     * 
     */
    @Test
    @Ignore
    public void testCharge() {
        SupplierProduct supplierproduct = new SupplierProduct();
        supplierproduct.setCode("gl_kdsh_xsll2");
        Mockito.when(supplierProductService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(supplierproduct);
        Mockito.when(enterprisesService.selectByPrimaryKey(Mockito.anyLong())).thenReturn(new Enterprise());
        System.out.print(cqNewBossServiceImpl.charge(1l, 1l, "13618273172", "1234", null).getResultDesc());
    }
}
