package com.cmcc.vrp.province.mdrc.enums;

import com.thoughtworks.xstream.converters.basic.DateConverter;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Date;

/**
 * Created by sunyiwei on 2016/6/2.
 */
public class DateTimeConverter extends DateConverter {
    @Override
    public Object fromString(String str) {
        return ISODateTimeFormat.dateTimeParser().parseDateTime(str).toDate();
    }

    @Override
    public String toString(Object obj) {
        Date date = (Date) obj;

        return new DateTime(date).toString();
    }
}
