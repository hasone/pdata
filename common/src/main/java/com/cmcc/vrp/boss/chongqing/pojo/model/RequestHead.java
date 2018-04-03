/**
 * @Title: RequestHead.java
 * @Package com.cmcc.vrp.chongqing.boss.pojo.model
 * @author: qihang
 * @date: 2015年4月27日 下午3:18:33
 * @version V1.0
 */
package com.cmcc.vrp.boss.chongqing.pojo.model;

import org.dom4j.Element;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: RequestHead
 * @Description: TODO
 * @author: qihang
 * @date: 2015年4月27日 下午3:18:33
 *
 */
public class RequestHead {
    public String opcode;//交易命令字
    public String reqformnum;//对方请求流水编号
    public String accesstype;//渠道编码
    public String unitid;//厂商编码
    public String terminalid;//终端编号
    public String reqtime;//请求交易时间
    public String operid;//备用节点,可以为空
    public String sessionid;//外围厂商传入，可以为空

    public String resqno;//测试文档里存在


    /**
     * 获取请求元素
     * @param cheerboss  元素
     */
    public void generateRequestHead(Element cheerboss) {
        Element headElement = cheerboss.addElement("HEAD");

        Element opcodeElement = headElement.addElement("OPCODE");
        opcodeElement.setText(opcode);

        Element reqformnumElement = headElement.addElement("REQFORMNUM");
        reqformnumElement.setText(reqformnum);

        Element accesstypeElement = headElement.addElement("ACCESSTYPE");
        accesstypeElement.setText(accesstype);

        Element unitidElement = headElement.addElement("UNITID");
        unitidElement.setText(unitid);

        Element terminalidElement = headElement.addElement("TERMINALID");
        terminalidElement.setText(terminalid);

        setReqtime();
        Element reqtimeElement = headElement.addElement("REQTIME");
        reqtimeElement.setText(reqtime);


        Element operidElement = headElement.addElement("OPERID");
        if (operid != null && operid.trim().length() >= 0) {
            operidElement.setText(accesstype);
        }

        Element sessionidElement = headElement.addElement("SESSIONID");
        if (sessionid != null && sessionid.trim().length() == 0) {
            sessionidElement.setText(sessionid);
        }

        Element resqnoElement = headElement.addElement("RESQNO");
        if (resqno != null && resqno.trim().length() == 0) {
            resqnoElement.setText(resqno);
        }
    }


    public String getOpcode() {
        return opcode;
    }

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    public String getReqformnum() {
        return reqformnum;
    }

    public void setReqformnum(String reqformnum) {
        this.reqformnum = reqformnum;
    }

    public String getAccesstype() {
        return accesstype;
    }

    public void setAccesstype(String accesstype) {
        this.accesstype = accesstype;
    }

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }

    public String getTerminalid() {
        return terminalid;
    }

    public void setTerminalid(String terminalid) {
        this.terminalid = terminalid;
    }

    public String getReqtime() {
        return reqtime;
    }

    public void setReqtime(String reqtime) {
        this.reqtime = reqtime;
    }

    public void setReqtime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        reqtime = dateFormat.format(new Date());
    }

    public String getOperid() {
        return operid;
    }

    public void setOperid(String operid) {
        this.operid = operid;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }


    public String getResqno() {
        return resqno;
    }


    public void setResqno(String resqno) {
        this.resqno = resqno;
    }
}
