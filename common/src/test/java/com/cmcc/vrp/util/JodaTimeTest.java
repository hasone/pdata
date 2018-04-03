package com.cmcc.vrp.util;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sunyiwei on 2016/6/22.
 */
public class JodaTimeTest {
    @Test
    public void testJodaTime() throws Exception {
        String str = new DateTime().toString();
        System.out.println(str);

        DateTime dateTime = ISODateTimeFormat.dateTimeParser().parseDateTime(str);
        System.out.println(dateTime);
    }

    @Test
    public void testDateTime() throws Exception {
        String date = "2017-12-31Tdfasklfjasdkljfdlk";

        Date newDeadLine = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        System.out.println(newDeadLine.toString());
    }

    @Test
    public void testDate() throws Exception {
        final String[] dateStr = new String[]{
            "2017-12-31",
//                "2017W521",
            "1997-365"
        };

        for (String s : dateStr) {
            DateTime dateTime = ISODateTimeFormat.dateTimeParser().parseDateTime(s);
            System.out.println(dateTime);
        }
    }
}
