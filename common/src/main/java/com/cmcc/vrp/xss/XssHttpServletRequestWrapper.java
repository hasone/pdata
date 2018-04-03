package com.cmcc.vrp.xss;

import org.apache.commons.lang.StringUtils;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Created by sunyiwei on 2016/8/16.
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * 
     * 
     * */
    public static String stripXss(String value) {
        if (StringUtils.isNotBlank(value) && !"http://localhost:8080".equals(value)) {
            value = value.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
                    .replaceAll(">", "&gt;")
                    .replaceAll("'", "&apos;");
            
            /*try{
                //校验是否是json
                JSONObject object = JSONObject.parseObject(value);
                Set<String> it = object.keySet();
                Iterator<String> its = it.iterator();
                while(its.hasNext()){
                    String key = (String)its.next();
                    String item = (String)object.get(key);
                    item = item.replaceAll("\"", "&quot;");
                    
                    object.put(key, item);
                }
               
                return JSONObject.toJSONString(object);
            }catch(Exception e){
                try{
                   //检查是否是数组，营销活动是的奖品设置就是数组形式的json，
                    System.out.println("value === " + value);
                    JSONArray objectsArray = JSONObject.parseArray(value);
                    int i =0;
                    if(i < objectsArray.size()){
                        JSONObject object = (JSONObject) objectsArray.get(i);
                        
                        Set<String> it = object.keySet();
                        Iterator<String> its = it.iterator();
                        while(its.hasNext()){
                            String key = (String)its.next();
                            String item = object.get(key).toString();
                            item = item.replaceAll("\"", "&quot;");
                            
                            object.put(key, item);
                        }
                        objectsArray.remove(i);
                        objectsArray.add(i, object);
                        i++;
                    }
                    
                    System.out.println("JSONObject.toJSONString(objectsArray) === " + JSONObject.toJSONString(objectsArray)); 
                    return JSONObject.toJSONString(objectsArray);
                }catch(Exception e1){
                    value = value.replaceAll("\"", "&quot;");
                    return value;
                }
            }*/
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }

        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = stripXss(values[i]);
        }

        return encodedValues;
    }

    @Override
    public String getParameter(String name) {
        return stripXss(super.getParameter(name));
    }

    @Override
    public String getHeader(String name) {
        return stripXss(super.getHeader(name));
    }
}
