package com.cmcc.vrp.boss.sichuan;

import com.cmcc.vrp.boss.BaseBossServiceTest;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.sichuan.model.SCBalanceRequest;
import com.cmcc.vrp.boss.sichuan.model.SCBalanceResponse;
import com.cmcc.vrp.boss.sichuan.model.SCBalanceResponseOutData;
import com.cmcc.vrp.boss.sichuan.model.SCChargeRequest;
import com.cmcc.vrp.boss.sichuan.model.SCChargeResponse;
import com.cmcc.vrp.boss.sichuan.service.SCAddMemberService;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.Enterprise;
import com.cmcc.vrp.province.service.EntProductService;
import com.cmcc.vrp.province.service.EnterpriseUserIdService;
import com.cmcc.vrp.province.service.EnterprisesService;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SupplierProductService;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by lilin on 2016/10/13.
 */
@PrepareForTest(SCBossServiceImpl.class)
public class ScBossServicImplTest extends BaseBossServiceTest {

    @InjectMocks
    SCBossServiceImpl scBossService = new SCBossServiceImpl();

    @Mock
    SCAddMemberService scAddMemberService;

    @Mock
    EnterpriseUserIdService enterpriseUserIdService;

    @Mock
    EnterprisesService enterprisesService;

    @Mock
    SupplierProductService supplierProductService;

    @Mock
    com.cmcc.vrp.boss.sichuan.service.SCBalanceService SCBalanceService;

    @Mock
    GlobalConfigService globalConfigService;

    @Mock
    EntProductService entProductService;

    @Before
    public void initMocks() throws Exception {
        when(globalConfigService.get("SC_BOSSBALANCE_CHECK")).thenReturn("OK");
        when(supplierProductService.selectByPrimaryKey(anyLong())).thenReturn(buildSP());
    }

    @Ignore
    @Test
    public void testCharge() throws Exception {
        String systemNum = SerialNumGenerator.buildSerialNum();
        SCChargeRequest request = mock(SCChargeRequest.class);
        PowerMockito.whenNew(SCChargeRequest.class).withNoArguments().thenReturn(request);
        when(scAddMemberService.sendChargeRequest(request)).thenReturn(buildScCR("0000000"));

        SCBalanceRequest balanceRequest = PowerMockito.mock(SCBalanceRequest.class);
        PowerMockito.whenNew(SCBalanceRequest.class).withNoArguments().thenReturn(balanceRequest);
        List entCodes = getEntCodes();
        for (int j = 0; j < entCodes.size(); j++) {
            String entCode = (String) entCodes.get(j);
            // 检查企业账户
            Enterprise enterprise = buildEnt(entCode);
            when(enterpriseUserIdService.getUserIdByEnterpriseCode(entCode)).thenReturn("xxxxxx");
            when(enterprisesService.selectByPrimaryKey(1l)).thenReturn(enterprise);
            when(enterprisesService.updateByPrimaryKeySelective(enterprise)).thenReturn(true);
            List list = getPrepayFees();
            for (int i = 0; i < list.size(); i++) {
                String preFee = (String) list.get(i);
                Integer fee = Integer.parseInt(preFee);
                when(SCBalanceService.sendBalanceRequest(balanceRequest)).thenReturn(buildSCbalanceResp(preFee));
                // 企业欠费,断言充值失败
                if (fee <= 0) {
                    if ("2807797504".equals(entCode)) {
                        BossOperationResult result = scBossService.charge(1l, 1l, "18867101129", systemNum, null);
                        Assert.assertTrue(result.isSuccess());
                    } else {
                        BossOperationResult result = scBossService.charge(1l, 1l, "18867101129", systemNum, null);
                        Assert.assertFalse(result.isSuccess());
                    }
                } else {  // 企业不欠费，断言充值成功
                    if ("2807797504".equals(entCode)) {
                        BossOperationResult result = scBossService.charge(1l, 1l, "18867101129", systemNum, null);
                        Assert.assertTrue(result.isSuccess());
                    } else {
                        BossOperationResult result = scBossService.charge(1l, 1l, "18867101129", systemNum, null);
                        Assert.assertTrue(result.isSuccess());
                    }
                }
            }
        }
        verify(scAddMemberService, times(4)).sendChargeRequest(request);
    }


    public Enterprise buildEnt(String entCode) {
        Enterprise enterprise = super.buildEnt();
        enterprise.setCode(entCode);
        return enterprise;
    }

    private SCBalanceResponse buildSCbalanceResp(String prepayFee) {
        SCBalanceResponse balanceResponse = new SCBalanceResponse();
        balanceResponse.setResCode("0000000");
        SCBalanceResponseOutData outData = new SCBalanceResponseOutData();
        outData.setPREPAY_FEE(prepayFee);
        balanceResponse.setOutData(outData);
        return balanceResponse;
    }

    private SCChargeResponse buildScCR(String resCode) {
        SCChargeResponse response = new SCChargeResponse();
        response.setResCode(resCode);
        return response;
    }

    private List getPrepayFees() {
        List list = new ArrayList();
        list.add("0");
        list.add("10");
        list.add("-10");
        return list;
    }

    private List getEntCodes() {
        List list = new ArrayList();
        list.add("xxxxxx");
        list.add("2807797504");
        return list;
    }
}
