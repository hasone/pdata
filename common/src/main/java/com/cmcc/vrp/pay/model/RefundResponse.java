package com.cmcc.vrp.pay.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 退款返回，包含同步和异步，具体参考RefundAsyncResponse
 *
 */
public class RefundResponse {
    @XStreamAlias("PubInfo")
    private PayHeader header;
    
    @XStreamAlias("BusiData")
    private RefundAsyncResponse body;

    public PayHeader getHeader() {
        return header;
    }

    public void setHeader(PayHeader header) {
        this.header = header;
    }

    public RefundAsyncResponse getBody() {
        return body;
    }

    public void setBody(RefundAsyncResponse body) {
        this.body = body;
    }

    /*public static void main(String[] args){
        XStream xStream = new XStream();
        xStream.alias("AdvPay",RefundResponse.class);
        xStream.autodetectAnnotations(true);
        
        RefundResponse response = null;
        String xml = "<AdvPay><PubInfo><Version>1</Version></PubInfo><BusiData><OrderId>1</OrderId><ReturnCode>201</ReturnCode></BusiData></AdvPay>";
        response = (RefundResponse) xStream.fromXML(xml);
        System.out.println("fini");
    }*/
    
    
}
