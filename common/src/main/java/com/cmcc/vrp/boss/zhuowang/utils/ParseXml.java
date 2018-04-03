package com.cmcc.vrp.boss.zhuowang.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午11:26:38
*/
public class ParseXml {

    /**
     * @param xml
     * @return
     * @throws DocumentException
     */
    public static Map xml2Map(String xml) throws DocumentException {

        SAXReader reader = new SAXReader();
        StringReader sr = null;
        try {
            sr = new StringReader(xml);
            Document document = reader.read(sr);
            Element root = document.getRootElement();
            return dom2Map(root);
        } finally {
            if (sr != null) {
                sr.close();
            }
        }
    }

    private static Map dom2Map(Element e) {
        Map map = new HashMap();
        List list = e.elements();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Element iter = (Element) list.get(i);
                List mapList = new ArrayList();
                if (iter.elements().size() > 0) {
                    Map m = dom2Map(iter);
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (obj instanceof List) {
                            mapList = (List) obj;
                            mapList.add(m);
                        } else {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(m);
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), m);
                    }
                } else {
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (obj instanceof List) {
                            mapList = (List) obj;
                            mapList.add(iter.getText());
                        } else {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(iter.getText());
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), iter.getText());
                    }
                }
            }
        } else {
            map.put(e.getName(), e.getText());
        }
        return map;
    }


}
