package com.cmcc.vrp.util;

import com.thoughtworks.xstream.converters.basic.DateConverter;

/**
 * Created by leelyn on 2016/6/19.
 */
public class CardDateConverter extends DateConverter {

    public CardDateConverter(String dateFormat) {
        super(dateFormat, new String[]{dateFormat});
    }
}
