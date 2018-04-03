package com.cmcc.vrp.boss.jiangxi.webservice;

import java.rmi.RemoteException;

import com.cmcc.vrp.boss.jiangxi.util.SecurityAESTool;

/**
 * @author lgk8023
 *
 */
public class ClientTest {

    public static void main(String[] args) throws RemoteException {

        IfAPServicePortType ifAPService = new IfAPServicePortTypeProxy();
        String resultStr = ifAPService.queryCorpAccount("<?xml version=\"1.0\" encoding=\"UTF-8\"?><QueryCorpAccountReq><HEAD><CODE>QueryCorpAccount</CODE>"
                + "<SID>20170619000000</SID><TIMESTAMP>20170619000000</TIMESTAMP><SERVICEID>ekndnuRS</SERVICEID></HEAD>"
                + "<BODY>58hpOpZ5OxiU3KjW+BWvVp+ghUZ8x5Lj3aTfBQNiQTf1Q03mdyA0K2yzbioVbEGjLmQirAdxQ48SPkdU2OThI57+"
                + "0elk1Y76mXAQdKsYmOdFV7AlXQvpwibW2nms6tDk1E9LOWzssHA=</BODY></QueryCorpAccountReq>");
        System.out.println(resultStr);
        System.out.println(SecurityAESTool.decrypt(
                "gGl47YouIJ+YdLFTSY7CDEi3id5x1GOdNE6ev1RMxsHd5G5QOyqv9c4L3PngDjlvPdPGuQaNXJAg+Qx1uksVsJbDS+" 
                        + "f5wZpuixuzu1rs3u9fjBqmNs0VCMnbHlGBBp2FeTiJ8vTp3GNoGMhGHypAuGZT/83DFrXDz+" 
                        + "llnMMdkwbyLRjZZUC7fm1+6YTwG7a2sdc8FYN2tIUBO0P1lcVf9g==",
                "CC933BE6EDC6CF9C"));

    }
}