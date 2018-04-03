package com.cmcc.vrp.boss.heilongjiang.fee;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.spi.container.servlet.ServletContainer;

/**
 * Using built in server eclipse.jetty to test. Controller is in jersey not handler.
 * 
 * @author Lenovo
 *
 */
public class BossServerMockJersey {
    private Logger LOG = LoggerFactory.getLogger(BossServerMockJersey.class);
    public final static int DEFAULT_PORT = 9191;
    public final static String DEFAULT_CONTENT_TYPE = "application/json";
    public final static int DEFAULT_STATUS_CODE = HttpServletResponse.SC_OK;

    private Server _httpServer;
    private int _port;

    public BossServerMockJersey() {
        _port = DEFAULT_PORT;
    }

    public BossServerMockJersey(int port) {
        _port = port;
    }

    public void stop() throws Exception {
        LOG.info("stop server");
        if (null != _httpServer) {
            _httpServer.stop();
            _httpServer = null;
        }
    }

    public void start(String jerseyReceiverPath) throws Exception {
        _httpServer = new Server(_port);

        ResourceConfig config = new PackagesResourceConfig("com.cmcc.vrp.boss.heilongjiang.fee");
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        handler.addServlet(servlet, "/*");
        _httpServer.setHandler(handler);
        _httpServer.start();
    }
}
