/**
 * @Title: LoginFailureHandler.java
 * @Package com.cmcc.vrp.province.security
 * @author: qihang
 * @date: 2015年4月22日 下午5:03:27
 * @version V1.0
 */
package com.cmcc.vrp.province.security;

import com.cmcc.vrp.province.service.impl.LoginFailRateLimitServiceImpl;
import com.cmcc.vrp.util.AESUtil;
import com.cmcc.vrp.util.StringUtils;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @ClassName: LoginFailureHandler
 * @Description: 登陆失败的处理页, 参照org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
 * @author: qihang
 * @date: 2015年4月22日 下午5:03:27
 */
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    LoginFailRateLimitServiceImpl loginFailRateLimitService;

    private Logger logger = Logger.getLogger(LoginFailureHandler.class);

    /**
     * @param request
     * @param response
     * @param exception
     * @throws IOException
     * @throws ServletException
     * @Title: onAuthenticationFailure
     * @Description: AuthenticationFailureHandler接口的功能，处理登陆失败
     * @see org.springframework.security.web.authentication.AuthenticationFailureHandler#onAuthenticationFailure(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception)
        throws IOException, ServletException {
        //0.清除session中验证码
        HttpSession session = request.getSession();
        String token = (String)session.getAttribute("SESSION_LOGIN_TOKEN");
        String key = RandomStringUtils.randomAlphanumeric(16);
        session.setAttribute("SESSION_LOGIN_TOKEN", key);
        
        session.invalidate();
        
        saveException(request, exception);

        //记录登陆使用的登录名称
        String loginUserName = "";
        if (exception instanceof UsernameNotFoundException) {//处理用户不存在的异常UsernameNotFoundException
            //logger.info("用户名不存在");
            UsernameNotFoundException ex = (UsernameNotFoundException) exception;
            Object[] objects = (Object[]) ex.getExtraInformation();
            loginUserName = (String) objects[0];
            
            logger.info("loginUserName == " + loginUserName);
        } else if (exception instanceof BadCredentialsException) {//处理姓名密码不匹配的异常BadCredentialsException
            //logger.info("用户名密码不匹配");
            //得到登陆用的账户密码
            UserAuthorityDetailImpl userAuthorityDetailImpl = (UserAuthorityDetailImpl) exception.getExtraInformation();
            loginUserName = userAuthorityDetailImpl.getUsername();
            
            logger.info("loginUserName == " + loginUserName);
        } else {
            //request.setAttribute("errorMsg", "未知错误");
        }
        
        if(!StringUtils.isValidMobile(loginUserName)){
            logger.info("获取到的用户名非正常手机号，需要解密：loginUserName = " + loginUserName
                    + "token = "+ token);
          //解密手机号
            try {
                loginUserName = AESUtil.aesDecrypt(loginUserName, token);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                logger.info("解密用户名报错,错误原因："+e.getMessage());
                //session.setAttribute("SESSION_LOGIN_TOKEN", key);
                //跳转到登陆页面,采用redirect方法
                response.sendRedirect(request.getContextPath() + "/manage/user/login.html");
                return;
            }
        }


        logger.info("用户手机号：" + loginUserName + " 登录失败");

        //频率限制
        loginFailRateLimitService.add(loginUserName);

        //跳转到登陆页面,采用redirect方法
        response.sendRedirect(request.getContextPath() + "/manage/user/login.html");

        //request.getRequestDispatcher("/manage/user/login.html").forward(request, response);
        
        // savagechen11 test session
        return;
    }

    protected final void saveException(HttpServletRequest request, AuthenticationException exception) {
        request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
    }
}
