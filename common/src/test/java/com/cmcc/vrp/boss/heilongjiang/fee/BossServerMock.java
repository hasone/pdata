package com.cmcc.vrp.boss.heilongjiang.fee;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Using built in server eclipse.jetty to test. Controller is in handler not jersey.
 * 
 * @author Lenovo
 *
 */
public class BossServerMock {

    private Logger LOG = LoggerFactory.getLogger(BossServerMock.class);
    public final static int DEFAULT_PORT = 9191;
    public final static String DEFAULT_CONTENT_TYPE = "application/json";
    public final static int DEFAULT_STATUS_CODE = HttpServletResponse.SC_OK;

    private Server _httpServer;
    private int _port;

    public BossServerMock() {
        _port = DEFAULT_PORT;
    }

    public BossServerMock(int port) {
        _port = port;
    }

    /**
     * 启动Jetty服务器。默认的响应status code为"200"，content type为"application/json"。
     * 
     * @param content
     *            响应内容
     */
    public void start(String content) throws Exception {
        start(content, DEFAULT_CONTENT_TYPE, DEFAULT_STATUS_CODE);
    }

    /**
     * 启动Jetty服务器。默认的响应status code为"200"。
     * 
     * @param content
     *            响应内容
     * @param contentType
     *            响应内容的MIME类型
     */
    public void start(String content, String contentType) throws Exception {
        start(content, contentType, DEFAULT_STATUS_CODE);
    }

    public void start(String content, Map<String, AbstractHandler> map) throws Exception {
        start(content, null, 0);
    }

    // public void start(Map<String, AbstractHandler> map) throws Exception {
    // _httpServer = new Server(_port);
    // for (Iterator<?> iter = map.entrySet().iterator(); iter.hasNext(); ) {
    // Entry<?, ?> entry = (Entry<?, ?>) iter.next();
    // new Context(_httpServer,(String) entry.getKey()).setHandler((AbstractHandler)entry.getValue());
    // }
    // _httpServer.start();
    // }

    public void start(Map<String, AbstractHandler> map) throws Exception {
        _httpServer = new Server(_port);

        List<Handler> list = new ArrayList<Handler>();

        for (Iterator<?> iter = map.entrySet().iterator(); iter.hasNext();) {
            Entry<?, ?> entry = (Entry<?, ?>) iter.next();

            ContextHandler context = new ContextHandler(entry.getKey().toString());
            context.setHandler((Handler) entry.getValue());
            _httpServer.setHandler(context);
            list.add(context);
        }

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers((Handler[]) list.toArray(new Handler[list.size()]));
        _httpServer.start();
        // _httpServer.join();
    }

    public void start() throws Exception {
        _httpServer = new Server(_port);
        _httpServer.start();
    }

    /**
     * 启动Jetty服务器。
     * 
     * @param content
     *            响应内容
     * @param contentType
     *            响应内容的MIME类型
     * @param statuCode
     *            响应状态码
     */
    public void start(String content, String contentType, int statuCode) throws Exception {

        _httpServer = new Server(_port);
        _httpServer.setHandler(defaultHandler(content, contentType, statuCode));
    }

    /**
     * 停止Jetty服务器。
     */
    public void stop() throws Exception {
        LOG.info("stop server");
        if (null != _httpServer) {
            _httpServer.stop();
            _httpServer = null;
        }
    }

    /**
     * An ecco handler tests {@link BossServerMock} itself
     * 
     * @param content
     * @param contentType
     * @param statusCode
     * @return
     */
    private Handler defaultHandler(final String content, final String contentType, final int statusCode) {

        return new AbstractHandler() {

            @Override
            public void handle(String target, Request arg1, HttpServletRequest baseRequest, HttpServletResponse response) throws IOException, ServletException {
                response.setContentType(contentType);
                response.setStatus(statusCode);
                response.getWriter().print(content);

            }
        };
    }

}
