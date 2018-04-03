package com.cmcc.vrp.xss;

import com.google.gson.Gson;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by sunyiwei on 2016/8/18.
 */
public class XssHttpServletRequestWrapperTest {
    @Test
    public void testStripXss() throws Exception {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("name", "fdsfasd");
        map.put("dfksf", "kfdsjklf");
        map.put("dfksffdsfd", "<script>alert('hello')</script>");

        System.out.println(XssHttpServletRequestWrapper.stripXss(new Gson().toJson(map)));
    }
}