package com.cmcc.vrp.pay.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * PayCallbackResponse
 *
 */
public class PayCallbackResponse {
    @XStreamAlias("PubInfo")
    private PayHeader payHeader;
    
    @XStreamAlias("BusiData")
    private PayCallbackBody paybody;

    public PayHeader getPayHeader() {
        return payHeader;
    }

    public void setPayHeader(PayHeader payHeader) {
        this.payHeader = payHeader;
    }

    public PayCallbackBody getPaybody() {
        return paybody;
    }

    public void setPaybody(PayCallbackBody paybody) {
        this.paybody = paybody;
    }
    
    /**
     * 排序
     */
    private class MapKeyComparator implements Comparator<String>{

        @Override
        public int compare(String str1, String str2) {
            
            return str1.compareTo(str2);
        }
    }
    
    
    /**
     * 将所有字段组装成需要加密的数据,字典排序.
     */
    public String encode(String encodeSecret){
        Map<String, String> paramsMap = new HashMap<String, String>();
        getPayHeader().putParamsToMap(paramsMap);
        getPaybody().putParamsToMap(paramsMap);
        
        Map<String, String> sortMap = new TreeMap<String, String>(
                new MapKeyComparator());
        
        sortMap.putAll(paramsMap);
        
        StringBuffer buffer = new StringBuffer();
        for(Entry<String, String> entry:sortMap.entrySet()){ 
            buffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        
        String str = buffer.toString();
        return str.substring(0, str.length()-1) + encodeSecret;
    }
    
    
   
}
