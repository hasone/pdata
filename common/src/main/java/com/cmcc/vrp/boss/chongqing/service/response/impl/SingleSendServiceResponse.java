/**
 * @Title: SingleSendServiceResponse.java
 * @Package com.cmcc.vrp.chongqing.boss.service.response
 * @author: qihang
 * @date: 2015年4月28日 下午7:27:02
 * @version V1.0
 */
package com.cmcc.vrp.boss.chongqing.service.response.impl;

import com.cmcc.vrp.util.Dom4jXml;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @ClassName: SingleSendServiceResponse
 * @Description: 100251 (赠送业务) 的response类
 * @author: qihang
 * @date: 2015年4月28日 下午7:27:02
 *
 *
 *        这里只有retCode为100时表示赠送成功，非100时失败，失败原因在retMsg中
 */
public class SingleSendServiceResponse implements BasicResponse {

    private static final Log logger = LogFactory.getLog(SingleSendServiceResponse.class);

    public String retCode = "";// "100"为正确，其它为错误
    public String retMsg = "";
    private Map<String, Object> params;

    /**
     * 打印信息
     */
    public void printAll() {
        //System.out.println("retCode:" + retCode + " retMsg:" + retMsg);
    }

    /**
     * @Title: recvData
     * @Description: TODO
     * @param recvDatas
     * @throws UnsupportedEncodingException
     * @see com.cmcc.vrp.chongqing.boss.service.response.BasicResponse#recvData(byte[])
     */
    public void recvData(byte[] recvDatas) throws UnsupportedEncodingException {
        if (recvDatas.length < 54) {
            return;
        }


        byte[] xmlDatas = new byte[recvDatas.length - 54];

        //截取从报文54位起的字节，复制到xmlDatas中
        System.arraycopy(recvDatas, 54, xmlDatas, 0, recvDatas.length - 54);

        //转化为string类型
        String xml = new String(xmlDatas, "gb2312");

        Element rootElement;

        // 从root元素开始处理所得元素
        try {
            rootElement = Dom4jXml.getRootElement(xml);
            parseXml(rootElement);

        } catch (DocumentException e) {// 不符合格式，处理异常信息
            logger.error(e);

        }
    }


    /**
     * 元素转换
     * @param rootElement   root元素
     */
    public void parseXml(Element rootElement) {
        Element headElement = rootElement.element("HEAD");

        if (headElement != null) {
            Element retCodeElement = headElement.element("RETCODE");
            retCode = retCodeElement.getText();

            Element retMsgElement = headElement.element("RETMSG");
            retMsg = retMsgElement.getText();
        }
    }

    /**
     * @Title: responseHandler
     * @Description: TODO
     * @see com.cmcc.vrp.chongqing.boss.service.response.BasicResponse#responseHandler()
     */
    public void responseHandler() {
        // TODO 处理retCode,填回数据库

    }

    @Override
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    /**
     * @Title: getRetCode
     * @Description: TODO
     * @return
     * @see com.cmcc.vrp.chongqing.boss.service.response.impl.BasicResponse#getRetCode()
     */
    @Override
    public String getRetCode() {
        return retCode;
    }

    /**
     * @Title: getRetMsg
     * @Description: TODO
     * @return
     * @see com.cmcc.vrp.chongqing.boss.service.response.impl.BasicResponse#getRetMsg()
     */
    @Override
    public String getRetMsg() {
        return retMsg;
    }

    /**
     * @Title: getReturnContent
     * @Description: TODO
     * @return
     * @see com.cmcc.vrp.chongqing.boss.service.response.impl.BasicResponse#getReturnContent()
     */
    @Override
    public Object getReturnContent() {
        // TODO Auto-generated method stub
        return null;
    }

}