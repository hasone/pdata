package com.cmcc.vrp.util;

import com.cmcc.vrp.province.mdrc.model.MdrcEcRequest;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import org.junit.Test;

import java.util.Date;

/**
 * Created by sunyiwei on 2016/6/22.
 */
public class DateTimeConverterTest {
    @Test
    public void testConverter() throws Exception {
        final String content = "<Request><Datetime>2016-06-22</Datetime><Card><Operate>Stock In</Operate><CardNum>1000</CardNum><ExpireDate>2016-06-23T22:42:50.236+08:00</ExpireDate><ExchangeCards>1001</ExchangeCards><GroupId>2809877123</GroupId><ProductCode>2</ProductCode></Card></Request>";

        XStream xstream = new XStream();
        xstream.alias("Request", MdrcEcRequest.class);
        xstream.autodetectAnnotations(true);

        MdrcEcRequest mdrcEcRequest = (MdrcEcRequest) xstream.fromXML(content);
        System.out.println(new Gson().toJson(mdrcEcRequest));
    }

    @Test
    public void testDateTime() throws Exception {
        long value = 1467288248;

        System.out.println(new Date(value));
    }
}
