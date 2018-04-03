package com.cmcc.vrp.pay.model;

import java.util.Map;

/**
 * 
 * PayReqBasicBody
 *
 */
public interface PayReqBasicBody {

    /**
     * 将所有的参数按照xml报文要求key-value 格式放到map中
     */
    public boolean putParamsToMap(Map<String, String> map);
    
    /**
     * 将会出现中文和其它特殊符号的字段进行urlEncode
     */
    public void encode();
    
}
