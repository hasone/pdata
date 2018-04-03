package com.cmcc.vrp.util;

import com.cmcc.webservice.constants.Constant;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AssembleXml {

    /**
     * 默认根节点为“AdvPay”
     *
     * @param map
     * @return
     * @throws DocumentException
     */
    public static String map2Xml(Map map) throws IOException {
        return map2Xml(map, Constant.PublicInfo.ADVPAY);
    }
    /**
     * 指定根节点
     * @param map
     * @param rootNode
     * @return
     * @throws DocumentException
     */


    /**
     * 指定根节点
     *
     * @param map
     * @param rootNode
     * @return
     * @throws DocumentException
     */
    public static String map2Xml(Map map, String rootNode) throws IOException {
        Set set = map.keySet();
        Iterator it = set.iterator();

        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("utf-8");
        Element catalogElement = null;
        catalogElement = document.addElement(rootNode);
        while (it.hasNext()) {
            Object obj = it.next();
            map2Dom(catalogElement, obj.toString(), map.get(obj));
        }
        return document.asXML();
    }

    /**
     * 指定根节点
     *
     * @param map
     * @param rootNode
     * @return
     * @throws DocumentException
     */
    public static String map2Xml(Map map, String rootNode, String encoding) throws IOException {
        Set set = map.keySet();
        Iterator it = set.iterator();

        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding(encoding);
        Element catalogElement = null;
        catalogElement = document.addElement(rootNode);
        while (it.hasNext()) {
            Object obj = it.next();
            map2Dom(catalogElement, obj.toString(), map.get(obj));
        }
        return document.asXML();
    }

    public static void main(String[] args) throws IOException {
        Map map = new HashMap();
        Map busiMap = new HashMap();
        busiMap.put("AccountCode", "100123");
        map.put("busi", busiMap);
        map.put("pub", 4321);
        String xml = AssembleXml.map2Xml(map);
        System.out.println(xml);
    }

    private static void map2Dom(Element catalogElement, String key, Object obj) {
        if (obj instanceof Map) {
            Set set = ((Map) obj).keySet();
            Iterator it = set.iterator();
            Element child = catalogElement.addElement(key);
            while (it.hasNext()) {
                Object objTmp = it.next();
                map2Dom(child, objTmp.toString(), ((Map) obj).get(objTmp));
            }
        } else if (obj instanceof List) {
            List list = (List) obj;
            for (int i = 0; i < list.size(); i++) {
                map2Dom(catalogElement, key, list.get(i));
            }
        } else {
            Element child = catalogElement.addElement(key);
            if (obj != null && obj.toString().length() > 0) {
                child.addText(obj.toString());
            } else {
                child.addText("");
            }

        }

    }

}
