package com.cmcc.vrp.util;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义将STRING转化成DATE
 * Created by sunyiwei on 2016/5/26.
 */
public class StringToDateConverter implements Converter<String, Date> {
    @Override
    public Date convert(String source) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(source);
        } catch (ParseException e) {
            return null;
        }
    }
}
