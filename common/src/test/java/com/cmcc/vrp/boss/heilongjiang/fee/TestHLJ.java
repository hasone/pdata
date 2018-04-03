package com.cmcc.vrp.boss.heilongjiang.fee;

import com.cmcc.vrp.boss.sichuan.model.Sign;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoAnnotations.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import static org.mockito.Mockito.when;

public class TestHLJ {

    private Logger LOG = LoggerFactory.getLogger(TestHLJ.class);
    private BossServerMock bossServer;

    private HLJFeeService hljFeeService = new HLJFeeServiceImpl();
    private String privateKey = Thread.currentThread().getContextClassLoader().getResource("hlj_private_key_pkcs8.pem").getPath();
    private String host = "http://localhost:9191";

    @Mock
    private GlobalConfigService globalConfigService;

    // private GroupPersonRequest groupPersonRequest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        hljFeeService.setGlobalConfigService(globalConfigService);
        bossServer = new BossServerMock();

        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_FEE_PRIVATE_KEY.getKey())).thenReturn(privateKey);
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_FEE_PREPAY_QUERY.getKey())).thenReturn(host + "/rest/1.0/bs_jtlimit");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_FEE_GROUP_PERSON.getKey())).thenReturn(host + "/rest/1.0/s_jtzz");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_FEE_BALANCE_QUERY.getKey())).thenReturn(host + "/rest/1.0/sPhoneDefMsgVW_NWS");
        when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_HLJ_FEE_ELECTRONIC_COUPON.getKey())).thenReturn(host + "/rest/1.0/bs_zgamCfm");
    }

    @After
    public void tearDown() throws Exception {
        LOG.info("stop server");
        if (null != bossServer) {
            bossServer.stop();
        }
    }

    // when(mockedList.get( 1 )).thenThrow( new RuntimeException());
    @Ignore
    @Test
    public void testBalanceQuery() throws Exception {
        bossServer.start();
        BalanceQueryRequest request = new BalanceQueryRequest();
        request.setAppKey("appKey");
        request.setPhoneNo("13112345667");
        request.setTimestamp(new Date());
        hljFeeService.balanceQuery(request);
    }

    @Ignore
    @Test
    public void testGroupPerson() throws Exception {
        Map<String, AbstractHandler> map = new HashMap<String, AbstractHandler>();
        map.put("/rest/1.0/s_jtzz", new HLJBalanceQueryServerHandler());
        bossServer.start(map);
        // hljFeeService.groupPerson(groupPersonRequest);
    }

    public SignRequestParam getStringToSign(Map<String, String[]> mapOrig) {
        SignRequestParam signParam = new SignRequestParam();
        String paramToSign = null;
        Map<String, String[]> mapSorted = new TreeMap<String, String[]>();
        mapSorted.putAll(mapOrig);
        URIBuilder builder = new URIBuilder();
        String param = null;
        String paramSign = null;
        for (Iterator<Entry<String, String[]>> iter = mapSorted.entrySet().iterator(); iter.hasNext();) {
            Entry<String, String[]> entry = (Entry<String, String[]>) iter.next();
            if (entry.getKey().toString().equals("sign")) {
                paramSign = entry.getValue()[0].toString();
                signParam.setSign(paramSign);
                continue;
            }
            builder.setParameter(entry.getKey().toString(), entry.getValue()[0].toString());
        }
        try {
            param = builder.build().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        paramToSign = StringUtils.isNotBlank(param) && param.charAt(0) == '?' ? param.substring(1) : param;
        signParam.setStringToSign(paramToSign);
        return signParam;
    }

    private class SignRequestParam {
        private String stringToSign;
        private String sign;

        public String getStringToSign() {
            return stringToSign;
        }

        public void setStringToSign(String stringToSign) {
            this.stringToSign = stringToSign;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }

    private class HLJBalanceQueryServerHandler extends AbstractHandler {

        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            // baseRequest.setHandled(true);
            Map<String, String[]> mapOrig = request.getParameterMap();
            SignRequestParam signParam = getStringToSign(mapOrig);
            if (signParam.getStringToSign() != null) {
                try {
                    String sign = Sign.sign(signParam.getStringToSign(), privateKey);
                    if (sign.equals(signParam.getSign())) {
                        PrepayQueryResponse bqr = new PrepayQueryResponse();
                        bqr.setDeposit("deposit");
                        bqr.setResCode("00000000");
                        bqr.setResMsg("success or what");
                        String json = JsonUtil.getJson(bqr);
                        response.getWriter().write(json);
                        response.setStatus(HttpServletResponse.SC_OK);
                    } else {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }

        }

    }

}
