package com.cmcc.vrp.sms.opt;

import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.queue.pojo.SmsPojo;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Random;

import static junit.framework.TestCase.assertFalse;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 测试开放平台短信通道的服务是否正常
 *
 * Created by sunyiwei on 2017/2/13.
 */
@RunWith(MockitoJUnitRunner.class)
public class OptSendMessageServiceImplTest {
    @InjectMocks
    private OptSendMessageServiceImpl optSendMessageService = new OptSendMessageServiceImpl();

    @Mock
    private GlobalConfigService globalConfigService;

    private static String randStr(int length) {
        StringBuilder sb = new StringBuilder();

        Random r = new Random();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + r.nextInt(26)));
        }

        return sb.toString();
    }

    /**
     * 测试开放平台短信通道
     */
    @Test
    public void testSend() throws Exception {
        //测试无效的情况
        assertFalse(optSendMessageService.send(invalidMobile()));
        assertFalse(optSendMessageService.send(emptyContent()));
        assertFalse(optSendMessageService.send(emptyMobile()));

        //测试正常的情况, 以下为测试代码
        SmsPojo smsPojo = buildSmsPojo();
        when(globalConfigService.get(GlobalConfigKeyEnum.OPT_MSG_APP_KEY.getKey()))
                .thenReturn("23c041f978224fbcaa45b21b6a74c960");
        when(globalConfigService.get(GlobalConfigKeyEnum.OPT_MSG_SECRET_KEY.getKey()))
                .thenReturn("AAAAAAAA");
        when(globalConfigService.get(GlobalConfigKeyEnum.OPT_MSG_REQ_ADDR.getKey()))
                .thenReturn("http://www.openservice.com.cn:8001/open/sms/send");

        optSendMessageService.send(smsPojo);

        //verify
        verify(globalConfigService).get(GlobalConfigKeyEnum.OPT_MSG_APP_KEY.getKey());
        verify(globalConfigService).get(GlobalConfigKeyEnum.OPT_MSG_SECRET_KEY.getKey());
        verify(globalConfigService, atMost(2)).get(GlobalConfigKeyEnum.OPT_MSG_REQ_ADDR.getKey());
    }

    //无效的手机号码
    private SmsPojo invalidMobile() {
        SmsPojo smsPojo = buildSmsPojo();
        smsPojo.setMobile("1886710210");

        return smsPojo;
    }

    //空手机号码
    private SmsPojo emptyMobile() {
        SmsPojo smsPojo = buildSmsPojo();
        smsPojo.setMobile(null);

        return smsPojo;
    }

    //空短信内容
    private SmsPojo emptyContent() {
        SmsPojo smsPojo = buildSmsPojo();
        smsPojo.setContent(null);

        return smsPojo;
    }

    //测试短信对象
    private SmsPojo buildSmsPojo() {
        SmsPojo smsPojo = new SmsPojo();
        smsPojo.setMobile("14444444444");
        smsPojo.setContent(randStr(16));

        return smsPojo;
    }
}