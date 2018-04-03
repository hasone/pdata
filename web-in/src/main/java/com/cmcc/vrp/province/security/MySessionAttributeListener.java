package com.cmcc.vrp.province.security;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午4:09:26
*/
public class MySessionAttributeListener implements HttpSessionAttributeListener {
    public static final Map<String, HttpSession> sessionMap = new ConcurrentHashMap<String, HttpSession>();

    @Override
    public void attributeAdded(HttpSessionBindingEvent se) {
        String name = (String) se.getName();
        if ("currentUserId".equals(name)) {//name属性保存用户登录信息，name=为唯一信息如用户名
            String currentUserId = (String) se.getValue();
            if (sessionMap.containsKey(currentUserId)) {
                HttpSession session = sessionMap.remove(currentUserId);
                session.invalidate();
                throw new UsernameNotFoundException("首次登陆自动注册账户失败！", new Object[]{});
            }
            sessionMap.put(name, se.getSession());
        }

    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent se) {

    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent se) {
    }

}