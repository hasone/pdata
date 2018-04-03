package com.cmcc.vrp.weixin.controller.aop;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cmcc.vrp.province.model.WxAdminister;
import com.cmcc.vrp.province.webin.controller.BaseController;
import com.cmcc.vrp.util.IpUtils;
import com.cmcc.vrp.wx.WetchatService;

/**
 * WxControllerAspect.java
 *
 * @author wujiamin
 * @date 2017年2月22日
 */
@Aspect
@Component
public class WxControllerAspect extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WxControllerAspect.class);

    @Autowired
    WetchatService wetchatService;

    /**
     * 微信controller层鉴权
     *
     * @Title: wxControllerAuth
     */
    @Pointcut("execution(public * com.cmcc.vrp.weixin.controller..*(..))")
    public void wxControllerAuth() {
    }

    @Pointcut("execution(public * com.cmcc.vrp.weixin.controller.CommonController.*(..))")
    private void allCommonMethods() {
    }

    @Pointcut("execution(public * com.cmcc.vrp.weixin.controller.InterfaceController.*(..))")
    private void allInterfaceMethods() {
    }

    @Pointcut("execution(* com.cmcc.vrp.weixin.controller.InviteController.showByAdminId(..))")
    private void showInviteByAdminId() {
    }


    /**
     * @Title: around
     * @Description:
     */
    @Around("wxControllerAuth() && !allCommonMethods() && !allInterfaceMethods() && !showInviteByAdminId()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // get req and resp
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        logger.debug("Request from {}:{}", IpUtils.getRemoteAddr(req), req.getRemotePort());

        //For test
//        req.getSession().setAttribute("currentUserId", 1L);//将adminId放到session中
//        req.setAttribute("wxAspect", "done");//表示鉴权通过
//        req.getSession().setAttribute(SESSION_MOBILE_KEY, "18867103685");//将手机号放到session中

        //1、请求的session中带有手机号，说明鉴权通过
        WxAdminister admin = getWxCurrentUser();
        if (admin != null && admin.getId() != null) {
            logger.info("adminId = {} in session.", admin.getId());
            req.setAttribute("wxAspect", "done");//表示鉴权通过
        } else {
            if (StringUtils.isEmpty(req.getParameter("code"))) {//参数不带code，或者参数中的code已经在缓存中存在
                //换取code， 需要封装方法
                String getCodeUrl = wetchatService.getCode(req.getRequestURI());
                logger.info("需要换取code：getCodeUrl:{}", getCodeUrl);
                req.setAttribute("wxAspect", getCodeUrl);
            } else {
                String result = wetchatService.getUserType(req);
                if ("busiPage".equals(result)) {
                    req.setAttribute("wxAspect", "done");

                    //第一次进入页面，就点击分享，分享时会带着code，因此当此时链接中有code时，重定向到该页面。不带参数的页面（如果接js-sdk则不用该处理模式）
                    String queryString = req.getQueryString();
                    if(!StringUtils.isEmpty(queryString) && queryString.contains("code=")){
                        String returnUrl = "redirect:" + req.getRequestURL();
                        logger.info("wxAspect:returnUrl{}", returnUrl);
                        req.setAttribute("wxAspect", returnUrl);
                    }

                } else {
                    req.setAttribute("wxAspect", result);
                }
            }
        }

        String wxAspect = (String) req.getAttribute("wxAspect");
        if (com.cmcc.vrp.util.StringUtils.isEmpty(wxAspect)) { //为空的情况说明上面的处理出了问题
            return "gdzc/404.ftl";
        } else if ("done".equals(wxAspect)) { //切面处理通过,正常调用业务逻辑
            logger.info("Invoke {}:{}.request.wxAspect={}", joinPoint
                    .getTarget().getClass(), joinPoint.getSignature(), req.getAttribute("wxAspect"));
            return joinPoint.proceed();
        } else {  //跳转到其它页面
            return wxAspect;
        }
    }
}
