package com.cmcc.vrp.province.mdrc.enums;

import com.thoughtworks.xstream.converters.enums.EnumSingleValueConverter;

/**
 * Created by sunyiwei on 2016/6/2.
 */
public class MdrcEcOperationEnumConverter extends EnumSingleValueConverter {
    public MdrcEcOperationEnumConverter(Class<? extends Enum> type) {
        super(MdrcEcOperationEnum.class);
    }

    @Override
    public String toString(Object obj) {
        return ((MdrcEcOperationEnum) obj).getOperCode();
    }

    @Override
    public Object fromString(String str) {
        return MdrcEcOperationEnum.fromCode(str);
    }
}
