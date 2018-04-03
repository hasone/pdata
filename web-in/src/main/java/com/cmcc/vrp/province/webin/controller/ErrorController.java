/**
 * @Title: ErrorController.java
 * @Package com.cmcc.vrp.province.webin.controller
 * @author: sunyiwei
 * @date: 2015年4月2日 上午10:47:07
 * @version V1.0
 */
package com.cmcc.vrp.province.webin.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: ErrorController
 * @Description: 错误页面处理器
 * @author: sunyiwei
 * @date: 2015年4月2日 上午10:47:07
 *
 */
@Controller
@RequestMapping("/error")
public class ErrorController {

    private static final Logger LOGGER = Logger
        .getLogger(ErrorController.class);

    /** 
     * @Title: handle400Error 
     * @return
     * @Author: wujiamin
     * @date 2016年11月1日
    */
    @RequestMapping("400")
    public String handle400Error() {
        return "errorPages/400.ftl";
    }

    /** 
     * @Title: handle403Error 
     * @return
    */
    @RequestMapping("403")
    public String handle403Error() {
        return "errorPages/403.ftl";
    }

    /** 
     * @Title: handle404Error 
     * @return
    */
    @RequestMapping("404")
    public String handle404Error() {
        return "errorPages/404.ftl";
    }

    /** 
     * @Title: handle500Error 
     * @param request
     * @return
    */
    @RequestMapping("500")
    public String handle500Error(HttpServletRequest request) {
        LOGGER.error(request.getAttribute("javax.servlet.error.exception"));
        return "errorPages/500.ftl";
    }
}
