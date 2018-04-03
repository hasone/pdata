/**
 * @Title: ControllerInterceptor.java
 * @Package com.cmcc.vrp.province.security
 * @author: qihang
 * @date: 2015年6月15日 下午2:37:59
 * @version V1.0
 */
package com.cmcc.vrp.province.security;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @ClassName: ControllerInterceptor
 * @Description: 用于控制查询个数的拦截器
 * @author: qihang
 * @date: 2015年6月15日 下午2:37:59
 *
 */
public class ControllerInterceptor implements HandlerInterceptor {

    /**
     * @Title: preHandle
     * @Description: TODO
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();

        String pageSizeStr = request.getParameter("pageSize");


        int pageSize = NumberUtils.toInt(pageSizeStr);

        if (pageSize == 10 || pageSize == 50 || pageSize == 100) {
            session.setAttribute("pageSize", pageSize);
        } else if (session.getAttribute("pageSize") == null) {
            session.setAttribute("pageSize", 10);
        }

        return true;
    }

    /**
     * @Title: postHandle
     * @Description: TODO
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {


    }

    /**
     * @Title: afterCompletion
     * @Description: TODO
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
        throws Exception {


    }

}
