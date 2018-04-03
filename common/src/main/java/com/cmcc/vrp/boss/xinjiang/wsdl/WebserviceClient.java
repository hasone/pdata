package com.cmcc.vrp.boss.xinjiang.wsdl;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

/**
 * 该package内部的类都是通过wsdl，使用客户端自动生成的，无需做任何改动
 *
 */

public class WebserviceClient {

    /**
     * 得到赠送的报文
     */
    public static String sendPacket(String packet, String uipsoapAddress)
            throws ServiceException, RemoteException {
        UipService service = new UipServiceLocator();

        service.setUIPSOAPAddress(uipsoapAddress);

        // service.setUIPSOAPAddress("http://10.238.249.5:43000/uip_inws/services/UIPSOAP");
        UipPortType account = service.getUIPSOAP();

        BusinessCallRequest request = new BusinessCallRequest();
        request.setRequestMessage(packet);

        String response = account.businessCall(request);

        return response;
    }

    /*
     * public static void main(String[] args) throws Exception { UIP_Service
     * service =new UIP_ServiceLocator(); UIP_PortType account =
     * service.getUIPSOAP();
     * 
     * BusinessCallRequest request=new BusinessCallRequest();
     * 
     * 
     * 
     * 
     * String string=
     * "<SvcInfo><Header><Security><SEQID value=\"E0032009040116502000000000\"
     *  index=\"0\"/><DESPWD value=\"7d05bd542beaaecf0eb40cbf15d6bdfc\" index=\"0\"/>
     *  </Security><System><COMMUNICATE value=\"02\" index=\"0\"/>
     *  <TRANSFER value=\"01\" index=\"0\"/>
     *  <CONTACTID value=\"3200903120000\" index=\"0\"/>
     *  <BUSSID value=\"11111111110000000000000000\" index=\"0\"/>
     *  <ORGCHANNELID value=\"E003\" index=\"0\"/><HOMECHANNELID value=\"A001\" index=\"0\"/>
     *  </System><ACTIONCODE value=\"0\" index=\"0\"/><TESTFLAG value=\"0\" index=\"0\"/>
     *  <Inparam><TRADE_DEPART_ID value=\"22758\" index=\"0\"/>
     *  <TRADE_CITY_CODE value=\"INTF\" index=\"0\"/>
     *  <TRADE_EPARCHY_CODE value=\"0991\" index=\"0\"/>
     *  <ROUTE_EPARCHY_CODE value=\"0991\" index=\"0\"/><PROVINCE_CODE value=\"991\" index=\"0\"/><
     *  TRADE_ROUTE_TYPE value=\"00\" index=\"0\"/><TRADE_ROUTE_VALUE value=\"0991\" index=\"0\"/>
     *  <TRADE_STAFF_ID value=\"ITFCC000\" index=\"0\"/><IN_MODE_CODE value=\"4\" index=\"0\"/>
     *  <TRADE_DEPART_PASSWD value=\"0\" index=\"0\"/><CHANNEL_TRADE_ID value=\"E00320090401165020000000000000\" index=\"0\"/>
     *  <TRADE_TERMINAL_ID value=\"10.238.240.56\" index=\"0\"/><BIZ_CODE value=\"0000000\" index=\"0\"/>
     *  <TRANS_CODE value=\"ITF_FLHQ_GetGroupInfo\" index=\"0\"/></Inparam><InFields><InField>
     *  <INFO_CODE/><INFO_VALUE/></InField></InFields></Header><Body><SVC_CONTENT>{GROUP_ID=[\"9910106036\"]}
     *  </SVC_CONTENT></Body></SvcInfo>"
     * ; request.setRequestMessage(string);
     * 
     * 
     * System.out.println("Request:"+string); String response =
     * account.businessCall(request); System.out.println("Response:"+response);
     * 
     * }
     */
}
