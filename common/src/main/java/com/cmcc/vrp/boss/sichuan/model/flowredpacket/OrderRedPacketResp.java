package com.cmcc.vrp.boss.sichuan.model.flowredpacket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.thoughtworks.xstream.XStream;
/**
 * <!--[if !supportLists]-->1.1.1.5. <!--[endif]-->响应参数
 * 流量红包返回参数
 * @author qihang
 *
 */
public class OrderRedPacketResp {
    private static Logger logger =  LoggerFactory.getLogger(OrderRedPacketResp.class);
    
    @JSONField(name="resCode")
    private String resCode;
    
    @JSONField(name="resMsg")
    private String resMsg;

    @JSONField(name="outData")
    private OrderRedPacketRespOutdata outData;
    
    public String getResCode() {
        return resCode;
    }


    public void setResCode(String resCode) {
        this.resCode = resCode;
    }


    public OrderRedPacketRespOutdata getOutData() {
        return outData;
    }


    public void setOutData(OrderRedPacketRespOutdata outData) {
        this.outData = outData;
    }

    public String getResMsg() {
        return resMsg;
    }


    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }


    /**
     * 从xml解析,解析成功返回对象，失败返回空
     */
    public static OrderRedPacketResp fromXml(String xml){
        if(xml == null){
            return null;
        }
        
        try{
            XStream xStream = new XStream();
            xStream.alias("ROOT", OrderRedPacketResp.class);
            xStream.alias("OUTDATA",OrderRedPacketRespOutdata.class);
            xStream.autodetectAnnotations(true);
            return (OrderRedPacketResp)xStream.fromXML(xml);
            
        }catch(com.thoughtworks.xstream.converters.ConversionException e){
            logger.error("xml解析失败,原数据为{}",xml);
            return null;
        }
        
    }
    
    /**
     * 从json解析,解析成功返回对象，失败返回空
     */
    public static OrderRedPacketResp fromJson(String jsonString){
        if(jsonString == null){
            return null;
        }
        
        try{
            return new Gson().fromJson(jsonString, OrderRedPacketResp.class);
        }catch(JsonSyntaxException e){
            logger.error("json解析失败 ,原数据为{}",jsonString);
        }
        
        return null;
        
    }
    
    
    public static void main(String[] args) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ROOT>"
                + "<resCode>0</resCode><resMsg>1</resMsg><detail_msg>2</detail_msg>"
                + "<outData>"
                + "<LOGIN_ACCEPT>1</LOGIN_ACCEPT>"
                + "<EFF_DATE>2</EFF_DATE>"
                + "<EXP_DATE>3</EXP_DATE>"
                + "<BRAND_ID>4</BRAND_ID>"
                + "<EFFEXP_MODE>5</EFFEXP_MODE>"
                + "</outData></ROOT>";
        OrderRedPacketResp resp = OrderRedPacketResp.fromXml(xml);
        
        String jsonString = new Gson().toJson(resp);
        System.out.println("解析为json是为" +jsonString);
        
        
        resp = fromJson(jsonString);
        System.out.println(resp.toString());
        
        
    }

}

