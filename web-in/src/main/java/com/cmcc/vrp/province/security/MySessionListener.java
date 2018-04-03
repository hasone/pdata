package com.cmcc.vrp.province.security;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午4:09:40
*/
// savagechen11 add spring session
// HttpSessionEventPublisher继承了HttpSessionListener
// public class MySessionListener implements HttpSessionListener {
public class MySessionListener extends HttpSessionEventPublisher {

    // key为sessionId，value为HttpSession，使用static，定义静态变量，使之程序运行时，一直存在内存中。
    private static java.util.Map<String, HttpSession> sessionMap = new java.util.concurrent.ConcurrentHashMap<String, HttpSession>(500);

    /**
     * 得到在线用户会话集合
     */
    public static List<HttpSession> getUserSessions() {
        List<HttpSession> list = new ArrayList<HttpSession>();
        Iterator<String> iterator = getSessionMapKeySetIt();
        while (iterator.hasNext()) {
            String key = iterator.next();
            HttpSession session = getSessionMap().get(key);
            list.add(session);
        }
        return list;
    }

    /**
     * 得到用户对应会话map，key为用户手机号,value为会话ID
     */
    public static Map<Long, String> getUserSessionMap() {
        Map<Long, String> map = new HashMap<Long, String>();
        Iterator<String> iter = getSessionMapKeySetIt();
        while (iter.hasNext()) {
            String sessionId = iter.next();
            HttpSession session = getSessionMap().get(sessionId);
            Long userId = (Long) session.getAttribute("currentUserId");
            if (userId != null) {
                map.put(userId, sessionId);
            }
        }
        return map;
    }

    /**
     * 移除用户Session
     */
    public synchronized static void removeUserSession(Long userId) {
        Map<Long, String> userSessionMap = getUserSessionMap();
        if (userSessionMap.containsKey(userId)) {
            String sessionId = userSessionMap.get(userId);
            getSessionMap().get(sessionId).invalidate();
            getSessionMap().remove(sessionId);
        }
    }

    /**
     * 增加用户到session集合中
     */
    public static void addUserSession(HttpSession session) {
        getSessionMap().put(session.getId(), session);
    }

    /**
     * 移除一个session
     */
    public static void removeSession(String sessionID) {
        getSessionMap().remove(sessionID);
    }
    
    public static void removeAndInvalidByMobile(String mobile){
        if(StringUtils.isBlank(mobile)){
            return;
        }        
        Iterator<String> iter = getSessionMapKeySetIt();
        while (iter.hasNext()) {
            String key = iter.next();
            HttpSession session = getSessionMap().get(key);
            if(session != null && mobile.equals(session.getAttribute("mobile"))){
                removeSession(key);
                session.invalidate();
            }
        }
    }

    /**
     * @param key
     * @return
     */
    public static boolean containsKey(String key) {
        return getSessionMap().containsKey(key);
    }

    /**
     * 判断该用户是否已重复登录，使用
     * 同步方法，只允许一个线程进入，才好验证是否重复登录
     *
     * @param user
     * @return
     */
    public synchronized static boolean checkIfHasLogin(Long userId) {
        Iterator<String> iter = getSessionMapKeySetIt();
        while (iter.hasNext()) {
            String sessionId = iter.next();
            HttpSession session = getSessionMap().get(sessionId);
            Long sessionuser = (Long) session.getAttribute("currentUserId");
            if (sessionuser != null) {
                if (sessionuser.equals(userId)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取在线的sessionMap
     */
    public static Map<String, HttpSession> getSessionMap() {
        return sessionMap;
    }

    /**
     * 获取在线sessionMap中的SessionId
     */
    public static Iterator<String> getSessionMapKeySetIt() {
        return getSessionMap().keySet().iterator();
    }

    /**
     * HttpSessionListener中的方法，在创建session
     */
    public void sessionCreated(HttpSessionEvent event) {
        // TODO Auto-generated method stub
    }

    /**
     * HttpSessionListener中的方法，回收session时,删除sessionMap中对应的session
     */
    public void sessionDestroyed(HttpSessionEvent event) {
        // savagechen11 test session
        System.out.println("remove session [" + event.getSession().getId() + "]");
        getSessionMap().remove(event.getSession().getId());
    }
}

