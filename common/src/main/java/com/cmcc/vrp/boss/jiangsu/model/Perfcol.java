package com.cmcc.vrp.boss.jiangsu.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

/**
 * @author lgk8023
 *
 */
@XStreamAlias("perfcol")
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"value"})
public class Perfcol {
    private String need;
    
    private String value;

    public String getNeed() {
        return need;
    }

    public void setNeed(String need) {
        this.need = need;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
