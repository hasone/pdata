package com.cmcc.vrp.pay.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * PayRequest
 *
 */
public class PayRequest {
    @XStreamAlias("PubInfo")
    private PayHeader payHeader;
    
    @XStreamAlias("BusiData")
    private PayReqBasicBody payBody;

    
    public PayRequest(){
        /*String encodeSecret = "vv5my48k9hhvij2fbxzkmm48c7iyxkhr";
        
        Map<String, String> paramsMap = new HashMap<String, String>();
        
        setPayHeader(new PayHeader());
        setPayBody(new PayReqBody());
        
        getPayHeader().putParamsToMap(paramsMap);
        
        
        getPayBody().putParamsToMap(paramsMap);
        getPayBody().encode();
        encodeParams(paramsMap,encodeSecret);*/
    }
    
    /**
     * 
     * encode
     */
    public void encode(String encodeSecret){
        Map<String, String> paramsMap = new HashMap<String, String>();
        getPayHeader().putParamsToMap(paramsMap);
        getPayBody().putParamsToMap(paramsMap);
        getPayBody().encode();
        encodeParams(paramsMap,encodeSecret);
    }
    
    public PayHeader getPayHeader() {
        return payHeader;
    }

    public void setPayHeader(PayHeader payHeader) {
        this.payHeader = payHeader;
    }

    public PayReqBasicBody getPayBody() {
        return payBody;
    }

    public void setPayBody(PayReqBasicBody payBody) {
        this.payBody = payBody;
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
     * encodeParams
     */
    private void encodeParams(Map<String, String> paramsMap,String encodeSecret){
        Map<String, String> sortMap = new TreeMap<String, String>(
                new MapKeyComparator());
        
        sortMap.putAll(paramsMap);
        
        StringBuffer buffer = new StringBuffer();
        for(Entry<String, String> entry:sortMap.entrySet()){ 
            buffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        
        String str = buffer.toString();
        str = str.substring(0, str.length()-1) + encodeSecret;
        
        /*System.out.println("签名字符串:");
        System.out.println(str);
        System.out.println("签名结果:");
        System.out.println(DigestUtils.md5Hex(str));*/
        payHeader.setVerifyCode(DigestUtils.md5Hex(str));
    }
       
    public static void main(String[] args){
        PayRequest request = new PayRequest();
        XStream xStream = new XStream();
        xStream.alias("AdvPay",PayRequest.class);
        xStream.autodetectAnnotations(true);
        
        String xml = xStream.toXML(request);
        //System.out.println(xml);
        
        String xml1 = xml.replace("\n","").replace(" ", "");
        System.out.println("生成url：");
        System.out.println("https://paypre.4ggogo.com/core/pay/ask-for.do?xml=" + xml1);
        
    }
    
}
