package com.cmcc.vrp.boss.jiangxi;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.cmcc.vrp.boss.jiangxi.util.SecurityAESTool;
import com.cmcc.vrp.boss.jiangxi.webservice.IfAPServicePortType;
import com.cmcc.vrp.boss.jiangxi.webservice.IfAPServicePortTypeProxy;
import com.cmcc.vrp.ec.utils.SerialNumGenerator;
import com.cmcc.vrp.util.DateUtil;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午10:58:14
*/
public class WebserviceImpl {
    public static void main(String[] args) throws ServiceException, MalformedURLException, RemoteException {
        
        
        String body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><BODY><ECCODE>256</ECCODE></BODY>";
        body = SecurityAESTool.encrypt(body, "CC933BE6EDC6CF9C");
        String bossReqNum = SerialNumGenerator.buildNormalBossReqNum("jiangxi", 25);
        String sendMsg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><QueryCorpAccountReq><HEAD>" 
                + "<CODE>QueryCorpAccount</CODE>" 
                + "<SID>" + bossReqNum + "</SID><TIMESTAMP>" + DateUtil.getJiangXiBossTime() + "</TIMESTAMP>" 
                + "<SERVICEID>ekndnuRS</SERVICEID></HEAD><BODY>" + body +"</BODY></QueryCorpAccountReq>";
        IfAPServicePortType ifAPService = new IfAPServicePortTypeProxy();
        ifAPService.queryCorpAccount(sendMsg);
        String recMsg = ifAPService.queryCorpAccount(sendMsg);
        
        System.out.println("msg result =" + recMsg);
        System.out.println(SecurityAESTool.decrypt("fa4+AlUVQTdmmMSiYIQ6dvuviMES6Tu/T3+LGFpQ877d5G5QOyqv9c4L3PngDjlvPdPGuQaNXJAg+Qx1uksVsJbDS+" 
                + "f5wZpuixuzu1rs3u/vfI2D0YkcHkGSqzbkTCsueTiJ8vTp3GNoGMhGHypAuLMP5x6XC1L2+" 
                + "L3K2luoAuG1MhflEHkkOwmSArVhe8Q/5s7AacCqzvKiT4AVX0ccen8VZ2n0aCYp3fdq2LVWmiM=", 
                "CC933BE6EDC6CF9C"));
    }
}
