package com.cmcc.vrp.boss.beijing;

import com.cmcc.vrp.boss.BaseBossServiceTest;
import com.cmcc.vrp.boss.BossOperationResult;
import com.cmcc.vrp.boss.beijing.model.BjReturnCode;
import com.cmcc.vrp.boss.beijing.model.OrderReqBody;
import com.cmcc.vrp.boss.beijing.model.OrderReqHeader;
import com.cmcc.vrp.boss.beijing.model.OrderReqPara;
import com.cmcc.vrp.boss.beijing.model.OrderRespBody;
import com.cmcc.vrp.boss.beijing.model.OrderRespHeader;
import com.cmcc.vrp.boss.beijing.model.OrderRetInfo;
import com.cmcc.vrp.boss.beijing.model.OrderWebResponse;
import com.cmcc.vrp.boss.beijing.webservice.TfllbServiceServiceLocator;
import com.cmcc.vrp.boss.beijing.webservice.TfllbService_PortType;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.cmcc.vrp.province.service.SupplierProductService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;
import com.thoughtworks.xstream.XStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;

/**
 * Created by lilin on 2016/10/11.
 */
@PrepareForTest(BjBossServiceImpl.class)
public class BjBossServiceimplTest extends BaseBossServiceTest {

    @InjectMocks
    BjBossServiceImpl bjBossService = new BjBossServiceImpl();

    @Mock
    GlobalConfigService globalConfigService;

    @Mock
    SupplierProductService productService;

    @Mock
    SerialNumService serialNumService;

    @Before
    public void initMocks() throws ServiceException, RemoteException {
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_BJ_ADMIN_NAME.getKey())).thenReturn("xxxxxx");
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_BJ_CA.getKey())).thenReturn("xxxxxx");
        PowerMockito.when(globalConfigService.get(GlobalConfigKeyEnum.BOSS_BJ_PASSWORD.getKey())).thenReturn("xxxxxx");
        PowerMockito.when(productService.selectByPrimaryKey(anyLong())).thenReturn(buildSP());

    }

    @Ignore
    @Test
    public void testCharge() throws Exception {
        String systemNum = SerialNumGenerator.buildSerialNum();
        final String phone = "18867101129";
        final String pCode = "123456";

        PowerMockito.when(serialNumService.getByPltSerialNum(systemNum)).thenReturn(new SerialNum());
        PowerMockito.when(serialNumService.updateSerial(any(SerialNum.class))).thenReturn(true);

        bjBossService = PowerMockito.spy(bjBossService);
        OrderReqHeader reqHeader = new OrderReqHeader();
        OrderReqBody reqBody = new OrderReqBody();
        OrderReqPara reqPara = new OrderReqPara();
        PowerMockito.when(bjBossService, "buildOrderHeader").thenReturn(reqHeader);
        PowerMockito.doReturn(reqPara).when(bjBossService, "buildORPara", phone, pCode);
        PowerMockito.when(bjBossService, "buildOrderBody", reqPara).thenReturn(reqBody);
        PowerMockito.when(bjBossService, "buildOrderXML", reqHeader, reqBody).thenReturn("xxxxxx");
        TfllbServiceServiceLocator serviceLocator = PowerMockito.mock(TfllbServiceServiceLocator.class, RETURNS_DEEP_STUBS);
        TfllbService_PortType portType = PowerMockito.mock(TfllbService_PortType.class, RETURNS_DEEP_STUBS);
        PowerMockito.whenNew(TfllbServiceServiceLocator.class).withNoArguments().thenReturn(serviceLocator);
        when(serviceLocator.getTfllbService()).thenReturn(portType);
        when(portType.tf_tjzd("xxxxxx")).thenReturn(buildOWR(Integer.parseInt(BjReturnCode.SUCCESS.getCode())));
        BossOperationResult result = bjBossService.charge(1l, 1l, "18867101129", systemNum, null);
        Assert.assertTrue(result.isSuccess());
    }

    private String buildOWR(int bjReturnCode) {
        OrderWebResponse response = new OrderWebResponse();
        OrderRespHeader respHeader = new OrderRespHeader();
        OrderRespBody respBody = new OrderRespBody();
        respHeader.setRetCode(bjReturnCode);
        response.setRespHeader(respHeader);
        OrderRetInfo retInfo = new OrderRetInfo();
        retInfo.setOrderId("xxxxxx");
        respBody.setRetInfo(retInfo);
        response.setRespBody(respBody);
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        xStream.alias("WebResponse", OrderWebResponse.class);
        return xStream.toXML(response);
    }
}
