package com.cmcc.vrp.boss.shanghai;

import com.chinamobile.cn.openapi.sdk.v2.client.OpenapiHttpCilent;
import com.cmcc.vrp.boss.BaseBossServiceTest;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.shanghai.model.ShReturnCode;
import com.cmcc.vrp.boss.shanghai.model.common.AsiaDTO;
import com.cmcc.vrp.boss.shanghai.model.common.ErrorInfo;
import com.cmcc.vrp.boss.shanghai.model.paymember.PayMemberReq;
import com.cmcc.vrp.boss.shanghai.model.paymember.PmAsiaResp;
import com.cmcc.vrp.boss.shanghai.model.paymember.PmAsiaResult;
import com.cmcc.vrp.boss.shanghai.model.paymember.PmRetInfo;
import com.cmcc.vrp.boss.shanghai.model.paymember.PmReturnContent;
import com.cmcc.vrp.boss.shanghai.model.queryproduct.ProductItem;
import com.cmcc.vrp.boss.shanghai.service.impl.QueryAllGroupServiceImpl;
import com.cmcc.vrp.boss.shanghai.service.impl.QueryMemberRoleImpl;
import com.cmcc.vrp.boss.shanghai.service.impl.QueryProductServiceIml;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.model.SupplierProduct;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by lilin on 2016/10/12.
 */
@PrepareForTest({ShNationalBossServiceImpl.class, SerialNumGenerator.class, Gson.class})
public class ShBossServiceImplTest extends BaseBossServiceTest {

    @InjectMocks
    ShNationalBossServiceImpl shNationalBossService = new ShNationalBossServiceImpl();

    @Mock
    SupplierProductService productService;

    @Mock
    GlobalConfigService globalConfigService;

    @Mock
    QueryAllGroupServiceImpl QueryAllGroupService;

    @Mock
    QueryMemberRoleImpl QueryMemberRoleService;

    @Mock
    QueryProductServiceIml QueryProductService;

    @Mock
    SerialNumService serialNumService;

    @Before
    public void initMocks() {
        PowerMockito.when(productService.selectByPrimaryKey(anyLong())).thenReturn(this.buildSP());
        PowerMockito.when(QueryAllGroupService.queryAllGroupOrderInfo(anyString())).thenReturn(new AsiaDTO());
        PowerMockito.when(QueryMemberRoleService.queryMemberRoleByOfferId(any(AsiaDTO.class))).thenReturn(new AsiaDTO());
        PowerMockito.when(QueryProductService.queryProductByOfferIdAndRoleId(any(AsiaDTO.class))).thenReturn(buildProducts());
    }

    @Ignore
    @Test
    public void testCharge() throws Exception {
        String systemNum = SerialNumGenerator.buildSerialNum();
        when(serialNumService.getByPltSerialNum(systemNum)).thenReturn(new SerialNum());
        when(serialNumService.updateSerial(any(SerialNum.class))).thenReturn(true);

        PowerMockito.mockStatic(SerialNumGenerator.class);
        PowerMockito.when(SerialNumGenerator.buildNormalBossReqNum("shanghai", 25)).thenReturn("xxxxxx");

        shNationalBossService = PowerMockito.spy(shNationalBossService);
        PayMemberReq req = PowerMockito.mock(PayMemberReq.class);
        PowerMockito.doReturn(req).when(shNationalBossService, "buildCR", "18867101129", "xxxxxx", "xxxxxx", "123456");

        Gson gson = PowerMockito.mock(Gson.class);
        PowerMockito.when(gson.toJson(req)).thenReturn("xxxxxx");
        ReflectionTestUtils.setField(shNationalBossService, "gson", gson);

        OpenapiHttpCilent cilent = PowerMockito.mock(OpenapiHttpCilent.class);
        PowerMockito.whenNew(OpenapiHttpCilent.class).withArguments("A0007690", "p+nVO839uj5tC6cVeUAsZMtYOGdkPXV5").thenReturn(cilent);
        String msgResp = "{\"status\":\"SUCCESS\",\"result\":\"xxxxxx\"}";
        PowerMockito.when(cilent.call("CRM4186", "xxxxxx", "xxxxxx")).thenReturn(msgResp);

        PowerMockito.when(gson.fromJson("xxxxxx", PmAsiaResult.class)).thenReturn(buildPAR(ShReturnCode.SUCCESS.getCode()));
        BossOperationResult success = shNationalBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertTrue(success.isSuccess());

        PowerMockito.when(gson.fromJson("xxxxxx", PmAsiaResult.class)).thenReturn(buildPAR(ShReturnCode.FAILD.getCode()));
        BossOperationResult faild = shNationalBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertFalse(faild.isSuccess());

        PowerMockito.when(gson.fromJson("xxxxxx", PmAsiaResult.class)).thenReturn(buildPAR(ShReturnCode.RESP_ILLEGALITY.getCode()));
        BossOperationResult respIllegalty = shNationalBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertFalse(respIllegalty.isSuccess());

        PowerMockito.when(gson.fromJson("xxxxxx", PmAsiaResult.class)).thenReturn(buildPAR(ShReturnCode.PARA_ILLEGALITY.getCode()));
        BossOperationResult paraIllegelty = shNationalBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertFalse(paraIllegelty.isSuccess());

        verify(cilent, times(4)).call("CRM4186", "xxxxxx", "xxxxxx");

    }

    @Override
    public SupplierProduct buildSP() {
        SupplierProduct product = super.buildSP();
        product.setFeature("xxxxxx");
        return product;
    }

    private PmAsiaResult buildPAR(String returnCode) {
        PmAsiaResult result = new PmAsiaResult();
        PmAsiaResp resp = new PmAsiaResp();
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setCode("0000");
        resp.setErrorInfo(errorInfo);
        PmRetInfo retInfo = new PmRetInfo();
        retInfo.setReturnCode(returnCode);
        PmReturnContent returnContent = new PmReturnContent();
        returnContent.setSerialNum("xxxxxx");
        retInfo.setReturnContent(returnContent);
        resp.setRetInfo(retInfo);
        result.setResponse(resp);
        return result;
    }

    private List<ProductItem> buildProducts() {
        List<ProductItem> productItems = new ArrayList<ProductItem>();
        ProductItem item = new ProductItem();
        item.setProdId("");
        item.setProdRate("");
        item.setProductName("");
        productItems.add(item);
        return productItems;
    }
}
