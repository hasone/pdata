/**
 * @Title: EnterProQueryRequest.java
 * @Package com.cmcc.vrp.chongqing.boss.service.request
 * @author: qihang
 * @date: 2015年4月29日 上午9:52:17
 * @version V1.0
 */
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
 * @ClassName: EnterProQueryRequest
 * @Description: 100249 (赠送业务查询) request
 * @author: qihang
 * @date: 2015年4月29日 上午9:52:17
 *
 *
 *  功能：查找集团的某个产品的订购个数，赠送个数，赠送有效期
 *  参数说明：telnum（必填） ：企业的Id
 *         prodid（必填）：产品Id 
 *
 */
public class EnterProQueryRequest implements BasicRequest {
    //包头
    public PacketHead packetHead;
    //包体head元素
    public RequestHead requestHeader;
    //手机号码/集团产品用户id
    public String telnum = "";
    //业务包编码
    public String prodid = "";

    public EnterProQueryRequest() {
        setHeader();
    }

    public EnterProQueryRequest(String telnum, String prodid) {
        setHeader();
        this.telnum = telnum;
        this.prodid = prodid;
    }

    public void setParams(Map<String, Object> params) {
        telnum = (String) params.get("telnum");
        prodid = (String) params.get("prodid");
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
        requestHeader.setOpcode("100249");
        requestHeader.setReqformnum("10000000000546");
        requestHeader.setAccesstype("bsacTYDPEC");
        requestHeader.setUnitid("bsacTYDPEC");
        requestHeader.setTerminalid("BYD");
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
        if (prodid == null || prodid.trim().length() == 0) {
            return false;
        }

        return true;
    }

    /**
     * @Title: getRequestDatas
     * @Description: 将包总长，包头，业务包体组装成字节流
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
        //生成DATA->TELNUM元素
        Element prodidElement = dataElement.addElement("PRODID");
        prodidElement.setText(prodid);

        return root.asXML();
    }

    public String getTelnum() {
        return telnum;
    }

    public void setTelnum(String telnum) {
        this.telnum = telnum;
    }

    public String getProdid() {
        return prodid;
    }

    public void setProdid(String prodid) {
        this.prodid = prodid;
    }


}
