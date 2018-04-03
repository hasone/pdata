package com.cmcc.vrp.boss.chongqing.service.request.impl;

import com.cmcc.vrp.boss.chongqing.pojo.model.PacketHead;
import com.cmcc.vrp.boss.chongqing.pojo.model.RequestHead;
import com.cmcc.vrp.util.ByteUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: EnterOrderQueryRequest
 * @Description: 200072 (查询集团已订购的有效的流量产品) 的request
 * @author: qihang
 * @date: 2015年4月29日 上午9:52:17
 * <p>
 * 功能：集团客户选择服务密码登陆验证接口，通过企业联系人电话查找相关企业和产品信息
 * 参数说明：subsid（必填） ：每个企业的产品Id，即使不同集团的同一产品也不同
 * <p>
 * retCode:100
 * retCode:192 没有相关信息
 */
public class EnterOrderQueryRequest implements BasicRequest {

    //包头
    public PacketHead packetHead;
    //包体head元素
    public RequestHead requestHeader;
    //集团编码SGROUPID元素
    public String subsid = "";

    public EnterOrderQueryRequest() {
        setHeader();
    }

    public void setHeader() {
        //TODO 设置包体的head元素,根据具体的参数可能需要重新配置
        packetHead = new PacketHead();
        packetHead.setReqformnum("10000000000546");
        packetHead.setOpcode("100011");
        packetHead.setSessionid("");
        packetHead.setRetcode("");
        packetHead.setCurpkgno("1");
        packetHead.setTotalpkgno("1");

        requestHeader = new RequestHead();
        requestHeader.setOpcode("200072");
        requestHeader.setReqformnum("10000000000546");
        requestHeader.setAccesstype("bsacTYDPEC");
        requestHeader.setUnitid("bsacTYDPEC");
        requestHeader.setTerminalid("BYD");

    }

    public void setParams(Map<String, Object> params) {
        subsid = (String) params.get("subsid");
    }

    /**
     * 检查头
     */
    public boolean frontCheck() {
        if (subsid == null || subsid.trim().length() == 0) {
            return false;
        }
        return true;
    }

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
        //生成DATA->SUBSID元素
        Element subsidElement = dataElement.addElement("SUBSID");
        subsidElement.setText(subsid);


        return root.asXML();
    }

}

