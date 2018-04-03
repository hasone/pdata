package com.cmcc.vrp.test.servlet;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * ServletContext事件监听器
 *
 * Created by sunyiwei on 2017/3/10.
 */
public class CustomServletContextListener implements ServletContextListener {
    /**
     * Receives notification that the web application initialization process is starting.
     *
     * <p>All ServletContextListeners are notified of context initialization before any filters or
     * servlets in the web application are initialized.
     *
     * @param sce the ServletContextEvent containing the ServletContext that is being initialized
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("ServletContext initialized.");
        ServletContext servletContext = sce.getServletContext();
        Enumeration<String> keys = servletContext.getInitParameterNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            System.out.format("%s = %s.%n", key, servletContext.getInitParameter(key));
        }
    }

    /**
     * Receives notification that the ServletContext is about to be shut down.
     *
     * <p>All servlets and filters will have been destroyed before any ServletContextListeners are
     * notified of context destruction.
     *
     * @param sce the ServletContextEvent containing the ServletContext that is being destroyed
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
