/**
 * @Title: SingleSendServiceRequest.java
 * @Package com.cmcc.vrp.chongqing.boss.service.request
 * @author: qihang
 * @date: 2015年4月28日 下午7:21:37
 * @version V1.0
 */
package com.cmcc.vrp.boss.chongqing.service.request.impl;

import com.cmcc.vrp.boss.chongqing.pojo.model.PacketHead;
import com.cmcc.vrp.boss.chongqing.pojo.model.RequestHead;
import com.cmcc.vrp.util.ByteUtil;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: SingleSendServiceRequest
 * @Description: 100251 (赠送业务) 的request类
 * @author: qihang
 * @date: 2015年4月28日 下午7:21:37
 *
 *
 *   功能：单个电话赠送，立即返回结果
 *  参数说明：telnum(必填)  集团产品用户id
recnum(必填)    受赠号码
prodid(必填)  产品
 */
public class SingleSendServiceRequest implements BasicRequest {
    //包头
    public PacketHead packetHead;
    //包体head元素
    public RequestHead requestHeader;

    //手机号码,外围厂商传入，办理赠送业务包的用户号码
    public String telnum = "";
    //受赠号码
    public String recnum = "";
    //产品
    public String prodid = "";
    //0411新增 增加字段isNext，0当月生效，1次月生效
    public String isnext = "";


    public SingleSendServiceRequest() {
        setHeader();
    }


    public SingleSendServiceRequest(String telnum, String recnum, String prodid,String isnext) {
        this.telnum = telnum;
        this.recnum = recnum;
        this.prodid = prodid;
        this.isnext = isnext;
        setHeader();
    }


    /**
     * @Title: setHeader
     * @Description: 设置充值使用的头部参数，固定
     * @see com.cmcc.vrp.chongqing.boss.service.request.BasicRequest#setHeader()
     */
    public void setHeader() {
        packetHead = new PacketHead();
        packetHead.setReqformnum("10000000000546");
        packetHead.setOpcode("100011");
        packetHead.setSessionid("");
        packetHead.setRetcode("");
        packetHead.setCurpkgno("1");
        packetHead.setTotalpkgno("1");

        requestHeader = new RequestHead();
        requestHeader.setOpcode("100251");
        requestHeader.setReqformnum("10000000000546");
        requestHeader.setAccesstype("bsacTYDPEC");
        requestHeader.setUnitid("bsacTYDPEC");
        requestHeader.setTerminalid("BYD");
    }


    /**
     * @Title: setParams
     * @Description: 用于添加需要的元素
     * @param params
     * @see com.cmcc.vrp.chongqing.boss.service.request.BasicRequest#setParams(java.util.Map)
     */
    public void setParams(Map<String, Object> params) {
        telnum = (String) params.get("telnum");
        recnum = (String) params.get("recnum");
        prodid = (String) params.get("prodid");
        isnext = (String) params.get("isnext");
    }

    /**
     * @Title: frontCheck
     * @Description: TODO
     * @return
     * @see com.cmcc.vrp.chongqing.boss.service.request.BasicRequest#frontCheck()
     */
    public boolean frontCheck() {
        if (telnum == null || telnum.trim().length() == 0) {
            return false;
        }
        if (recnum == null || recnum.trim().length() == 0) {
            return false;
        }
        if (prodid == null || prodid.trim().length() == 0) {
            return false;
        }
        if (isnext == null || isnext.trim().length() == 0) {
            return false;
        }
        return true;
    }


    /**
     * @Title: getRequestDatas
     * @Description: TODO
     * @return
     * @see com.cmcc.vrp.chongqing.boss.service.request.BasicRequest#getRequestDatas()
     */
    public byte[] getRequestDatas() {
        byte[] byteTotalLength;//包文的总长度

        byte[] bytesHead = packetHead.getSendBytes();//得到包头
        byte[] bytesBody = generatePacketXml().getBytes();//得到包体xml内容,转化为byte[]

        byte[] bytesBodyLength = ByteUtil.convertIntToBytes(bytesBody.length);//得到xml包体长度

        int packetLength = 8 + bytesHead.length + bytesBody.length;//计算总包体长度，由3部分组成


        byteTotalLength = ByteUtil.convertIntToBytes(packetLength);//得到总包体长度

        List<byte[]> listBytes = new LinkedList<byte[]>();
        listBytes.add(byteTotalLength);
        listBytes.add(bytesBodyLength);
        listBytes.add(bytesHead);
        listBytes.add(bytesBody);

        return ByteUtil.concatByteArrays(listBytes);
    }

    /**
     * 生成Packet
     * @return PacketXml结果
     */
    public String generatePacketXml() {
        Document document = DocumentHelper.createDocument();// 创建根节点

        //生成HEAD元素
        Element root = document.addElement("CHEERBOSS");
        requestHeader.generateRequestHead(root);

        //生成DATA元素
        Element dataElement = root.addElement("DATA");
        //生成DATA->TELNUM元素
        Element telnumElement = dataElement.addElement("TELNUM");
        telnumElement.setText(telnum);
        //生成DATA->RECNUM元素
        Element recumElement = dataElement.addElement("RECNUM");
        recumElement.setText(recnum);
        //生成DATA->PRODID元素
        Element prodidElement = dataElement.addElement("PRODID");
        prodidElement.setText(prodid);
        //生成DATA->PRODID元素
        Element isnextElement = dataElement.addElement("ISNEXT");
        isnextElement.setText(isnext);

        return root.asXML();
    }

    public static void main(String[] args){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("telnum", "123456");
        params.put("recnum", "18867103685");
        params.put("prodid", "prd_3MB");
        params.put("isnext", "1");
        
        SingleSendServiceRequest request = new SingleSendServiceRequest();
        request.setParams(params);
        
        System.out.println(request.generatePacketXml());
    }
    
}
