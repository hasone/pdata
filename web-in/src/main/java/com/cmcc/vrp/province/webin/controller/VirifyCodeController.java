/**
 * @Title: VirifyCodeController.java
 * @Package com.cmcc.vrp.province.webin.controller
 * @author: qihang
 * @date: 2015年4月7日 下午1:55:12
 * @version V1.0
 */
package com.cmcc.vrp.province.webin.controller;

import com.cmcc.vrp.province.model.RandomCodeInfo;
import com.cmcc.vrp.province.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//
//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @ClassName: VirifyCodeController
 * @Description: 生成验证码的相关类
 * @author: qihang
 * @date: 2015年4月7日 下午1:55:12
 */

@Controller
@RequestMapping("/manage/virifyCode")
public class VirifyCodeController {
    @Autowired
    ImageService imageService;

    //普通赠送图形验证码的KEY
    public final String PRESENT_IMG_CHECK_CODE_KEY = "presentImgCheckCodeKey";

    //普通赠送图形验证码的KEY
    public final String LOGIN_IMG_CHECK_CODE_KEY = "virifyCode";

    /**
     * 普通赠送验证码
     *
     * @param request  请求对象
     * @param response 响应对象
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("getPresentImg")
    public void presentImgCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //验证码图片长宽
        int width = 200;
        int height = 100;

        //验证码内容
        RandomCodeInfo randomCodeInfo = imageService.createCalcRandomCode(height, width);
        writeToResponse(request, response, randomCodeInfo, new Processor() {
            @Override
            public String getSessionKey() {
                return PRESENT_IMG_CHECK_CODE_KEY;
            }
        });
    }


    /**
     * @param request
     * @param response
     * @Title: index
     * @Description: 生成JPEG的图像，放到response的输出流中
     */
    @RequestMapping("getImg")
    public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //验证码图片长宽
        int width = 200;
        int height = 100;

        //验证码内容
        RandomCodeInfo randomCodeInfo = imageService.createSimpleRandomCode(height, width);
        writeToResponse(request, response, randomCodeInfo, new Processor() {
            @Override
            public String getSessionKey() {
                return LOGIN_IMG_CHECK_CODE_KEY;
            }
        });
    }

    private void writeToResponse(HttpServletRequest request, HttpServletResponse response,
                                 RandomCodeInfo rci, Processor processor) throws IOException {
        //设置不缓存图片
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "No-cache");
        response.setDateHeader("Expires", 0);
        //指定生成的相应图片
        response.setContentType("image/png");

        //将图形验证码的值设置到会话中
        HttpSession session = request.getSession(true);
        session.setAttribute(processor.getSessionKey(), rci.getExpectedValue());
        
        System.out.println("verifycode session[" + session.getId() + "]");
        
        // savagechen11 add spring session
        // 图形验证码会话永不过期，否则会造成登录post 403错误
        session.setMaxInactiveInterval(-1);

        // 转成PNG格式，输出到response流中
        ImageIO.write(rci.getBufferedImage(), "png", response.getOutputStream());
    }

    private interface Processor {
        //获取设置到session中的key
        String getSessionKey();
    }
}
