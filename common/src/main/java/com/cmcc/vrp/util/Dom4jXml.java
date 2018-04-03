/**
 * @Title: Dom4jXml.java
 * @Package com.cmcc.vrp.util
 * @author: qihang
 * @date: 2015年4月27日 下午4:37:21
 * @version V1.0
 */
package com.cmcc.vrp.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;

/**
 * @ClassName: Dom4jXml
 * @Description: TODO
 * @author: qihang
 * @date: 2015年4月27日 下午4:37:21
 *
 */
public class Dom4jXml {

    public static Element getRootElement(String xml) throws DocumentException {
        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();
        return root;
    }

    public static String getElementValue(Element element, String propName) {
        QName qname = QName.get(propName);
        return element.attributeValue(qname);
    }
}
