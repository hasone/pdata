package com.cmcc.vrp.boss.heilongjiang.fee;

import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import static org.mockito.Mockito.when;

public class TestHLJJersey {

    private Logger LOG = LoggerFactory.getLogger(TestHLJ.class);
    private BossServerMockJersey bossServer;

    private HLJFeeService hljFeeService = new HLJFeeServiceImpl();
    private String privateKey = Thread.currentThread().getContextClassLoader().getResource("hlj_private_key_pkcs8.pem").getPath();
    private String host = "http://localhost:9191/hlj"; // UT local jetty server.
    // private String host = "HTTP://218.203.14.9:32000"; // Given by Mili. they use this.
    // private String host = "HTTP://10.111.67.208:22000"; // Mili test environment, use this in LINUX environment.

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private GlobalConfigService globalConfigService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        hljFeeService.setGlobalConfigService(globalConfigService);
        bossServer = new BossServerMockJersey();
        bossServer.start("com.cmcc.vrp.boss.heilongjiang.fee");

        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_FEE_PRIVATE_KEY.getKey())).thenReturn(privateKey);
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_FEE_BALANCE_QUERY.getKey())).thenReturn(host + "/rest/1.0/sPhoneDefMsgVW_NWS_ml");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_FEE_ELECTRONIC_COUPON.getKey())).thenReturn(host + "/rest/1.0/bs_zgamCfm_ml");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_FEE_GROUP_PERSON.getKey())).thenReturn(host + "/rest/1.0/s_jtzz_ml");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_FEE_PREPAY_QUERY.getKey())).thenReturn(host + "/rest/1.0/bs_jtlimit_ml");
    }

    @After
    public void tearDown() throws Exception {
        LOG.info("stop server");
        if (null != bossServer) {
            bossServer.stop();
        }
    }

    @Ignore
    @Test
    public void testBalanceQuery() throws Exception {
        BalanceQueryRequest request = new BalanceQueryRequest();
        request.setAppKey("10001007");
        request.setPhoneNo("13112345667");
        request.setTimestamp(new Date());
        hljFeeService.balanceQuery(request);
    }

    @Test
    @Ignore
    public void testGroupPerson() throws Exception {
        GroupPersonRequest request = new GroupPersonRequest();
        request.setAccNbr("18867101809");
        request.setAppKey("10001007");
        request.setGroupAccNo("groupAccNo");
        request.setGroupNo("groupNo");
        request.setOrderDate("orderDate");
        request.setOrderNo("orderNo");
        request.setTimestamp(new Date());
        request.setTotalFee("totalFee");
        hljFeeService.groupPerson(request);
    }

    /**
     * Test private method {@link getURI}
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    @Ignore
    public void testGetUriNullParamter() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        HLJFeeServiceImpl impl = (HLJFeeServiceImpl) hljFeeService;
        Class<?>[] cArg = new Class[2];
        cArg[0] = String.class;
        cArg[1] = Map.class;
        Method method = impl.getClass().getDeclaredMethod("getURI", cArg);
        method.setAccessible(true);
        method.invoke(impl, null, null);
    }

    @Test
    @Ignore
    public void testEcoupon() throws Exception {
        ECouponRequest request = new ECouponRequest();
        request.setAccNbr("accNbr");
        request.setAppKey("10001007");
        request.setBossOrderNo("bossOrderNo");
        request.setGroupAccNo("groupAccNo");
        request.setGroupNo("groupNo");
        request.setOrderDate("orderDate");
        request.setOrderNo("orderNo");
        request.setOrderTime("orderTime");
        request.setPhoneNo("phoneNo");
        request.setTimestamp(new Date());
        hljFeeService.eCoupon(request);
    }

    @Ignore
    @Test
    public void testPrepay() throws Exception {
        PrepayQueryRequest request = new PrepayQueryRequest();
        request.setAppKey("10001007");
        request.setGroupAccNo("groupAccNo");
        request.setTimestamp(new Date());
        hljFeeService.prepayQuery(request);
    }

    @Test
    @Ignore
    public void testNullRequest() {
        exception.expect(RuntimeException.class);
        hljFeeService.balanceQuery(null);
    }

    @Test
    @Ignore
    public void testNullRequest2() {
        exception.expect(RuntimeException.class);
        hljFeeService.groupPerson(null);
    }

    @Test
    @Ignore
    public void testNullRequest3() {
        exception.expect(RuntimeException.class);
        hljFeeService.eCoupon(null);
    }

    @Test
    @Ignore
    public void testNullRequest4() {
        exception.expect(RuntimeException.class);
        hljFeeService.prepayQuery(null);
    }

    @Test
    @Ignore
    public void testRequestHasNullField() {
        exception.expect(RuntimeException.class);
        ECouponRequest eCouponRequest = new ECouponRequest();
        hljFeeService.eCoupon(eCouponRequest);
    }

}
