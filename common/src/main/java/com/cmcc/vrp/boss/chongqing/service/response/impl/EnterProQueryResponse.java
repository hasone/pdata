/**
 * @Title: EnterProQueryResponse.java
 * @Package com.cmcc.vrp.chongqing.boss.service.response
 * @author: qihang
 * @date: 2015年4月29日 上午10:04:53
 * @version V1.0
 */
package com.cmcc.vrp.boss.chongqing.service.response.impl;

import com.cmcc.vrp.util.Dom4jXml;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @ClassName: EnterProQueryResponse
 * @Description: 100249 (赠送业务查询) response
 * @author: qihang
 * @date: 2015年4月29日 上午10:04:53
 *
 *  返回数据说明：remain：剩余的订购数量为订购数量剩余量+赠送数量剩余
 *           orderLeftnum:订购流量包数量剩余
 *           freeLeftnum：赠送流量包数量剩余
 *          当赠送流量包时若有赠送流量包数量剩余则优先赠送该中流量包
 *
 *
 *
 *          retCode:100   
 *          retCode:987357  企业Id错误时
 *          retCode:982009  流量包Id错误时
 */
public class EnterProQueryResponse implements BasicResponse {
    private static final Logger logger = Logger
        .getLogger(BasicResponse.class);
    public String remain = "";//剩余订购数量
    public String retCode = "";//"100"为正确，其它为错误
    public String retMsg = "";
    //	public String orderLeftnum="";//订购数量剩余量
//	public String freeLeftnum="";//赠送数量剩余
//	public String freeLeftEndDate="";//赠送数量到期时间
    private Map<String, Object> params;

    /**
     * 打印信息
     */
    public void printAll() {
        /*System.out.println("retCode:"+retCode+" retMsg:"+retMsg);
		if(retCode.equals("100")){
			System.out.println("remain:"+remain);
		}*/
    }


    /**
     * @Title: recvData
     * @Description: TODO
     * @param recvDatas
     * @throws UnsupportedEncodingException
     */
    public void recvData(byte[] recvDatas) throws UnsupportedEncodingException {
        if (recvDatas.length < 54) {
            return;
        }

        byte[] xmlDatas = new byte[recvDatas.length - 54];

        System.arraycopy(recvDatas, 54, xmlDatas, 0, recvDatas.length - 54);

        String xml = new String(xmlDatas, "gb2312");

        Element rootElement;

        try {
            rootElement = Dom4jXml.getRootElement(xml);
            parseXml(rootElement);

        } catch (DocumentException e) {
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

            if (!"100".equals(retCode)) {
                return;
            }
        }


        Element dataElement=rootElement.element("DATA");
        if(dataElement!=null){
            Element remainElement=dataElement.element("REMAIN");
            remain= remainElement.getText();
//			
//			Element orderLeftnumElement=dataElement.element("ORDER_LEFTNUM");
//			orderLeftnum=orderLeftnumElement.getText();
//			
//			Element freeLeftnumElement=dataElement.element("FREE_LEFTNUM");
//			freeLeftnum=freeLeftnumElement.getText();
//			
//			Element freeLeftEndDateElement=dataElement.element("FREE_LEFTENDDATE");
//			freeLeftEndDate=freeLeftEndDateElement.getText();
        }
    }

    /**
     * @Title: responseHandler
     * @Description: TODO
     */
    public void responseHandler() {
        // TODO 处理查询到的流量的问题
        if ("100".equals(retCode)) {

        }
    }


    @Override
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }


    /**
     * @Title: getRetCode
     * @Description: TODO
     * @return
     */
    @Override
    public String getRetCode() {
        // TODO Auto-generated method stub
        return retCode;
    }


    /**
     * @Title: getRetMsg
     * @Description: TODO
     * @return
     */
    @Override
    public String getRetMsg() {
        // TODO Auto-generated method stub
        return retMsg;
    }


    /**
     * @Title: getReturnContent
     * @Description: TODO
     * @return
     */
    @Override
    public Object getReturnContent() {

        return remain;
    }
}
