package com.cmcc.vrp.boss.chongqing.service.response.impl;

import com.cmcc.vrp.util.Dom4jXml;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: EnterOrderQueryRequest
 * @Description: 200072 (查询集团已订购的有效的流量产品) 的response
 * @author: qihang
 * @date: 2015年4月29日 上午9:52:17
 * <p>
 * 功能：集团客户选择服务密码登陆验证接口，通过企业联系人电话查找相关企业和产品信息
 */
//retCode:192 retMsg:No information!
//PRODID:gl_mwsq_11G
public class EnterOrderQueryResponse implements BasicResponse {

//    public String remain;//剩余订购数量

    public String retCode = "";//"100"为正确，其它为错误
    public String retMsg = "";
    //查询出的产品ID，通用，如gl_mwsq_11G
    public List<String> listProdid = new ArrayList<String>();
    private Map<String, Object> params;

    /**
     * 打印信息
     */
    public void printAll() {
        System.out.println("retCode:" + retCode + " retMsg:" + retMsg);
        if ("100".equals(retCode)) {
            for (String prodid : listProdid) {
                System.out.println("PRODID:" + prodid);
            }
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

        if ("100".equals(retCode)) {//返回码是100，表示正确，可以继续分析DATA元素

            Element dataElement = rootElement.element("DATA");
            if (dataElement != null) {
                listProdid.clear();
                Element detailinfoElement = dataElement.element("DETAILINFO");
                List<Element> listRowElement = detailinfoElement.elements();
                for (Element rowElement : listRowElement) {
                    listProdid.add(rowElement.element("PRODID").getText());
                }

            }
        }


    }
    
    /**
     * 接受数据
     */
    public void recvData(byte[] recvDatas) throws UnsupportedEncodingException {
        byte[] xmlDatas = new byte[recvDatas.length - 54];

        System.arraycopy(recvDatas, 54, xmlDatas, 0, recvDatas.length - 54);

        String xml = new String(xmlDatas, "gb2312");

        Element rootElement;

        try {
            rootElement = Dom4jXml.getRootElement(xml);

            parseXml(rootElement);

        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }


    /**
     * 响应处理
     */
    public void responseHandler() {
        // TODO Auto-generated method stub
        if ("100".equals(retCode)) {

        }
    }


    @Override
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }


    /**
     * @return
     * @Title: getRetCode
     * @Description: TODO
     * @see com.cmcc.vrp.chongqing.boss.service.response.impl.BasicResponse#getRetCode()
     */
    @Override
    public String getRetCode() {
        // TODO Auto-generated method stub
        return retCode;
    }


    /**
     * @return
     * @Title: getRetMsg
     * @Description: TODO
     * @see com.cmcc.vrp.chongqing.boss.service.response.impl.BasicResponse#getRetMsg()
     */
    @Override
    public String getRetMsg() {
        return retMsg;
    }


    /**
     * @return
     * @Title: getReturnContent
     * @Description: TODO
     * @see com.cmcc.vrp.chongqing.boss.service.response.impl.BasicResponse#getReturnContent()
     */
    @Override
    public Object getReturnContent() {
        return listProdid;
    }


}
