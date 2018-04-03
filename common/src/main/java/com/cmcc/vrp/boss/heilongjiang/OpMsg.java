package com.cmcc.vrp.boss.heilongjiang;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author qihang
 *         <p>
 *         <p>
 *         最原始的黑龙江的充值接口
 */
public class OpMsg {
    public static List<String> getXml(String endpoint, String localPart, Object[] opArgs, String namespaceURI) {
        try {
            ServiceClient sender = new ServiceClient();
            EndpointReference targetEPR = new EndpointReference(endpoint);


            Options options = new Options();
            options.setAction("http://10.110.26.23:61000/esbWS/services/sWebOpMsgCfmLLWS/callService");
            options.setTo(targetEPR);
            sender.setOptions(options);

            options.setTo(targetEPR);
            OMFactory fac = OMAbstractFactory.getOMFactory();
            OMNamespace omNs = fac.createOMNamespace(namespaceURI, localPart);
            OMElement data = fac.createOMElement("callService", omNs);

            for (int i = 0; i < 14; i++) {
                OMElement inner = fac.createOMElement("params", omNs);
                inner.setText((String) opArgs[i]);
                data.addChild(inner);
            }
            OMElement result = sender.sendReceive(data);
            System.out.println(result.toString());


            List<String> resultRecv = new ArrayList<String>();

            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("");
            OMElement firstElement = result.getFirstElement();
            Iterator it = firstElement.getChildElements();
            while (it.hasNext()) {
                OMElement childs = (OMElement) it.next();
                if (childs.getLocalName().equals("esbRetCode")) {
                    System.out.println("esbRetCode=" + childs.getText());
                    resultRecv.add(childs.getText());
                } else if (childs.getLocalName().equals("retCode")) {
                    System.out.println("retCode=" + childs.getText());
                    resultRecv.add(childs.getText());
                } else if (childs.getLocalName().equals("retMsg")) {
                    System.out.println("retMsg=" + childs.getText());
                    resultRecv.add(childs.getText());

                }
            }
            return resultRecv;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * @param enterCode
     * @param productCode
     * @param mobile
     * @param priceCode
     * @return
     */
    public static List<String> charge(String enterCode, String productCode, String mobile, String priceCode) {
        String[] s = new String[14];
        s[0] = "0";
        s[1] = "37";
        s[2] = "g684";
        s[3] = "newweb";
        s[4] = "EIGBDHBHPHHPMJCE";
        s[5] = "1390450015";
        s[6] = "";
        s[7] = enterCode;
        s[8] = mobile;
        s[9] = productCode;
        s[10] = priceCode;
        s[11] = "3";
        s[12] = "2";
        s[13] = "0";

        List<String> list = getXml("http://10.110.26.23:61000/esbWS/services/sWebOpMsgCfmLLWS?wsdl", "callService", s, "http://txdoWS.esb.sitech.com");
        return list;
    }


    public static void main(String[] args) {
        String[] s = new String[14];
        s[0] = "0";
        s[1] = "37";
        s[2] = "g684";
        s[3] = "newweb";
        s[4] = "EIGBDHBHPHHPMJCE";
        s[5] = "1390450015";
        s[6] = "";
        s[7] = "4511232548";
        s[8] = "13904514343";
        s[9] = "10672";
        s[10] = "39915";
        s[11] = "3";
        s[12] = "2";
        s[13] = "0";


        List<String> list = getXml("http://10.110.26.23:61000/esbWS/services/sWebOpMsgCfmLLWS?wsdl", "callService", s, "http://txdoWS.esb.sitech.com");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i));
        }
        System.out.println();
    }

}
