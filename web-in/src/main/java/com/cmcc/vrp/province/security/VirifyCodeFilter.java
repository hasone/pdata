/**
 * @Title: VirifyCodeFilter.java
 * @Package com.cmcc.vrp.province.security
 * @author: qihang
 * @date: 2015年4月8日 上午10:40:50
 * @version V1.0
 */
package com.cmcc.vrp.province.security;

import com.cmcc.vrp.enums.LoginFailureType;
import com.cmcc.vrp.enums.SmsType;
import com.cmcc.vrp.province.sms.login.LoginSmsPojo;
import com.cmcc.vrp.province.sms.login.SmsRedisListener;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName: VirifyCodeFilter
 * @Description: 验证码的过滤器，在登陆验证之前进行
 * @author: qihang
 * @date: 2015年4月8日 上午10:40:50
 *
 */
public class VirifyCodeFilter implements Filter {

    @Autowired
    SmsRedisListener smsRedisListener;

    /**
     * @Title: init
     * @Description: TODO
     * @param filterConfig
     * @throws ServletException
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {


    }

    /**
     * @Title: doFilter
     * @Description: 将session中存储的验证码与用户提交的验证码做比较，如果错误则回到登陆页面
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {
        // TODO Auto-generated method stub
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String mobile = request.getParameter("j_username");
        String verify = request.getParameter("randomPassword");
        //String verifypicture = request.getParameter("verify");
        //String sessionCode =  (String)request.getSession(true).getAttribute("virifyCode");

        LoginSmsPojo pojo = smsRedisListener.getLoginInfo(mobile, SmsType.WEBINLOGIN_SMS);
        String smsPass = "";

        if (pojo == null) {
            smsPass = "";
        } else {
            smsPass = pojo.getPassword();
        }


        //将redis中存储的验证码与用户提交的验证码做比较，如果错误则回到登陆页面

        if (verify.equalsIgnoreCase(smsPass)) {  //验证通过
            chain.doFilter(request, response);
        } else {  //验证失败

            //记录登陆使用的登录名称
            String userName = request.getParameter("j_username");


            response.sendRedirect(request.getContextPath() + "/manage/user/login.html?"
                + "username=" + userName + "&errorCode=" + LoginFailureType.VIRIFYCODE_ERR.getCode());


        }


    }

    /**
     * @Title: destroy
     * @Description: TODO
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {

    }

}
