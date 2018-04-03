package com.cmcc.vrp.boss.xinjiang;

import static org.mockito.Mockito.when;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

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

import com.cmcc.vrp.boss.xinjiang.response.GroupInfoResp;
import com.cmcc.vrp.boss.xinjiang.response.NewResourcePoolResp;
import com.cmcc.vrp.boss.xinjiang.response.ResourcePoolResp;
import com.cmcc.vrp.boss.xinjiang.response.SendResp;
import com.cmcc.vrp.boss.xinjiang.service.XinjiangBossService;
import com.cmcc.vrp.boss.xinjiang.service.impl.XinjiangBossServiceImpl;
import com.cmcc.vrp.boss.xinjiang.wsdl.WebserviceClient;
import com.cmcc.vrp.province.service.GlobalConfigService;
import com.cmcc.vrp.util.GlobalConfigKeyEnum;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WebserviceClient.class})
public class XinjiangBossServiceImplTest {
    @InjectMocks
    XinjiangBossService xinjiangBossService = new XinjiangBossServiceImpl();
    
    @Mock
    GlobalConfigService configService;
    
    private String address = "http://www.baidu.com";
    
    @Before
    public void initMocks() {
        when(configService.get(GlobalConfigKeyEnum.BOSS_XJ_URL.getKey())).thenReturn(address);
    }
    
    /**
     * 5.1. 集团用户信息查询 参数： group_id：集团ID
     * 
     * 返回： GroupInfoResp 集团的基本信息，详情请参考该类
     * @throws ServiceException 
     * @throws RemoteException 
     */
    @Test
    public void testGetGroupInfo() throws ServiceException{
        try{
            PowerMockito.mockStatic(WebserviceClient.class);
            String msgError = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<SvcInfo><Header><System><COMMUNICATE value=\"02\" index=\"0\" />"
                    + "<TRANSFER value=\"01\" index=\"0\" /><ORGCHANNELID value=\"A001\" index=\"0\" />"
                    + "<HOMECHANNELID /></System><Inparam><CHANNEL_TRADE_ID value=\"E00320090401165020000000000000\" index=\"0\" />"
                    + "</Inparam><Outparam><RESULT_CODE value=\"0\" /><RESULT_INFO value=\"OK!\" />"
                    + "</Outparam><TESTFLAG value=\"0\" index=\"0\" /><ACTIONCODE value=\"1\" index=\"0\" /></Header>"
                    + "<Body><SVC_CONTENT>{X_RESULTINFO=[\"查询无数据！\"], X_RECORDNUM=[\"1\"], X_RESULTCODE=[\"0\"]}"
                    + "</SVC_CONTENT></Body></SvcInfo>";
            
            PowerMockito.when(WebserviceClient.sendPacket(Mockito.anyString(),Mockito.anyString())).thenReturn(msgError);
            GroupInfoResp resp = xinjiangBossService.getGetGroupInfo(msgError);
            Assert.assertEquals(resp.getResultCode(), "0");
            
            msgError = msgError.replaceAll("0", "1");
            PowerMockito.when(WebserviceClient.sendPacket(Mockito.anyString(),Mockito.anyString())).thenReturn(msgError);
            resp = xinjiangBossService.getGetGroupInfo(msgError);
            Assert.assertEquals(resp.getResultCode(), "1");
            
            PowerMockito.when(WebserviceClient.sendPacket(Mockito.anyString(),Mockito.anyString())).thenThrow(RemoteException.class);
            xinjiangBossService.getGetGroupInfo(msgError);
            PowerMockito.when(WebserviceClient.sendPacket(Mockito.anyString(),Mockito.anyString())).thenThrow(ServiceException.class);
            xinjiangBossService.getGetGroupInfo(msgError);
            
        }catch(RemoteException e){

        }catch (ServiceException e) {

        }
        
    }
    
    /**
     * 5.6. 集团用户产品流量池信息查询 参数： group_id：集团ID
     * 
     * 返回： ResourcePoolResp 集团的基本信息，详情请参考该类
     */
    @Test
    public void testGetResourcePool() throws ServiceException{
        try{
            PowerMockito.mockStatic(WebserviceClient.class);
            String msgCorrect = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SvcInfo><Header>"
                    + "<System><COMMUNICATE value=\"02\" index=\"0\" />"
                    + "<TRANSFER value=\"01\" index=\"0\" /><ORGCHANNELID value=\"A001\" index=\"0\" />"
                    + "<HOMECHANNELID /></System><Inparam>"
                    + "<CHANNEL_TRADE_ID value=\"E00320090401165020000000000000\" index=\"0\" />"
                    + "</Inparam><Outparam><RESULT_CODE value=\"0\" />"
                    + "<RESULT_INFO value=\"OK!\" /></Outparam><TESTFLAG value=\"0\" index=\"0\" />"
                    + "<ACTIONCODE value=\"1\" index=\"0\" /></Header><Body><SVC_CONTENT>"
                    + "{X_RESULTINFO=[\"OK\"], ALL_INIT_VALUE=[\"0.0\"], START_DATE=[\"null000000\"], USER_ID=[\"\"], "
                    + "ALL_VALUE=[\"0.0\"], END_DATE=[\"null235959\"], RES_ID=[\"\"], X_RESULTCODE=[\"0\"], "
                    + "X_RECORDNUM=[\"1\"]}</SVC_CONTENT></Body></SvcInfo>";
            
            PowerMockito.when(WebserviceClient.sendPacket(Mockito.anyString(),Mockito.anyString())).thenReturn(msgCorrect);
            ResourcePoolResp resp =xinjiangBossService.getResourcePoolResp("12");
            Assert.assertEquals(resp.getResultCode(), "0");
            
            PowerMockito.when(WebserviceClient.sendPacket(Mockito.anyString(),Mockito.anyString())).thenThrow(RemoteException.class);
            xinjiangBossService.getResourcePoolResp("12");
            PowerMockito.when(WebserviceClient.sendPacket(Mockito.anyString(),Mockito.anyString())).thenThrow(ServiceException.class);
            xinjiangBossService.getResourcePoolResp("12");
            
        }catch(RemoteException e){

        }catch (ServiceException e) {

        }
    
    }
    
    /**
     * 5.6. 集团用户产品流量池信息查询 参数： group_id：集团ID
     * 
     * 返回： ResourcePoolResp 集团的基本信息，详情请参考该类
     */
    @Test
    public void testGetResourcePoolResp() throws ServiceException{
        try{
            PowerMockito.mockStatic(WebserviceClient.class);
            String msgCorrect = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SvcInfo><Header><System>"
                    + "<COMMUNICATE value=\"02\" index=\"0\" /><TRANSFER value=\"01\" index=\"0\" />"
                    + "<ORGCHANNELID value=\"A001\" index=\"0\" /><HOMECHANNELID /></System><Inparam>"
                    + "<CHANNEL_TRADE_ID value=\"E00320090401165020000000000000\" index=\"0\" />"
                    + "</Inparam><Outparam><RESULT_CODE value=\"0\" /><RESULT_INFO value=\"OK!\" />"
                    + "</Outparam><TESTFLAG value=\"0\" index=\"0\" /><ACTIONCODE value=\"1\" index=\"0\" />"
                    + "</Header><Body><SVC_CONTENT>{X_RESULTINFO=[\"OK\"], START_DATE=[\"null000000\"], "
                    + "END_DATE=[\"null235959\"], ALL_VALUE=[\"0\"], X_RECORDNUM=[\"1\"], X_RESULTCODE=[\"0\"]}"
                    + "</SVC_CONTENT></Body></SvcInfo>";
            
            PowerMockito.when(WebserviceClient.sendPacket(Mockito.anyString(),Mockito.anyString())).thenReturn(msgCorrect);
            NewResourcePoolResp resp =xinjiangBossService.getResourcePoolRespNew("12");
            Assert.assertEquals(resp.getResultCode(), "0");
            
            PowerMockito.when(WebserviceClient.sendPacket(Mockito.anyString(),Mockito.anyString())).thenThrow(RemoteException.class);
            xinjiangBossService.getResourcePoolRespNew("12");
            PowerMockito.when(WebserviceClient.sendPacket(Mockito.anyString(),Mockito.anyString())).thenThrow(ServiceException.class);
            xinjiangBossService.getResourcePoolRespNew("12");
            
        }catch(RemoteException e){

        }catch (ServiceException e) {

        }
    }
    
    /**
     * 
     * testGetSendResp
     */
    @Test
    public void testGetSendResp() throws ServiceException{
        try{
            PowerMockito.mockStatic(WebserviceClient.class);
            String msgCorrect = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<SvcInfo><Header><System><COMMUNICATE value=\"02\" index=\"0\" />"
                    + "<TRANSFER value=\"01\" index=\"0\" />"
                    + "<ORGCHANNELID value=\"A001\" index=\"0\" /><HOMECHANNELID />"
                    + "</System><Inparam><CHANNEL_TRADE_ID value=\"E00320090401165020000000000000\" index=\"0\" />"
                    + "</Inparam><Outparam><RESULT_CODE value=\"0\" /><RESULT_INFO value=\"OK!\" />"
                    + "</Outparam><TESTFLAG value=\"0\" index=\"0\" /><ACTIONCODE value=\"1\" index=\"0\" />"
                    + "</Header><Body><SVC_CONTENT>{X_RESULTINFO=[\"根据集团客户编码[9919016339]，未订购该流量转赠产品! \"],"
                    + " X_RESULTCODE=[\"1\"], X_RECORDNUM=[\"0\"]}</SVC_CONTENT></Body></SvcInfo>";
            
            PowerMockito.when(WebserviceClient.sendPacket(Mockito.anyString(),Mockito.anyString())).thenReturn(msgCorrect);
            SendResp resp =xinjiangBossService.getSendResp("1", "1", "1", "1", "1");
            Assert.assertEquals(resp.getResultCode(), "1");
            
            PowerMockito.when(WebserviceClient.sendPacket(Mockito.anyString(),Mockito.anyString())).thenThrow(RemoteException.class);
            xinjiangBossService.getSendResp("1", "1", "1", "1", "1");
            PowerMockito.when(WebserviceClient.sendPacket(Mockito.anyString(),Mockito.anyString())).thenThrow(ServiceException.class);
            xinjiangBossService.getSendResp("1", "1", "1", "1", "1");
            
        }catch(RemoteException e){

        }catch (ServiceException e) {

        }
    }
    
    /**
     * 
     * testGetNewSendResp
     */
    @Test
    public void testGetNewSendResp() throws ServiceException{
        try{
            PowerMockito.mockStatic(WebserviceClient.class);
            String msgCorrect = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<SvcInfo><Header><System><COMMUNICATE value=\"02\" index=\"0\" />"
                    + "<TRANSFER value=\"01\" index=\"0\" />"
                    + "<ORGCHANNELID value=\"A001\" index=\"0\" /><HOMECHANNELID />"
                    + "</System><Inparam><CHANNEL_TRADE_ID value=\"E00320090401165020000000000000\" index=\"0\" />"
                    + "</Inparam><Outparam><RESULT_CODE value=\"0\" /><RESULT_INFO value=\"OK!\" />"
                    + "</Outparam><TESTFLAG value=\"0\" index=\"0\" /><ACTIONCODE value=\"1\" index=\"0\" />"
                    + "</Header><Body><SVC_CONTENT>{X_RESULTINFO=[\"根据集团客户编码[9919016339]，未订购该流量转赠产品! \"],"
                    + " X_RESULTCODE=[\"1\"], X_RECORDNUM=[\"0\"]}</SVC_CONTENT></Body></SvcInfo>";
            
            PowerMockito.when(WebserviceClient.sendPacket(Mockito.anyString(),Mockito.anyString())).thenReturn(msgCorrect);
            SendResp resp =xinjiangBossService.getNewSendResp("1", "1", "1", "1");
            Assert.assertEquals(resp.getResultCode(), "1");
            
            PowerMockito.when(WebserviceClient.sendPacket(Mockito.anyString(),Mockito.anyString())).thenThrow(RemoteException.class);
            xinjiangBossService.getNewSendResp("1", "1", "1", "1");
            PowerMockito.when(WebserviceClient.sendPacket(Mockito.anyString(),Mockito.anyString())).thenThrow(ServiceException.class);
            xinjiangBossService.getNewSendResp("1", "1", "1", "1");
            
        }catch(RemoteException e){

        }catch (ServiceException e) {

        }
    }
}
