/**
 * 
 *//*
package com.cmcc.vrp.ec.controller.aop;


import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cmcc.vrp.exception.LimitIPRequestException;
import com.cmcc.vrp.province.cache.CacheService;
import com.cmcc.vrp.util.IpUtils;

*//**
 *  @author: wuguoping 
 *  @data: 2017年5月9日
 *//*
@Aspect
@Component
public class LimitIPRequestAspect {
	 private static final Logger logger = LoggerFactory.getLogger(LimitIPRequestAspect.class);
	
	@Autowired
	private CacheService cacheService;
	
//	@Pointcut("execution(* com.cmcc.vrp.ec.controller.*.*(..)) && @annotation(com.cmcc.vrp.ec.controller.aop.LimitIPRequest)")
	public void before(){
	}
	
	@Before("before()")
	public void requestLimit(JoinPoint joinPoint) throws LimitIPRequestException {
		try{
			//获取httprequest
			ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = attributes.getRequest();
			HttpServletResponse response = attributes.getResponse();
			
			if(request == null)
				throw new LimitIPRequestException("HttpServletRequest is wrong!");
			
			LimitIPRequest limitIPRequest = this.getAnnotation(joinPoint);
			if(limitIPRequest == null){
				logger.error("Request is null, limitIPRequest = {}", limitIPRequest);
				return;
			}
			
			String ip = IpUtils.getRemoteAddr(request);
            String uri = request.getRequestURI().toString();
            String redisKey = "limit-ip-request:" + uri + ":" + ip;
            
            long count = cacheService.getIncrOrUpdate(redisKey, limitIPRequest.limitTime());
            
            if (count > limitIPRequest.limitCounts()) {
                logger.info("用户IP[" + ip + "]访问地址[" + uri + "]超过了限定的次数[" + limitIPRequest.limitCounts() + "]");
                String str = "用户IP[" + ip + "]访问地址[" + uri + "]超过了限定的次数[" + limitIPRequest.limitCounts() + "]";
//                StreamUtils.copy(new Gson().toJson(str), Charsets.UTF_8, response.getOutputStream());    
               throw new LimitIPRequestException(str);
            }
		}catch(LimitIPRequestException e1){
			e1.printStackTrace();
		}catch(Exception e){
			logger.error("",e);
		}
	}
	
	*//**
	 * 
	 * title: getAnnotation
	 * desc: 为了获取注解
	 * @param joinPoint
	 * @return
	 * @throws Exception
	 * wuguoping
	 * 2017年5月9日
	 *//*
	private LimitIPRequest getAnnotation(JoinPoint  joinPoint ) throws Exception{
		 MethodSignature methodSignature = 
				 (MethodSignature) joinPoint.getSignature();//获取连接点的方法签名对象;  
		 Method method = methodSignature.getMethod();
		 
		 if(method != null)
			 return method.getAnnotation(LimitIPRequest.class);
		 return null;
	}
}
*/