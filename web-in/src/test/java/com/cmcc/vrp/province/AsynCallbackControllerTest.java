package com.cmcc.vrp.province;

import com.cmcc.vrp.boss.core.model.CoreCallbackReq;
import com.cmcc.vrp.boss.core.model.FailInfo;
import com.cmcc.vrp.boss.core.model.SuccInfo;
import com.cmcc.vrp.ec.bean.CallBackReq;
import com.cmcc.vrp.ec.bean.CallBackReqData;
import com.cmcc.vrp.ec.bean.CallbackResp;
import com.cmcc.vrp.ec.utils.CallbackResult;
import com.cmcc.vrp.enums.ChargeRecordStatus;
import com.cmcc.vrp.province.model.AsyncCallbackReq;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.AsyncCallbackService;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.util.HttpUtils;
import com.thoughtworks.xstream.XStream;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by sunyiwei on 2016/9/20.
 */
@Ignore
public class AsynCallbackControllerTest {
    private static XStream xStream;

    static {
        xStream = new XStream();
        xStream.alias("Request", CallBackReq.class);
        xStream.alias("Response", CallbackResp.class);
        xStream.autodetectAnnotations(true);
    }

    @InjectMocks
    AsynCallbackController asynCallbackController;
    @Mock
    ChargeRecordService chargeRecordService;
    @Mock
    AsyncCallbackService asyncCallbackService;
    @Mock
    SerialNumService serialNumService;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(asynCallbackController).build();
    }

    /**
     * 测试山东回调接口， 失败的情况
     */
    @Test
    @Ignore
    public void testSdCallbackFailed() throws Exception {
        final String URL = "http://localhost:8080/web-in/charge/callback.html";
        String serialNum = "fdsfdfsdfd";
        process(URL, serialNum, CallbackResult.OTHERS);
    }

    /**
     * 测试山东回调接口，回调成功，充值状态为成功
     */
    @Test
    @Ignore
    public void testSdCallbackSuccess() throws Exception {
        final String URL = "http://localhost:8080/web-in/charge/callback.html";
        String serialNum = "c2a62f04af1a8ddbc3599e1050fff16471eac10f";
        process(URL, serialNum, CallbackResult.SUCCESS);
    }

    /**
     * 测试山东回调接口，回调成功，充值状态为失败
     */
    @Test
    @Ignore
    public void testSdCallbackSuccess2() throws Exception {
        final String URL = "http://localhost:8080/web-in/charge/callback.html";
        String serialNum = "c2a62f04af1a8ddbc3599e1050fff16471eac10f";
        process(URL, serialNum, ChargeRecordStatus.FAILED, CallbackResult.SUCCESS);
    }

    /**
     * 测试core回调, 充值失败的情况, 但BOSS侧返回的序列号不存在
     */
    @Test
    @Ignore
    public void testCoreCallbackFail() throws Exception {
        final String URL = "http://localhost:8080/web-in/charge/core/callback.html";
        String bossRspSerialNum = "dfdasf";
        String reqStr = buildReq("18867102100", bossRspSerialNum, false);
        HttpUtils.post(URL, reqStr, "application/xml");
    }

    /**
     * 测试core回调, 充值失败的情况, BOSS侧返回的序列号存在
     */
    @Test
    @Ignore
    public void testCoreCallbackSuccess() throws Exception {
        final String URL = "http://localhost:8080/web-in/charge/core/callback.html";
        String bossRspSerialNum = "asfdsafasfasdfasdasdf";
        String reqStr = buildReq("18867102100", bossRspSerialNum, false);
        HttpUtils.post(URL, reqStr, "application/xml");
    }

    /**
     * 使用mockito框架测试controller方法
     */
    @Test
    public void testCoreCallbackSuccess3() throws Exception {
        String bossRspSerialNum = "asfdsafasfasdfasdasdf";
        String reqStr = buildReq("18867102100", bossRspSerialNum, false);
        SerialNum serialNum = new SerialNum();
        serialNum.setPlatformSerialNum("fdasdfsa");

        when(asyncCallbackService.process(any(AsyncCallbackReq.class)))
                .thenReturn(false).thenReturn(true);
        when(serialNumService.getByBossRespSerialNum(anyString()))
                .thenReturn(null).thenReturn(serialNum);

        mockMvc.perform((post("/charge/core/callback.html"))
                .content(reqStr).contentType("application/xml").characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform((post("/charge/core/callback.html"))
                .content(reqStr).contentType("application/xml").characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(asyncCallbackService, times(1)).process(any(AsyncCallbackReq.class));
        verify(serialNumService, times(2)).getByBossRespSerialNum(anyString());
    }

    /**
     * 测试core回调, 充值成功的情况, BOSS侧返回的序列号存在
     */
    @Test
    @Ignore
    public void testCoreCallbackSuccess2() throws Exception {
        final String URL = "http://localhost:8080/web-in/charge/core/callback.html";
        String bossRspSerialNum = "asfdsafasfasdfasdasdf";
        String reqStr = buildReq("18867102100", bossRspSerialNum, true);
        HttpUtils.post(URL, reqStr, "application/xml");
    }


    private void process(String url, String serialNum, CallbackResult callbackResult) {
        process(url, serialNum, ChargeRecordStatus.COMPLETE, callbackResult);
    }

    private void process(String url, String serialNum, ChargeRecordStatus chargeRecordStatus,
                         CallbackResult callbackResult) {
        String reqStr = buildReq(serialNum, chargeRecordStatus, "回调接口测试");
        String resp = HttpUtils.post(url, reqStr, "application/xml");
        CallbackResp response = parseResp(resp);
        assertEquals(response.getCode(), callbackResult.getCode());
    }

    private String buildReq(String mobile, String bossSerialNum, boolean isSuccess) {
        CoreCallbackReq callbackReq = new CoreCallbackReq();

        callbackReq.setOperSeq(bossSerialNum);
        if (isSuccess) {
            List<SuccInfo> succInfos = new LinkedList<SuccInfo>();
            SuccInfo succInfo = new SuccInfo();
            succInfo.setMobNum(mobile);

            succInfos.add(succInfo);
            callbackReq.setSuccInfo(succInfos);
        } else {
            List<FailInfo> failInfos = new LinkedList<FailInfo>();
            FailInfo failInfo = new FailInfo();
            failInfo.setMobNum(mobile);
            failInfo.setFailDesc("fdkafjldasjfklasdj");

            failInfos.add(failInfo);
            callbackReq.setFailInfo(failInfos);
        }

        XStream xStream = new XStream();
        xStream.alias("Request", CoreCallbackReq.class);
        xStream.autodetectAnnotations(true);

        return xStream.toXML(callbackReq);
    }

    private CallbackResp parseResp(String str) {
        return (CallbackResp) xStream.fromXML(str);
    }

    private String buildReq(String ecSerialNum, ChargeRecordStatus status, String errorMsg) {
        String dateTime = new DateTime().toString();

        CallBackReqData cbrd = new CallBackReqData();
        cbrd.setChargeTime(new DateTime().toString());
        cbrd.setDescription(errorMsg);
        cbrd.setStatus(status.getCode());
        cbrd.setSystemNum("sddfdsafasdf");
        cbrd.setEcSerialNum(ecSerialNum);
        cbrd.setMobile("18867102100");

        CallBackReq cbr = new CallBackReq();
        cbr.setDateTime(dateTime);
        cbr.setCallBackReqData(cbrd);

        XStream xStream = new XStream();
        xStream.alias("Request", CallBackReq.class);
        xStream.autodetectAnnotations(true);

        return xStream.toXML(cbr);
    }
}