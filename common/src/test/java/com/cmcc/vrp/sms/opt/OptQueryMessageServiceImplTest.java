package com.cmcc.vrp.sms.opt;

import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 开放平台短信发送状态查询测试
 *
 * Created by sunyiwei on 2017/2/13.
 */
@RunWith(MockitoJUnitRunner.class)
public class OptQueryMessageServiceImplTest {

    @InjectMocks
    private OptQueryMessageServiceImpl optQueryMessageService = new OptQueryMessageServiceImpl();

    @Mock
    private GlobalConfigService globalConfigService;

    /**
     * 测试短信状态查询
     */
    @Test
    public void testQuery() throws Exception {
        //测试无效的状态
        assertNull(optQueryMessageService.query(null));
        assertNull(optQueryMessageService.query(""));

        when(globalConfigService.get(GlobalConfigKeyEnum.OPT_MSG_QUERY_ADDR.getKey()))
                .thenReturn("http://www.openservice.com.cn:8001/open/sms/deliveryStatus");

        assertNotNull(optQueryMessageService.query("d86218c11c48401e9670868627476ed6"));

        //verify
        verify(globalConfigService, atMost(2)).get(GlobalConfigKeyEnum.OPT_MSG_QUERY_ADDR.getKey());
    }
}