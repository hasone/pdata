/**
 * 
 */
package com.cmcc.vrp.ec.controller.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 *  @desc:  限制某个IP在某个时间段内请求某个方法的次数
 *  @author: wuguoping 
 *  @data: 2017年5月9日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface LimitIPRequest {
	/**
	 * title: limitCounts
	 * desc: 限制某时间段内可以访问的次数，默认设置100
	 * @return
	 * wuguoping
	 * 2017年5月9日
	 */
	public int limitCounts() default 100;
	
	/**
	 * 
	 * title: linitTime
	 * desc: 限制访问的某一个时间段，单位为秒，默认值60s
	 * @return
	 * wuguoping
	 * 2017年5月9日
	 */
	public int limitTime() default 60;
}