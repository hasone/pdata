/**
 * 
 */
package com.cmcc.vrp.ec.controller.aop;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cmcc.vrp.ec.bean.TimeResp;
import com.cmcc.vrp.exception.LimitIPRequestException;
import com.cmcc.vrp.province.cache.CacheService;
import com.cmcc.vrp.util.IpUtils;
import com.thoughtworks.xstream.XStream;

/**
 *  @desc:获取时间接口拦截器
 *  @author: wuguoping 
 *  @data: 2017年5月10日
 */
public class RequestLimitIPInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(RequestLimitIPInterceptor.class);
	
    @Autowired
	private CacheService cacheService;
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
            throws LimitIPRequestException,Exception{
        try{
            if (handler instanceof HandlerMethod){
                LimitIPRequest limitIPRequest = ((HandlerMethod) handler).getMethodAnnotation(LimitIPRequest.class);
				//controller没有添加LimitIPRequest注解
                if (limitIPRequest == null){
                    logger.info("controller没有添加LimitIPRequest注解");
                    System.out.println("controller没有添加LimitIPRequest注解");
                    return false;
                }
	            
                String ip = IpUtils.getRemoteAddr(request);
                String uri = request.getRequestURI().toString();
                String redisKey = "limit-ip-request:" + uri + ":" + ip;
	            
                long count = cacheService.getIncrOrUpdate(redisKey, limitIPRequest.limitTime());
	            
                if (count > limitIPRequest.limitCounts()) {
                    logger.info("用户IP[" + ip + "]访问地址[" + uri + "]超过了限定的次数[" + limitIPRequest.limitCounts() + "]");
                    System.out.println("用户IP[" + ip + "]访问地址[" + uri + "]超过了限定的次数[" + limitIPRequest.limitCounts() + "]");
                    String str = "少年不要这么频繁，你太快了！！！";
	              
                    TimeResp time = new TimeResp();
                    time.setResponseTime(str);
                    XStream xStream = new XStream();
                    xStream.alias("Response", TimeResp.class);
                    xStream.autodetectAnnotations(true);

                    try {
                        StreamUtils.copy(xStream.toXML(time), Charsets.UTF_8, response.getOutputStream());
                    } catch (IOException e) {
                        logger.error("获取服务器时间出错，错误信息为{}.", e.toString());
                    }
		           
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return false;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    } 
}
