package com.cmcc.vrp.boss.liaoning;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.cmcc.vrp.boss.BossQueryResult;
import com.cmcc.vrp.boss.liaoning.util.LnGlobalConfig;
import com.cmcc.vrp.boss.liaoning.util.SignUtil;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.SerialNum;
import com.cmcc.vrp.province.service.ChargeRecordService;
import com.cmcc.vrp.province.service.SerialNumService;
import com.google.gson.Gson;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年5月24日 下午4:41:02
*/
@RunWith(PowerMockRunner.class)
@PrepareForTest({SignUtil.class, Gson.class})
public class LnBossQueryServiceImplTest {
    @InjectMocks
    LnBossQueryServiceImpl bossService = new LnBossQueryServiceImpl();

    @Mock
    ChargeRecordService recordService;
    
    @Mock
    SerialNumService serialNumService;
    
    @Mock
    LnGlobalConfig lnGlobalConfig;
    /**
     * 
     */
    @Before
    public void initMocks() {
        PowerMockito.when(lnGlobalConfig.getAppId()).thenReturn("501800");
        PowerMockito.when(lnGlobalConfig.getAppKey()).thenReturn("09b149b082d320cb54cd7385b129371f");
        PowerMockito.when(lnGlobalConfig.getCustId()).thenReturn("100016261826");
        PowerMockito.when(lnGlobalConfig.getEffectiveWay()).thenReturn("0");
        PowerMockito.when(lnGlobalConfig.getSendMsg()).thenReturn("0");
        PowerMockito.when(lnGlobalConfig.getGiveMonth()).thenReturn("1");
        PowerMockito.when(lnGlobalConfig.getOpenId()).thenReturn("e2f94078-4c61-4d25-96ec-9a5aa82cb063");
        PowerMockito.when(lnGlobalConfig.getUrl()).thenReturn("http://221.180.247.69:5291/oppf");
        PowerMockito.when(lnGlobalConfig.getAccessToken()).thenReturn("58d238d7-45ff-4f02-bc94-6cd93c5fff2c");
        PowerMockito.when(lnGlobalConfig.getOperId()).thenReturn("100004638522");
        Gson gson = new Gson();
        ReflectionTestUtils.setField(bossService, "gson", gson);    

    }
    /**
     * @throws Exception
     */
    @Test
    public void testQueryStatus() throws Exception {
        String meString = "{\"Response\":{\"ErrorInfo\":{\"Code\":\"0000\",\"DealTime\":10,\"Message\":\"成功\"}," + 
                "\"RetInfo\":{\"Detail\":[],\"ErrorInfo\":{\"Code\":\"1045\",\"Hint\":\"查询无结果\",\"Message\":\"查询无结果\"}}}}";
        String meString1 = "{\"Response\":{\"ErrorInfo\":{\"Code\":\"0000\",\"DealTime\":10,\"Message\":\"成功\"}," + 
                "\"RetInfo\":{\"Detail\":[],\"ErrorInfo\":{\"Code\":\"0000\",\"Hint\":\"查询无结果\",\"Message\":\"查询无结果\"}}}}";
        String meString2 = "{\"Response\":{\"ErrorInfo\":{\"Code\":\"0000\",\"DealTime\":10,\"Message\":\"成功\"}," 
                + "\"RetInfo\":{\"Detail\":[{\"AcceptDate\":\"2017-05-24\",\"BatchNO\":\"053206757637251\"," 
                + "\"CustName\":\"中移（杭州）信息技术有限公司\",\"FEE\":\"50\",\"PROD\":\"50元/月含1G国内流量\"," 
                + "\"ProdType\":\"流量统付\",\"Region\":\"大连\",\"SATE\":\"2\",\"SubmitDate\":\"2017-05-24\"," 
                + "\"SucessBillId\":\"15242511773\"}],\"ErrorInfo\":{\"Code\":\"0000\",\"Hint\":\"成功\",\"Message\":\"成功\"}}}}";
        String meString3 = "{\"Response\":{\"ErrorInfo\":{\"Code\":\"0000\",\"DealTime\":10,\"Message\":\"成功\"}," 
                + "\"RetInfo\":{\"Detail\":[{\"AcceptDate\":\"2017-05-24\",\"BatchNO\":\"053206757637251\"," 
                + "\"CustName\":\"中移（杭州）信息技术有限公司\",\"FEE\":\"50\",\"PROD\":\"50元/月含1G国内流量\",\"ProdType\":\"流量统付\"," 
                + "\"Region\":\"大连\",\"SATE\":\"1\",\"SubmitDate\":\"2017-05-24\",\"SucessBillId\":\"15242511773\"}]," 
                + "\"ErrorInfo\":{\"Code\":\"0000\",\"Hint\":\"成功\",\"Message\":\"成功\"}}}}";
        PowerMockito.mockStatic(SignUtil.class);
        String systemNum = SerialNumGenerator.buildSerialNum();
        Assert.assertEquals(BossQueryResult.FAILD, bossService.queryStatus(null));
        Assert.assertEquals(BossQueryResult.FAILD, bossService.queryStatus(""));
        PowerMockito.when(recordService.getRecordBySN(Mockito.anyString())).thenReturn(null).thenReturn(buildChargeRecord());
        Assert.assertEquals(BossQueryResult.FAILD, bossService.queryStatus(systemNum));
        PowerMockito.when(serialNumService.getByPltSerialNum(Mockito.anyString())).thenReturn(null).thenReturn(buildSerialNum());
        Assert.assertEquals(BossQueryResult.FAILD, bossService.queryStatus(systemNum));
        PowerMockito.when(SignUtil.execute(Mockito.anyMap(), Mockito.anyString(),Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
            .thenReturn(meString);
        Assert.assertEquals(BossQueryResult.FAILD, bossService.queryStatus(systemNum));
        PowerMockito.when(SignUtil.execute(Mockito.anyMap(), Mockito.anyString(),Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
            .thenReturn(meString1);
        Assert.assertEquals(BossQueryResult.FAILD, bossService.queryStatus(systemNum));
        PowerMockito.when(SignUtil.execute(Mockito.anyMap(), Mockito.anyString(),Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
            .thenReturn(meString2);
        Assert.assertEquals(BossQueryResult.SUCCESS, bossService.queryStatus(systemNum));
        PowerMockito.when(SignUtil.execute(Mockito.anyMap(), Mockito.anyString(),Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
            .thenReturn(meString3);
        Assert.assertEquals(BossQueryResult.FAILD, bossService.queryStatus(systemNum));
    }
    /**
     * @return
     */
    public ChargeRecord buildChargeRecord() {
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setPhone("18867105766");
        chargeRecord.setChargeTime(new Date());
        return chargeRecord;
    }
    /**
     * @return
     */
    public SerialNum buildSerialNum() {
        SerialNum serialNum = new SerialNum();
        serialNum.setBossReqSerialNum("123456");
        return serialNum;
    }
}
